package com.xm.client.ctui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xm.client.R;
import com.xm.client.net.ctdata.CtBaseDataBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wm on 15/7/14.
 */
public class CtListDataAdapter extends BaseAdapter {

    private Context mContext;

    private static final int CT_DATA_SHOW_SEND = 0;
    private static final int CT_DATA_SHOW_RECEIVE = 1;

    private static final int mCtViewCount = 2;

    private List<CtBaseDataBean> mDataList = new ArrayList<CtBaseDataBean>();

    public CtListDataAdapter(Context context) {
        mContext = context;
    }

    public void setDataList(List<CtBaseDataBean> dataList) {
        mDataList = dataList;
    }

    @Override
    public int getViewTypeCount() {
        return mCtViewCount;
    }

    @Override
    public int getItemViewType(int position) {
        CtBaseDataBean bean = mDataList.get(position);
        if (bean.isSendData()) {
            return CT_DATA_SHOW_SEND;
        } else {
            return CT_DATA_SHOW_RECEIVE;
        }
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return -1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        int viewType = getItemViewType(position);
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            if (viewType == CT_DATA_SHOW_SEND) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_ct_send_view, null);
                viewHolder.textView = (TextView) convertView.findViewById(R.id.tv_ct_send_message);
            } else if (viewType == CT_DATA_SHOW_RECEIVE) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_ct_receive_view, null);
                viewHolder.textView = (TextView) convertView.findViewById(R.id.tv_ct_receive_message);
            }

            convertView.setTag(viewHolder);
        }

        viewHolder = (ViewHolder) convertView.getTag();
        CtBaseDataBean bean = mDataList.get(position);
        viewHolder.textView.setText(bean.getMessage());
        return convertView;
    }

    class ViewHolder {
        TextView textView;
    }

}
