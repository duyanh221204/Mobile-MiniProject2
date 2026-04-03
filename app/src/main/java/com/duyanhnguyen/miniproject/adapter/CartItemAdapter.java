package com.duyanhnguyen.miniproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.duyanhnguyen.miniproject.R;
import com.duyanhnguyen.miniproject.database.AppDatabase;
import com.duyanhnguyen.miniproject.database.entity.Product;
import com.duyanhnguyen.miniproject.utils.CartManager;
import com.duyanhnguyen.miniproject.utils.ProductImages;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.Holder> {

    public interface Listener {
        void onCartChanged();
    }

    private final Context context;
    private final AppDatabase db;
    private final Listener listener;
    private List<CartManager.Line> lines = new ArrayList<>();

    public CartItemAdapter(Context context, AppDatabase db, Listener listener) {
        this.context = context;
        this.db = db;
        this.listener = listener;
    }

    public void setLines(List<CartManager.Line> lines) {
        this.lines = lines != null ? new ArrayList<>(lines) : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_cart_line, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder h, int position) {
        CartManager.Line line = lines.get(position);
        Product p = db.productDao().getProductById(line.productId);
        if (p == null) {
            h.tvCartName.setText("Unknown product");
            return;
        }

        h.tvCartName.setText(p.getProductName());
        h.tvCartUnitPrice.setText(String.format(Locale.US, "$%.2f / %s", p.getPrice(), p.getUnit()));

        int[] colors = {0xFFFFF3E0, 0xFFE8F5E9, 0xFFFCE4EC, 0xFFF3E5F5, 0xFFE3F2FD, 0xFFFFF9C4, 0xFFE0F7FA, 0xFFFFEBEE};
        h.ivCartThumb.setImageResource(ProductImages.getResId(p.getImageUrl()));
        h.ivCartThumb.setBackgroundColor(colors[Math.abs(p.getProductId()) % colors.length]);

        h.tvQty.setText(String.valueOf(line.quantity));
        double lineTotal = p.getPrice() * line.quantity;
        h.tvLineTotal.setText(String.format(Locale.US, "$%.2f", lineTotal));

        h.ivQtyMinus.setOnClickListener(v -> {
            int pos = h.getBindingAdapterPosition();
            if (pos == RecyclerView.NO_POSITION) return;
            CartManager.Line cur = lines.get(pos);
            CartManager.getInstance(context).setQuantity(cur.productId, cur.quantity - 1);
            refreshAndNotify();
        });
        h.ivQtyPlus.setOnClickListener(v -> {
            int pos = h.getBindingAdapterPosition();
            if (pos == RecyclerView.NO_POSITION) return;
            CartManager.Line cur = lines.get(pos);
            CartManager.getInstance(context).setQuantity(cur.productId, cur.quantity + 1);
            refreshAndNotify();
        });
        h.ivDelete.setOnClickListener(v -> {
            int pos = h.getBindingAdapterPosition();
            if (pos == RecyclerView.NO_POSITION) return;
            CartManager.Line cur = lines.get(pos);
            CartManager.getInstance(context).removeProduct(cur.productId);
            refreshAndNotify();
        });
    }

    private void refreshAndNotify() {
        setLines(CartManager.getInstance(context).getLines());
        if (listener != null) {
            listener.onCartChanged();
        }
    }

    @Override
    public int getItemCount() {
        return lines.size();
    }

    static class Holder extends RecyclerView.ViewHolder {
        ImageView ivCartThumb, ivQtyMinus, ivQtyPlus, ivDelete;
        TextView tvCartName, tvCartUnitPrice, tvQty, tvLineTotal;

        Holder(@NonNull View itemView) {
            super(itemView);
            ivCartThumb = itemView.findViewById(R.id.ivCartThumb);
            ivQtyMinus = itemView.findViewById(R.id.ivQtyMinus);
            ivQtyPlus = itemView.findViewById(R.id.ivQtyPlus);
            ivDelete = itemView.findViewById(R.id.ivDelete);
            tvCartName = itemView.findViewById(R.id.tvCartName);
            tvCartUnitPrice = itemView.findViewById(R.id.tvCartUnitPrice);
            tvQty = itemView.findViewById(R.id.tvQty);
            tvLineTotal = itemView.findViewById(R.id.tvLineTotal);
        }
    }
}
