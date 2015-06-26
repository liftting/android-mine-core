package com.xm.webview.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.xm.webview.R;
import com.xm.webview.adapter.XmWebViewListAdapter;
import com.xm.webview.controller.TitleHandler;
import com.xm.webview.controller.WebScanHandler;
import com.xm.webview.swipelist.SwipeDismissListViewTouchListener;
import com.xm.webview.util.Constants;
import com.xm.webview.view.AnimatedProgressBar;
import com.xm.webview.view.WebTitleView;
import com.xm.webview.view.XmWebView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class WebScanActivity extends Activity implements WebScanHandler, TitleHandler {

    private FrameLayout mWebContainer;
    private XmWebView mCurrentView; // 多个

    private WebTitleView mTitleView; // 一个，
    private LinearLayout mHeadContainer;
    private AnimatedProgressBar mProgressBar;

    private final List<WebViewBean> mWebViewList = new ArrayList<WebViewBean>();
    private String mDefaultTitle;


    private ListView mSwipeListView;
    private View mWebMultiContainer;
    private XmWebViewListAdapter mMultiAdapter;

    //menu 的容器
    private LinearLayout mMenuContainer;


    private static final ViewGroup.LayoutParams MATCH_PARENT = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT);


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

        mWebMultiContainer = findViewById(R.id.web_multi_container);
        mSwipeListView = (ListView) findViewById(R.id.web_swipe_listview);


        WebViewBean bean = getWrapWebView();
        mCurrentView = bean.getWebView();
        mWebViewList.add(bean);
        mWebContainer.addView(bean.getWrapContent(), MATCH_PARENT);


        mMultiAdapter = new XmWebViewListAdapter(this, R.layout.layout_web_multi_window_item, mWebViewList);
        mSwipeListView.setAdapter(mMultiAdapter);

        mWebContainer.setDrawingCacheEnabled(true);

        configMultiList();


        // menu
        mMenuContainer = (LinearLayout) findViewById(R.id.rly_web_menu_container);
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
        mMenuContainer.setVisibility(View.VISIBLE);
    }

    // -- menu窗口显示时

    public void onMenuToolSetting(View v) {
        Intent intent = new Intent();
        intent.setClass(this, WebSettingActivity.class);
        startActivity(intent);
    }

    public void onMenuToolBack(View v) {

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


}
