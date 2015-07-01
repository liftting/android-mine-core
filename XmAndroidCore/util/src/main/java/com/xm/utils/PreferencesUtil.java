package com.xm.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;


/**
 * Created by wm on 15/6/25.
 */
public class PreferencesUtil {

    private static String XM_WEB_SP_NAME = "default_sp";

    private static SharedPreferences mSharedPrefs;
    private static PreferencesUtil instance;

    public static PreferencesUtil getInstance(Context context) {
        if (instance == null) {
            instance = new PreferencesUtil(context);
        }
        return instance;
    }

    public static void initPreferenceName(String preferenceName) {
        if (!TextUtils.isEmpty(preferenceName)) {
            XM_WEB_SP_NAME = preferenceName;
        }
    }

    private PreferencesUtil(Context context) {
        mSharedPrefs = context.getSharedPreferences(XM_WEB_SP_NAME, Context.MODE_PRIVATE);
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

    public void putInt(String key, int value) {
        SharedPreferences.Editor editor = mSharedPrefs.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public int getInt(String key) {
        return mSharedPrefs.getInt(key, 0);
    }


}
