package com.xm.cygcore.util.log.lib.outer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wm on 15/6/11.
 * <p/>
 * 处理配置
 */
public class XmLogOuterManager {

    // 要进行输出worker
    private List<XmBaseLogOuter> mLogOuterList = new ArrayList<XmBaseLogOuter>();
    private XmBaseLogOuter mDefaultLogOuter;
    private static XmLogOuterManager instance;

    //如果有很多thread来请求日志输出时，这里会异常
    public static XmLogOuterManager getInstance() {
        if (instance == null) {
            instance = new XmLogOuterManager();
            instance.mDefaultLogOuter = new XmConsoleLogOuter();
        }
        return instance;
    }

    public void addOuter(XmBaseLogOuter logOuter) {
        mLogOuterList.add(logOuter);
    }

    public List<XmBaseLogOuter> getAllOuter() {
        return mLogOuterList;
    }

    public void clearOuter() {
        mLogOuterList.clear();
    }

    public void updateDefaultOuter(XmBaseLogOuter defaultOuter) {
        mDefaultLogOuter = defaultOuter;
    }

    public XmBaseLogOuter getDefaultOuter() {
        return mDefaultLogOuter;
    }

}
