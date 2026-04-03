package com.duyanhnguyen.miniproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.duyanhnguyen.miniproject.database.AppDatabase;
import com.duyanhnguyen.miniproject.database.entity.User;
import com.duyanhnguyen.miniproject.utils.SessionManager;

public class LoginActivity extends AppCompatActivity {

    public static final String EXTRA_REDIRECT = "redirect";
    public static final String REDIRECT_CART = "cart";
    public static final String REDIRECT_ORDERS = "orders";

    private EditText etUsername, etPassword;
    private AppDatabase db;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = AppDatabase.getInstance(this);
        sessionManager = new SessionManager(this);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        TextView btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> doLogin());
    }

    private void doLogin() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = db.userDao().login(username, password);
        if (user != null) {
            sessionManager.createLoginSession(user.getUserId(), user.getUsername(), user.getFullName());
            Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
            String redirect = getIntent().getStringExtra(EXTRA_REDIRECT);
            Intent next;
            if (REDIRECT_CART.equals(redirect)) {
                next = new Intent(this, CartActivity.class);
            } else if (REDIRECT_ORDERS.equals(redirect)) {
                next = new Intent(this, OrderHistoryActivity.class);
            } else {
                next = new Intent(this, MainActivity.class);
            }
            startActivity(next);
            finish();
        } else {
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
        }
    }
}
