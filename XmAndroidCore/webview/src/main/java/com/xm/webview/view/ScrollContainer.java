package com.xm.webview.view;

import android.content.Context;
import android.graphics.Point;
import android.support.v4.view.ViewConfigurationCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Scroller;

import com.xm.log.base.XmLogger;
import com.xm.webview.R;

/**
 * Created by wm on 15/7/2.
 */
public class ScrollContainer extends LinearLayout {

    private XmLogger logger = new XmLogger("ScrollContainer");
    private View mHeaderView;
    private View mScrollView;
    private Context mContext;

    private int viewPagerTouchSlop;
    private int mTopViewHeight;

    private boolean mDragging;


    private Point mLastPosition = new Point();

    public ScrollContainer(Context context) {
        this(context, null, 0);
    }

    public ScrollContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        ViewConfiguration configuration = ViewConfiguration.get(context);
        viewPagerTouchSlop = configuration.getScaledTouchSlop();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mHeaderView = findViewById(R.id.headerView);
        mScrollView = findViewById(R.id.scorllView);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTopViewHeight = mHeaderView.getMeasuredHeight();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            ey = mLastPosition.y = (int) ev.getY();
            ex = mLastPosition.x = (int) ev.getX();

        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        ViewGroup.LayoutParams params = mScrollView.getLayoutParams();
        // 这样能保证滑动到顶部之后，不会出现下面空白，
        params.height = getMeasuredHeight(); // 设置完整高度

    }

    private float lastY;
    private boolean isTopHidden;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        logger.d("enter onTnerceptTouchEvent");

        float dy = ev.getY() - mLastPosition.y;
        float dx = ev.getX() - mLastPosition.x;

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                logger.d("move:dy:" + dy + " and getScrollY:" + getScrollY() + " and headerView height : " + mHeaderView.getMeasuredHeight() + " and isTopHidden:" + isTopHidden);


                if (Math.abs(dy) > viewPagerTouchSlop) {
                    mDragging = true;
                }


                if (getScrollY() < mTopViewHeight && dy < 0 && Math.abs(dy) > Math.abs(dx)
                        || getScrollY() >= mTopViewHeight && dy > 0 && Math.abs(dy) > Math.abs(dx)) {
                    logger.d("lian:need lanjie");
                    return true;
                }

                break;
        }

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public void scrollTo(int x, int y) {
        logger.d("enter scrollTo method");
        if (y < 0) {
            y = 0;
        }
        if (y > mTopViewHeight) {
            y = mTopViewHeight;
        }
        if (y != getScrollY()) {
            super.scrollTo(x, y);
        }

    }

    private int ex, ey;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float y = event.getY();
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                lastY = event.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                float dy = y - lastY;

                if (!mDragging && Math.abs(dy) > viewPagerTouchSlop) {
                    mDragging = true;
                }

                if (mDragging) {
                    scrollBy(0, (int) -dy);
                    lastY = y;
                }

                if ((getScrollY() >= mTopViewHeight) && !isSend) {
                    isSend = true;
                    logger.d("lian:begin dispatch");
                    mScrollView.dispatchTouchEvent(event);
                }
                break;

            case MotionEvent.ACTION_UP:
                mDragging = false;
                isSend = false;
                break;

        }

        return super.onTouchEvent(event);
    }

    private boolean isNeedSendEvent() {
        if (isSend && getScrollY() < mTopViewHeight - 4) {
            isSend = true;
        }
        return isSend;
    }

    private boolean isSend = false;
}
