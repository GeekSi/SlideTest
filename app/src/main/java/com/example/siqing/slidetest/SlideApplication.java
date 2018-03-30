package com.example.siqing.slidetest;

import android.app.Application;

import com.example.siqing.slidetest.service.SlideViewService;

/**
 * Created by siqing on 2018/3/29.
 */

public class SlideApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        SlideViewService.startSlideWindowService(getApplicationContext());

    }


}
