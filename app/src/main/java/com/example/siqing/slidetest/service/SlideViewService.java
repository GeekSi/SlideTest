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
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.example.siqing.slidetest.R;
import com.example.siqing.slidetest.utils.Utils;
import com.example.siqing.slidetest.views.slide.SlideGroupListener;
import com.example.siqing.slidetest.views.slide.SlideViewGroup2;

/**
 * Created by siqing on 2018/3/28.
 */

public class SlideViewService extends Service {
    public String TAG = "SlideViewService";
    private SlideWindowManager slideWindowManager;

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
        slideWindowManager = new SlideWindowManager(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        slideWindowManager.requestAdd();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        slideWindowManager.onDestory();
        super.onDestroy();
    }


}
