package com.xm.client.net.ctdata;

/**
 * Created by wm on 15/7/9.
 */
public class CtSendDataBean extends CtBaseDataBean {

    private int sendHeaderCode;

    public int getSendHeaderCode() {
        return sendHeaderCode;
    }

    public void setSendHeaderCode(int sendHeaderCode) {
        this.sendHeaderCode = sendHeaderCode;
    }


    @Override
    public boolean isSendData() {
        return true;
    }
}
