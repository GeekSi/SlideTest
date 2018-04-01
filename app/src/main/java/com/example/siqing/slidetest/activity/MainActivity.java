package com.example.siqing.slidetest.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;

import com.example.siqing.slidetest.R;
import com.example.siqing.slidetest.service.SlideViewService;

public class MainActivity extends Activity {
    public String TAG = "MainActivity";

    private boolean isAdded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermissionAndStartSubPage();
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1234) {
            if (Settings.canDrawOverlays(this)) {
                SlideViewService.startSlideWindowService(this);
            }
        }
    }

    private void requestPermissionAndStartSubPage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 1234);
                return;
            }
            Intent intent = new Intent(MainActivity.this, SubActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(MainActivity.this, SubActivity.class);
            startActivity(intent);
        }
    }


}
