package com.xm.zxing;

import android.content.Context;
import android.os.Build;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

/**
 *
 *
 *
 */
public class QrcodeUtil {

    private static Context mContext;

    private static int SCREEN_HEIGHT = 0;
    private static int SCREEN_WIDTH = 0;

    public static void setContext(Context context) {
        mContext = context;
    }

    public static Context getAppContext() {
        return mContext;
    }

    public static int getScreenHeight() {
        if (SCREEN_HEIGHT == 0) {
            WindowManager w = (WindowManager) getAppContext().getSystemService(
                    Context.WINDOW_SERVICE);
            Display d = w.getDefaultDisplay();
            SCREEN_WIDTH = d.getWidth();
            SCREEN_HEIGHT = d.getHeight();

            // MD MX2竟然最底有一个工具栏，需要减去
            if (Build.MANUFACTURER.equalsIgnoreCase("Meizu")
                    && Build.PRODUCT.equalsIgnoreCase("meizu_mx2")) {
                SCREEN_HEIGHT = SCREEN_HEIGHT - dip2px(getAppContext(), 48);
            }
        }

        if (SCREEN_WIDTH > 0 && SCREEN_HEIGHT > 0 && SCREEN_WIDTH > SCREEN_HEIGHT) {
            int temp = SCREEN_HEIGHT;
            SCREEN_HEIGHT = SCREEN_WIDTH;
            SCREEN_WIDTH = temp;
        }

        return SCREEN_HEIGHT;
    }

    public static int getScreenWidth() {
        if (SCREEN_WIDTH == 0) {
            WindowManager w = (WindowManager) getAppContext().getSystemService(
                    Context.WINDOW_SERVICE);
            Display d = w.getDefaultDisplay();
            SCREEN_WIDTH = d.getWidth();
            SCREEN_HEIGHT = d.getHeight();
        }

        if (SCREEN_WIDTH > 0 && SCREEN_HEIGHT > 0 && SCREEN_WIDTH > SCREEN_HEIGHT) {
            int temp = SCREEN_HEIGHT;
            SCREEN_HEIGHT = SCREEN_WIDTH;
            SCREEN_WIDTH = temp;
        }

        return SCREEN_WIDTH;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static void makeToast(Context context, String test) {
        Toast.makeText(context, test, Toast.LENGTH_SHORT).show();
    }

}
