package com.duyanhnguyen.miniproject.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public final class TopToast {

    public static void show(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP | Gravity.END, 32, 100);
        toast.show();
    }

    private TopToast() {}
}
