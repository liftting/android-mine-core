package com.xm.log.server;


import com.xm.log.base.XmLoggerBean;
import com.xm.log.outer.XmBaseLogOuter;

/**
 * Created by wm on 15/6/18.
 */
public interface LogService {

    public void syncLog(final XmBaseLogOuter outer, final XmLoggerBean bean);

}
