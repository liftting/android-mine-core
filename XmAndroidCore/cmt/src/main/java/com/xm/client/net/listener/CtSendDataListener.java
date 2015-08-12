package com.xm.client.net.listener;

import com.xm.client.net.ctdata.CtSendDataBean;

/**
 * Created by wm on 15/7/14.
 */
public interface CtSendDataListener {

    public void onSendSuccess();

    public void onSendFail(Exception e);

    public void onHandleData(CtSendDataBean dataBean);

}
