package com.duyanhnguyen.miniproject.utils;

import java.util.HashMap;
import java.util.Map;

public final class ProductImages {
    private static final Map<String, Integer> MAP = new HashMap<>();

    static {
        MAP.put("grapefruit", android.R.drawable.ic_menu_gallery);
        MAP.put("blueberries", android.R.drawable.ic_menu_gallery);
        MAP.put("mango", android.R.drawable.ic_menu_gallery);
        MAP.put("apple", android.R.drawable.ic_menu_gallery);
        MAP.put("orange", android.R.drawable.ic_menu_gallery);
        MAP.put("strawberry", android.R.drawable.ic_menu_gallery);
        MAP.put("banana", android.R.drawable.ic_menu_gallery);
        MAP.put("watermelon", android.R.drawable.ic_menu_gallery);
    }

    public static int getResId(String imageUrlKey) {
        Integer id = MAP.get(imageUrlKey);
        return id != null ? id : android.R.drawable.ic_menu_gallery;
    }

    private ProductImages() {}
}
