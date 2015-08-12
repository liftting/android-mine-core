package com.xm.client.net;

import com.xm.client.net.ctdata.CtDataFactory;
import com.xm.client.net.ctdata.CtSendDataBean;
import com.xm.client.net.listener.CtSocConnectListener;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by wm on 15/7/9.
 */
public class CtExecutors {

    private ExecutorService executorService = null;

    public static CtExecutors instance;

    public static CtExecutors getInstance() {
        if (instance == null) {
            instance = new CtExecutors();
        }
        return instance;
    }

    private CtExecutors() {
        executorService = Executors.newCachedThreadPool();
    }

    public void syncSendCtRunnable(CtDataSendRunnable runnable) {
        executorService.submit(runnable);
    }


    public void connect() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                connectSync();

            }
        });
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.start();
    }

    public void connect(final CtSocConnectListener listener) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                connectSync(listener);

            }
        });
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.start();
    }

    /**
     * 构造connection对象
     */
    private void connectCreateSync() {
        CtSenderHelper helper = CtSenderHelper.getInstance();
        helper.createConnection();
    }

    /**
     * 真去链接
     */
    private void connectInitSync() {
        CtSenderHelper helper = CtSenderHelper.getInstance();
        helper.initCtConnection();
        // 初始data  要带上userId
        helper.sendCtData(CtDataFactory.getInstance().createInitData());

        // 开启心跳
        beginHeartBeat();
    }

    private synchronized void connectSync() {
        connectCreateSync();
        connectInitSync();
    }

    private synchronized void connectSync(CtSocConnectListener listener) {
        connectCreateSync();

        CtSenderHelper.getInstance().addCtSocConnectListener(listener);

        connectInitSync();
    }


    private void beginHeartBeat() {

        Thread thread = new Thread() {
            public void run() {
                while (!CtConfig.isStopHeartBeat) {
                    CtSenderHelper.getInstance().sendCtData(CtDataFactory.getInstance().createHeartData());

                    try {
                        Thread.sleep(CtConfig.heartBeatTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        };

        thread.start();

    }

    public void shutDownConnection() {
        CtConfig.isStopHeartBeat = true;

        // 先发送数据，再关掉流
        CtSenderHelper.getInstance().sendCtData(CtDataFactory.getInstance().createShutDownData());
        CtSenderHelper.getInstance().shutDownReadAndWriter();

    }


//    ////  一些数据请求

    public static class CtDataSendRunnable implements Runnable {

        private CtSendDataBean data;

        public CtDataSendRunnable(CtSendDataBean bean) {
            data = bean;
        }

        public void run() {
            CtSenderHelper.getInstance().sendCtData(data);
        }
    }

}
