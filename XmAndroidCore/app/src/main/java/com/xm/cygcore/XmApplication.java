package com.xm.cygcore;

import android.app.Application;

import com.xm.zxing.QrcodeUtil;

/**
 * Created by wm on 15/6/19.
 */
public class XmApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        QrcodeUtil.setContext(this);

    }
}
