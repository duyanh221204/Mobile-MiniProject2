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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryGridAdapter extends RecyclerView.Adapter<CategoryGridAdapter.CategoryViewHolder> {

    private final Context context;
    private final List<Category> categoryList;
    private final OnCategoryClickListener listener;

    private static final Map<String, Integer> CATEGORY_IMAGE_MAP = new HashMap<>();

    static {
        CATEGORY_IMAGE_MAP.put("apple", android.R.drawable.ic_menu_gallery);
        CATEGORY_IMAGE_MAP.put("citrus", android.R.drawable.ic_menu_gallery);
        CATEGORY_IMAGE_MAP.put("berries", android.R.drawable.ic_menu_gallery);
        CATEGORY_IMAGE_MAP.put("tropical", android.R.drawable.ic_menu_gallery);
        CATEGORY_IMAGE_MAP.put("melons", android.R.drawable.ic_menu_gallery);
    }

    public interface OnCategoryClickListener {
        void onCategoryClick(Category category);
    }

    public CategoryGridAdapter(Context context, List<Category> categoryList, OnCategoryClickListener listener) {
        this.context = context;
        this.categoryList = categoryList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category_grid, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.tvCategoryName.setText(category.getCategoryName());

        // Use same image logic as CategoryAdapter
        Integer resId = CATEGORY_IMAGE_MAP.get(category.getImageUrl());
        if (resId != null) {
            holder.ivCategoryImage.setImageResource(resId);
        } else {
            holder.ivCategoryImage.setImageResource(android.R.drawable.ic_menu_gallery);
        }

        holder.itemView.setOnClickListener(v -> listener.onCategoryClick(category));
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
