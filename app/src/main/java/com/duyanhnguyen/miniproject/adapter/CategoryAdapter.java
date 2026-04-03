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
import com.duyanhnguyen.miniproject.database.entity.Category;
import com.duyanhnguyen.miniproject.utils.ProductImages;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private final Context context;
    private final List<Category> categoryList;
    private OnCategoryClickListener listener;
    private static final Map<String, Integer> CATEGORY_COLORS = new HashMap<>();

    static {
        CATEGORY_COLORS.put("citrus", 0xFFFFF3E0);
        CATEGORY_COLORS.put("berries", 0xFFFCE4EC);
        CATEGORY_COLORS.put("tropical", 0xFFFFF9C4);
        CATEGORY_COLORS.put("melons", 0xFFE8F5E9);
        CATEGORY_COLORS.put("apples", 0xFFFFEBEE);
    }

    public interface OnCategoryClickListener {
        void onCategoryClick(Category category);
    }

    public CategoryAdapter(Context context, List<Category> categoryList, OnCategoryClickListener listener) {
        this.context = context;
        this.categoryList = categoryList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.tvCategoryName.setText(category.getCategoryName());

        // Set background color
        Integer color = CATEGORY_COLORS.get(category.getImageUrl());
        if (color != null) {
            holder.ivCategoryImage.setBackgroundColor(color);
        }

        holder.ivCategoryImage.setImageResource(ProductImages.getCategoryResId(category.getImageUrl()));

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCategoryClick(category);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList != null ? categoryList.size() : 0;
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCategoryImage;
        TextView tvCategoryName;

        CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCategoryImage = itemView.findViewById(R.id.ivCategoryImage);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
        }
    }
}
