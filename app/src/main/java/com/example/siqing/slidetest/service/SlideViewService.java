package com.example.siqing.slidetest.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.example.siqing.slidetest.R;
import com.example.siqing.slidetest.utils.Utils;

/**
 * Created by siqing on 2018/3/28.
 */

public class SlideViewService extends Service {
    private boolean isTouchWindowAdded = false;
    private WindowManager mWidnowManager;
    private View slideWindowView;

    public static void startSlideWindowService(Context context) {
        Intent intent = new Intent(context, SlideViewService.class);
        context.startService(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mWidnowManager = (WindowManager) getApplicationContext().getSystemService(
                Context.WINDOW_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!isTouchWindowAdded) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(getApplicationContext())) {
                    addSlideWindowView();
                } else {
                    Intent settingIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + getPackageName()));
                    startActivity(settingIntent);
                }
            }
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (slideWindowView != null && slideWindowView.getParent() != null) {
            mWidnowManager.removeView(slideWindowView);
        }
        isTouchWindowAdded = false;
    }

    /**
     * 填充手势Window
     */
    private void addSlideWindowView() {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;

//        params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;

        params.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明

        // 设置悬浮窗的长得宽
        params.width = Utils.dip2px(this, 20);
        params.height = Utils.getScreenHeight(this);
        params.gravity = Gravity.START;


        slideWindowView = LayoutInflater.from(this).inflate(R.layout.custom_slide_view, null);
        mWidnowManager.addView(slideWindowView, params);

        isTouchWindowAdded = true;
    }

}
