package com.xm.cygcore.util.log.lib.filter;

import com.xm.cygcore.util.log.lib.base.XmLoggerLevel;
import com.xm.cygcore.util.log.lib.base.XmLoggerBean;

import java.util.List;

/**
 * Created by wm on 15/6/11.
 * 根据级别进行过滤操作
 */
public class XmLevelFilter implements XmILogFilter {

    private List<XmLoggerLevel> mFilterLevelLogList;//要过滤的level

    @Override
    public boolean isFilter(XmLoggerBean bean) {
        if (mFilterLevelLogList == null || mFilterLevelLogList.size() <= 0) return false;

        for (XmLoggerLevel loggerLevel : mFilterLevelLogList) {
            // 存在，要过滤掉
            if (loggerLevel.equals(bean.logLevel)) return true;
        }

        return false;

    }
}
