package com.xm.webview.view;

import android.content.Context;
import android.support.v4.view.ViewConfigurationCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

import com.xm.log.base.XmLogger;
import com.xm.webview.util.XmWebUtils;

/**
 * Created by wm on 15/7/1.
 */
public class SwipeBackView extends FrameLayout {

    private OnOperateListener mOperateListener;
    private GestureDetector mGestureDetector;
    private OnOperateGestureListener mgestureListener;
    private Context mContext;

    private boolean isIntercept;

    private XmLogger logger = new XmLogger("swipeBackView");

    private int ex, ey;
    private int touchSlop;
    private int screenWidth = XmWebUtils.getScreenWidth();
    private int moveDis = (int) (XmWebUtils.getScreenWidth() * 0.2);

    public SwipeBackView(Context context) {
        this(context, null, 0);
    }

    public SwipeBackView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeBackView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        mgestureListener = new OnOperateGestureListener();
        mGestureDetector = new GestureDetector(mContext, mgestureListener);

        ViewConfiguration configuration = ViewConfiguration.get(context);
        touchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration); // 滑动时，大于这个距离开始移动
    }

    public void setOperateListener(OnOperateListener listener) {
        this.mOperateListener = listener;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mGestureDetector != null) {
            if (mGestureDetector.onTouchEvent(ev))
                //If the gestureDetector handles the event, a swipe has been executed and no more needs to be done.
                return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mOperateListener != null) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    ex = (int) ev.getX();
                    ey = (int) ev.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float dx = ev.getX() - ex;
                    float dy = ev.getY() - ey;

                    if (isEdge() && Math.abs(dx) > Math.abs(dy) && Math.abs(dx) > touchSlop) {
                        logger.d("can intercept and move dx:" + Math.abs(dx) + " and nees touchSlop:" + touchSlop);
                        isIntercept = true;
                        return true;
                    }

                    break;
            }
        }
        return super.onInterceptTouchEvent(ev);
    }

    private boolean isEdge() {
        if (ex <= 100 || ex >= screenWidth - 100) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return true;
    }

    private class OnOperateGestureListener extends GestureDetector.SimpleOnGestureListener {
        protected MotionEvent mLastOnDownEvent = null;

        @Override
        public boolean onDown(MotionEvent e) {
            mLastOnDownEvent = e;
            return super.onDown(e);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            logger.d("enter onFilng");

            if (e1 == null) {
                e1 = mLastOnDownEvent;
            }

            if (e1 == null || e2 == null) {
                return false;
            }

            if (!isIntercept) return false;

            logger.d("onFling and distance is:" + (e2.getX() - e1.getX()));

            // 向右滑动， 后退
            if (e2.getX() - e1.getX() > moveDis) {
                if (mOperateListener != null) {
                    logger.d("enter onback");
                    isIntercept = false;
                    mOperateListener.onBack();
                    return true;
                }
            } else if (e1.getX() - e2.getX() > moveDis) {
                if (mOperateListener != null) {
                    logger.d("enter onForward");
                    isIntercept = false;
                    mOperateListener.onForward();
                    return true;
                }
            }

            return false;
        }
    }


    public static interface OnOperateListener {

        public void onBack();

        public void onForward();
    }
}
