package com.duyanhnguyen.miniproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.duyanhnguyen.miniproject.database.AppDatabase;
import com.duyanhnguyen.miniproject.database.entity.User;
import com.duyanhnguyen.miniproject.utils.CartManager;
import com.duyanhnguyen.miniproject.utils.SessionManager;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {

    private SessionManager sessionManager;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        sessionManager = new SessionManager(this);
        db = AppDatabase.getInstance(this);

        loadUserInfo();
        setupLogout();
        setupBottomNav();
        updateCartBadge();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCartBadge();
    }

    private void loadUserInfo() {
        int userId = sessionManager.getUserId();
        User user = db.userDao().getUserById(userId);

        TextView tvFullName = findViewById(R.id.tvProfileFullName);
        TextView tvUsername = findViewById(R.id.tvProfileUsername);
        TextView tvEmail = findViewById(R.id.tvProfileEmail);
        TextView tvPhone = findViewById(R.id.tvProfilePhone);
        TextView tvUserId = findViewById(R.id.tvProfileUserId);

        if (user != null) {
            tvFullName.setText(user.getFullName());
            tvUsername.setText("@" + user.getUsername());
            tvEmail.setText(user.getEmail());
            tvPhone.setText(user.getPhone());
            tvUserId.setText("#" + user.getUserId());
        } else {
            tvFullName.setText(sessionManager.getFullName());
            tvUsername.setText("@" + sessionManager.getUsername());
            tvEmail.setText("N/A");
            tvPhone.setText("N/A");
            tvUserId.setText("#" + userId);
        }
    }

    private void setupLogout() {
        findViewById(R.id.btnLogout).setOnClickListener(v -> {
            sessionManager.logout();
            startActivity(new Intent(this, LoginActivity.class));
            finishAffinity();
        });
    }

    private void updateCartBadge() {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
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

    private void setupBottomNav() {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setSelectedItemId(R.id.nav_profile);
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                startActivity(new Intent(this, MainActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (itemId == R.id.nav_categories) {
                startActivity(new Intent(this, CategoryActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (itemId == R.id.nav_cart) {
                startActivity(new Intent(this, CartActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (itemId == R.id.nav_orders) {
                startActivity(new Intent(this, OrderHistoryActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            }
            return true;
        });
    }
}
