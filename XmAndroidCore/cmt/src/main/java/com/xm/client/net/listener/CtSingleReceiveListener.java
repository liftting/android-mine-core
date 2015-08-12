package com.xm.client.net.listener;

import com.xm.client.net.CtHandler;
import com.xm.client.net.ctdata.CtReceiveDataBean;

/**
 * Created by wm on 15/7/13.
 */
public class CtSingleReceiveListener extends CtBaseReceiveDataListener {

    @Override
    public void onReceiveData(final CtReceiveDataBean receiveDataBean) {
        // 这里被调用是sync的，要显示post出去

        CtHandler handler = CtHandler.getInstance();
        handler.post(new Runnable() {
            @Override
            public void run() {
//                Toast.makeText(CtApplication.getContext(), receiveDataBean.getMessage(), Toast.LENGTH_SHORT).show();
                handleDataOnMain(receiveDataBean);
            }
        });

    }

    public void handleDataOnMain(CtReceiveDataBean receiveDataBean) {
        for (CtPostMainDataListener mainDataListener : mPostMainListener) {
            mainDataListener.showMessage(receiveDataBean);
        }
    }


}
