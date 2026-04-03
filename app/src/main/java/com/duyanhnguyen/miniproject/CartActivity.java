package com.duyanhnguyen.miniproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.duyanhnguyen.miniproject.adapter.CartItemAdapter;
import com.duyanhnguyen.miniproject.database.AppDatabase;
import com.duyanhnguyen.miniproject.database.entity.Order;
import com.duyanhnguyen.miniproject.database.entity.OrderDetail;
import com.duyanhnguyen.miniproject.database.entity.Product;
import com.duyanhnguyen.miniproject.utils.CartManager;
import com.duyanhnguyen.miniproject.utils.OrderStatus;
import com.duyanhnguyen.miniproject.utils.SessionManager;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CartActivity extends AppCompatActivity implements CartItemAdapter.Listener {

    static final double DELIVERY_FEE = 3.0;
    static final double TAX_RATE = 0.05;

    private AppDatabase db;
    private SessionManager sessionManager;
    private CartItemAdapter adapter;
    private TextView tvSubtotal, tvDelivery, tvTax, tvGrandTotal;
    private TextView tvCartEmpty;
    private CardView cardSummary;
    private Button btnPlaceOrder;
    private RecyclerView rvCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        db = AppDatabase.getInstance(this);
        sessionManager = new SessionManager(this);

        tvSubtotal = findViewById(R.id.tvSubtotal);
        tvDelivery = findViewById(R.id.tvDelivery);
        tvTax = findViewById(R.id.tvTax);
        tvGrandTotal = findViewById(R.id.tvGrandTotal);
        tvCartEmpty = findViewById(R.id.tvCartEmpty);
        cardSummary = findViewById(R.id.cardSummary);
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);
        rvCart = findViewById(R.id.rvCart);

        findViewById(R.id.ivBack).setOnClickListener(v -> finish());

        rvCart.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CartItemAdapter(this, db, this);
        rvCart.setAdapter(adapter);

        btnPlaceOrder.setOnClickListener(v -> placeOrder());

        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setSelectedItemId(R.id.nav_cart);
        bottomNav.setOnItemSelectedListener(this::handleBottomNav);

        refreshCartUi();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshCartUi();
        updateCartBadge(findViewById(R.id.bottomNav));
    }

    private boolean handleBottomNav(android.view.MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            startActivity(new Intent(this, MainActivity.class));
            overridePendingTransition(0, 0);
            finish();
            return true;
        }
        if (id == R.id.nav_categories) {
            startActivity(new Intent(this, CategoryActivity.class));
            overridePendingTransition(0, 0);
            finish();
            return true;
        }
        if (id == R.id.nav_cart) {
            return true;
        }
        if (id == R.id.nav_orders) {
            startActivity(new Intent(this, OrderHistoryActivity.class));
            overridePendingTransition(0, 0);
            finish();
            return true;
        }
        if (id == R.id.nav_profile) {
            startActivity(new Intent(this, ProfileActivity.class));
            overridePendingTransition(0, 0);
            finish();
            return true;
        }
        return true;
    }

    @Override
    public void onCartChanged() {
        refreshCartUi();
        updateCartBadge(findViewById(R.id.bottomNav));
    }

    private void refreshCartUi() {
        List<CartManager.Line> lines = CartManager.getInstance(this).getLines();
        adapter.setLines(lines);

        boolean empty = lines.isEmpty();
        tvCartEmpty.setVisibility(empty ? View.VISIBLE : View.GONE);
        rvCart.setVisibility(empty ? View.GONE : View.VISIBLE);
        cardSummary.setVisibility(empty ? View.GONE : View.VISIBLE);
        btnPlaceOrder.setVisibility(empty ? View.GONE : View.VISIBLE);

        double subtotal = 0;
        for (CartManager.Line line : lines) {
            Product p = db.productDao().getProductById(line.productId);
            if (p != null) {
                subtotal += p.getPrice() * line.quantity;
            }
        }
        double tax = subtotal * TAX_RATE;
        double total = subtotal + DELIVERY_FEE + tax;

        tvSubtotal.setText(String.format(Locale.US, "$%.2f", subtotal));
        tvDelivery.setText(String.format(Locale.US, "$%.2f", DELIVERY_FEE));
        tvTax.setText(String.format(Locale.US, "$%.2f", tax));
        tvGrandTotal.setText(String.format(Locale.US, "$%.2f", total));
    }

    private void placeOrder() {
        if (CartManager.getInstance(this).isEmpty()) {
            Toast.makeText(this, R.string.cart_empty, Toast.LENGTH_SHORT).show();
            return;
        }
        if (!sessionManager.isLoggedIn()) {
            Toast.makeText(this, R.string.login_required_checkout, Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, LoginActivity.class);
            i.putExtra(LoginActivity.EXTRA_REDIRECT, LoginActivity.REDIRECT_CART);
            startActivity(i);
            return;
        }

        List<CartManager.Line> lines = CartManager.getInstance(this).getLines();
        double subtotal = 0;
        for (CartManager.Line line : lines) {
            Product p = db.productDao().getProductById(line.productId);
            if (p != null) {
                subtotal += p.getPrice() * line.quantity;
            }
        }
        double tax = subtotal * TAX_RATE;
        double grandTotal = subtotal + DELIVERY_FEE + tax;

        String orderDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        int userId = sessionManager.getUserId();

        Order order = new Order(userId, orderDate, grandTotal, OrderStatus.PAID);
        long orderRowId = db.orderDao().insert(order);
        int newOrderId = (int) orderRowId;

        for (CartManager.Line line : lines) {
            Product p = db.productDao().getProductById(line.productId);
            if (p == null) continue;
            OrderDetail od = new OrderDetail(newOrderId, line.productId, line.quantity, p.getPrice());
            db.orderDetailDao().insert(od);
        }

        CartManager.getInstance(this).clear();
        Toast.makeText(this, "Order placed!", Toast.LENGTH_SHORT).show();

        Intent detail = new Intent(this, InvoiceDetailActivity.class);
        detail.putExtra(InvoiceDetailActivity.EXTRA_ORDER_ID, newOrderId);
        startActivity(detail);
        finish();
    }

    private void updateCartBadge(BottomNavigationView bottomNav) {
        if (bottomNav == null) return;
        int count = CartManager.getInstance(this).getTotalItemCount();
        BadgeDrawable badge = bottomNav.getOrCreateBadge(R.id.nav_cart);
        if (count > 0) {
            badge.setVisible(true);
            badge.setNumber(count);
        } else {
            badge.setVisible(false);
        }
    }
}
