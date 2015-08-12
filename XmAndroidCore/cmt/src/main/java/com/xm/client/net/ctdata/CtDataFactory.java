package com.xm.client.net.ctdata;

import com.xm.client.apdata.UserManager;
import com.xm.client.net.CtConfig;
import com.xm.client.net.ctserver.CtDataProtocol;

/**
 * Created by wm on 15/7/13.
 */
public class CtDataFactory {

    private static CtDataFactory instance;

    public static CtDataFactory getInstance() {
        if (instance == null) {
            instance = new CtDataFactory();
        }
        return instance;
    }

    private CtDataFactory() {

    }

    public CtSendDataBean createInitData() {
        CtSendDataBean bean = new CtSendDataBean();
        bean.setMessage(CtConfig.init_data_to_server);
        bean.setSendHeaderCode(CtDataProtocol.CT_CONNECT_INIT);

        bean.setFromUserId(UserManager.getInstance().getCurrentUserId());
        return bean;
    }

    public CtSendDataBean createHeartData() {
        CtSendDataBean bean = new CtSendDataBean();
        bean.setSendHeaderCode(CtDataProtocol.CT_CONNECT_HEART_BEAT);
        bean.setMessage(CtConfig.heart_head_to_server);
        return bean;
    }

    public CtSendDataBean createShutDownData() {

        CtSendDataBean bean = new CtSendDataBean();
        bean.setMessage(CtConfig.shut_down_to_server);
        bean.setSendHeaderCode(CtDataProtocol.CT_CONNECT_SHUT_DOWN_FROM_CLIENT);

        return bean;
    }

}
