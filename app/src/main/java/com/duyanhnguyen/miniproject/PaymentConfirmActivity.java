package com.duyanhnguyen.miniproject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Locale;

public class PaymentConfirmActivity extends AppCompatActivity {

    public static final String EXTRA_GRAND_TOTAL = "grand_total";
    public static final String EXTRA_PAYMENT_METHOD = "payment_method";

    private static final String CORRECT_PIN = "123456";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_confirm);

        double grandTotal = getIntent().getDoubleExtra(EXTRA_GRAND_TOTAL, 0);
        String method = getIntent().getStringExtra(EXTRA_PAYMENT_METHOD);

        TextView tvPaymentTotal = findViewById(R.id.tvPaymentTotal);
        TextView tvLogoLine1 = findViewById(R.id.tvLogoLine1);
        TextView tvLogoLine2 = findViewById(R.id.tvLogoLine2);
        LinearLayout llPaymentLogo = findViewById(R.id.llPaymentLogo);
        TextInputEditText etPinCode = findViewById(R.id.etPinCode);
        TextView tvPinError = findViewById(R.id.tvPinError);
        Button btnConfirm = findViewById(R.id.btnConfirm);
        Button btnCancel = findViewById(R.id.btnCancel);

        tvPaymentTotal.setText(String.format(Locale.US, "Total: $%.2f", grandTotal));

        if (SelectPaymentActivity.METHOD_VNPAY.equals(method)) {
            llPaymentLogo.setBackgroundResource(R.drawable.bg_vnpay);
            tvLogoLine1.setText("VN");
            tvLogoLine2.setText("PAY");
            btnConfirm.setBackgroundTintList(
                    getColorStateList(R.color.vnpay_blue));
        } else {
            llPaymentLogo.setBackgroundResource(R.drawable.bg_momo);
            tvLogoLine1.setText("mo");
            tvLogoLine2.setText("mo");
            btnConfirm.setBackgroundTintList(
                    getColorStateList(R.color.momo_pink));
        }

        btnCancel.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });

        btnConfirm.setOnClickListener(v -> {
            String pin = etPinCode.getText() != null
                    ? etPinCode.getText().toString().trim()
                    : "";

            if (pin.isEmpty()) {
                tvPinError.setText(R.string.pin_empty);
                tvPinError.setVisibility(View.VISIBLE);
                return;
            }

            if (CORRECT_PIN.equals(pin)) {
                Toast.makeText(this, R.string.payment_confirmed, Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            } else {
                tvPinError.setText(R.string.pin_incorrect);
                tvPinError.setVisibility(View.VISIBLE);
                etPinCode.setText("");
            }
        });
    }
}
