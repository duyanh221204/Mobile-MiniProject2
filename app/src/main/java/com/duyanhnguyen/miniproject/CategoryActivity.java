package com.duyanhnguyen.miniproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.duyanhnguyen.miniproject.adapter.CategoryGridAdapter;
import com.duyanhnguyen.miniproject.database.AppDatabase;
import com.duyanhnguyen.miniproject.database.entity.Category;
import com.duyanhnguyen.miniproject.utils.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    private AppDatabase db;
    private RecyclerView rvCategoryGrid;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        db = AppDatabase.getInstance(this);
        sessionManager = new SessionManager(this);

        initViews();
        loadCategories();
        setupBottomNav();
    }

    private void initViews() {
        rvCategoryGrid = findViewById(R.id.rvCategoryGrid);
        rvCategoryGrid.setLayoutManager(new GridLayoutManager(this, 2));

        ImageView ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }

    private void loadCategories() {
        List<Category> categories = db.categoryDao().getAllCategories();
        CategoryGridAdapter adapter = new CategoryGridAdapter(this, categories, category -> {
            // Optional: navigate to search/filter in future
            Toast.makeText(this, "Selected: " + category.getCategoryName(), Toast.LENGTH_SHORT).show();
        });
        rvCategoryGrid.setAdapter(adapter);
    }

    private void setupBottomNav() {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setSelectedItemId(R.id.nav_categories);
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                startActivity(new Intent(this, MainActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (itemId == R.id.nav_profile) {
                sessionManager.logout();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                return true;
            }
            return true;
        });
    }
}
