package com.xm.log.base;


import com.xm.log.outer.XmBaseLogOuter;
import com.xm.log.outer.XmLogOuterManager;
import com.xm.log.server.LogService;
import com.xm.log.server.XmLoggerThread;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wm on 15/6/10.
 * 调用入口
 */
public class XmLogger {

    private String tag;

    public XmLogger(String tagName) {
        tag = tagName;
    }

    public void i(Object msg) {
        if (isEnable()) {
            log(XmLoggerLevel.INFO, msg);
        }
    }

    public void i(Object msg, Throwable t) {
        if (isEnable()) {
            log(XmLoggerLevel.INFO, msg, t);
        }
    }

    public void d(Object msg) {
        if (isEnable()) {
            log(XmLoggerLevel.DEBUG, msg);
        }
    }

    public void d(Object msg, Throwable t) {
        if (isEnable()) {
            log(XmLoggerLevel.DEBUG, msg, t);
        }
    }

    public void w(Object msg) {
        if (isEnable()) {
            log(XmLoggerLevel.WARN, msg);
        }
    }

    public void w(Object msg, Throwable t) {
        if (isEnable()) {
            log(XmLoggerLevel.WARN, msg, t);
        }
    }

    public void e(Object msg) {
        if (isEnable()) {
            log(XmLoggerLevel.ERROR, msg);
        }
    }

    public void e(Object msg, Throwable t) {
        if (isEnable()) {
            log(XmLoggerLevel.ERROR, msg, t);
        }
    }

    public void v(Object msg) {
        if (isEnable()) {
            log(XmLoggerLevel.VERBOSE, msg);
        }
    }

    public void v(Object msg, Throwable t) {
        if (isEnable()) {
            log(XmLoggerLevel.VERBOSE, msg, t);
        }
    }

    private boolean isEnable() {
        return XmLoggerConfig.isEnable();
    }

    private void log(XmLoggerLevel level, Object message) {
        log(level, message, null);
    }

    /**
     * 带上异常的日志输出
     *
     * @param level
     * @param msg
     * @param t
     */
    private void log(XmLoggerLevel level, Object msg, Throwable t) {

        try {

            StackTraceElement stackTraceelement = null;

            if (t == null) {
                stackTraceelement = new Throwable().getStackTrace()[3];
            } else {
                stackTraceelement = t.getStackTrace()[1];
            }

            // add exception
            XmThrowableBean info = new XmThrowableBean(stackTraceelement, t);
            String message = (msg == null ? "null" : msg.toString());

            XmLoggerBean logMsg = buildMessage(level, message, info);

            outerLog(logMsg);

        } catch (Exception e) {

        }
    }

    private XmLoggerBean buildMessage(XmLoggerLevel level, String message, XmThrowableBean throwableBean) {
        XmLoggerBean log = new XmLoggerBean();
//        log.className = info.callerClass;
        log.logLevel = level;

        log.logTag = tag; // 传递构造的
        log.logMessage = message;
        log.logThreadName = Thread.currentThread().getName();
        log.logThrowBean = throwableBean;
        log.logTimeStamp = System.currentTimeMillis();

        return log;

    }

    private void outerLog(XmLoggerBean bean) {
        // 多个地方输出，不只是console  调用Outer进行输出
        List<XmBaseLogOuter> logOuterList = XmLogOuterManager.getInstance().getAllOuter();

        if (logOuterList == null || logOuterList.size() <= 0) {
            // 没有配置，默认到控制台
            logOuterList = new ArrayList<XmBaseLogOuter>();
            logOuterList.add(XmLogOuterManager.getInstance().getDefaultOuter());
        }

        LogService loggerService = getLogService();

        for (XmBaseLogOuter outer : logOuterList) {
            loggerService.syncLog(outer, bean);
        }

    }

    /**
     * 配置log service
     *
     * @return
     */
    private LogService getLogService() {
        LogService service = XmLoggerThread.getInstance();
        return service;
    }


}
