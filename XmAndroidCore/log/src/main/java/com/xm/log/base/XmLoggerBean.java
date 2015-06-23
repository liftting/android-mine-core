package com.xm.log.base;

/**
 * Created by wm on 15/6/11.
 */
public class XmLoggerBean {

    public String logTag;
    public String logMessage;

    public XmLoggerLevel logLevel;
    public XmThrowableBean logThrowBean;


    public String logThreadName;
    public long logTimeStamp;


    // 判断是否崩溃的日志
    public boolean isCrashed() {
        return logThrowBean != null && logThrowBean.throwable != null
                && logThrowBean.throwable.getStackTrace().length > 3;
    }


}
