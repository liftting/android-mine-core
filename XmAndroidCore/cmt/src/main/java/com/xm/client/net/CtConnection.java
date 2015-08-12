package com.xm.client.net;

import com.google.gson.Gson;
import com.xm.client.net.ctdata.CtSendDataBean;
import com.xm.client.net.listener.CtBaseReceiveDataListener;
import com.xm.client.net.listener.CtSendDataListener;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class CtConnection {

    private static final String host = CtConfig.host;
    private static final int port = CtConfig.port;

    // client socket
    private Socket mConnectSocket;

    private BufferedOutputStream mBufferOs;
    private BufferedInputStream mBufferIs;

    private Map<String, CtBaseReceiveDataListener> mDataReceiverListenerMap = new HashMap<String, CtBaseReceiveDataListener>();
    public CtSendDataListener mDataSendListener;


    public void initConnection() throws IOException {
        mConnectSocket = new Socket(host, port);
        mConnectSocket.setKeepAlive(true);
        mConnectSocket.setSoTimeout(0);
        mConnectSocket.setSoLinger(true, CtConfig.so_linger_time);

        mBufferOs = new BufferedOutputStream(mConnectSocket.getOutputStream());
        mBufferIs = new BufferedInputStream(mConnectSocket.getInputStream());


    }

    public BufferedOutputStream getBufferedOutputStream() {
        return mBufferOs;
    }

    public BufferedInputStream getBufferedInputStream() {
        return mBufferIs;
    }

    public Map<String, CtBaseReceiveDataListener> getReceiveDataListener() {
        return mDataReceiverListenerMap;
    }


    public void sendData(final CtSendDataBean message) throws IOException {

        if (mDataSendListener != null) {
            CtHandler.getInstance().post(new Runnable() {
                @Override
                public void run() {
                    mDataSendListener.onHandleData(message);
                }
            });
        }

        Gson gson = new Gson();
        String json = gson.toJson(message);
        CtDataSenderUtil.writeStr2Stream(json, mBufferOs);
    }

    public String receiveData() throws IOException {
        // 这里异常不处理，因为上层知道如何进行下次处理机制
        String data = null;
        data = CtDataSenderUtil.readStrFromStream(mBufferIs);
        return data;
    }

    public void addReceiveListener(CtBaseReceiveDataListener dataListener) {

        mDataReceiverListenerMap.put(CtConfig.chat_main_listener, dataListener);

    }

    public void addSendListener(CtSendDataListener sendDataListener) {
        mDataSendListener = sendDataListener;
    }

    public void closeSocket() {

        try {
            if (mConnectSocket != null) {
                mConnectSocket.close();
            }
        } catch (IOException e) {

        }

    }
}
