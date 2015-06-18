package com.xm.cygcore.util.log.lib.outer;

import com.xm.cygcore.util.log.lib.base.XmLoggerBean;
import com.xm.cygcore.util.log.lib.filter.XmILogFilter;
import com.xm.cygcore.util.log.lib.preview.XmILogPreview;

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
