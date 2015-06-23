package com.xm.log.preview;


import com.xm.log.base.XmLoggerBean;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wm on 15/6/11.
 */
public class XmSimplePreview implements XmILogPreview {

    @Override
    public String format(XmLoggerBean logBean) {
        StringBuffer sbf = new StringBuffer();
        sbf.append(logBean.logLevel.getName());
        sbf.append(" - ");
        sbf.append(logBean.logMessage);

        if (logBean.isCrashed()) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd mm:HH:ss");
            sbf.append(" crashed time："
                    + dateFormat.format(new Date(logBean.logTimeStamp)));
        }

        sbf.append("\n");
        return sbf.toString();
    }

}
