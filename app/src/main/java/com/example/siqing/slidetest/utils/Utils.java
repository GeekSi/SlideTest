package com.example.siqing.slidetest.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

/**
 * Created by siqing on 2018/3/28.
 */

public class Utils {

    public static int dip2px(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }


    public static int getScreenHeight(Context context) {
        return getMetrics(context).heightPixels;
    }

    public static int getScreenWidth(Context context) {
        return getMetrics(context).widthPixels;
    }

    public static DisplayMetrics getMetrics(Context context) {
        DisplayMetrics metric = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metric);
        return metric;
    }
}
