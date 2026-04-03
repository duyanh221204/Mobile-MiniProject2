package com.duyanhnguyen.miniproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.duyanhnguyen.miniproject.adapter.CategoryAdapter;
import com.duyanhnguyen.miniproject.adapter.ProductAdapter;
import com.duyanhnguyen.miniproject.database.AppDatabase;
import com.duyanhnguyen.miniproject.database.entity.Category;
import com.duyanhnguyen.miniproject.database.entity.Product;
import com.duyanhnguyen.miniproject.utils.CartManager;
import com.duyanhnguyen.miniproject.utils.SessionManager;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private AppDatabase db;
    private ProductAdapter productAdapter;
    private RecyclerView rvProducts, rvCategories;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = AppDatabase.getInstance(this);
        sessionManager = new SessionManager(this);

        initViews();
        loadCategories();
        loadTodayProducts();
        setupSearch();
        setupBottomNav();
        updateCartBadge();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCartBadge();
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

    private void initViews() {
        rvProducts = findViewById(R.id.rvProducts);
        rvCategories = findViewById(R.id.rvCategories);

        // Products grid - 2 columns
        rvProducts.setLayoutManager(new GridLayoutManager(this, 2));

        // Categories horizontal list
        rvCategories.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    private void loadCategories() {
        List<Category> categories = db.categoryDao().getAllCategories();
        CategoryAdapter categoryAdapter = new CategoryAdapter(this, categories, category -> {
            List<Product> filtered = db.productDao().getProductsByCategory(category.getCategoryId());
            productAdapter.updateList(filtered);
            Toast.makeText(this, category.getCategoryName(), Toast.LENGTH_SHORT).show();
        });
        rvCategories.setAdapter(categoryAdapter);
    }

    private void loadTodayProducts() {
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        List<Product> products = db.productDao().getProductsByDate(today);

        // If no products for today, show all products
        if (products.isEmpty()) {
            products = db.productDao().getAllProducts();
        }

        productAdapter = new ProductAdapter(this, products);
        rvProducts.setAdapter(productAdapter);
    }

    private void setupSearch() {
        EditText etSearch = findViewById(R.id.etSearch);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();
                if (query.isEmpty()) {
                    loadTodayProducts();
                } else {
                    List<Product> results = db.productDao().searchProducts(query);
                    productAdapter.updateList(results);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void setupBottomNav() {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setSelectedItemId(R.id.nav_home);
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                loadTodayProducts();
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
            } else if (itemId == R.id.nav_profile) {
                // Logout
                sessionManager.logout();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                return true;
            }
            return true;
        });
    }
}
