package com.duyanhnguyen.miniproject.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.duyanhnguyen.miniproject.InvoiceDetailActivity;
import com.duyanhnguyen.miniproject.R;
import com.duyanhnguyen.miniproject.database.AppDatabase;
import com.duyanhnguyen.miniproject.database.entity.Order;
import com.duyanhnguyen.miniproject.database.entity.OrderDetail;
import com.duyanhnguyen.miniproject.database.entity.Product;
import com.duyanhnguyen.miniproject.utils.OrderDisplay;
import com.duyanhnguyen.miniproject.utils.OrderStatus;
import com.duyanhnguyen.miniproject.utils.ProductImages;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.Holder> {

    private final Context context;
    private final AppDatabase db;
    private List<Order> orders = new ArrayList<>();

    public OrderHistoryAdapter(Context context, AppDatabase db) {
        this.context = context;
        this.db = db;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders != null ? new ArrayList<>(orders) : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_order_row, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder h, int position) {
        Order order = orders.get(position);
        String displayDate = OrderDisplay.formatDisplayDate(order.getOrderDate());
        h.tvOrderTitle.setText(String.format(Locale.US, "Order #%d — %s", order.getOrderId(), displayDate));

        String st = order.getStatus() != null ? order.getStatus() : OrderStatus.PAID;
        h.tvOrderStatus.setText(st);
        if (OrderStatus.DELIVERED.equalsIgnoreCase(st)) {
            h.tvOrderStatus.setBackgroundResource(R.drawable.bg_status_delivered);
            h.tvOrderStatus.setTextColor(0xFF1976D2);
        } else if (OrderStatus.CANCELLED.equalsIgnoreCase(st)) {
            h.tvOrderStatus.setBackgroundResource(R.drawable.bg_status_cancelled);
            h.tvOrderStatus.setTextColor(0xFFE53935);
        } else {
            h.tvOrderStatus.setBackgroundResource(R.drawable.bg_status_paid);
            h.tvOrderStatus.setTextColor(0xFF388E3C);
        }

        h.tvOrderTotal.setText(String.format(Locale.US, "Total: $%.2f", order.getTotalAmount()));

        List<OrderDetail> details = db.orderDetailDao().getOrderDetailsByOrderId(order.getOrderId());
        Product thumbProduct = null;
        if (!details.isEmpty()) {
            thumbProduct = db.productDao().getProductById(details.get(0).getProductId());
        }
        int[] colors = {0xFFFFF3E0, 0xFFE8F5E9, 0xFFFCE4EC, 0xFFF3E5F5, 0xFFE3F2FD, 0xFFFFF9C4, 0xFFE0F7FA, 0xFFFFEBEE};
        if (thumbProduct != null) {
            h.ivOrderThumb.setImageResource(ProductImages.getResId(thumbProduct.getImageUrl()));
            h.ivOrderThumb.setBackgroundColor(colors[Math.abs(thumbProduct.getProductId()) % colors.length]);
        } else {
            h.ivOrderThumb.setImageResource(R.drawable.ic_fruit_logo);
            h.ivOrderThumb.setBackgroundColor(0xFFE8F5E9);
        }

        h.itemView.setOnClickListener(v -> {
            Intent i = new Intent(context, InvoiceDetailActivity.class);
            i.putExtra(InvoiceDetailActivity.EXTRA_ORDER_ID, order.getOrderId());
            context.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    static class Holder extends RecyclerView.ViewHolder {
        ImageView ivOrderThumb;
        TextView tvOrderTitle, tvOrderStatus, tvOrderTotal;

        Holder(@NonNull View itemView) {
            super(itemView);
            ivOrderThumb = itemView.findViewById(R.id.ivOrderThumb);
            tvOrderTitle = itemView.findViewById(R.id.tvOrderTitle);
            tvOrderStatus = itemView.findViewById(R.id.tvOrderStatus);
            tvOrderTotal = itemView.findViewById(R.id.tvOrderTotal);
        }
    }
}
