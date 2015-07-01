package com.xm.webview.controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;

import com.xm.utils.PreferencesUtil;
import com.xm.utils.download.WebDownLoadHandler;
import com.xm.webview.util.Constants;

/**
 * Created by wm on 15/6/30.
 */
public class WebDownLoadListener implements DownloadListener {

    private Activity mActivity;

    public WebDownLoadListener(Activity activity) {
        mActivity = activity;
    }

    @Override
    public void onDownloadStart(final String url, final String userAgent, final String contentDisposition, final String mimetype, long contentLength) {
        String fileName = URLUtil.guessFileName(url, contentDisposition, mimetype);
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        WebDownLoadHandler.downLoad(mActivity, url, PreferencesUtil.getInstance(mActivity).getString(Constants.SP_WEB_DOWN_PATH), userAgent, contentDisposition, mimetype);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity); // dialog
        builder.setTitle(fileName)
                .setMessage("开始download")
                .setPositiveButton("begin",
                        dialogClickListener)
                .setNegativeButton("cancel",
                        dialogClickListener).show();
    }
}
