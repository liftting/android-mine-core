package com.xm.webview.util;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.webkit.WebView;

import java.net.URISyntaxException;
import java.util.regex.Matcher;

/**
 * Created by wm on 15/6/23.
 */
public class XmWebUtils {


    public static int convertDpToPixels(int dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return (int) (dp * metrics.density + 0.5f);
    }



}
