package com.xm.webview;

import android.app.Application;
import android.content.Context;

/**
 * Created by wm on 15/6/25.
 */
public class XmWebApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }

}
