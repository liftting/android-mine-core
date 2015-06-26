package com.xm.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * 判断网络状况
 */
public class NetWorkUtil {

    /**
     * 是否处于Wi-Fi模式，只有移动网络模式才需要设置代理
     *
     * @param context
     * @return
     */
    public static boolean isWifiAvailable(Context context) {
        boolean isWifiAvailable = false;

        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            isWifiAvailable = true;
        }

        return isWifiAvailable;
    }

    /**
     * getIMSI
     *
     * @param context
     * @return
     */
    public static String getIMSI(Context context) {
        TelephonyManager mTelephonyMgr = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String imsi = mTelephonyMgr.getSubscriberId();
        return imsi == null ? "" : imsi;
    }

    /**
     * 连网类型
     *
     * @return
     */
    public static String getNetWorkType(Context context) {
        String netWorkType = null;
        ConnectivityManager mConnectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (mConnectivity != null) {
            NetworkInfo info = mConnectivity.getActiveNetworkInfo();
            if (info != null) {
                netWorkType = info.getTypeName();
            }
        }
        return netWorkType == null ? "" : netWorkType;
    }

    /**
     * 是否有网络连接
     *
     * @param context
     * @return
     */
    public static boolean hasNetWork(Context context) {
        ConnectivityManager mConnectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (mConnectivity != null) {
            NetworkInfo info = mConnectivity.getActiveNetworkInfo();
            if (info != null) {
                return true;
            }
        }
        return false;
    }

}
