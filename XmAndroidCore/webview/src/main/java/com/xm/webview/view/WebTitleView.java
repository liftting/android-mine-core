package com.xm.webview.view;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xm.webview.R;
import com.xm.webview.controller.TitleHandler;

/**
 * Created by wm on 15/6/23.
 */
public class WebTitleView extends RelativeLayout {

    private Context mContext;
    private View mTitleView;
    private String mTitle;

    private TitleHandler mTitleHandler;
    private AutoCompleteTextView mSearch;
    private ImageView mImgCollect;

    public WebTitleView(Context context) {
        this(context, null, 0);
    }

    public WebTitleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WebTitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        setUp();
    }

    public void setTitleHandler(TitleHandler titleHandler) {
        mTitleHandler = titleHandler;
    }


    private void setUp() {
        mTitleView = LayoutInflater.from(mContext).inflate(R.layout.layout_web_title_view, this);
        mSearch = (AutoCompleteTextView) mTitleView.findViewById(R.id.tv_web_title);
        mImgCollect = (ImageView) mTitleView.findViewById(R.id.img_web_address_collect);

        mImgCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTitleHandler != null) {
                    mTitleHandler.onWebAddressCollect(v.isSelected());
                }
            }
        });

    }

    public AutoCompleteTextView getAutoTextView() {
        return mSearch;
    }


    public void setTitle(String title) {
        if (title == null) title = "";
        mSearch.setText(title);
    }

}
