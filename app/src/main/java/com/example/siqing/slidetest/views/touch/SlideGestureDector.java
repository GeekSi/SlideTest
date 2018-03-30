package com.example.siqing.slidetest.views.touch;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.example.siqing.slidetest.utils.Utils;

/**
 * Created by siqing on 2018/3/28.
 */

public class SlideGestureDector implements GestureDetector.OnGestureListener {
    private String TAG = "SlideGestureDector";

    public interface SlideListener {
        void onSlideChange(boolean isLeft);
    }

    private int FLIP_DISTANCE = 200;

    private GestureDetector mDetector = null;
    private SlideListener slideListener;
    private Context context;


    public SlideGestureDector(Context context, SlideListener slideListener) {
        this.context = context;
        this.slideListener = slideListener;
        init(context);
    }

    private void init(Context context) {
        mDetector = new GestureDetector(context, this);
        FLIP_DISTANCE = Utils.getScreenWidth(context) / 6;
    }

    public boolean onTouchEvent(MotionEvent event) {
        return mDetector.onTouchEvent(event);
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        int distance = Utils.getScreenWidth(context) / 4;
        Log.i(TAG, "onScroll====" + (e2.getX() - e1.getX()) + "distance == " + distance);
        if (slideListener != null && (e2.getX() - e1.getX()) > distance) {
            slideListener.onSlideChange(false);
            return true;
        }
        return false;
    }


    @Override
    public void onLongPress(MotionEvent e) {

    }

    /**
     * e1 The first down motion event that started the fling. e2 The
     * move motion event that triggered the current onFling.
     */
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e1.getX() - e2.getX() > FLIP_DISTANCE) {
            Log.i(TAG, "<--- left, left, go go go");
            if (slideListener != null) {
                slideListener.onSlideChange(true);
            }
            return true;
        }
        if (e2.getX() - e1.getX() > FLIP_DISTANCE) {
            Log.i(TAG, "right, right, go go go --->");
            if (slideListener != null) {
                slideListener.onSlideChange(false);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        // TODO Auto-generated method stub
        return false;
    }

}
