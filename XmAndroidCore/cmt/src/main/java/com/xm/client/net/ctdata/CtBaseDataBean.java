package com.xm.client.net.ctdata;

/**
 * Created by wm on 15/7/14.
 */
public abstract class CtBaseDataBean {

    protected String message;
    protected String fromUserId;
    protected String toUserId;
    protected String time;

    public abstract boolean isSendData();

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
