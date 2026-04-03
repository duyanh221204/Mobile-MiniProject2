package com.duyanhnguyen.miniproject;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.duyanhnguyen.miniproject.adapter.OrderHistoryAdapter;
import com.duyanhnguyen.miniproject.database.AppDatabase;
import com.duyanhnguyen.miniproject.database.entity.Order;
import com.duyanhnguyen.miniproject.utils.OrderStatus;
import com.duyanhnguyen.miniproject.utils.SessionManager;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class OrderHistoryActivity extends AppCompatActivity {

    private AppDatabase db;
    private SessionManager sessionManager;
    private OrderHistoryAdapter adapter;
    private TextView tabAll, tabPaid, tabDelivered, tabCancelled;
    private TextView tvOrdersEmpty;
    private RecyclerView rvOrders;

    private String filter = "ALL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionManager = new SessionManager(this);
        if (!sessionManager.isLoggedIn()) {
            Toast.makeText(this, R.string.login_required_orders, Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, LoginActivity.class);
            i.putExtra(LoginActivity.EXTRA_REDIRECT, LoginActivity.REDIRECT_ORDERS);
            startActivity(i);
            finish();
            return;
        }

        setContentView(R.layout.activity_order_history);
        db = AppDatabase.getInstance(this);

        rvOrders = findViewById(R.id.rvOrders);
        tvOrdersEmpty = findViewById(R.id.tvOrdersEmpty);
        tabAll = findViewById(R.id.tabAll);
        tabPaid = findViewById(R.id.tabPaid);
        tabDelivered = findViewById(R.id.tabDelivered);
        tabCancelled = findViewById(R.id.tabCancelled);

        findViewById(R.id.ivBack).setOnClickListener(v -> finish());

        tabAll.setOnClickListener(v -> selectFilter("ALL"));
        tabPaid.setOnClickListener(v -> selectFilter(OrderStatus.PAID));
        tabDelivered.setOnClickListener(v -> selectFilter(OrderStatus.DELIVERED));
        tabCancelled.setOnClickListener(v -> selectFilter(OrderStatus.CANCELLED));

        rvOrders.setLayoutManager(new LinearLayoutManager(this));
        adapter = new OrderHistoryAdapter(this, db);
        rvOrders.setAdapter(adapter);

        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setSelectedItemId(R.id.nav_orders);
        bottomNav.setOnItemSelectedListener(this::handleBottomNav);

        selectFilter("ALL");
    }

    @Override
    protected void onResume() {
        super.onResume();
        applyFilter();
        updateCartBadge(findViewById(R.id.bottomNav));
    }

    private void selectFilter(String f) {
        filter = f;
        tabAll.setBackgroundResource(R.drawable.bg_filter_tab_unselected);
        tabPaid.setBackgroundResource(R.drawable.bg_filter_tab_unselected);
        tabDelivered.setBackgroundResource(R.drawable.bg_filter_tab_unselected);
        tabCancelled.setBackgroundResource(R.drawable.bg_filter_tab_unselected);
        tabAll.setTypeface(null, Typeface.NORMAL);
        tabPaid.setTypeface(null, Typeface.NORMAL);
        tabDelivered.setTypeface(null, Typeface.NORMAL);
        tabCancelled.setTypeface(null, Typeface.NORMAL);
        int secondary = ContextCompat.getColor(this, R.color.text_secondary);
        int primary = ContextCompat.getColor(this, R.color.text_primary);
        tabAll.setTextColor(secondary);
        tabPaid.setTextColor(secondary);
        tabDelivered.setTextColor(secondary);
        tabCancelled.setTextColor(secondary);

        TextView active = tabAll;
        if (OrderStatus.PAID.equals(f)) active = tabPaid;
        else if (OrderStatus.DELIVERED.equals(f)) active = tabDelivered;
        else if (OrderStatus.CANCELLED.equals(f)) active = tabCancelled;

        active.setBackgroundResource(R.drawable.bg_filter_tab_selected);
        active.setTypeface(null, Typeface.BOLD);
        active.setTextColor(primary);

        applyFilter();
    }

    private void applyFilter() {
        int userId = sessionManager.getUserId();
        List<Order> all = db.orderDao().getOrdersByUser(userId);
        List<Order> filtered = new ArrayList<>();
        for (Order o : all) {
            String st = o.getStatus() != null ? o.getStatus() : OrderStatus.PAID;
            if ("ALL".equals(filter)) {
                filtered.add(o);
            } else if (filter.equalsIgnoreCase(st)) {
                filtered.add(o);
            }
        }
        adapter.setOrders(filtered);
        boolean empty = filtered.isEmpty();
        tvOrdersEmpty.setVisibility(empty ? android.view.View.VISIBLE : android.view.View.GONE);
        rvOrders.setVisibility(empty ? android.view.View.GONE : android.view.View.VISIBLE);
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
            startActivity(new Intent(this, CartActivity.class));
            overridePendingTransition(0, 0);
            finish();
            return true;
        }
        if (id == R.id.nav_orders) {
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

    private void updateCartBadge(BottomNavigationView bottomNav) {
        if (bottomNav == null) return;
        int count = com.duyanhnguyen.miniproject.utils.CartManager.getInstance(this).getTotalItemCount();
        BadgeDrawable badge = bottomNav.getOrCreateBadge(R.id.nav_cart);
        if (count > 0) {
            badge.setVisible(true);
            badge.setNumber(count);
        } else {
            badge.setVisible(false);
        }
    }
}
