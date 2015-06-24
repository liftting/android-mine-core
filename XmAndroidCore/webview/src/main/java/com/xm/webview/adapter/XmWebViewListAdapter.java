package com.xm.webview.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.xm.webview.view.XmWebView;

/**
 * 显示打开网页列表
 */
public class XmWebViewListAdapter extends ArrayAdapter<XmWebView> {


    public XmWebViewListAdapter(Context context, int resource) {
        super(context, resource);
    }


}
