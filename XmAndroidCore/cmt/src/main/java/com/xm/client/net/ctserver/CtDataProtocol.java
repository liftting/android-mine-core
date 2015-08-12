package com.xm.client.net.ctserver;

/**
 * 数据协议
 */
public class CtDataProtocol {

    public static final int CT_CONNECT_INIT = 41001;
    public static final int CT_CONNECT_HEART_BEAT = 41002;
    public static final int CT_CONNECT_SHUT_DOWN_FROM_CLIENT = 41003;
    public static final int CT_CONNECT_NORMAL = 41004;

    public static final String header = "ct_header";
    public static final String message = "ct_message";
    public static final String userid = "ct_userid";
    public static final String sendToUserId = "ct_send_to_user_id";


}
