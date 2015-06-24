package com.xm.webview.activity;

import android.app.Activity;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.xm.webview.R;
import com.xm.webview.controller.TitleHandler;
import com.xm.webview.controller.WebScanHandler;
import com.xm.webview.util.Constants;
import com.xm.webview.view.AnimatedProgressBar;
import com.xm.webview.view.WebTitleView;
import com.xm.webview.view.XmWebView;

import java.util.ArrayList;
import java.util.List;


public class WebScanActivity extends Activity implements WebScanHandler, TitleHandler {

    private FrameLayout mWebContainer;
    private XmWebView mCurrentView; // 多个

    private WebTitleView mTitleView; // 一个，
    private LinearLayout mHeadContainer;
    private AnimatedProgressBar mProgressBar;

    private final List<XmWebView> mWebViewList = new ArrayList<XmWebView>();


    private String mDefaultTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        init();

    }

    private void init() {
        mWebViewList.clear();


        mDefaultTitle = getString(R.string.default_web_title);
        mTitleView = (WebTitleView) findViewById(R.id.title_view_container);
        mProgressBar = (AnimatedProgressBar) findViewById(R.id.progress_view);
        mWebContainer = (FrameLayout) findViewById(R.id.fly_webview_container);
        mHeadContainer = (LinearLayout) findViewById(R.id.ly_webview_container_title);


        mCurrentView = new XmWebView(this, this);
        mWebContainer.addView(mCurrentView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }


    @Override
    public void onCreateWindow(WebView webView, Message message) {

    }

    @Override
    public boolean onShowCustomView(View view, int requestedOrientation, WebChromeClient.CustomViewCallback callback) {
        return false;
    }

    @Override
    public boolean onShowCustomView(View view, WebChromeClient.CustomViewCallback callback) {
        return false;
    }

    @Override
    public boolean onHideCustomView() {
        return false;
    }

    @Override
    public void showTitleBar() {
        if (mHeadContainer.getVisibility() == View.VISIBLE) return;
        Animation show = AnimationUtils.loadAnimation(this, R.anim.slide_down);
        show.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mHeadContainer.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

        });
        mHeadContainer.startAnimation(show);
    }

    @Override
    public void toggleTitleBar() {
        if (mHeadContainer.getVisibility() != View.VISIBLE) {
            showTitleBar();
        } else {
            hideTitleBar();
        }
    }

    @Override
    public void hideTitleBar() {
        if (mHeadContainer.getVisibility() == View.GONE) return;
        Animation show = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        show.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mHeadContainer.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

        });
        mHeadContainer.startAnimation(show);
    }

    @Override
    public void onLongPress() {

    }

    @Override
    public void updateUrl(String title, boolean shortUrl) {
        if (title == null || mTitleView == null) {
            return;
        }
        if (shortUrl && !title.startsWith(Constants.FILE)) {
            if (mCurrentView != null && !mCurrentView.getTitle().isEmpty()) {
                mTitleView.setTitle(mCurrentView.getTitle());
            } else {
                mTitleView.setTitle(mDefaultTitle);
            }

        } else {
            if (title.startsWith(Constants.FILE)) {
                title = "";
            }
            mTitleView.setTitle(title);
        }
    }

    @Override
    public void updateProgress(int n) {
        mProgressBar.setProgress(n);
    }

    @Override
    public void updateHistory(String title, String url) {

    }

    public void toolMenuClick(View v) {

        mCurrentView.loadUrl("http://www.baidu.com");
    }

    @Override
    public void onWebAddressCollect(boolean isCollect) {

    }

    @Override
    public void onBackPressed() {
        if (mCurrentView != null) {
            if (mCurrentView.canGoBack()) {
                mCurrentView.back();
            } else {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

    public void onToolGoBack(View v) {
        if (mCurrentView.canGoBack()) {
            mCurrentView.back();
        }
    }

    public void onToolGoForward(View v) {
        if (mCurrentView.canGoForward()) {
            mCurrentView.goForward();
        }
    }

    public void onToolGoHome(View v) {

    }

    public void onToolMultiWindows(View v) {

    }

    public void onToolMenu(View v) {

    }
}
