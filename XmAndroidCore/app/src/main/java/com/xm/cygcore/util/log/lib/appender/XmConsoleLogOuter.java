package com.xm.cygcore.util.log.lib.appender;

import android.text.TextUtils;
import android.util.Log;

import com.xm.cygcore.util.log.lib.base.XmLoggerConfig;
import com.xm.cygcore.util.log.lib.base.XmLoggerLevel;
import com.xm.cygcore.util.log.lib.base.XmLoggerBean;

/**
 * Created by wm on 15/6/11.
 */
public class XmConsoleLogOuter extends XmBaseLogOuter {

    private static final String DEFAULT_TAG = XmLoggerConfig.getDefaultTAG();

    public XmConsoleLogOuter() {
        super(null, null);

    }

    @Override
    public void outer(XmLoggerBean bean, String formatMessage) {

        String tag = TextUtils.isEmpty(bean.logTag) ? DEFAULT_TAG : bean.logTag;

        if (bean.logLevel.equals(XmLoggerLevel.VERBOSE)) {
            Log.v(tag, formatMessage);
        } else if (bean.logLevel.equals(XmLoggerLevel.DEBUG)) {
            Log.d(tag, formatMessage);
        } else if (bean.logLevel.equals(XmLoggerLevel.INFO)) {
            Log.i(tag, formatMessage);
        } else if (bean.logLevel.equals(XmLoggerLevel.WARN)) {
            Log.w(tag, formatMessage);
        } else if (bean.logLevel.equals(XmLoggerLevel.ERROR)) {
            Log.e(tag, formatMessage);
        }
    }
}
