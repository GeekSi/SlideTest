package com.example.siqing.slidetest.views.slide;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.TextView;

/**
 * Created by siqing on 18/3/31.
 */

@SuppressLint("AppCompatCustomView")
public class CustomTextView extends TextView {
    public String TAG = "CustomTextView";
    private float startX = 0;


    public CustomTextView(Context context) {
        super(context);
    }

    public CustomTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


}
