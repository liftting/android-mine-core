package com.xm.cygcore.util.log.lib.preview;

import com.xm.cygcore.util.log.lib.base.XmLoggerBean;

/**
 * Created by wm on 15/6/11.
 */
public interface XmILogPreview {


    /**
     * * 数据输出，
     * @param logBean  包含要显示log的信息
     * @return 进行格式化处理后要显示出来的
     */
    public String format(XmLoggerBean logBean);

}
