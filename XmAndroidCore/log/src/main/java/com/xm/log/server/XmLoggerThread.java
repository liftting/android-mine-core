package com.xm.log.server;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import com.xm.log.base.XmLoggerBean;
import com.xm.log.outer.XmBaseLogOuter;


/**
 * 使用Android的异步处理机制，来输出log
 */
public class XmLoggerThread implements LogService {

    private HandlerThread mHandlerThread;
    private Looper mLooper;
    private LoggerHandler mHandler;

    private static XmLoggerThread instance;

    private static final String threadTag = "loggerThread";
    private static final int MSG_LOGGER_HANDLER = 0x013;
    private static final int MSG_LOGGER_CHECK = 0x014;

    private static final int MAX_IDLE_TIME = 5 * 60 * 1000;
    private static final int CHECK_DELAY_TIME = 30 * 1000;

//    private boolean isOuterLog;

    private long mLastOuterTime = 0;

    public static XmLoggerThread getInstance() {
        if (instance == null) {
            instance = new XmLoggerThread();
        }
        return instance;
    }

    private XmLoggerThread() {

        mHandlerThread = new HandlerThread(threadTag, android.os.Process.THREAD_PRIORITY_BACKGROUND);
        mHandlerThread.start();

        mLooper = mHandlerThread.getLooper();
        mHandler = new LoggerHandler(mLooper); // 设置了handler处理消息的执行队列looper，而这里的looper是上面handlerThread的，是子线程，所以这里方法是运行再子线程中

        mHandler.sendEmptyMessageDelayed(MSG_LOGGER_CHECK, CHECK_DELAY_TIME);

    }

    /**
     * 将任务放置到线程池中
     * 这里提供这种方式，是因为有时如果日志要输出到文件中时，会有阻塞在主线程中执行时，
     *
     * @param outer
     * @param bean
     */
    @Override
    public void syncLog(final XmBaseLogOuter outer, final XmLoggerBean bean) {

        LoggerHolder holder = new LoggerHolder();
        holder.mOuter = outer;
        holder.mBean = bean;

        Message message = new Message();
        message.obj = holder;
        message.what = MSG_LOGGER_HANDLER;

        mHandler.sendMessage(message);

    }

    private class LoggerHandler extends Handler {

        public LoggerHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void dispatchMessage(Message msg) {

            switch (msg.what) {
                case MSG_LOGGER_HANDLER:
                    LoggerHolder holder = (LoggerHolder) msg.obj;
                    XmBaseLogOuter outer = holder.mOuter;
                    if (outer != null) {
                        //  这就在线程中进行了处理
                        outer.outLog(holder.mBean);
                    }

                    mLastOuterTime = System.currentTimeMillis();
                    break;

                case MSG_LOGGER_CHECK:
                    check();
                    break;
            }

        }
    }

    private void check() {
        if (mLastOuterTime == 0) {
            mHandler.sendEmptyMessageDelayed(MSG_LOGGER_CHECK, CHECK_DELAY_TIME);
            return;
        }

        if (System.currentTimeMillis() - mLastOuterTime > MAX_IDLE_TIME && instance != null) {
            // 取消掉
            mLooper.quit();
            instance = null;
            mHandler = null;
            return;
        }

        if (mHandler != null) {
            mHandler.sendEmptyMessageDelayed(MSG_LOGGER_CHECK, CHECK_DELAY_TIME);
        }
    }

    private static class LoggerHolder {
        public XmBaseLogOuter mOuter;
        public XmLoggerBean mBean;
    }

}
