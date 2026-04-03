package com.duyanhnguyen.miniproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.duyanhnguyen.miniproject.database.AppDatabase;
import com.duyanhnguyen.miniproject.database.entity.Order;
import com.duyanhnguyen.miniproject.database.entity.OrderDetail;
import com.duyanhnguyen.miniproject.database.entity.Product;
import com.duyanhnguyen.miniproject.utils.OrderDisplay;
import com.duyanhnguyen.miniproject.utils.OrderStatus;
import com.duyanhnguyen.miniproject.utils.ProductImages;
import com.duyanhnguyen.miniproject.utils.SessionManager;

import java.util.List;
import java.util.Locale;

public class InvoiceDetailActivity extends AppCompatActivity {

    public static final String EXTRA_ORDER_ID = "ORDER_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_detail);

        SessionManager session = new SessionManager(this);
        if (!session.isLoggedIn()) {
            Toast.makeText(this, R.string.login_required_orders, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        int orderId = getIntent().getIntExtra(EXTRA_ORDER_ID, -1);
        if (orderId < 0) {
            Toast.makeText(this, "Invalid order", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        AppDatabase db = AppDatabase.getInstance(this);
        Order order = db.orderDao().getOrderById(orderId);
        if (order == null || order.getUserId() != session.getUserId()) {
            Toast.makeText(this, "Order not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        findViewById(R.id.ivBack).setOnClickListener(v -> finish());
        findViewById(R.id.btnReturnHome).setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

        findViewById(R.id.ivShare).setOnClickListener(v -> shareSummary(order, db));

        TextView tvCode = findViewById(R.id.tvInvoiceOrderCode);
        tvCode.setText(OrderDisplay.formatOrderCode(order.getOrderId(), order.getOrderDate()));

        TextView tvTotal = findViewById(R.id.tvInvoiceTotal);
        tvTotal.setText(String.format(Locale.US, "$%.2f", order.getTotalAmount()));

        LinearLayout llLines = findViewById(R.id.llInvoiceLines);
        llLines.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);
        List<OrderDetail> details = db.orderDetailDao().getOrderDetailsByOrderId(orderId);
        for (OrderDetail d : details) {
            Product p = db.productDao().getProductById(d.getProductId());
            View row = inflater.inflate(R.layout.include_invoice_line, llLines, false);
            ImageView iv = row.findViewById(R.id.ivLineIcon);
            TextView tvName = row.findViewById(R.id.tvLineName);
            TextView tvPrice = row.findViewById(R.id.tvLinePrice);
            if (p != null) {
                iv.setImageResource(ProductImages.getResId(p.getImageUrl()));
                String name = p.getProductName() + " × " + d.getQuantity();
                tvName.setText(name);
            } else {
                tvName.setText("Item × " + d.getQuantity());
            }
            double line = d.getUnitPrice() * d.getQuantity();
            tvPrice.setText(String.format(Locale.US, "$%.2f", line));
            llLines.addView(row);
        }

        String status = order.getStatus() != null ? order.getStatus() : OrderStatus.PAID;
        LinearLayout llSteps = findViewById(R.id.llDeliverySteps);
        TextView tvSummary = findViewById(R.id.tvDeliverySummary);
        llSteps.removeAllViews();

        if (OrderStatus.CANCELLED.equalsIgnoreCase(status)) {
            tvSummary.setText("This order was cancelled.");
            addStepRow(inflater, llSteps, "Cancelled", true);
        } else if (OrderStatus.DELIVERED.equalsIgnoreCase(status)) {
            String paidDate = OrderDisplay.formatDisplayDate(order.getOrderDate());
            addStepRow(inflater, llSteps, "PAID (" + paidDate + ")", true);
            addStepRow(inflater, llSteps, "PROCESSING", true);
            addStepRow(inflater, llSteps, "OUT FOR DELIVERY", true);
            addStepRow(inflater, llSteps, "DELIVERED", true);
            tvSummary.setText("Delivered");
        } else {
            String paidDate = OrderDisplay.formatDisplayDate(order.getOrderDate());
            addStepRow(inflater, llSteps, "PAID (" + paidDate + ")", true);
            addStepRow(inflater, llSteps, "PROCESSING", false);
            addStepRow(inflater, llSteps, "OUT FOR DELIVERY", false);
            tvSummary.setText("PAID & PROCESSING");
        }
    }

    private void addStepRow(LayoutInflater inflater, LinearLayout parent, String label, boolean done) {
        TextView tv = new TextView(this);
        tv.setText((done ? "✓ " : "○ ") + label);
        tv.setTextSize(14f);
        tv.setPadding(0, 8, 0, 0);
        tv.setTextColor(done ? 0xFF388E3C : 0xFF9E9E9E);
        parent.addView(tv);
    }

    private void shareSummary(Order order, AppDatabase db) {
        StringBuilder sb = new StringBuilder();
        sb.append(OrderDisplay.formatOrderCode(order.getOrderId(), order.getOrderDate())).append("\n");
        sb.append("Total: ").append(String.format(Locale.US, "$%.2f", order.getTotalAmount())).append("\n");
        for (OrderDetail d : db.orderDetailDao().getOrderDetailsByOrderId(order.getOrderId())) {
            Product p = db.productDao().getProductById(d.getProductId());
            String name = p != null ? p.getProductName() : "Item";
            sb.append(name).append(" × ").append(d.getQuantity()).append("\n");
        }
        Intent send = new Intent(Intent.ACTION_SEND);
        send.setType("text/plain");
        send.putExtra(Intent.EXTRA_TEXT, sb.toString());
        startActivity(Intent.createChooser(send, getString(R.string.invoice)));
    }
}
