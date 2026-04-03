package com.duyanhnguyen.miniproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;

import java.util.Locale;

public class SelectPaymentActivity extends AppCompatActivity {

    public static final String EXTRA_GRAND_TOTAL = "grand_total";

    static final String METHOD_VNPAY = "VNPAY";
    static final String METHOD_MOMO = "MOMO";

    private String selectedMethod = null;
    private double grandTotal;

    private MaterialCardView cardVnpay, cardMomo;
    private RadioButton rbVnpay, rbMomo;

    private ActivityResultLauncher<Intent> confirmLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_payment);

        grandTotal = getIntent().getDoubleExtra(EXTRA_GRAND_TOTAL, 0);

        cardVnpay = findViewById(R.id.cardVnpay);
        cardMomo = findViewById(R.id.cardMomo);
        rbVnpay = findViewById(R.id.rbVnpay);
        rbMomo = findViewById(R.id.rbMomo);
        Button btnProceedToPay = findViewById(R.id.btnProceedToPay);
        TextView tvOrderTotal = findViewById(R.id.tvOrderTotal);

        tvOrderTotal.setText(String.format(Locale.US, "$%.2f", grandTotal));

        findViewById(R.id.ivBack).setOnClickListener(v -> finish());

        cardVnpay.setOnClickListener(v -> selectMethod(METHOD_VNPAY));
        cardMomo.setOnClickListener(v -> selectMethod(METHOD_MOMO));

        confirmLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        setResult(RESULT_OK);
                        finish();
                    }
                }
        );

        btnProceedToPay.setOnClickListener(v -> {
            if (selectedMethod == null) {
                Toast.makeText(this, R.string.please_select_payment, Toast.LENGTH_SHORT).show();
                return;
            }
            Intent i = new Intent(this, PaymentConfirmActivity.class);
            i.putExtra(PaymentConfirmActivity.EXTRA_GRAND_TOTAL, grandTotal);
            i.putExtra(PaymentConfirmActivity.EXTRA_PAYMENT_METHOD, selectedMethod);
            confirmLauncher.launch(i);
        });
    }

    private void selectMethod(String method) {
        selectedMethod = method;
        boolean isVnpay = METHOD_VNPAY.equals(method);

        rbVnpay.setChecked(isVnpay);
        rbMomo.setChecked(!isVnpay);

        int strokeWidth = Math.round(2 * getResources().getDisplayMetrics().density);
        int selectedColor = getColor(R.color.green_primary);

        cardVnpay.setStrokeWidth(isVnpay ? strokeWidth : 0);
        cardVnpay.setStrokeColor(selectedColor);
        cardMomo.setStrokeWidth(!isVnpay ? strokeWidth : 0);
        cardMomo.setStrokeColor(selectedColor);
    }
}
