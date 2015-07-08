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
 * <p/>
 * headerview 滑动组件
 * 支持连续不间断滑动效果
 */
public class ScrollContainer extends LinearLayout {

    private XmLogger logger = new XmLogger("ScrollContainer");
    private View mHeaderView;
    private View mScrollView;
    private Context mContext;

    private int viewPagerTouchSlop;
    private int mTopViewHeight;

    private boolean mDragging;

    private int headState = STATE_DEFAULT;

    public static final int STATE_DEFAULT = 1; // 默认
    public static final int STATE_MOVING = 2; // 顶部header在滑动
    public static final int STATE_CHANGE_TO_SEE = 3; // header要全部看着
    public static final int STATE_CHANGE_TO_HIDE = 4; // header 要全部消失
    public static final int STATE_CHILD_MOVE = 5; // 子view获取到焦点，在滑动中


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

                if (getScrollY() < mTopViewHeight && Math.abs(dy) > viewPagerTouchSlop) {
                    MotionEvent ev2 = MotionEvent.obtain(ev);
                    ev2.setAction(MotionEvent.ACTION_DOWN);
                    onTouchEvent(ev2);
                    ev2.recycle();
                    return true;
                }

                break;
            case MotionEvent.ACTION_MOVE:
                logger.d("move:dy:" + dy + " and getScrollY:" + getScrollY() + " and headerView height : " + mHeaderView.getMeasuredHeight() + " and headState:" + headState);


                if (Math.abs(dy) > viewPagerTouchSlop) {
                    // 开始滑动了
                    mDragging = true;
                }

                if (mDragging && (headState == STATE_CHANGE_TO_SEE || headState == STATE_CHANGE_TO_HIDE)) {
                    headState = STATE_CHILD_MOVE;
                    return false;
                }

                if (dy < 0 && (headState == STATE_DEFAULT ||
                        headState == STATE_MOVING)) {
                    // 上滑动
                    return true;
                }

                if ((headState == STATE_DEFAULT ||
                        headState == STATE_MOVING) && Math.abs(dy) > Math.abs(dx)) {
                    logger.d("lian:need lanjie");
                    return true;
                }


                break;
            case MotionEvent.ACTION_UP:
                isSend = false;
                headState = STATE_DEFAULT;
                break;
        }

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public void scrollTo(int x, int y) {
        if (y <= 0) {
            headState = STATE_CHANGE_TO_HIDE;
            y = 0;
        }
        if (y >= mTopViewHeight) {
            y = mTopViewHeight;
            headState = STATE_CHANGE_TO_SEE;
        }
        logger.d("enter scrollTo method and headState is :" + headState);
        if (y != getScrollY()) {
            headState = STATE_MOVING;
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

                logger.d("move : dy-" + dy + " and headState" + headState + "and isSend is -" + isSend);

                if (isNeedSendEvent() && !isSend) {
                    isSend = true;
                    logger.d("move : lian:begin dispatch");
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

    public void showTopView() {
        if (getScrollY() <= mTopViewHeight) {
            scrollBy(0, -(mTopViewHeight - getScrollY()));
        }
    }

    private boolean isNeedSendEvent() {
        boolean isNeedSend = false;
        if (headState == STATE_CHANGE_TO_SEE) {
            // 完整拉出来时
            isNeedSend = true;
        }

        if (headState == STATE_CHANGE_TO_HIDE) {
            // 向上时，完全隐藏时
            isNeedSend = true;
        }

        return isNeedSend;
    }

    private boolean isSend = false;
}
