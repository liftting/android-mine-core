package com.xm.cygcore.util.log.lib.appender;

import com.xm.cygcore.util.log.lib.base.XmLoggerBean;
import com.xm.cygcore.util.log.lib.filter.XmILogFilter;
import com.xm.cygcore.util.log.lib.preview.XmILogPreview;
import com.xm.cygcore.util.log.lib.preview.XmSimplePreview;

/**
 * Created by wm on 15/6/11.
 * <p/>
 * this is output manager
 * <p/>
 * 1, 拦截
 * 2, 过滤输出message
 * 3,配置输出路径，console  或  日志文件
 */
public abstract class XmBaseLogOuter {

    private XmILogPreview mLogPreview;
    private XmILogFilter mLogFilter;

    public XmBaseLogOuter(XmILogPreview preview, XmILogFilter filter) {

        mLogPreview = preview != null ? preview : new XmSimplePreview();
        mLogFilter = filter;

    }

    public void outLog(XmLoggerBean bean) {
        // this is not show this log
        if (mLogFilter != null && mLogFilter.isFilter(bean)) return;

        String message = bean.logMessage;
        if (mLogPreview != null) {
            message = mLogPreview.format(bean);
        }

        outer(bean, message);

    }

    /**
     * 进行输出操作，之前是给数据进行特殊拦截处理
     *
     * @param bean
     * @param formatMessage
     */
    protected abstract void outer(XmLoggerBean bean, String formatMessage);

}
