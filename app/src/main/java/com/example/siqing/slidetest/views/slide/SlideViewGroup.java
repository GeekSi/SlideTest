package com.example.siqing.slidetest.views.slide;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.example.siqing.slidetest.activity.ApplistActivity;
import com.example.siqing.slidetest.views.touch.SlideGestureDector;

public class SlideViewGroup extends FrameLayout {
    public String TAG = "SlideViewGroup";
    private SlideGestureDector mDetector;

    private float startX = 0;

    public SlideViewGroup(@NonNull Context context) {
        super(context);
        init();
    }

    public SlideViewGroup(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mDetector = new SlideGestureDector(getContext(), new SlideGestureDector.SlideListener() {
            @Override
            public void onSlideChange(boolean isLeft) {
                if (!isLeft) {
                    ApplistActivity.actionStart(getContext());
                }
            }
        });
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.i(TAG, "onInterceptSSSTouchEvent === " + ev.getAction() + ", x == " + ev.getX());
        return super.onInterceptTouchEvent(ev);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = MotionEvent.ACTION_MASK & event.getAction();
        Log.i(TAG, "onTouchEvent === " + action);
//        switch(action) {
//            case MotionEvent.ACTION_DOWN:
//                startX = event.getX();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                float disX = event.getX() - startX;
//                if (disX > ViewConfiguration.getTouchSlop()) {
//                    return true;
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//                break;
//        }
         mDetector.onTouchEvent(event);
         return true;
    }
}