package com.xm.client.net.ctdata;

import com.xm.client.net.CtConnection;
import com.xm.client.net.CtHandler;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by wm on 15/7/13.
 */
public class CtMessageWriter {

    private CtConnection mCtConnection;
    private boolean isStopSend = false;

    private final BlockingQueue<CtSendDataBean> queue = new ArrayBlockingQueue<CtSendDataBean>(
            500, true);

    public CtMessageWriter(CtConnection connection) {
        mCtConnection = connection;

        Thread sendThread = new Thread() {
            public void run() {
                handleSend();
            }
        };
        sendThread.setDaemon(true);
        sendThread.start();
    }

    private void handleSend() {
        while (!isStopSend) {
            try {
                CtSendDataBean dataBean = getSendData();
                if (dataBean != null) {
                    mCtConnection.sendData(dataBean);
                }
                if (mCtConnection.mDataSendListener != null) {
                    CtHandler.getInstance().post(new Runnable() {
                        @Override
                        public void run() {
                            mCtConnection.mDataSendListener.onSendSuccess();
                        }
                    });

                }
            } catch (final IOException e) {
                // 发送异常
                if (mCtConnection.mDataSendListener != null) {
                    CtHandler.getInstance().post(new Runnable() {
                        @Override
                        public void run() {
                            mCtConnection.mDataSendListener.onSendFail(e);
                        }
                    });
                }
            }
        }
    }

    public CtSendDataBean getSendData() {

        CtSendDataBean dataBean = null;

        while (!isStopSend && (dataBean = queue.poll()) == null) {
            try {
                synchronized (queue) {
                    queue.wait();
                }
            } catch (InterruptedException localInterruptedException) {
            }
        }

        return dataBean;
    }

    public void sendData(CtSendDataBean dataBean) {

        try {
            queue.put(dataBean);
        } catch (InterruptedException exception) {
            return;
        }

        synchronized (queue) {
            queue.notifyAll();
        }
    }

    public void shutDownWriter() {
        isStopSend = true;
    }


}
