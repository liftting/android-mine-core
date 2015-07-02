package com.xm.webview.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xm.utils.PreferencesUtil;
import com.xm.webview.R;
import com.xm.webview.adapter.XmWebViewListAdapter;
import com.xm.webview.controller.TitleHandler;
import com.xm.webview.controller.WebScanHandler;
import com.xm.webview.swipelist.SwipeDismissListViewTouchListener;
import com.xm.webview.util.Constants;
import com.xm.webview.view.AnimatedProgressBar;
import com.xm.webview.view.SwipeBackView;
import com.xm.webview.view.WebTitleView;
import com.xm.webview.view.XmWebView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


public class WebScanActivity extends Activity implements WebScanHandler, TitleHandler, SwipeBackView.OnOperateListener {

    private FrameLayout mWebContainer;
    private XmWebView mCurrentView; // 多个

    private WebTitleView mTitleViewFloat; // 一个，
    private LinearLayout mHeadContainerFloat;
    private AnimatedProgressBar mProgressBarFloat;

    private final List<WebViewBean> mWebViewList = new ArrayList<WebViewBean>();
    private String mDefaultTitle;


    private ListView mSwipeListView;
    private View mWebMultiContainer;
    private XmWebViewListAdapter mMultiAdapter;

    //menu 的容器
    private LinearLayout mWebMenuContainer;

    // video
    private Bitmap mDefaultVideoPoster;
    private View mVideoProgressView;

    private PreferencesUtil mPreUtil;

    // search
    private String mSearchText = Constants.BAIDU_SEARCH;

    private static final ViewGroup.LayoutParams MATCH_PARENT = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configView(R.layout.activity_main);

        preferenceInit();
        init();

    }

    private void configView(int layoutId) {
        ViewGroup contentView = (ViewGroup) LayoutInflater.from(this).inflate(layoutId, null);

        SwipeBackView rootView = new SwipeBackView(this);
        rootView.addView(contentView,
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT)
        );

        rootView.setOperateListener(this);

        setContentView(rootView);
    }

    private void init() {
        mWebViewList.clear();
        mDefaultTitle = getString(R.string.default_web_title);


        mWebContainer = (FrameLayout) findViewById(R.id.scorllView);

        mWebMultiContainer = findViewById(R.id.web_multi_container);
        mSwipeListView = (ListView) findViewById(R.id.web_swipe_listview);

        WebViewBean bean = getWrapWebView();
        mCurrentView = bean.getWebView();
        mWebViewList.add(bean);
        mWebContainer.addView(bean.getWrapContent(), MATCH_PARENT);

        mMultiAdapter = new XmWebViewListAdapter(this, R.layout.layout_web_multi_window_item, mWebViewList);
        mSwipeListView.setAdapter(mMultiAdapter);
        mWebContainer.setDrawingCacheEnabled(true);
        configTitle();
        configMultiList();


        // menu
        mWebMenuContainer = (LinearLayout) findViewById(R.id.rly_web_menu_container);
        mSearch = mTitleViewFloat.getAutoTextView();
//        mSearch.setFocusable(false);

        configSearch();

    }

    private void configTitle() {
        mTitleViewFloat = (WebTitleView) findViewById(R.id.title_view_container);
        mProgressBarFloat = (AnimatedProgressBar) findViewById(R.id.progress_view);
        mHeadContainerFloat = (LinearLayout) findViewById(R.id.headerView);

    }

    private void preferenceInit() {
        mPreUtil = PreferencesUtil.getInstance(this);
        // 手机存储
        String downPath = this.getFilesDir().getAbsolutePath();

        // sdcard的存储地址
        downPath = this.getExternalFilesDir(null).getAbsolutePath();
        mPreUtil.putString(Constants.SP_WEB_DOWN_PATH, downPath);

    }

    private WebViewBean getWrapWebView() {
        XmWebView webView = new XmWebView(this, this);
        WebViewBean webViewBean = new WebViewBean(new RelativeLayout(this), webView);
//        webViewBean.getWrapContent().setDrawingCacheEnabled(true);
        return webViewBean;
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
//        if (mHeadContainerFloat.getVisibility() == View.VISIBLE) return;
//        Animation show = AnimationUtils.loadAnimation(this, R.anim.slide_down);
//        show.setAnimationListener(new Animation.AnimationListener() {
//
//            @Override
//            public void onAnimationStart(Animation animation) {
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                mHeadContainerFloat.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//            }
//
//        });
//        mHeadContainerFloat.startAnimation(show);
    }

    @Override
    public void toggleTitleBar() {
//        if (mHeadContainerFloat.getVisibility() != View.VISIBLE) {
//            showTitleBar();
//        } else {
//            hideTitleBar();
//        }
    }

    @Override
    public void hideTitleBar() {
//        if (mHeadContainerFloat.getVisibility() == View.INVISIBLE) return;
//        Animation show = AnimationUtils.loadAnimation(this, R.anim.slide_up);
//        show.setAnimationListener(new Animation.AnimationListener() {
//
//            @Override
//            public void onAnimationStart(Animation animation) {
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                mHeadContainerFloat.setVisibility(View.INVISIBLE);
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//            }
//
//        });
//        mHeadContainerFloat.startAnimation(show);
    }

    @Override
    public void onLongPress() {

    }

    @Override
    public void updateUrl(String title, boolean shortUrl) {
        if (title == null || mTitleViewFloat == null) {
            return;
        }
        if (shortUrl && !title.startsWith(Constants.FILE)) {
            if (mCurrentView != null && !mCurrentView.getTitle().isEmpty()) {
                mTitleViewFloat.setTitle(mCurrentView.getTitle());

            } else {
                mTitleViewFloat.setTitle(mDefaultTitle);
            }

        } else {
            if (title.startsWith(Constants.FILE)) {
                title = "";
            }
            mTitleViewFloat.setTitle(title);
        }
    }

    @Override
    public void updateProgress(int n) {
        mProgressBarFloat.setProgress(n);
    }

    @Override
    public void updateHistory(String title, String url) {

    }

    @Override
    public Bitmap getDefaultVideoPoster() {
        if (mDefaultVideoPoster == null) {
            mDefaultVideoPoster = BitmapFactory.decodeResource(getResources(),
                    android.R.drawable.ic_media_play);
        }
        return mDefaultVideoPoster;
    }

    @Override
    public View getVideoLoadingProgressView() {
        if (mVideoProgressView == null) {
            LayoutInflater inflater = LayoutInflater.from(this);
            mVideoProgressView = inflater.inflate(R.layout.video_loading_progress, null);
        }
        return mVideoProgressView;
    }

    @Override
    public void onWebAddressCollect(boolean isCollect) {

    }

    @Override
    public void onBackPressed() {

        if (mWebMultiContainer.getVisibility() == View.VISIBLE) {
            mWebMultiContainer.setVisibility(View.GONE);
            return;
        }

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
        showMultiWindow();
    }

    public void onToolMenu(View v) {
        mWebMenuContainer.setVisibility(View.VISIBLE);
    }

    // -- menu窗口显示时

    public void onMenuToolSetting(View v) {
        Intent intent = new Intent();
        intent.setClass(this, WebSettingActivity.class);
        startActivity(intent);
    }

    public void onMenuToolBack(View v) {
        mWebMenuContainer.setVisibility(View.GONE);
    }

    public void onMenuToolShare(View v) {

    }


    // --

    // ---  多窗口时

    public void onToolMultiCloseAll(View v) {

    }

    public void onToolMultiAddTab(View v) {
        addWebView();
        mWebMultiContainer.setVisibility(View.GONE);
    }

    public void onToolMultiReturn(View v) {
        mWebMultiContainer.setVisibility(View.GONE);
    }

    // --


    private void addWebView() {

        XmWebView newTab = new XmWebView(this, this); // 设置控制回调
        WebViewBean bean = getWrapWebView();
        mWebViewList.add(bean);
        showChoiceWebView(newTab);

    }

    /**
     * 显示选择的webView
     *
     * @param webView
     */
    private void showChoiceWebView(XmWebView webView) {

        mWebContainer.removeAllViews();
        mCurrentView = webView;
        mWebContainer.addView(webView, MATCH_PARENT);
    }

    private void showMultiWindow() {

        mWebMultiContainer.setVisibility(View.VISIBLE);
        mMultiAdapter.notifyDataSetChanged();

    }

    /**
     * 设置滑动删除
     */
    private void configMultiList() {
        SwipeDismissListViewTouchListener touchListener = new SwipeDismissListViewTouchListener(mSwipeListView, new SwipeDismissListViewTouchListener.OnDismissCallback() {
            @Override
            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                for (int position : reverseSortedPositions) {
                    mMultiAdapter.remove(mMultiAdapter.getItem(position));
                }
                mMultiAdapter.notifyDataSetChanged();
            }
        });

        mSwipeListView.setOnTouchListener(touchListener);
        // Setting this scroll listener is required to ensure that during ListView scrolling,
        // we don't look for swipes.
        mSwipeListView.setOnScrollListener(touchListener.makeScrollListener());

    }

    @Override
    public void onBack() {

        if (mCurrentView != null && mCurrentView.canGoBack()) {
            mCurrentView.goBack();
        }
    }

    @Override
    public void onForward() {
        if (mCurrentView != null && mCurrentView.canGoForward()) {
            mCurrentView.goForward();
        }
    }

    /**
     * webView的包装类，添加截图功能，直接获取webView的，获取不到
     */
    public static class WebViewBean {
        private RelativeLayout mViewWrapper;
        private XmWebView mWebView;

        public WebViewBean(RelativeLayout relativeLayout, XmWebView webView) {
            this.mViewWrapper = relativeLayout;
            this.mWebView = webView;

            wrap();

        }

        private void wrap() {
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mViewWrapper.addView(mWebView, params);
        }

        public View getWrapContent() {
            return mViewWrapper;
        }

        public XmWebView getWebView() {
            return mWebView;
        }

        public Bitmap getDrawingCacheBitmap() {

            mViewWrapper.setDrawingCacheEnabled(true);
            mViewWrapper.buildDrawingCache(true);

            //
            Bitmap bitmap = comp(mViewWrapper.getDrawingCache());

            // 压缩完成之后，给释放掉
            mViewWrapper.setDrawingCacheEnabled(false);
            mViewWrapper.destroyDrawingCache();
            return bitmap;
        }

        /**
         * 进行大小压缩，
         *
         * @param image
         * @return
         */
        private Bitmap comp(Bitmap image) {

            ByteArrayOutputStream baos = null;
            ByteArrayInputStream isBm = null;
            Bitmap bitmap = null;
            try {
                baos = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                if (baos.toByteArray().length / 1024 > 1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
                    baos.reset();//重置baos即清空baos
                    image.compress(Bitmap.CompressFormat.JPEG, 50, baos);//这里压缩50%，把压缩后的数据存放到baos中
                }
                isBm = new ByteArrayInputStream(baos.toByteArray());
                BitmapFactory.Options newOpts = new BitmapFactory.Options();
                newOpts.inJustDecodeBounds = false;
                newOpts.inSampleSize = 12;//设置缩放比例
                //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了

                bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
            } finally {
                if (baos != null) {
                    try {
                        baos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (isBm != null) {
                    try {
                        isBm.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


            }

            return bitmap;//压缩好比例大小后再进行质量压缩
        }


    }


    // --- search

    private AutoCompleteTextView mSearch;

    public void configSearch() {

        SearchClass search = new SearchClass();
//        mSearch.setCompoundDrawables(null, null, mRefreshIcon, null);
        mSearch.setOnKeyListener(search.new KeyListener());
        mSearch.setOnFocusChangeListener(search.new FocusChangeListener());
        mSearch.setOnEditorActionListener(search.new EditorActionListener());
        mSearch.setOnTouchListener(search.new TouchListener());

//        mSearch.setFocusable(true);
    }

    private void searchTheWeb(String query) {
        if (query.equals("")) {
            return;
        }
        String SEARCH = mSearchText;
        query = query.trim();
        mCurrentView.stopLoading();

        if (query.startsWith("www.")) {
            query = Constants.HTTP + query;
        } else if (query.startsWith("ftp.")) {
            query = "ftp://" + query;
        }

        boolean containsPeriod = query.contains(".");
        boolean isIPAddress = (TextUtils.isDigitsOnly(query.replace(".", ""))
                && (query.replace(".", "").length() >= 4) && query.contains("."));
        boolean aboutScheme = query.contains("about:");
        boolean validURL = (query.startsWith("ftp://") || query.startsWith(Constants.HTTP)
                || query.startsWith(Constants.FILE) || query.startsWith(Constants.HTTPS))
                || isIPAddress;
        boolean isSearch = ((query.contains(" ") || !containsPeriod) && !aboutScheme);

        if (isIPAddress
                && (!query.startsWith(Constants.HTTP) || !query.startsWith(Constants.HTTPS))) {
            query = Constants.HTTP + query;
        }

        if (isSearch) {
            try {
                query = URLEncoder.encode(query, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            mCurrentView.loadUrl(SEARCH + query);
        } else if (!validURL) {
            mCurrentView.loadUrl(Constants.HTTP + query);
        } else {
            mCurrentView.loadUrl(query);
        }
    }

    private class SearchClass {

        public class KeyListener implements View.OnKeyListener {

            @Override
            public boolean onKey(View arg0, int arg1, KeyEvent arg2) {

                switch (arg1) {
                    case KeyEvent.KEYCODE_ENTER:
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(mSearch.getWindowToken(), 0);
                        searchTheWeb(mSearch.getText().toString());
                        if (mCurrentView != null) {
                            mCurrentView.requestFocus();
                        }
                        return true;
                    default:
                        break;
                }
                return false;
            }

        }

        public class EditorActionListener implements TextView.OnEditorActionListener {
            @Override
            public boolean onEditorAction(TextView arg0, int actionId, KeyEvent arg2) {
                // hide the keyboard and search the web when the enter key
                // button is pressed
                if (actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_DONE
                        || actionId == EditorInfo.IME_ACTION_NEXT
                        || actionId == EditorInfo.IME_ACTION_SEND
                        || actionId == EditorInfo.IME_ACTION_SEARCH
                        || (arg2.getAction() == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mSearch.getWindowToken(), 0);
                    searchTheWeb(mSearch.getText().toString());
                    if (mCurrentView != null) {
                        mCurrentView.requestFocus();
                    }
                    return true;
                }
                return false;
            }
        }

        public class FocusChangeListener implements View.OnFocusChangeListener {
            @Override
            public void onFocusChange(View v, final boolean hasFocus) {
                if (!hasFocus && mCurrentView != null) {
                    // 变成url地址
                    updateUrl(mCurrentView.getUrl(), true);
                } else if (hasFocus) {
                    String url = mCurrentView.getUrl();
                    if (url == null || url.startsWith(Constants.FILE)) {
                        mSearch.setText("");
                    } else {
                        mSearch.setText(url);
                    }
                    //选中
                    ((AutoCompleteTextView) v).selectAll(); // Hack to make sure
                    // the text gets
                    // selected
                }

                if (!hasFocus) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mSearch.getWindowToken(), 0);
                }
            }
        }

        public class TouchListener implements View.OnTouchListener {

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }

        }
    }

}
