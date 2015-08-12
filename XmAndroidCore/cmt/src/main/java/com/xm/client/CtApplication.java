package com.xm.client;

import android.app.Application;
import android.content.Context;

import com.xm.client.apdata.UserInfo;
import com.xm.client.apdata.UserManager;
import com.xm.client.net.CtExecutors;

import java.util.UUID;

/**
 * Created by wm on 15/7/10.
 */
public class CtApplication extends Application {

    private static Context mContext;

    public static Context getContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;



    }


}
