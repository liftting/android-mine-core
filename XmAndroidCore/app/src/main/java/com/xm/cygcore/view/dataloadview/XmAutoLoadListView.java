package com.xm.cygcore.view.dataloadview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * Created by wm on 15/4/27.
 */
public class XmAutoLoadListView extends ListView implements AbsListView.OnScrollListener {

    private Context mContext;
    //    private XmLoadFooterView mLoadFooterView;
    private XmLoadHeaderView mLoadHeaderView;
    private int mHeadViewHeight;

    private float mLastY;

    public XmAutoLoadListView(Context context) {
        super(context);
    }

    public XmAutoLoadListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public XmAutoLoadListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        mLoadHeaderView = new XmLoadHeaderView(mContext);

        addHeaderView(mLoadHeaderView);
        mLoadHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mHeadViewHeight = mLoadHeaderView.getHeight();
                getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });


    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                float y = ev.getRawY();
                float yDis = y - mLastY;
                mLastY = y;

                if (getFirstVisiblePosition() == 0 && yDis > 0) {
                    updateHeadViewHeight((int) yDis);
                }


                break;
            case MotionEvent.ACTION_UP:
                break;
        }

        return super.onTouchEvent(ev);
    }

    private void updateHeadViewHeight(int dis) {
        mLoadHeaderView.updateHeight(mLoadHeaderView.getVisibleHeight() + dis);

        if (mLoadHeaderView.getVisibleHeight() >= mHeadViewHeight) {
            // release to refresh
            mLoadHeaderView.updateStateInfo(XmLoadHeaderView.STATE_PULLING);
        } else {
            mLoadHeaderView.updateStateInfo(XmLoadHeaderView.STATE_IDEL);
        }

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
}
