package com.duyanhnguyen.miniproject.utils;

import com.duyanhnguyen.miniproject.R;

import java.util.HashMap;
import java.util.Map;

public final class ProductImages {
    private static final Map<String, Integer> MAP = new HashMap<>();

    static {
        MAP.put("grapefruit", R.drawable.img_grapefruit);
        MAP.put("blueberries", R.drawable.img_blueberries);
        MAP.put("mango", R.drawable.img_mango);
        MAP.put("apple", R.drawable.img_apple);
        MAP.put("orange", R.drawable.img_orange);
        MAP.put("strawberry", R.drawable.img_strawberry);
        MAP.put("banana", R.drawable.img_banana);
        MAP.put("watermelon", R.drawable.img_watermelon);
    }

    // Category icon mapping
    private static final Map<String, Integer> CATEGORY_MAP = new HashMap<>();

    static {
        CATEGORY_MAP.put("citrus", R.drawable.ic_cat_citrus);
        CATEGORY_MAP.put("berries", R.drawable.ic_cat_berries);
        CATEGORY_MAP.put("tropical", R.drawable.ic_cat_tropical);
        CATEGORY_MAP.put("melons", R.drawable.ic_cat_melons);
        CATEGORY_MAP.put("apples", R.drawable.ic_cat_apples);
    }

    public static int getResId(String imageUrlKey) {
        Integer id = MAP.get(imageUrlKey);
        return id != null ? id : android.R.drawable.ic_menu_gallery;
    }

    public static int getCategoryResId(String imageUrlKey) {
        Integer id = CATEGORY_MAP.get(imageUrlKey);
        return id != null ? id : android.R.drawable.ic_menu_gallery;
    }

    private ProductImages() {}
}
