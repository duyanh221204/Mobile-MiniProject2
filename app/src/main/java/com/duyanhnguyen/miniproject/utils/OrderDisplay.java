package com.duyanhnguyen.miniproject.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class OrderDisplay {

    public static String formatOrderCode(int orderId, String orderDateStored) {
        try {
            String dayPart = orderDateStored.contains(" ") ? orderDateStored.split(" ")[0] : orderDateStored;
            SimpleDateFormat in = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            Date d = in.parse(dayPart);
            SimpleDateFormat compact = new SimpleDateFormat("yyMMdd", Locale.US);
            return "#ORD-" + compact.format(d) + "-" + String.format(Locale.US, "%04d", orderId);
        } catch (ParseException e) {
            return "#ORD-" + orderId;
        }
    }

    public static String formatDisplayDate(String orderDateStored) {
        try {
            String dayPart = orderDateStored.contains(" ") ? orderDateStored.split(" ")[0] : orderDateStored;
            SimpleDateFormat in = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            Date d = in.parse(dayPart);
            return new SimpleDateFormat("MMM d, yyyy", Locale.US).format(d);
        } catch (ParseException e) {
            return orderDateStored;
        }
    }

    private OrderDisplay() {}
}
