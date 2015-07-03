package com.xm.utils.jssafeview;

import android.util.Log;
import android.webkit.JavascriptInterface;

/**
 * Created by wm on 15/7/3.
 */
public class JsInterface {

    @JavascriptInterface
    public String onButtonClick(String text) {
        final String str = text;

        // java do something
        // 注意这里可能是异步线程，要post到主线程中
        // 带返回值时的调用方式

        return "This text is returned from Java layer.  js text = " + text;
    }

    @JavascriptInterface
    public void onImageClick(String url, int width, int height) {
        final String str = "onImageClick: text = " + url + "  width = " + width + "  height = " + height;
        Log.i("leehong2", str);
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//            }
//        });
    }

}
