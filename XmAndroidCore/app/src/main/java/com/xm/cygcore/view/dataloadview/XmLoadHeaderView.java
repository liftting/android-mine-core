package com.xm.cygcore.view.dataloadview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by wm on 15/4/27.
 */
public class XmLoadHeaderView extends LinearLayout {

    public final static int STATE_NORMAL = 0; //
    public final static int STATE_PULLING = 1; //
    public final static int STATE_REFRESH = 2; //

    private Context mContext;

    public XmLoadHeaderView(Context context) {
        super(context);
    }

    public XmLoadHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public XmLoadHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(Context context){

    }
}
