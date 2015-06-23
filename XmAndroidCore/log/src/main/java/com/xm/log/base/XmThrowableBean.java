package com.xm.log.base;

import java.io.Serializable;

/**
 * Created by wm on 15/6/11.
 */
public class XmThrowableBean implements Serializable {

    private static final long serialVersionUID = 1L;

    public Throwable throwable;

    public int sourceLine;

    public String callerMethod;

    public String callerClass;

    public String sourceFile;

    public XmThrowableBean() {
    }

    public XmThrowableBean(StackTraceElement el, Throwable t) {
        this.callerClass = el.getClassName();
        this.callerMethod = el.getMethodName();
        this.sourceLine = el.getLineNumber();
        this.sourceFile = el.getFileName();
        this.throwable = t;
    }

}
