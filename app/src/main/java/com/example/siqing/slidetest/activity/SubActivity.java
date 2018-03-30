package com.example.siqing.slidetest.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.widget.TextView;

/**
 * Created by siqing on 2018/3/27.
 */

public class SubActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView textView = new TextView(this);
        textView.setText("SubPage");
        textView.setGravity(Gravity.CENTER);
        textView.setBackgroundColor(Color.RED);
        textView.setBackgroundColor(Color.parseColor("#66333333"));
        setContentView(textView);

//        setContentView(R.layout.custom_slide_view);
    }
}
