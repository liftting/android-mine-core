package com.xm.client.net;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 *
 */
public class CtHandler extends Handler {

    public static final int CT_SEND_MESSAGE = 0X001;
    public static final int CT_RECEIVE_MESSAGE = 0X002;

    private static CtHandler instance;

    public static CtHandler getInstance() {
        if (instance == null) {
            instance = new CtHandler();
        }
        return instance;
    }

    private CtHandler() {
        super(Looper.getMainLooper());
    }

//    @Override
//    public void handleMessage(Message message) {
//        int what = message.what;
//        switch (what) {
//            case CT_RECEIVE_MESSAGE:
//                break;
//            case CT_SEND_MESSAGE:
//                break;
//            default:
//                super.handleMessage(message);
//                break;
//        }
//    }

}
