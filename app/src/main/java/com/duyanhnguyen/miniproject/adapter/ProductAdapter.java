package com.duyanhnguyen.miniproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.duyanhnguyen.miniproject.R;
import com.duyanhnguyen.miniproject.database.entity.Product;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private final Context context;
    private List<Product> productList;
    private static final Map<String, Integer> IMAGE_MAP = new HashMap<>();

    static {
        IMAGE_MAP.put("grapefruit", android.R.drawable.ic_menu_gallery);
        IMAGE_MAP.put("blueberries", android.R.drawable.ic_menu_gallery);
        IMAGE_MAP.put("mango", android.R.drawable.ic_menu_gallery);
        IMAGE_MAP.put("apple", android.R.drawable.ic_menu_gallery);
        IMAGE_MAP.put("orange", android.R.drawable.ic_menu_gallery);
        IMAGE_MAP.put("strawberry", android.R.drawable.ic_menu_gallery);
        IMAGE_MAP.put("banana", android.R.drawable.ic_menu_gallery);
        IMAGE_MAP.put("watermelon", android.R.drawable.ic_menu_gallery);
    }

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    public void updateList(List<Product> newList) {
        this.productList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.tvProductName.setText(product.getProductName());
        holder.tvPrice.setText(String.format(Locale.US, "$%.2f", product.getPrice()));
        holder.tvUnit.setText(String.format("/ %s", product.getUnit()));

        // Set product image based on imageUrl key
        Integer resId = IMAGE_MAP.get(product.getImageUrl());
        if (resId != null) {
            holder.ivProductImage.setImageResource(resId);
        }

        // Set background color based on product type
        int[] colors = {0xFFFFF3E0, 0xFFE8F5E9, 0xFFFCE4EC, 0xFFF3E5F5, 0xFFE3F2FD, 0xFFFFF9C4, 0xFFE0F7FA, 0xFFFFEBEE};
        holder.ivProductImage.setBackgroundColor(colors[position % colors.length]);

        holder.btnAdd.setOnClickListener(v ->
                Toast.makeText(context, product.getProductName() + " added!", Toast.LENGTH_SHORT).show());
        holder.ivAddIcon.setOnClickListener(v ->
                Toast.makeText(context, product.getProductName() + " added!", Toast.LENGTH_SHORT).show());
    }

    @Override
    public int getItemCount() {
        return productList != null ? productList.size() : 0;
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProductImage, ivAddIcon;
        TextView tvProductName, tvPrice, tvUnit, btnAdd;

        ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvUnit = itemView.findViewById(R.id.tvUnit);
            btnAdd = itemView.findViewById(R.id.btnAdd);
            ivAddIcon = itemView.findViewById(R.id.ivAddIcon);
        }
    }
}
