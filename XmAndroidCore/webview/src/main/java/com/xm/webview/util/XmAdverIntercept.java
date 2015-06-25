package com.xm.webview.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.xm.log.base.XmLogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * Created by wm on 15/6/25.
 */
public class XmAdverIntercept {

    private XmLogger logger = new XmLogger("AdverIntercept");

    private boolean isAdIntercept;
    private static final String BLOCKED_DOMAINS_LIST_FILE_NAME = "hosts.txt";
    private static XmAdverIntercept mInstance;

    private static final Set<String> mBlockedDomainsList = new HashSet<String>();
    private static final Locale mLocale = Locale.getDefault();

    public static XmAdverIntercept getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new XmAdverIntercept(context);
        }
        return mInstance;
    }

    private XmAdverIntercept(Context context) {
        if (mBlockedDomainsList.isEmpty() && Constants.FULL_VERSION) {
            loadBlockedDomainsList(context);
        }
        isAdIntercept = PreferencesUtil.getInstance().getBoolean(PreferencesUtil.SP_AD_INTERCEPT);
        isAdIntercept = true;
    }

    public void updatePreference() {
        isAdIntercept = PreferencesUtil.getInstance().getBoolean(PreferencesUtil.SP_AD_INTERCEPT);

        isAdIntercept = true;
    }

    private void loadBlockedDomainsList(final Context context) {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                AssetManager asset = context.getAssets();
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(
                            asset.open(BLOCKED_DOMAINS_LIST_FILE_NAME)));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        mBlockedDomainsList.add(line.trim().toLowerCase(mLocale));
                    }
                } catch (IOException e) {
                    logger.e("loadHost:Reading blocked domains list from file '"
                            + BLOCKED_DOMAINS_LIST_FILE_NAME + "' failed.", e);
                }
            }
        });
        thread.start();
    }

    public boolean isAd(String url) {
        if (!isAdIntercept || url == null) {
            return false;
        }

        String domain;
        try {
            domain = getDomainName(url);
        } catch (URISyntaxException e) {
            logger.d("URL '" + url + "' is invalid", e);
            return false;
        }

        boolean isOnBlacklist = mBlockedDomainsList.contains(domain.toLowerCase(mLocale));
        if (isOnBlacklist) {
            logger.d("URL '" + url + "' is an ad");
        }
        return isOnBlacklist;
    }

    private static String getDomainName(String url) throws URISyntaxException {
        int index = url.indexOf('/', 8);
        if (index != -1) {
            url = url.substring(0, index);
        }

        URI uri = new URI(url);
        String domain = uri.getHost();
        if (domain == null) {
            return url;
        }

        return domain.startsWith("www.") ? domain.substring(4) : domain;
    }

}
