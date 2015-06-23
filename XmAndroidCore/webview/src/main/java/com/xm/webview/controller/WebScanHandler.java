package com.xm.webview.controller;

import android.os.Message;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.xm.webview.view.XmWebView;

/**
 * Created by wm on 15/6/23.
 */
public interface WebScanHandler {

    public void onCreateWindow(WebView webView, Message message);

    public boolean onShowCustomView(View view, int requestedOrientation, WebChromeClient.CustomViewCallback callback);

    public boolean onShowCustomView(View view, WebChromeClient.CustomViewCallback callback);

    public boolean onHideCustomView();

    public void showTitleBar();

    public void toggleTitleBar();

    public void hideTitleBar();

    public void onLongPress();
}
