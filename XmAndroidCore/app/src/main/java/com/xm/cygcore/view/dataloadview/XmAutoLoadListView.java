package com.xm.cygcore.view.dataloadview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * Created by wm on 15/4/27.
 */
public class XmAutoLoadListView extends ListView implements AbsListView.OnScrollListener {

    private Context mContext;
    private XmLoadFooterView mLoadFooterView;
    private XmLoadHeaderView mLoadHeaderView;

    public XmAutoLoadListView(Context context) {
        super(context);
    }

    public XmAutoLoadListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public XmAutoLoadListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(Context context) {
        mContext = context;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
}
