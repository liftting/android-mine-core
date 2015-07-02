package com.xm.webview.controller;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.xm.webview.util.XmWebUtils;
import com.xm.webview.view.XmWebView;

/**
 * Created by wm on 15/6/23.
 */
public class XmScanListener implements View.OnTouchListener {

    private XmWebView mWebView;
    private WebScanHandler mScanHandler;
    private GestureDetector mGestureDetector;

    private static final int SCROLL_UP_THRESHOLD = XmWebUtils.convertDpToPixels(10);
    private static final int SCROLL_DOWN_THRESHOLD = XmWebUtils.convertDpToPixels(100);

    float mLocation;
    float mY;
    int mAction;

    public XmScanListener(XmWebView webView, WebScanHandler scanHandler, GestureDetector gestureDetector) {
        this.mWebView = webView;
        this.mScanHandler = scanHandler;
        this.mGestureDetector = gestureDetector;
    }

    @Override
    public boolean onTouch(View view, MotionEvent arg1) {
        if (view != null && !view.hasFocus()) {
            view.requestFocus();
        }
        mAction = arg1.getAction();
        mY = arg1.getY();
        if (mAction == MotionEvent.ACTION_DOWN) {
            mLocation = mY;
        } else if (mAction == MotionEvent.ACTION_UP) {
            if ((mY - mLocation) > SCROLL_DOWN_THRESHOLD) {
                if (mWebView.getScrollY() != 0) {
                    mScanHandler.showTitleBar();
                } else {
                    //滑动到顶部时，
//                    mScanHandler.toggleTitleBar();
                }
            } else if ((mY - mLocation) < -SCROLL_UP_THRESHOLD) {
                mScanHandler.hideTitleBar();
            }
            mLocation = 0;
        }
        mGestureDetector.onTouchEvent(arg1);
        return false;
    }
}
