package com.xm.webview.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
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
    private TextView mTvTitle;
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
        mTvTitle = (TextView) mTitleView.findViewById(R.id.tv_web_title);
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


    public void setTitle(String title) {
        if (title == null) title = "";
        mTvTitle.setText(title);
    }


}
