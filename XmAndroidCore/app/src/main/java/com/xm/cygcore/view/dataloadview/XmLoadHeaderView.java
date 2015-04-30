package com.xm.cygcore.view.dataloadview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xm.cygcore.R;

/**
 *
 *
 *
 */
public class XmLoadHeaderView extends LinearLayout {

    public final static int STATE_IDEL = 0; //
    public final static int STATE_PULLING = 1; //
    public final static int STATE_REFRESH = 2; //
    private RelativeLayout mContainer;
    private TextView mTvTipView; // show status tip


    private Context mContext;
    private int mCurrentState = STATE_IDEL;

    public XmLoadHeaderView(Context context) {
        super(context);
    }

    public XmLoadHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public XmLoadHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        mContainer = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.layer_auto_load_header, null);
        this.addView(mContainer, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
        mTvTipView = (TextView) mContainer.findViewById(R.id.tv_auto_head_loading_status);

    }

    public void updateStateInfo(int currentState) {
        if (mCurrentState == currentState) return;

        switch (currentState) {
            case STATE_IDEL:
                mTvTipView.setText("下拉刷新");
                break;
            case STATE_PULLING:
                mTvTipView.setText("释放刷新");
                break;
            case STATE_REFRESH:
                mTvTipView.setText("正在刷新");
                break;
        }
        mCurrentState = currentState;

    }

    public void updateHeight(int height) {

        if (height < 0) height = 0;

        LinearLayout.LayoutParams params = (LayoutParams) mContainer.getLayoutParams();
        params.height = height;
        mContainer.setLayoutParams(params);
    }

    public int getVisibleHeight(){
        return mContainer.getHeight();
    }

}
