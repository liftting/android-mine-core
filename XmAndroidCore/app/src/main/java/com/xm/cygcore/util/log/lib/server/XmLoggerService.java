package com.xm.cygcore.util.log.lib.server;

import com.xm.cygcore.util.log.lib.outer.XmBaseLogOuter;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by wm on 15/6/17.
 */
public class XmLoggerService {

    private ExecutorService service = Executors.newSingleThreadExecutor();
    private long timeout;
    private TimeUnit timeUnit = TimeUnit.SECONDS;

    private BlockingQueue<XmBaseLogOuter> mOuterQueue;//放置进行输出操作的队列

    public void start() {

    }

    public void stop() {
        try {
            service.shutdown();
            service.awaitTermination(timeout, timeUnit);
        } catch (InterruptedException e) {

        } finally {

        }
    }

}
