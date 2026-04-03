package com.duyanhnguyen.miniproject.utils;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Local cart persisted in SharedPreferences (productId + quantity).
 */
public class CartManager {
    private static final String PREF = "FruitAppCart";
    private static final String KEY_LINES = "lines";

    private static volatile CartManager instance;
    private final SharedPreferences prefs;

    public static class Line {
        public final int productId;
        public int quantity;

        public Line(int productId, int quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }
    }

    public static CartManager getInstance(Context context) {
        if (instance == null) {
            synchronized (CartManager.class) {
                if (instance == null) {
                    instance = new CartManager(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    private CartManager(Context appContext) {
        prefs = appContext.getSharedPreferences(PREF, Context.MODE_PRIVATE);
    }

    public List<Line> getLines() {
        List<Line> out = new ArrayList<>();
        try {
            JSONArray arr = new JSONArray(prefs.getString(KEY_LINES, "[]"));
            for (int i = 0; i < arr.length(); i++) {
                JSONObject o = arr.getJSONObject(i);
                out.add(new Line(o.getInt("p"), o.getInt("q")));
            }
        } catch (JSONException ignored) {
        }
        return out;
    }

    private void saveLines(List<Line> lines) {
        JSONArray arr = new JSONArray();
        try {
            for (Line l : lines) {
                JSONObject o = new JSONObject();
                o.put("p", l.productId);
                o.put("q", l.quantity);
                arr.put(o);
            }
        } catch (JSONException ignored) {
        }
        prefs.edit().putString(KEY_LINES, arr.toString()).apply();
    }

    public void addProduct(int productId, int quantity) {
        if (quantity <= 0) return;
        List<Line> lines = getLines();
        boolean found = false;
        for (Line l : lines) {
            if (l.productId == productId) {
                l.quantity += quantity;
                found = true;
                break;
            }
        }
        if (!found) {
            lines.add(new Line(productId, quantity));
        }
        saveLines(lines);
    }

    public void setQuantity(int productId, int quantity) {
        List<Line> lines = getLines();
        if (quantity <= 0) {
            lines.removeIf(l -> l.productId == productId);
        } else {
            boolean found = false;
            for (Line l : lines) {
                if (l.productId == productId) {
                    l.quantity = quantity;
                    found = true;
                    break;
                }
            }
            if (!found) {
                lines.add(new Line(productId, quantity));
            }
        }
        saveLines(lines);
    }

    public void removeProduct(int productId) {
        List<Line> lines = getLines();
        lines.removeIf(l -> l.productId == productId);
        saveLines(lines);
    }

    public void clear() {
        prefs.edit().remove(KEY_LINES).apply();
    }

    /** Sum of line quantities (for badge). */
    public int getTotalItemCount() {
        int n = 0;
        for (Line l : getLines()) {
            n += l.quantity;
        }
        return n;
    }

    public boolean isEmpty() {
        return getLines().isEmpty();
    }
}
