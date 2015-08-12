package com.xm.client.net;

/**
 * Created by wm on 15/7/10.
 */
public class CtConfig {

    public static final String host = "172.16.1.44";
    public static final int port = 39999;

    public static final int so_linger_time = 10; // 单位 s

    public static final String heart_head_to_server = "heart_break_to_server";
    public static final String heart_head_to_client = "heart_break_to_client";
    public static final String shut_down_to_server = "shut_down_to_server";

    public static final String chat_main_listener = "single_chat_main";

    public static final String init_data_to_server = "connect_init:";
    public static final String message_send_to_server = "normal_send_to_server:";

    private static boolean isKeepAlive = true;

    public static boolean isStopHeartBeat = false;

    // soTime config 4000
    public static int heartBeatTime = 3000;


}
