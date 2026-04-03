package com.duyanhnguyen.miniproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.duyanhnguyen.miniproject.database.AppDatabase;
import com.duyanhnguyen.miniproject.database.entity.Product;
import com.duyanhnguyen.miniproject.utils.CartManager;
import com.duyanhnguyen.miniproject.utils.ProductImages;
import com.duyanhnguyen.miniproject.utils.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Locale;

public class ProductDetailActivity extends AppCompatActivity {

    private SessionManager sessionManager;
    private AppDatabase db;
    private int quantity = 1;
    private Product currentProduct;

    private TextView tvQuantity, tvDetailName, tvDetailPrice, tvDetailUnit, tvDetailDesc, tvDateAdded, tvExpiryDate;
    private ImageView ivDetailImage, ivPlus, ivMinus, ivBack;
    private Button btnAddToCart;
    private LinearLayout llHeaderBg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        sessionManager = new SessionManager(this);
        db = AppDatabase.getInstance(this);

        initViews();
        setupBottomNav();

        int productId = getIntent().getIntExtra("PRODUCT_ID", -1);
        if (productId != -1) {
            loadProductData(productId);
        } else {
            Toast.makeText(this, "Product not found!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initViews() {
        tvDetailName = findViewById(R.id.tvDetailName);
        tvDetailPrice = findViewById(R.id.tvDetailPrice);
        tvDetailUnit = findViewById(R.id.tvDetailUnit);
        tvDetailDesc = findViewById(R.id.tvDetailDesc);
        tvDateAdded = findViewById(R.id.tvDateAdded);
        tvExpiryDate = findViewById(R.id.tvExpiryDate);
        tvQuantity = findViewById(R.id.tvQuantity);
        
        ivDetailImage = findViewById(R.id.ivDetailImage);
        ivPlus = findViewById(R.id.ivPlus);
        ivMinus = findViewById(R.id.ivMinus);
        ivBack = findViewById(R.id.ivBack);
        
        btnAddToCart = findViewById(R.id.btnAddToCart);
        llHeaderBg = findViewById(R.id.llHeaderBg);

        ivBack.setOnClickListener(v -> finish());

        ivPlus.setOnClickListener(v -> {
            quantity++;
            updateQuantityDisplay();
        });

        ivMinus.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                updateQuantityDisplay();
            }
        });

        btnAddToCart.setOnClickListener(v -> {
            if (currentProduct != null) {
                CartManager.getInstance(this).addProduct(currentProduct.getProductId(), quantity);
                Toast.makeText(this, "Added " + quantity + " " + currentProduct.getUnit() + " of " + currentProduct.getProductName() + " to cart", Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void updateQuantityDisplay() {
        if (currentProduct != null) {
            tvQuantity.setText(quantity + " " + currentProduct.getUnit().replace(" / ", "").replace("/", "").trim());
        } else {
            tvQuantity.setText(quantity + " kg");
        }
    }

    private void loadProductData(int productId) {
        new Thread(() -> {
            // Note: Since AppDatabase might not allow main thread queries, running in worker
            // but for starter apps it's sometimes main thread OK. Let's do main thread for simplicity 
            // if AppDatabase allowed it. Here we assume it allows or run thread.
            runOnUiThread(() -> {
                try {
                    currentProduct = db.productDao().getAllProducts().stream()
                            .filter(p -> p.getProductId() == productId)
                            .findFirst()
                            .orElse(null);
                            
                    if (currentProduct != null) {
                        displayProduct();
                    } else {
                        Toast.makeText(this, "Product not found!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }).start();
    }

    private void displayProduct() {
        tvDetailName.setText(currentProduct.getProductName());
        tvDetailPrice.setText(String.format(Locale.US, "$%.2f", currentProduct.getPrice()));
        tvDetailUnit.setText(String.format("/ %s", currentProduct.getUnit()));
        tvDetailDesc.setText(currentProduct.getDescription());
        tvDateAdded.setText(currentProduct.getDateAdded() != null ? currentProduct.getDateAdded() : "N/A");
        tvExpiryDate.setText(currentProduct.getExpiryDate() != null ? currentProduct.getExpiryDate() : "N/A");
        updateQuantityDisplay();
        
        ivDetailImage.setImageResource(ProductImages.getResId(currentProduct.getImageUrl()));
        
        // Background matching category logic
        int categoryId = currentProduct.getCategoryId();
        int[] colors = {0xFFFFF3E0, 0xFFE8F5E9, 0xFFFCE4EC, 0xFFF3E5F5, 0xFFE3F2FD, 0xFFFFF9C4, 0xFFE0F7FA, 0xFFFFEBEE};
        int bgCol = colors[(categoryId > 0 ? categoryId - 1 : 0) % colors.length];
        ivDetailImage.setBackgroundColor(bgCol);
        llHeaderBg.setBackgroundColor(bgCol);
    }

    private void setupBottomNav() {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        // Note: product details usually doesn't highlight a tab, or keep previous, 
        // we'll uncheck all or leave it
        bottomNav.getMenu().setGroupCheckable(0, false, true);
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
            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            }
            return true;
        });
    }
}
