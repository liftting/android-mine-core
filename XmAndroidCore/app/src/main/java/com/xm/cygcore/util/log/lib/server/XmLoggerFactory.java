package com.xm.cygcore.util.log.lib.server;

import com.xm.cygcore.util.log.lib.base.XmLogger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by wm on 15/6/12.
 */
public class XmLoggerFactory {

    private static Map<String, XmLogger> loggers = null;

    static {
        loggers = new ConcurrentHashMap<String, XmLogger>();
    }

    public static XmLogger getLogger(String tag) {

        if (loggers.containsKey(tag)) {
            return loggers.get(tag);
        }
        XmLogger logger = new XmLogger(tag);

        // 对于CrashLogger采用单独的配置，目前由于配置都是写死的，此处特殊处理一下
//        if (CrashLogger.CRASH_LOGGER_NAME.equals(tag)) {
//            logger = new CrashLogger(CrashLogger.CRASH_LOGGER_NAME);
//        }

        loggers.put(tag, logger);

        return logger;
    }

}
