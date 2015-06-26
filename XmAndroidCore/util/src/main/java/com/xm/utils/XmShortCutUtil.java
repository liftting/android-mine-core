package com.xm.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by wm on 15/6/15.
 * <p/>
 * 创建快捷方式的逻辑
 */
public class XmShortCutUtil {

    private static final String ADD_SHORCUT_TAG = "add_short_cut";
    private static final String SHORT_CUT_UTIL_SP = "short_cut_sp";

    private Context mContext;
    private static XmShortCutUtil instance;

    private XmShortCutUtil(Context context) {
        mContext = context;
    }

    public static XmShortCutUtil getInstance(Context context) {
        if (instance == null) {
            instance = new XmShortCutUtil(context);
        }
        return instance;
    }

    /**
     * 创建快捷方式
     */
    public void createShortCut(Class startActivityClass) {
        if (!hasShortcut(mContext)) {
            goAddShortCut(startActivityClass);
        }
    }

    /**
     * 创建桌面快捷方式
     */
    private void goAddShortCut(Class startActivityClass) {
        Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, getAppName(mContext));

        Intent intent = new Intent(mContext, startActivityClass); //设置点击图标处理
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setAction(Intent.ACTION_MAIN);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
        Intent.ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(mContext,
                R.drawable.ic_launcher);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);
        shortcut.putExtra("duplicate", false);
        mContext.sendBroadcast(shortcut);

        // 双保险，避免重复创建
        SharedPreferences sp = mContext.getSharedPreferences(SHORT_CUT_UTIL_SP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(ADD_SHORCUT_TAG, true);
        editor.commit();
    }

    /**
     * 判断系统是否已存在快捷方式
     *
     * @param context
     * @return
     */
    private boolean hasShortcut(Context context) {

        try {
            SharedPreferences sp = context.getSharedPreferences(SHORT_CUT_UTIL_SP, Context.MODE_PRIVATE);
            boolean isHasAddCut = sp.getBoolean(ADD_SHORCUT_TAG, false);

            if (isHasAddCut) {
                return true;
            }

            // 这里获取时，之前的逻辑是参考stackoverflow中，获取一个权限

            //            String authority = getAuthorityFromPermission(AppUtil.getAppContext(),
//                    "com.android.launcher.permission.READ_SETTINGS");
//
//            if (TextUtils.isEmpty(authority)) {
//                return true;
//            }

            // --- 之前的逻辑处理，在有些国产定制手机中，读权限的名称不一样有的手机，

            ProviderInfo providerInfo = getDefaultLauncherProvider(context);

            if (providerInfo == null || TextUtils.isEmpty(providerInfo.authority)) {
                return false;
            }

            final Uri CONTENT_URI = Uri.parse("content://" + providerInfo.authority + "/favorites?notify=true");

            Cursor cursor = mContext
                    .getContentResolver()
                    .query(CONTENT_URI,
                            new String[]{
                                    "title"
                            },
                            "title='"
                                    + getAppName(context) + "'", null, null
                    );

            if (cursor != null && cursor.moveToNext()) {
                cursor.close();
                return true;
            }

            if (!checkShouldCreateByLauncher(providerInfo)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 默认Launcher的Provider，用于判断快捷方式是否创建
     *
     * @param context
     * @return
     */
    private ProviderInfo getDefaultLauncherProvider(Context context) {

        String defaultPackageName = getDefaultLauncherPackageName();
        if (TextUtils.isEmpty(defaultPackageName)) {
            return null;
        }

        List<ProviderInfo> providers = getLauncherProviders(context);
        if (providers == null || providers.size() == 0) {
            return null;
        }

        for (int i = 0; i < providers.size(); i++) {
            if (defaultPackageName.equals(providers.get(i).packageName)) {
                return providers.get(i);
            }
        }

        return null;
    }

    /**
     * 对于一些已知类型的桌面，不用再重复创建快捷方式
     *
     * @param providerInfo
     * @return
     */
    private boolean checkShouldCreateByLauncher(ProviderInfo providerInfo) {

        if (providerInfo.packageName == null) {
            return true;
        }

        // 小米桌面
        if (providerInfo.packageName.indexOf("com.miui") != -1) {
            return false;
        }

        return true;
    }

    /**
     * 系统所有Launcher应用的Provider
     *
     * @param context
     * @return
     */
    private List<ProviderInfo> getLauncherProviders(Context context) {

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);

        List<ResolveInfo> resolveInfos = getIntentProcess(context, intent);

        if (resolveInfos == null || resolveInfos.size() == 0) {
            return null;
        }

        Set<String> providerPackageNams = new HashSet<String>();
        for (int i = 0; i < resolveInfos.size(); i++) {

            ActivityInfo activityInfo = resolveInfos.get(i).activityInfo;
            if (activityInfo == null || TextUtils.isEmpty(activityInfo.packageName)) {
                continue;
            }

            providerPackageNams.add(activityInfo.packageName);
        }

        List<PackageInfo> packInfos = context.getPackageManager().getInstalledPackages(
                PackageManager.GET_PROVIDERS);

        if (packInfos == null || packInfos.size() == 0) {
            return null;
        }

        List<ProviderInfo> providers = new ArrayList<ProviderInfo>();

        for (int i = 0; i < packInfos.size(); i++) {
            ProviderInfo[] providerArray = packInfos.get(i).providers;
            if (providerArray != null && providerArray.length > 0) {
                for (ProviderInfo provider : providerArray) {
                    if (providerPackageNams.contains(provider.packageName)) {
                        providers.add(provider);
                    }
                }
            }
        }

        return providers;
    }

    /**
     * 获得默认Launcher的包名
     *
     * @return
     */
    private String getDefaultLauncherPackageName() {

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        ResolveInfo resolveInfo = mContext.getPackageManager()
                .resolveActivity(intent, 0);

        if (resolveInfo == null || resolveInfo.activityInfo == null) {
            return null;
        }

        // 没有设置默认Launcher
        if ("android".equals(resolveInfo.activityInfo.packageName)) {
            return null;
        }

        return resolveInfo.activityInfo.packageName;
    }


    /**
     * Intent处理器
     *
     * @param context
     * @param intent
     * @return
     */
    private List<ResolveInfo> getIntentProcess(Context context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
                PackageManager.GET_ACTIVITIES);
        return list;
    }

    private String getAppName(Context context) {
        return context.getString(R.string.app_name);
    }

    // ---- 获取读写权限

    /**
     * 解决不同的ROM快捷方式表存储路径不一样的问题
     *
     * @param context
     * @param permission
     * @return
     */
    public static String getAuthorityFromPermission(Context context, String permission) {

        if (TextUtils.isEmpty(permission)) {
            return null;
        }

        List<PackageInfo> packs = context.getPackageManager().getInstalledPackages(
                PackageManager.GET_PROVIDERS);

        if (packs == null) {
            return null;
        }

        for (PackageInfo pack : packs) {
            ProviderInfo[] providers = pack.providers;
            if (providers != null) {
                for (ProviderInfo provider : providers) {
                    if (permission.equals(provider.readPermission)
                            || permission.equals(provider.writePermission)) {
                        return provider.authority;
                    }
                }
            }
        }

        return null;
    }

}
