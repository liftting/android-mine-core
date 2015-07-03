package com.xm.webview.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.MailTo;
import android.net.http.SslError;
import android.os.Build;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.HttpAuthHandler;
import android.webkit.JsPromptResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.xm.log.base.XmLogger;
import com.xm.utils.NetWorkUtil;
import com.xm.utils.PreferencesUtil;
import com.xm.utils.jsbridge.InjectedChromeClient;
import com.xm.webview.R;
import com.xm.webview.controller.WebDownLoadListener;
import com.xm.webview.controller.WebJsScope;
import com.xm.webview.controller.WebScanHandler;
import com.xm.webview.controller.XmScanListener;
import com.xm.webview.util.Constants;
import com.xm.webview.util.IntentUtils;
import com.xm.webview.util.XmAdverIntercept;
import com.xm.webview.util.XmWebUtils;

import java.io.ByteArrayInputStream;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;


/**
 *
 */
public class XmWebView extends WebView {

    private static final XmLogger logger = new XmLogger("XmWebView");

    private Activity mContext;
    private XmWebViewClient mWebViewClient;
    private XmWebViewChromeClient mWebChromeClient;
    private WebScanHandler mScanHandler;
    private XmScanListener mScanListener;
    private WebSettings mWebSettings;
    private PreferencesUtil mPreferenceUtil;

    private Title mTitle;
    private Map<String, String> mTitleMap = new HashMap<String, String>();

    private GestureDetector gestureDetector;
    private IntentUtils mIntentUtils;
    private XmAdverIntercept mAdverIntercept;

    private static final int API = android.os.Build.VERSION.SDK_INT;
    private static final String APP_CACHE_DIRNAME = "XMWEB";

    public XmWebView(Activity context, WebScanHandler scanHandler) {
        super(context);
        this.mContext = context;

        mWebViewClient = new XmWebViewClient(this);
        mWebChromeClient = new XmWebViewChromeClient(this, Constants.JS_INJECT_NAME, WebJsScope.class);

//        mTitleView = new WebTitleView(mContext, this);
        mScanHandler = scanHandler;
        mTitle = new Title(mContext);

        gestureDetector = new GestureDetector(new CustomGestureListener());
        mScanListener = new XmScanListener(this, mScanHandler, gestureDetector);
        mIntentUtils = new IntentUtils(mContext);
        mAdverIntercept = XmAdverIntercept.getInstance(mContext);
        mPreferenceUtil = PreferencesUtil.getInstance(mContext);

        initWebSettings();

    }

    private synchronized void initWebSettings() {
        setFocusableInTouchMode(true);
        setFocusable(true);
        setAnimationCacheEnabled(false);

        //这里要做屏幕截图，所以这里的cache要打开，
        setDrawingCacheBackgroundColor(0x00000000);
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


//        mDefaultUserAgent = getSettings().getUserAgentString();
        mWebSettings = getSettings();

        initializeSettings(getSettings(), mContext);
//        initializePreferences(activity);

        setWebChromeClient(mWebChromeClient);
        setWebViewClient(mWebViewClient);
        setOnTouchListener(mScanListener);
        //
        setDownloadListener(new WebDownLoadListener(mContext));

        String defaultUrl = Constants.LOAD_DEFAULT_URL;

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

        //缓存
        settings.setDomStorageEnabled(true);
        settings.setAppCacheEnabled(true);
        if (NetWorkUtil.hasNetWork(mContext)) {
            settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        } else {
            settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }
        settings.setDatabaseEnabled(true);
        settings.setAppCachePath(context.getDir("appcache", 0).getPath());
        settings.setGeolocationDatabasePath(context.getDir("geolocation", 0).getPath());
        if (API < Build.VERSION_CODES.KITKAT) {
            settings.setDatabasePath(context.getDir("databases", 0).getPath());
        }


        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setAllowContentAccess(true);
        settings.setAllowFileAccess(true);
        settings.setDefaultTextEncodingName("utf-8");

        // js support
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);


        if (API > 16) {
            settings.setAllowFileAccessFromFileURLs(false);
            settings.setAllowUniversalAccessFromFileURLs(false);
        }

        settings.setUserAgentString(
                "Mozilla/5.0 (iPhone; CPU iPhone OS 7_1 like Mac OS X) AppleWebKit/537.51.2 (KHTML, like Gecko) Version/7.0 Mobile/11D5145e Safari/9537.53");

        // 文本重排 处理缩放时，文字自动重排功能

        initPreferenceSetting(settings);

        jsSetting();

    }

    private void jsSetting() {
        this.addJavascriptInterface(new InJavaScriptLocalObj(), "js_method");
    }

    final class InJavaScriptLocalObj {
        public void showSource(String html) {
            logger.d("this is html page:" + html);
        }
    }

    private void initPreferenceSetting(WebSettings settings) {
        //设置文本文字大小
        int sizeTag = mPreferenceUtil.getInt(Constants.SP_WEB_TEXTSIZE_NAME);
        sizeTag = 3;
        // api need 14
        switch (sizeTag) {
            case 1:
                settings.setTextZoom(200);
                break;
            case 2:
                settings.setTextZoom(150);
                break;
            case 3:
                settings.setTextZoom(100);
                break;
            case 4:
                settings.setTextZoom(75);
                break;
            case 5:
                settings.setTextZoom(50);
                break;
        }

//        mPreferenceUtil.putBoolean(Constants.SP_WEB_NOIMG,true);
        settings.setBlockNetworkImage(mPreferenceUtil.getBoolean(Constants.SP_WEB_NOIMG)); // 配置是否显示图片

        settings.setSupportMultipleWindows(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);

        // 设置flash (这个是启用adobe flash , 4.4 and more 过时，没有效果)
        if (API < 19) {
            settings.setPluginState(WebSettings.PluginState.ON);
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

            view.loadUrl("javascript:window.js_method.showSource('22')"); // iqiyi
            super.onPageFinished(view, url);


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

            // Check if configured proxy is available
//            if (!mBrowserController.isProxyReady()) {
//                // User has been notified
//                return true;
//            }

//            if (mBrowserController.isIncognito()) {
//                return super.shouldOverrideUrlLoading(view, url);
//            }
            if (url.startsWith("about:")) {
                return super.shouldOverrideUrlLoading(view, url);
            }
            if (url.contains("mailto:")) {
                MailTo mailTo = MailTo.parse(url);
                Intent i = XmWebUtils.newEmailIntent(mailTo.getTo(), mailTo.getSubject(),
                        mailTo.getBody(), mailTo.getCc());
                mContext.startActivity(i);
                view.reload();
                return true;
            } else if (url.startsWith("intent://")) {
                Intent intent;
                try {
                    intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                } catch (URISyntaxException ex) {
                    return false;
                }
                if (intent != null) {
                    try {
                        mContext.startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        logger.e("ActivityNotFoundException");
                    }
                    return true;
                }
            }
            return mIntentUtils.startActivityForUrl(XmWebView.this, url);

        }

        @Override
        public void onScaleChanged(WebView view, float oldScale, float newScale) {
            super.onScaleChanged(view, oldScale, newScale);

        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            if (mAdverIntercept.isAd(request.getUrl().getHost())) {
                //是广告
                ByteArrayInputStream EMPTY = new ByteArrayInputStream("广告".getBytes());
                return new WebResourceResponse("text/plain", "utf-8", EMPTY);
            }
            return super.shouldInterceptRequest(view, request);
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            //资源请求处理，可以我们先本地请求，
            logger.d("webLoading:shouldInterceptRequest");
            if (mAdverIntercept.isAd(url)) {
                //是广告
                ByteArrayInputStream EMPTY = new ByteArrayInputStream("广告".getBytes());
                return new WebResourceResponse("text/plain", "utf-8", EMPTY);
            }
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


    private class XmWebViewChromeClient extends InjectedChromeClient {
        private XmWebView webView;

        public XmWebViewChromeClient(XmWebView view, String injectedName, Class injectedClass) {
            super(injectedName, injectedClass);
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
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (isShown()) {
                mScanHandler.updateProgress(newProgress);
            }
        }


        @Override
        public void onGeolocationPermissionsShowPrompt(final String origin, final GeolocationPermissions.Callback callback) {
            final boolean remember = true;
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle(mContext.getString(R.string.location_title));
            String org;
            if (origin.length() > 50) {
                org = origin.subSequence(0, 50) + "...";
            } else {
                org = origin;
            }
            builder.setMessage(org + mContext.getString(R.string.location_message))
                    .setCancelable(true)
                    .setPositiveButton(mContext.getString(R.string.location_allow),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    callback.invoke(origin, true, remember);
                                }
                            }
                    )
                    .setNegativeButton(mContext.getString(R.string.location_cancel_allow),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    callback.invoke(origin, false, remember);
                                }
                            }
                    );
            AlertDialog alert = builder.create();
            alert.show();
        }

        // video play
        // 视频播放时，全屏会调用
        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            //通知webview 要显示一个custom view ，主要是视频全屏 h5 support
            logger.d("webLoading:--enter onShowCustomView");
            mScanHandler.onShowCustomView(view, callback);
            super.onShowCustomView(view, callback);
        }

        // 视频播放退出全屏回调
        @Override
        public void onHideCustomView() {
            logger.d("webLoading:--enter onHideCustomView");
            mScanHandler.onHideCustomView();
            super.onHideCustomView();
        }

        // 视频加载默认的图标
        @Override
        public Bitmap getDefaultVideoPoster() {
            return super.getDefaultVideoPoster();
        }

        // 视频加载时loading
        @Override
        public View getVideoLoadingProgressView() {
            return super.getVideoLoadingProgressView();
        }

        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
            return super.onJsPrompt(view, url, message, defaultValue, result);
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

    /**
     * 截取webView可视区域的截图
     * <p/>
     * 前提：WebView要设置webView.setDrawingCacheEnabled(true);
     *
     * @return
     */
    public Bitmap captureWebViewVisibleSize() {
//        Bitmap bmp = getDrawingCache();
        Bitmap bmp = XmWebUtils.convertViewToBitmap(this);
        return bmp;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
