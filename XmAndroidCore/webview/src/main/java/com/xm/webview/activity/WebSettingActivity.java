package com.xm.webview.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.TextView;

import com.xm.ui.switchbutton.SwitchButton;
import com.xm.webview.R;
import com.xm.webview.controller.WebScanHandler;
import com.xm.webview.view.ScrollContainer;
import com.xm.webview.view.XmWebView;

/**
 * Created by wm on 15/6/25.
 */
public class WebSettingActivity extends Activity implements WebScanHandler {

    private ScrollContainer mContainView;
    private TextView mHeaderView;
    private ViewGroup mScrollView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_web_setting_test);
        mContainView = (ScrollContainer) findViewById(R.id.scrollContainerView);
        mHeaderView = (TextView) findViewById(R.id.headerView);
        mScrollView = (ViewGroup) findViewById(R.id.scorllView);

        XmWebView xmWebView = new XmWebView(this, this);
        mScrollView.addView(xmWebView);

        xmWebView.loadUrl("http://www.baidu.com");

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

    }

    @Override
    public void toggleTitleBar() {

    }

    @Override
    public void hideTitleBar() {

    }

    @Override
    public void onLongPress() {

    }

    @Override
    public void updateUrl(String title, boolean shortUrl) {

    }

    @Override
    public void updateProgress(int n) {

    }

    @Override
    public void updateHistory(String title, String url) {

    }

    @Override
    public Bitmap getDefaultVideoPoster() {
        return null;
    }

    @Override
    public View getVideoLoadingProgressView() {
        return null;
    }
}
