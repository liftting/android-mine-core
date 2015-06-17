package com.xm.cygcore.util.log.lib.server;

import com.xm.cygcore.R;
import com.xm.cygcore.util.log.lib.base.XmLoggerBean;
import com.xm.cygcore.util.log.lib.outer.XmBaseLogOuter;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by wm on 15/6/17.
 */
public class XmLoggerService {

    private ExecutorService service = Executors.newSingleThreadExecutor();// 默认使用了BlockingQueue
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
     *
     * @param outer
     * @param bean
     */
    public void syncLog(final XmBaseLogOuter outer, final XmLoggerBean bean) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                outer.outLog(bean);
            }
        };

        go(runnable);
    }

}
