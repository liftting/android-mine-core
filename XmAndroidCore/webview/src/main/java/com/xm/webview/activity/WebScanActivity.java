package com.xm.webview.activity;

import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.FrameLayout;

import com.xm.webview.R;
import com.xm.webview.controller.WebScanHandler;
import com.xm.webview.view.XmWebView;


public class WebScanActivity extends ActionBarActivity implements WebScanHandler {

    private FrameLayout mWebContainer;
    private XmWebView mCurrentView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWebContainer = (FrameLayout) findViewById(R.id.fly_webview_container);

        mCurrentView = new XmWebView(this, this);
        mWebContainer.addView(mCurrentView);

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

    public void toolMenuClick(View v){
        mCurrentView.loadUrl("http://www.baidu.com");
    }

}
