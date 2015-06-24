package com.xm.webview.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Message;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.xm.log.base.XmLogger;
import com.xm.webview.R;
import com.xm.webview.controller.WebScanHandler;
import com.xm.webview.controller.XmScanListener;

import java.util.HashMap;
import java.util.Map;


/**
 *
 */
public class XmWebView extends WebView {

    private static final XmLogger logger = new XmLogger("XmWebView");

    private Context mContext;
    private XmWebViewClient mWebViewClient;
    private XmWebViewChromeClient mWebChromeClient;
    private WebScanHandler mScanHandler;
    private XmScanListener mScanListener;
    private WebSettings mWebSettings;

    private Title mTitle;
    private Map<String, String> mTitleMap = new HashMap<String, String>();

    private GestureDetector gestureDetector;

    private static final int API = android.os.Build.VERSION.SDK_INT;

    public XmWebView(Context context, WebScanHandler scanHandler) {
        super(context);
        this.mContext = context;

        mWebViewClient = new XmWebViewClient(this);
        mWebChromeClient = new XmWebViewChromeClient(this);

//        mTitleView = new WebTitleView(mContext, this);
        mScanHandler = scanHandler;
        mTitle = new Title(mContext);

        gestureDetector = new GestureDetector(new CustomGestureListener());
        mScanListener = new XmScanListener(this, mScanHandler, gestureDetector);

        initWebSettings();

    }

    private synchronized void initWebSettings() {
        setDrawingCacheBackgroundColor(0x00000000);
        setFocusableInTouchMode(true);
        setFocusable(true);
        setAnimationCacheEnabled(false);
        setDrawingCacheEnabled(false);
        setWillNotCacheDrawing(true);
        setAlwaysDrawnWithCacheEnabled(false);
        setBackgroundColor(mContext.getResources().getColor(android.R.color.white));

        if (API > 15) {
            setBackground(null);
            getRootView().setBackground(null);
        } else if (getRootView() != null) {
            getRootView().setBackgroundDrawable(null);
        }
        setScrollbarFadingEnabled(true);
        setSaveEnabled(true);
        setWebChromeClient(mWebChromeClient);
        setWebViewClient(mWebViewClient);

        setOnTouchListener(mScanListener);

//        mDefaultUserAgent = getSettings().getUserAgentString();
        mWebSettings = getSettings();

        initializeSettings(getSettings(), mContext);
//        initializePreferences(activity);

        String defaultUrl = "http://www.baidu.com";

        if (defaultUrl != null) {
            if (!defaultUrl.trim().isEmpty()) {
                loadUrl(defaultUrl);
            } else {
                // don't load anything, the user is looking for a blank tab
            }
        } else {

        }
    }

    public void initializeSettings(WebSettings settings, Context context) {
        if (API < 18) {
            settings.setAppCacheMaxSize(Long.MAX_VALUE);
        }
        if (API < 17) {
            settings.setEnableSmoothTransition(true);
        }
        if (API > 16) {
            settings.setMediaPlaybackRequiresUserGesture(true);
        }
        if (API >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        } else if (API >= Build.VERSION_CODES.LOLLIPOP) {
            // We're in Incognito mode, reject
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_NEVER_ALLOW);
        }
        settings.setDomStorageEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setDatabaseEnabled(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setAllowContentAccess(true);
        settings.setAllowFileAccess(true);
        settings.setDefaultTextEncodingName("utf-8");
        settings.setJavaScriptEnabled(true);
        if (API > 16) {
            settings.setAllowFileAccessFromFileURLs(false);
            settings.setAllowUniversalAccessFromFileURLs(false);
        }

        settings.setAppCachePath(context.getDir("appcache", 0).getPath());
        settings.setGeolocationDatabasePath(context.getDir("geolocation", 0).getPath());
        if (API < Build.VERSION_CODES.KITKAT) {
            settings.setDatabasePath(context.getDir("databases", 0).getPath());
        }
    }

    // 处理title一些信息
    public class Title {

        private String mTitle;

        public Title(Context context) {
            mTitle = mContext.getString(R.string.default_web_title);
        }


        public void setTitle(String title) {
            if (title == null) {
                mTitle = "";
            } else {
                mTitle = title;
            }
        }

        public String getTitle() {
            return mTitle;
        }


    }

    // webViewClient
    private class XmWebViewClient extends WebViewClient {

        private XmWebView webView;
        private static final String INTENT_TYPE_MESSAGE_RFC822 = "message/rfc822";

        public XmWebViewClient(XmWebView webView) {
            this.webView = webView;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            logger.d("webLoading:onPageFinished");
            super.onPageFinished(view, url);

            if (view.getTitle() == null || view.getTitle().isEmpty()) {
                mTitle.setTitle(mContext.getString(R.string.default_web_title));
            } else {
                mTitle.setTitle(view.getTitle());
            }

            if (view.isShown()) {
                mScanHandler.updateUrl(url, true);
                view.postInvalidate();
            }

            logger.d("webLoading:onPageFinished is " + view.getTitle());


//            if (API >= android.os.Build.VERSION_CODES.KITKAT && mInvertPage) {
//                view.evaluateJavascript(Constants.JAVASCRIPT_INVERT_PAGE, null);
//            }

//            mBrowserController.update();

        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            logger.d("webLoading:onPageStarted");
            super.onPageStarted(view, url, favicon);
            if (view.isShown()) {
                mScanHandler.updateUrl(url, false); // 处理显示的title
                mScanHandler.showTitleBar();
            }

        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            logger.d("webLoading:shouldOverrideUrlLoading");
            return super.shouldOverrideUrlLoading(view, url);

        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            //资源请求处理，可以我们先本地请求，
            logger.d("webLoading:shouldInterceptRequest");
            return super.shouldInterceptRequest(view, url);
        }

        @Override
        public void onFormResubmission(WebView view, Message dontResend, Message resend) {
            // 表单重新发送
            logger.d("webLoading:onFormResubmission");
            super.onFormResubmission(view, dontResend, resend);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            // 证书错误，ssl
            logger.d("webLoading:onReceivedSslError");
            super.onReceivedSslError(view, handler, error);
        }

        @Override
        public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
            logger.d("webLoading:onReceivedHttpAuthRequest");

        }
    }


    private class XmWebViewChromeClient extends WebChromeClient {
        private XmWebView webView;

        public XmWebViewChromeClient(XmWebView view) {
            this.webView = view;
        }

        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
            //请求创建一个新窗口
            mScanHandler.onCreateWindow(view, resultMsg);
            return true;
        }

        @Override
        public void onCloseWindow(WebView window) {
            super.onCloseWindow(window);
        }

        @Override
        public void onReceivedIcon(WebView view, Bitmap icon) {
            super.onReceivedIcon(view, icon);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {

            if (!title.isEmpty()) {
                mTitle.setTitle(title);
            } else {
                mTitle.setTitle(mContext.getString(R.string.default_web_title));
            }
            mScanHandler.updateUrl(title, true);
//            mScanHandler.update();
            logger.d("webLoading:onReceivedTitle is " + mTitle.getTitle());
            mScanHandler.updateHistory(title, view.getUrl());

        }


        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            //通知webview 要显示一个custom view ，主要是视频全屏 h5 support
            mScanHandler.onShowCustomView(view, callback);
            super.onShowCustomView(view, callback);
        }

        @Override
        public void onShowCustomView(View view, int requestedOrientation, CustomViewCallback callback) {
            mScanHandler.onShowCustomView(view, requestedOrientation, callback);
            super.onShowCustomView(view, requestedOrientation, callback);
        }

        @Override
        public void onHideCustomView() {
            mScanHandler.onHideCustomView();
            super.onHideCustomView();
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (isShown()) {
                mScanHandler.updateProgress(newProgress);
            }
        }
    }

    private class CustomGestureListener extends GestureDetector.SimpleOnGestureListener {

        /**
         * Without this, onLongPress is not called when user is zooming using
         * two fingers, but is when using only one.
         * <p/>
         * The required behaviour is to not trigger this when the user is
         * zooming, it shouldn't matter how much fingers the user's using.
         */
        private boolean mCanTriggerLongPress = true;

        @Override
        public void onLongPress(MotionEvent e) {
            if (mCanTriggerLongPress)
                mScanHandler.onLongPress();
        }

        /**
         * Is called when the user is swiping after the doubletap, which in our
         * case means that he is zooming.
         */
        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            mCanTriggerLongPress = false;
            return false;
        }

        /**
         * Is called when something is starting being pressed, always before
         * onLongPress.
         */
        @Override
        public void onShowPress(MotionEvent e) {
            mCanTriggerLongPress = true;
        }
    }

    public synchronized void onDestroy() {
        stopLoading();
        onPause();
        clearHistory();
        setVisibility(View.GONE);
        removeAllViews();
        destroyDrawingCache();
        // destroy(); //this is causing the segfault
    }

    public synchronized void back() {
        goBack();
    }

    public synchronized void forward() {
        goForward();
    }

}
