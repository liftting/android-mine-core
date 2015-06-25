package com.xm.webview.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xm.log.base.XmLogger;
import com.xm.webview.R;
import com.xm.webview.activity.WebScanActivity;
import com.xm.webview.activity.WebScanActivity.WebViewBean;

import java.util.List;

/**
 * 显示打开网页列表
 */
public class XmWebViewListAdapter extends ArrayAdapter<WebViewBean> {
    private XmLogger logger = new XmLogger("XmWebViewListAdapter");

    private final WebScanActivity mContext;
    private List<WebViewBean> mDataList;
    private final int mLayoutResourceId;


    public XmWebViewListAdapter(WebScanActivity context, int layoutResourceId, List<WebViewBean> data) {
        super(context, layoutResourceId, data);
        mContext = context;
        mDataList = data;
        mLayoutResourceId = layoutResourceId;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        logger.d("getView:" + mDataList.size());
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(mLayoutResourceId, parent, false);

            holder = new ViewHolder();

            holder.imgConver = (ImageView) convertView.findViewById(R.id.img_web_multi_item_conver);
            holder.tvClose = (TextView) convertView.findViewById(R.id.tv_web_multi_item_close);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        WebViewBean bean = mDataList.get(position);
        holder.imgConver.setImageBitmap(bean.getDrawingCacheBitmap());

        return convertView;
    }

    class ViewHolder {
        ImageView imgConver;
        TextView tvClose;
    }


}
