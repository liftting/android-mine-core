package com.xm.client.net.ctserver;

import com.google.gson.Gson;
import com.xm.client.net.CtConfig;
import com.xm.client.net.ctdata.CtSendDataBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 */
public class CtServer {

    private ExecutorService ctExecutorSer;
    private boolean isStopSer = false;

    public static void main(String[] args) {

        CtServer ctServer = new CtServer();
        ctServer.start();

    }

    // temp 将所有链接的socket缓存起来
    private Map<String, Socket> mConnectMap = new HashMap<String, Socket>();

    public void start() {

        ServerSocket server = null;
        ctExecutorSer = Executors.newCachedThreadPool();
        try {
            // new a socket server
            server = new ServerSocket(CtServerConfig.port);
            while (!isStopSer) {
                Socket soket = server.accept();

                ctExecutorSer.submit(new CtHandlerDataRunnable(soket));

            }
        } catch (IOException e) {

            System.out.println(e.getMessage());


        } finally {
            if (server != null) {
                try {
                    server.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }

    }

    public class CtHandlerDataRunnable implements Runnable {

        private Socket mSocket;

        private boolean isShutDown = false;

        public CtHandlerDataRunnable(Socket socket) {
            mSocket = socket;
        }

        @Override
        public void run() {

            try {
                // 连接到客户端就可以进行数据读写
                mSocket.setSoTimeout(CtServerConfig.SO_TIME);

                while (!isShutDown) {
                    log("begin enter while loop");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));

                    StringBuilder result = new StringBuilder();
                    // read 1024 bytes per time
                    char[] chars = new char[2048];
                    int len;

                    try {
                        while ((len = reader.read(chars)) != -1) {
                            // if the length of array is 1M
                            log("read data length is :" + len);
                            if (2048 == len) {
                                //then append all chars of the array
                                result.append(chars);
                                System.out.println("readStrFromStream : " + result.toString());
                            }
                            // if the length of array is less then 1M
                            else {
                                //then append the valid chars
                                for (int i = 0; i < len; i++) {
                                    result.append(chars[i]);
                                    //System.out.println("readStrFromStream : " + result.toString());
                                }
                                break;
                            }
                        }

                    } catch (IOException e) {
                        System.out.println(e);
                        throw e;
                    }

                    log("read data end");

                    String data = result.toString();

                    if (CtServerUtils.isEmpty(data)) {
                        // 读到为空
                    }


                    log("from client data is = " + data);
//                    JSONObject jsonObject = null;
                    Gson gson = new Gson();
//                        jsonObject = new JSONObject(data);
                    CtSendDataBean sendDataBean = gson.fromJson(data, CtSendDataBean.class);

                    if (sendDataBean != null) {
                        handleJsonData(sendDataBean);
                    }

                }

            } catch (IOException e) {
                e.printStackTrace();
                log("read IOException");
                closeSocket(mSocket);

            } finally {
                log("read finally close socket");
                closeSocket(mSocket);
            }

        }

        private void handleJsonData(CtSendDataBean data) throws IOException {
            int headerCode = data.getSendHeaderCode();
            String message = data.getMessage();
            String userId = data.getFromUserId();
            String toUserId = data.getToUserId();

            switch (headerCode) {
                case CtDataProtocol.CT_CONNECT_INIT:
                    // 第一次连接
                    mConnectMap.put(userId, mSocket);
                    log("from client first to connect and userid is " + userId);
                    break;

                case CtDataProtocol.CT_CONNECT_HEART_BEAT:
                    // 心跳
                    log("form client heart data,so dont handle the data");
//                        writeDataToClient(CtServerConfig.heart_head_to_client, mSocket.getOutputStream());
                    break;

                case CtDataProtocol.CT_CONNECT_NORMAL:
                    //正常
                    log("begin to write data to client");

                    Socket toSocket = mConnectMap.get(toUserId);
                    toSocket = mSocket;
                    if (toSocket != null) {
                        handleMessageOut(message, toSocket);
                        log("end to writer data to client");
                    }
                    break;

                case CtDataProtocol.CT_CONNECT_SHUT_DOWN_FROM_CLIENT:
                    // client request shut down
                    log("client request shut down socket");
                    isShutDown = true;
                    // 释放掉socket
                    mConnectMap.put(userId, null);

                    // 不用关闭socket，当heart beat 不发送时，直接触发超时，关闭掉
                    // 不关闭，以及会一直执行读操作，要控制外层循环
                    closeSocket(mSocket);
                    break;

                default:
                    break;

            }

        }
    }


    private void handleMessageOut(String message, Socket toClientSocket) throws IOException {
        writeDataToClient(message, toClientSocket.getOutputStream());
    }

    private void closeSocket(Socket socket) {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }


    private void writeDataToClient(String message, OutputStream os) throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new BufferedOutputStream(os)));
        writer.write(message);
        // 没刷一直在缓冲区那里，导致client接受不到
        writer.flush();
    }

    // 日志输出
    public void log(String mess) {
        CtServerLog.log(mess);
    }

}
