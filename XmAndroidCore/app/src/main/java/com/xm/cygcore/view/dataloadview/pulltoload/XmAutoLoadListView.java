package com.xm.cygcore.view.dataloadview.pulltoload;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Scroller;

/**
 * Created by wm on 15/4/27.
 */
public class XmAutoLoadListView extends ListView implements AbsListView.OnScrollListener {

    private static final String TAG = "XmAutoLoadListView";

    private Context mContext;
    //    private XmLoadFooterView mLoadFooterView;
    private XmLoadHeaderView mLoadHeaderView;
    private Scroller mScroller;
    private int mHeadViewHeight;

    private float mLastY;
    private boolean isRefreshing; // 正在刷新状态

    private DataLoadingListener mDataLoadingListener;

    private final static float OFFSET_RADIO = 1.6f;


    public XmAutoLoadListView(Context context) {
        this(context, null, 0);
    }

    public XmAutoLoadListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XmAutoLoadListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        mScroller = new Scroller(mContext);
        mLoadHeaderView = new XmLoadHeaderView(mContext);

        addHeaderView(mLoadHeaderView);
        mLoadHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mHeadViewHeight = mLoadHeaderView.getContentHeaderHeight();
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

                if (getFirstVisiblePosition() == 0 && (mLoadHeaderView.getCurrentHeight() > 0 || yDis > 0)) {
                    updateHeadViewHeight((int) (yDis / OFFSET_RADIO));
                }


                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (getFirstVisiblePosition() == 0) {
                    if (!isRefreshing && mLoadHeaderView.getCurrentHeight() > mHeadViewHeight) {
                        // all has pull to see
                        isRefreshing = true;
                        mLoadHeaderView.updateStateInfo(XmLoadHeaderView.STATE_REFRESH);

                        // call back
                        if (mDataLoadingListener != null) {

                            mDataLoadingListener.onRefresh();
                        }


                    }
                    resetHeaderView();
                }

                break;
        }

        return super.onTouchEvent(ev);
    }

    private void updateHeadViewHeight(int dis) {

        Log.w(TAG, "update head view height:" + mLoadHeaderView.getCurrentHeight());

        //  在刷新状态时，不可以在继续下拉，要保持当前的高度值，可以进行上滑动操作
        if (isRefreshing) return;


        mLoadHeaderView.updateHeight(mLoadHeaderView.getCurrentHeight() + dis);



        if (mLoadHeaderView.getCurrentHeight() >= mHeadViewHeight) {
            // release to refresh
            mLoadHeaderView.updateStateInfo(XmLoadHeaderView.STATE_PULLING);
        } else {
            mLoadHeaderView.updateStateInfo(XmLoadHeaderView.STATE_IDEL);
        }

        setSelection(0);

    }

    private void resetHeaderView() {
        int height = mLoadHeaderView.getCurrentHeight();
        if (height <= 0) return;

        if (isRefreshing && height < mHeadViewHeight) return;


        int needHeight = 0;

        if (!isRefreshing) {
            // not scroll all 没达到下拉释放刷新状态时，
            needHeight = -height;
        } else if (isRefreshing && height > mHeadViewHeight) {
            needHeight = mHeadViewHeight - height;
        }

//        this.scrollTo();
        mScroller.startScroll(0, height, 0, needHeight, 500);

        postInvalidate();

    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            mLoadHeaderView.updateHeight(mScroller.getCurrY());
        }
        super.computeScroll();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    public void stopRefresh() {
        if (isRefreshing) {
            isRefreshing = false;
            resetHeaderView();
        }
    }

    public void setDatLoadingListener(DataLoadingListener listener) {
        mDataLoadingListener = listener;
    }

    public interface DataLoadingListener {

        void onRefresh();

//        void onLoadMore();
    }

}
