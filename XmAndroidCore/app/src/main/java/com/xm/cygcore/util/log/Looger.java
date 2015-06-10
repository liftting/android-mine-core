package com.xm.cygcore.util.log;

/**
 * Created by wm on 15/6/10.
 */
public class Looger {

    private String mTag;

    public static Logger getLogger(Class<?> clazz) {
        return getLogger(clazz.getName());
    }

    public static Logger getLogger(String tag) {
        return LoggerFactory.getLogger(tag);
    }

    public Logger(String tag) {
        this.mTag = tag;
    }

    public void i(Object msg) {
        if (isEnable()) {
            log(LogLevel.INFO, msg);
        }
    }

    public void i(Object msg, Throwable t) {
        if (isEnable()) {
            log(LogLevel.INFO, msg, t);
        }
    }

    public void d(Object msg) {
        if (isEnable()) {
            log(LogLevel.DEBUG, msg);
        }
    }

    public void d(Object msg, Throwable t) {
        if (isEnable()) {
            log(LogLevel.DEBUG, msg, t);
        }
    }

    public void w(Object msg) {
        if (isEnable()) {
            log(LogLevel.WARN, msg);
        }
    }

    public void w(Object msg, Throwable t) {
        if (isEnable()) {
            log(LogLevel.WARN, msg, t);
        }
    }

    public void e(Object msg) {
        if (isEnable()) {
            log(LogLevel.ERROR, msg);
        }
    }

    public void e(Object msg, Throwable t) {
        if (isEnable()) {
            log(LogLevel.ERROR, msg, t);
        }
    }

    public void v(Object msg) {
        if (isEnable()) {
            log(LogLevel.VERBOSE, msg);
        }
    }

    public void v(Object msg, Throwable t) {
        if (isEnable()) {
            log(LogLevel.VERBOSE, msg, t);
        }
    }

    public void log(LogLevel level, Object msg) {
        if (isEnable()) {
            log(level, msg, null);
        }
    }


}
