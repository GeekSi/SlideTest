package com.example.siqing.slidetest.service;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.Button;

import com.example.siqing.slidetest.R;
import com.example.siqing.slidetest.adapter.AppItemAdapter;
import com.example.siqing.slidetest.modle.AppModel;
import com.example.siqing.slidetest.utils.Utils;
import com.example.siqing.slidetest.views.recyclerview.GridSpacingItemDecoration;
import com.example.siqing.slidetest.views.slide.SlideGroupListener;
import com.example.siqing.slidetest.views.slide.SlideViewGroup2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by siqing on 18/4/1.
 */

public class SlideWindowManager {
    public String TAG = "SlideWindowManager";
    private Context context;
    private WindowManager mWidnowManager;
    private int slideViewWidth = 15;
    private int count = 3;
    private int iconWidth = 84;//dp
    private int mTouchSlop;
    private List<AppModel> appModelList = new ArrayList<>();

    private View slideWindowView;
    private Button closeBtn;
    private SlideViewGroup2 slideBox;
    private RecyclerView appListRv;

    private AppItemAdapter adapter;
    private GridSpacingItemDecoration mDecoratioon;

    public SlideWindowManager(Context context) {
        this.context = context;
        this.mWidnowManager = (WindowManager) context.getSystemService(
                Context.WINDOW_SERVICE);
        this.slideViewWidth = Utils.dip2px(context, slideViewWidth);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void requestAdd() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.canDrawOverlays(context)) {
                addSLideWindowView();
            } else {
                Intent settingIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + context.getPackageName()));
                context.startActivity(settingIntent);
            }
        } else {
            addSLideWindowView();
        }
    }

    public void onDestory() {

        if (slideWindowView != null && slideWindowView.getParent() != null) {
            mWidnowManager.removeView(slideWindowView);
        }
    }

    /**
     * 添加slideWindow占位
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void addSLideWindowView() {
        if (slideBox != null && slideBox.isAttachedToWindow()) {
            return;
        }
        initViews();
        initActions();
    }

    private void initViews() {
        slideBox = (SlideViewGroup2) LayoutInflater.from(context).inflate(R.layout.custom_slide_view2, null);
//        closeBtn = slideBox.findViewById(R.id.click_btn);
        appListRv = slideBox.findViewById(R.id.app_list_rv);

        initBaseData();
        mDecoratioon = new GridSpacingItemDecoration(context, count, getSpaceing(Utils.getScreenWidth(context)), true);
        appListRv.addItemDecoration(mDecoratioon);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, count, GridLayoutManager.VERTICAL, false);
        appListRv.setLayoutManager(gridLayoutManager);

        adapter = new AppItemAdapter(context, appModelList);
        appListRv.setAdapter(adapter);
        addSlideWindowView(slideBox);
    }

    private void initActions() {
        slideBox.setSlideGroupListener(new SlideGroupListener() {
            @Override
            public void onTouchSlideRight() {
                Log.i(TAG, "onTouchSlideRight" + slideBox);
                WindowManager.LayoutParams params = (WindowManager.LayoutParams) slideBox.getLayoutParams();
                params.width = Utils.getScreenWidth(context);
                mWidnowManager.updateViewLayout(slideBox, params);
            }

            @Override
            public void onSlideOpen() {

            }

            @Override
            public void onSlideClose() {
                WindowManager.LayoutParams params = (WindowManager.LayoutParams) slideBox.getLayoutParams();
                params.width = slideViewWidth;
                mWidnowManager.updateViewLayout(slideBox, params);
            }
        });
//        closeBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.e(TAG, "============closeBtn==============");
//                slideBox.closePanelByHandle();
//            }
//        });

        appListRv.setOnTouchListener(new View.OnTouchListener() {
            private int startX, startY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction() & MotionEvent.ACTION_MASK;
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        startX = (int) event.getX();
                        startY = (int) event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        int disX = (int) Math.abs(event.getX() - startX);
                        int disY = (int) Math.abs(event.getY() - startY);
                        if (disX < mTouchSlop && disY < mTouchSlop) {
                            View childView = appListRv.findChildViewUnder(event.getX(), event.getY());
                            if (childView == null) {
                                slideBox.closePanelByHandle();
                                return true;
                            }
                        }
                        break;
                    default:
                        break;
                }

                return false;
            }
        });

        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                //首先回调的方法 返回int表示是否监听该方向
                int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;//拖拽
//                int swipeFlags = ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT;//侧滑删除
                return makeMovementFlags(dragFlags, 0);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                //滑动事件
                Collections.swap(appModelList, viewHolder.getAdapterPosition(), target.getAdapterPosition());
                adapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                Log.i(TAG, "position == " + viewHolder.getAdapterPosition() + ", target position == " + target.getAdapterPosition());
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            }

            @Override
            public boolean isLongPressDragEnabled() {
                //是否可拖拽
                return true;
            }

        });
        helper.attachToRecyclerView(appListRv);
    }


    private int getSpaceing(int width) {
        Log.i(TAG, "" + width);
        int spaceing = (width - (Utils.dip2px(context, iconWidth)) * count) / (count + 1);
        return spaceing;
    }

    /**
     * 填充手势Window
     */
    private void addSlideWindowView(View slideBox) {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        //设置窗口flag
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
//        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        params.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明
        // 设置悬浮窗的长得宽
        params.width = slideViewWidth;
//        params.width = Utils.getScreenWidth(getApplicationContext());
        params.height = Utils.getScreenHeight(context);
        params.gravity = Gravity.LEFT;
        slideWindowView = slideBox;
        mWidnowManager.addView(slideWindowView, params);
    }

    private void initBaseData() {
        mTouchSlop = ViewConfiguration.getTouchSlop();
        AppModel appModel = new AppModel();
        appModel.appIconRes = R.mipmap.alarm_icon;
        appModel.appName = "闹钟";
        appModelList.add(appModel);
        appModel = new AppModel();
        appModel.appIconRes = R.mipmap.weather_icon;
        appModel.appName = "天气";
        appModelList.add(appModel);
        appModel = new AppModel();
        appModel.appIconRes = R.mipmap.timer_icon;
        appModel.appName = "计时器";
        appModelList.add(appModel);
        appModel = new AppModel();
        appModel.appIconRes = R.mipmap.alarm_icon;
        appModel.appName = "闹钟1";
        appModelList.add(appModel);
        appModel = new AppModel();
        appModel.appIconRes = R.mipmap.weather_icon;
        appModel.appName = "天气1";
        appModelList.add(appModel);
        appModel = new AppModel();
        appModel.appIconRes = R.mipmap.timer_icon;
        appModel.appName = "计时器1";
        appModelList.add(appModel);
        appModel = new AppModel();
        appModel.appIconRes = R.mipmap.alarm_icon;
        appModel.appName = "闹钟2";
        appModelList.add(appModel);
        appModel = new AppModel();
        appModel.appIconRes = R.mipmap.weather_icon;
        appModel.appName = "天气2";
        appModelList.add(appModel);
        appModel = new AppModel();
        appModel.appIconRes = R.mipmap.timer_icon;
        appModel.appName = "计时器2";
        appModelList.add(appModel);


        appModel = new AppModel();
        appModel.appIconRes = R.mipmap.alarm_icon;
        appModel.appName = "闹钟3";
        appModelList.add(appModel);
        appModel = new AppModel();
        appModel.appIconRes = R.mipmap.weather_icon;
        appModel.appName = "天气3";
        appModelList.add(appModel);
        appModel = new AppModel();
        appModel.appIconRes = R.mipmap.timer_icon;
        appModel.appName = "计时器3";
        appModelList.add(appModel);
        appModel = new AppModel();
        appModel.appIconRes = R.mipmap.alarm_icon;
        appModel.appName = "闹钟4";
        appModelList.add(appModel);
        appModel = new AppModel();
        appModel.appIconRes = R.mipmap.weather_icon;
        appModel.appName = "天气4";
        appModelList.add(appModel);
        appModel = new AppModel();
        appModel.appIconRes = R.mipmap.timer_icon;
        appModel.appName = "计时器4";
        appModelList.add(appModel);
    }
}
