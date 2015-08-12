package com.xm.client.exception;

/**
 * Created by wm on 15/7/13.
 */
public class CtException extends Exception {

    public CtException(Throwable wrappedThrowable) {
        super(wrappedThrowable);
    }

    public CtException(String message) {
        super(message);
    }

    public CtException(String message, Throwable wrappedThrowable) {
        super(message, wrappedThrowable);
    }

    protected CtException() {
    }

}
