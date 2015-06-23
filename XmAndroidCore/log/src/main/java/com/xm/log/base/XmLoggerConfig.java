package com.xm.log.base;

import android.content.Context;

/**
 * Created by wm on 15/6/10.
 * config
 */
public class XmLoggerConfig {

    private static Context mContext;

    private static boolean isEnable = true;

    private static final String DEFAULT_LOGGER_TAG = "xm";

    private XmLoggerConfig() {
    }

    public static String getDefaultTAG() {

        if (mContext == null) {
            return DEFAULT_LOGGER_TAG;
        }

        String packageName = mContext.getPackageName();
        return packageName;
    }

    public static void setContext(Context context) {
        mContext = context;
    }

    public static void setEnable(boolean enable) {
        isEnable = enable;
    }

    public static boolean isEnable() {
        return isEnable;
    }

    public static Context getContext() {
        return mContext;
    }

}
