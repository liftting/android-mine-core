package com.xm.client.net.ctdata;

import android.text.TextUtils;

import com.xm.client.net.CtConnection;
import com.xm.client.net.listener.CtBaseReceiveDataListener;

import java.io.IOException;
import java.util.Map;

/**
 * Created by wm on 15/7/13.
 */
public class CtMessageRead {

    private CtConnection mCtConnection;

    private boolean isStopRead = false;

    public CtMessageRead(CtConnection connection) {
        this.mCtConnection = connection;
        new CtMessageReadThread().start();
    }

    public class CtMessageReadThread extends Thread {

        public void run() {
            while (!isStopRead) {

                try {
                    String readData = mCtConnection.receiveData();

                    // 回调调用
                    if (!TextUtils.isEmpty(readData)) {
                        //
                        handleReceive(mCtConnection.getReceiveDataListener(), parseReceiveData(readData));
                    }

                    // 线程休息
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {

                    }

                } catch (IOException e) {
                    // 读取失败
                    e.printStackTrace();
                }

            }
        }

    }

    private CtReceiveDataBean parseReceiveData(String data) {
        // 构造
        CtReceiveDataBean dataBean = new CtReceiveDataBean();
        dataBean.setMessage(data);
        return dataBean;
    }

    private void handleReceive(Map<String, CtBaseReceiveDataListener> listenerMap, CtReceiveDataBean dataBean) {
        for (String key : listenerMap.keySet()) {
            listenerMap.get(key).onReceiveData(dataBean);
        }
    }

    public void shutDownRead() {
        isStopRead = true;
    }

}
