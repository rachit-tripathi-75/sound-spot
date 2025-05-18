package com.rachit.tripathi75.soundspot.classes;

// FormatUtils.java

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class FormatUtils {

    public static String formatDuration(long milliseconds) {
        long minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds) -
                TimeUnit.MINUTES.toSeconds(minutes);

        return String.format(Locale.getDefault(), "%d:%02d", minutes, seconds);
    }

    public static String formatCount(long count) {
        if (count < 1000) {
            return String.valueOf(count);
        } else if (count < 1000000) {
            return String.format(Locale.getDefault(), "%.1fK", count / 1000.0);
        } else if (count < 1000000000) {
            return String.format(Locale.getDefault(), "%.1fM", count / 1000000.0);
        } else {
            return String.format(Locale.getDefault(), "%.1fB", count / 1000000000.0);
        }
    }

    public static String formatCurrency(double amount, String currencyCode) {
        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setCurrency(java.util.Currency.getInstance(currencyCode));
        return format.format(amount);
    }

    public static String formatDate(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.getDefault());
        return sdf.format(date);
    }

    public static String getRelativeTimeSpan(Date date) {
        long now = System.currentTimeMillis();
        long time = date.getTime();
        long diff = now - time;

        long seconds = TimeUnit.MILLISECONDS.toSeconds(diff);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
        long hours = TimeUnit.MILLISECONDS.toHours(diff);
        long days = TimeUnit.MILLISECONDS.toDays(diff);

        if (seconds < 60) {
            return "just now";
        } else if (minutes < 60) {
            return minutes + " minute" + (minutes > 1 ? "s" : "") + " ago";
        } else if (hours < 24) {
            return hours + " hour" + (hours > 1 ? "s" : "") + " ago";
        } else if (days < 7) {
            return days + " day" + (days > 1 ? "s" : "") + " ago";
        } else {
            return formatDate(date, "MMM d, yyyy");
        }
    }
}