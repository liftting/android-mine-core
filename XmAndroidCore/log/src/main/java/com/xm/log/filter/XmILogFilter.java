package com.xm.log.filter;


import com.xm.log.base.XmLoggerBean;

/**
 * Created by wm on 15/6/11.
 */
public interface XmILogFilter {

    /**
     * 处理log是否给filter掉，不进行后续处理
     * @param bean 要进行判断的log
     * @return false
     */
    public boolean isFilter(XmLoggerBean bean);

}
