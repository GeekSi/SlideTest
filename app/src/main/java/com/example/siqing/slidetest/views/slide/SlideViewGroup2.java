package com.example.siqing.slidetest.views.slide;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

import com.example.siqing.slidetest.utils.Utils;


public class SlideViewGroup2 extends FrameLayout {
    public String TAG = "SlideViewGroup2";
    private View targetView;
    private int screenWidth, screenHeight;

    private SlideGroupListener slideGroupListener;

    private final int STATE_CLOSE = 1;
    private final int STATE_SLIDING = 2;
    private final int STATE_OPEN = 3;

    private int currentState = STATE_CLOSE;

    private float startX = 0;

    private ViewDragHelper viewDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            Log.i(TAG, "tryCaptureView ===currentState== " + currentState);

            if (currentState == STATE_SLIDING) {
                return true;
            } else if (currentState == STATE_OPEN) {
                if (viewDragHelper.isEdgeTouched(ViewDragHelper.EDGE_RIGHT)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public void onViewCaptured(View capturedChild, int activePointerId) {
            Log.i(TAG, "onViewCaptured === ");
            super.onViewCaptured(capturedChild, activePointerId);
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            Log.i(TAG, "onViewReleased ==left==" + releasedChild.getLeft() + "===xvel===" + xvel + "==" + yvel);

            if (Math.abs(xvel) > 600) {
                if (xvel > 0) {
                    openPanelAuto();
                } else {
                    closePanelAuto();
                }
            } else {
                int left = releasedChild.getLeft();
                int splitWidth = releasedChild.getWidth() / 2;
                if (left < -splitWidth) {
                    closePanelAuto();
                } else {
                    openPanelAuto();
                }
            }
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
//            Log.i(TAG, "clampViewPositionHorizontal =left== " + left + "=dx=" + dx);
            int result = left > 0 ? 0 : left;
            return result;
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
//            Log.i(TAG, "onViewPositionChanged =left== " + left);
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            ViewCompat.postInvalidateOnAnimation(SlideViewGroup2.this);
        }

        @Override
        public void onViewDragStateChanged(int state) {
            super.onViewDragStateChanged(state);
            if (state == ViewDragHelper.STATE_IDLE) {
                Log.i(TAG, "onViewDragStateChanged =targetView== " + targetView.getLeft());
                if (targetView.getLeft() == -targetView.getWidth()) {
                    getSlideGroupListener().onSlideClose();
                    currentState = STATE_CLOSE;
                } else {
                    getSlideGroupListener().onSlideOpen();
                    currentState = STATE_OPEN;
                }
            }
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            Log.i(TAG, "========getViewHorizontalDragRange ========= ");
            return 1;
        }

    });

    public SlideViewGroup2(@NonNull Context context) {
        this(context, null);
    }

    public SlideViewGroup2(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setSlideGroupListener(SlideGroupListener slideGroupListener) {
        this.slideGroupListener = slideGroupListener;
    }

    public SlideGroupListener getSlideGroupListener() {
        if (slideGroupListener == null) {
            slideGroupListener = new SimpleSlideGroupListener();
        }
        return slideGroupListener;
    }

    private void init() {
        screenWidth = Utils.getScreenWidth(getContext());
        screenHeight = Utils.getScreenHeight(getContext());


    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        Log.i(TAG, "onLayout =state= " + currentState + " = change == " + changed + "=left=" + left + "=top=" + top + "=right=" + right + "=bottom=" + bottom);
        if (currentState == STATE_CLOSE) {
            int childLeft = left - targetView.getMeasuredWidth();
            int childRight = childLeft + targetView.getMeasuredWidth();
            targetView.layout(childLeft, top, childRight, bottom);
        } else if (currentState == STATE_OPEN) {
//            super.onLayout(changed, left, top, right, bottom);
            targetView.layout(left, top, right, bottom);
        } else if (currentState == STATE_SLIDING) {
            int childLeft = left - targetView.getMeasuredWidth();
            int childRight = childLeft + targetView.getMeasuredWidth();
            targetView.layout(childLeft, top, childRight, bottom);
        } else {
            super.onLayout(changed, left, top, right, bottom);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ensureTargetView();
        Log.i(TAG, "onFinishInflate === " + targetView.toString());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.i(TAG, "onMeasure ===screenWidth== " + screenWidth + "==screenHeight==" + screenHeight);
        targetView.measure(MeasureSpec.makeMeasureSpec(screenWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(screenHeight, MeasureSpec.EXACTLY));
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (viewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public void closePanelByHandle() {
        ValueAnimator animator = ValueAnimator.ofInt(0, -targetView.getWidth());
        animator.setDuration(500);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
//                targetView.layout(value, 0, (value + targetView.getWidth()), targetView.getBottom());
                targetView.setTranslationX(value);
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                currentState = STATE_CLOSE;
                getSlideGroupListener().onSlideClose();
//                targetView.layout(-targetView.getWidth(), 0, 0, targetView.getBottom());
                targetView.setTranslationX(0);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        animator.start();
    }

    private void closePanelAuto() {
        viewDragHelper.settleCapturedViewAt(-targetView.getWidth(), 0);
        ViewCompat.postInvalidateOnAnimation(SlideViewGroup2.this);
    }

    private void openPanelAuto() {
        viewDragHelper.settleCapturedViewAt(0, 0);
        ViewCompat.postInvalidateOnAnimation(SlideViewGroup2.this);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        Log.i(TAG, "onInterceptSSSTouchEvent === " + event.getAction() + ", x == " + event.getX() + "==state==" + currentState);
        boolean shouldIntercept = viewDragHelper.shouldInterceptTouchEvent(event);
        if (currentState == STATE_OPEN && shouldIntercept == true) {
            return true;
        }
        shouldIntercept = super.onInterceptTouchEvent(event);
//        Log.i(TAG, "onInterceptSSSTouchEvent ===shouldIntercept=== " + shouldIntercept);
        return shouldIntercept;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = MotionEvent.ACTION_MASK & event.getAction();
        Log.i(TAG, "onTouchEvent === " + action + "state == " + currentState);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                if (currentState == STATE_CLOSE) {
                    float disX = event.getX() - startX;
                    if (disX > ViewConfiguration.getTouchSlop()) {
                        Log.i(TAG, "currentState == STATE_CLOSE ===TO===SLIDING ");
                        getSlideGroupListener().onTouchSlideRight();
                        currentState = STATE_SLIDING;
                        viewDragHelper.captureChildView(targetView, 0);
                    }
                } else if (currentState == STATE_SLIDING) {
                } else if (currentState == STATE_OPEN) {
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        viewDragHelper.processTouchEvent(event);
        return super.onTouchEvent(event);
    }

    private View ensureTargetView() {
        if (targetView == null) {
            int childCount = getChildCount();
            if (childCount > 0) {
                targetView = getChildAt(0);
            }
        }
        return targetView;
    }
}