package com.example.siqing.slidetest.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.example.siqing.slidetest.R;
import com.example.siqing.slidetest.adapter.AppItemAdapter;
import com.example.siqing.slidetest.modle.AppModel;
import com.example.siqing.slidetest.utils.Utils;
import com.example.siqing.slidetest.views.recyclerview.GridSpacingItemDecoration;
import com.example.siqing.slidetest.views.touch.SlideGestureDector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by siqing on 2018/3/28.
 */

public class ApplistActivity extends Activity implements SlideGestureDector.SlideListener {
    private static final String TAG = "ApplistActivity";
    private SlideGestureDector dector;
    private RecyclerView appListRV = null;
    private List<AppModel> appModelList = new ArrayList<>();
    private int count = 6;
    private int iconWidth = 84;//dp
    private int mTouchSlop;
    private AppItemAdapter adapter;
    private GridSpacingItemDecoration mDecoratioon;

    private final int TIME_AUTO_FINISH = 10 * 1000;
    public static final int MSG_FINISH = 1001;

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            if (msg.what == MSG_FINISH) {
                exitWithAnimation();
            }
        }
    };

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, ApplistActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(TAG, "==========onCreate============" + savedInstanceState == null ? "bundle == null" : "bundle != null");

        overridePendingTransition(R.anim.slide_in_left, 0);

        initBaseData();

        initViews();

        initActions();

    }

    private void initViews() {

        setContentView(R.layout.activity_launcher);
        appListRV = findViewById(R.id.app_list_rv);

        int width = getResources().getDisplayMetrics().widthPixels;

        mDecoratioon = new GridSpacingItemDecoration(this, count, getSpaceing(width), true);
        appListRV.addItemDecoration(mDecoratioon);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, count, GridLayoutManager.VERTICAL, false);
        appListRV.setLayoutManager(gridLayoutManager);

        adapter = new AppItemAdapter(this, appModelList);
        appListRV.setAdapter(adapter);
        dector = new SlideGestureDector(this, this);

    }

    private int getSpaceing(int width) {
        Log.i(TAG, "" + width);
        int spaceing = (width - (Utils.dip2px(this, iconWidth)) * count) / (count + 1);
        return spaceing;
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


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        int action = ev.getAction() & MotionEvent.ACTION_MASK;
        if (action == MotionEvent.ACTION_DOWN) {
            clearFinishMessage();
        } else if (action == MotionEvent.ACTION_UP) {
            countDownFinish();
        }
        if (dector.onTouchEvent(ev)) {
            return false;
        }

        return super.dispatchTouchEvent(ev);
    }

    private void initActions() {
        appListRV.setOnTouchListener(new View.OnTouchListener() {
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
                            View childView = appListRV.findChildViewUnder(event.getX(), event.getY());
                            if (childView == null) {
                                exitWithAnimation();
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
        helper.attachToRecyclerView(appListRV);
    }

    @Override
    protected void onResume() {
        super.onResume();
        countDownFinish();
    }

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.i(TAG, "=========onSaveInstanceState===============");
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onSlideChange(boolean isLeft) {
        if (isLeft) {
            exitWithAnimation();
        }
    }

    /**
     * 退出携带动画
     */
    private void exitWithAnimation() {
        finish();
        overridePendingTransition(0, R.anim.slide_out_left);

    }

    private void countDownFinish() {
        clearFinishMessage();
        mHandler.sendEmptyMessageDelayed(MSG_FINISH, TIME_AUTO_FINISH);
    }

    /**
     * 清楚结束消息
     */
    private void clearFinishMessage(){
        mHandler.removeCallbacksAndMessages(null);
    }

}
