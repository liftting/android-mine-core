package com.xm.log.server;


import com.xm.log.base.XmLoggerBean;
import com.xm.log.outer.XmBaseLogOuter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by wm on 15/6/17.
 */
public class XmLoggerService implements LogService {

    private ExecutorService service;// 默认使用了BlockingQueue
    private long timeout = 2; //延迟2s
    private TimeUnit timeUnit = TimeUnit.SECONDS;

    private static XmLoggerService instance = new XmLoggerService();

    private XmLoggerService() {

    }

    public static XmLoggerService getInstance() {
        return instance;
    }

    private void go(Runnable runnable) {
        service.submit(runnable);
    }

    public void stop() {
        try {
            service.shutdown();
            service.awaitTermination(timeout, timeUnit);
        } catch (InterruptedException e) {

        } finally {

        }
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

        if (service == null) {
            service = Executors.newFixedThreadPool(5);
        }

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                outer.outLog(bean);
            }
        };

        go(runnable);
    }

    /**
     * 当前线程进行日志输出
     *
     * @param outer
     * @param bean
     */
    public void log(XmBaseLogOuter outer, XmLoggerBean bean) {
        outer.outLog(bean);
    }

}
