package com.xm.webview.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;

import com.xm.webview.XmWebApplication;


/**
 * Created by wm on 15/6/25.
 */
public class PreferencesUtil {

    private static final String XM_WEB_SP_NAME = "xm_web";
    public static final String SP_AD_INTERCEPT = "xm_wen_ad";

    private static SharedPreferences mSharedPrefs;
    private static PreferencesUtil instance;

    public static PreferencesUtil getInstance() {
        if (instance == null) {
            instance = new PreferencesUtil();
        }
        return instance;
    }

    private PreferencesUtil() {
        mSharedPrefs = XmWebApplication.getContext().getSharedPreferences(XM_WEB_SP_NAME, Context.MODE_PRIVATE);
    }


    public void putString(String key, String value) {
        SharedPreferences.Editor editor = mSharedPrefs.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getString(String key) {
        return mSharedPrefs.getString(key, null);
    }


    public void putBoolean(String key, Boolean value) {
        SharedPreferences.Editor editor = mSharedPrefs.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public Boolean getBoolean(String key) {
        return mSharedPrefs.getBoolean(key, false);
    }


    public void putFloat(String key, float value) {
        SharedPreferences.Editor editor = mSharedPrefs.edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    public float getFloat(String key) {
        return mSharedPrefs.getFloat(key, 0);
    }

    public void putLong(String key, long value) {
        SharedPreferences.Editor editor = mSharedPrefs.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public long getLong(String key) {
        return mSharedPrefs.getLong(key, 0);
    }


}
