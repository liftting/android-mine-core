package com.xm.cygcore.util.log.lib.server;

import com.xm.cygcore.util.log.lib.base.XmLoggerBean;
import com.xm.cygcore.util.log.lib.outer.XmBaseLogOuter;

/**
 * Created by wm on 15/6/18.
 */
public interface LogService {

    public void syncLog(final XmBaseLogOuter outer, final XmLoggerBean bean);

}
