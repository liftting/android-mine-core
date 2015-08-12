package com.xm.client.net.listener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wm on 15/7/13.
 */
public abstract class CtBaseReceiveDataListener implements CtReceiveDataListener {

    protected List<CtPostMainDataListener> mPostMainListener = new ArrayList<CtPostMainDataListener>();

    public void addPostMainDataListener(CtPostMainDataListener listener) {
        mPostMainListener.add(listener);
    }

    public void clearPostMainData() {
        mPostMainListener.clear();
    }


}
