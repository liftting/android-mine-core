package com.xm.client.net;

import com.xm.client.net.ctdata.CtMessageWriter;
import com.xm.client.net.ctdata.CtSendDataBean;
import com.xm.client.net.ctdata.CtMessageRead;
import com.xm.client.net.listener.CtBaseReceiveDataListener;
import com.xm.client.net.listener.CtPostMainDataListener;
import com.xm.client.net.listener.CtSendDataListener;
import com.xm.client.net.listener.CtSingleReceiveListener;
import com.xm.client.net.listener.CtSocConnectListener;

import java.io.IOException;
import java.util.Map;

/**
 * Created by wm on 15/7/9.
 */
public class CtSenderHelper {

    private CtConnection mCtConnection;
    private CtMessageRead mCtMessageReader;
    private CtMessageWriter mCtMessageWriter;

    private CtSocConnectListener mCtSocConnectListener;

    private static CtSenderHelper instance;

    public static CtSenderHelper getInstance() {
        if (instance == null) {
            instance = new CtSenderHelper();
        }
        return instance;
    }

    public CtConnection getCtConnection() {
        return mCtConnection;
    }

    // 连接初始化一层，

    public void createConnection() {
        // 在这里初始化
        if (mCtConnection != null) return;
        mCtConnection = new CtConnection();
    }

    public void initCtConnection() {
        try {
            mCtConnection.initConnection();
            onConnectFinished();

            // 开启消息轮询
            mCtMessageWriter = new CtMessageWriter(mCtConnection);
            mCtMessageReader = new CtMessageRead(mCtConnection);


        } catch (IOException e) {
            // 连接失败 要重连

        }
    }

    public void sendCtData(CtSendDataBean dataBean) {
        mCtMessageWriter.sendData(dataBean);
    }

    private void onConnectFinished() {

        //成功回调
        mCtSocConnectListener.connectSuccess();

        // 注册消息通知回调
        mCtConnection.addReceiveListener(new CtSingleReceiveListener());
    }

    public void addCtSocConnectListener(CtSocConnectListener listener) {

        mCtSocConnectListener = listener;

    }

    public void shutDownReadAndWriter() {

        mCtMessageReader.shutDownRead();
        mCtMessageWriter.shutDownWriter();

        mCtConnection.closeSocket();
    }

    public void addSendDataListener(CtSendDataListener listener){
        mCtConnection.addSendListener(listener);
    }

    public void addPostMainListener(String key, CtPostMainDataListener listener) {
        Map<String, CtBaseReceiveDataListener> dataListenerMap = mCtConnection.getReceiveDataListener();
        CtBaseReceiveDataListener baseDataListener = dataListenerMap.get(key);

        baseDataListener.addPostMainDataListener(listener);

    }


}
