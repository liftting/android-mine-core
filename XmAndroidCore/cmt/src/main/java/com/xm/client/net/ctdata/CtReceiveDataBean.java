package com.xm.client.net.ctdata;

/**
 * Created by wm on 15/7/13.
 */
public class CtReceiveDataBean extends CtBaseDataBean {
    private String receiveHeaderCode;

    public String getReceiveHeaderCode() {
        return receiveHeaderCode;
    }

    public void setReceiveHeaderCode(String receiveHeaderCode) {
        this.receiveHeaderCode = receiveHeaderCode;
    }

    @Override
    public boolean isSendData() {
        return false;
    }
}
