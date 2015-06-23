package com.xm.log.outer;


import com.xm.log.base.XmLoggerBean;
import com.xm.log.filter.XmILogFilter;
import com.xm.log.preview.XmILogPreview;

/**
 * Created by wm on 15/6/17.
 */
public class XmFileLogOuter extends XmBaseLogOuter {

    private static final String logFileName = "xmLog";

    public XmFileLogOuter(XmILogPreview preview, XmILogFilter filter) {
        super(preview, filter);
    }

    @Override
    protected void outer(XmLoggerBean bean, String formatMessage) {

        // 将message info 输出到file中

    }
}
