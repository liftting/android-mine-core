package com.xm.webview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by wm on 15/7/2.
 */
public class ScrollChangeFrameLayout extends FrameLayout {


    public ScrollChangeFrameLayout(Context context) {
        super(context);
    }

    public ScrollChangeFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollChangeFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                final ViewGroup parent = (ViewGroup) getParent();
//                if (parent.onInterceptTouchEvent(event)) {
                    event.setAction(MotionEvent.ACTION_DOWN);
                    post(new Runnable() {
                        @Override
                        public void run() {
                            parent.dispatchTouchEvent(event);

                        }
                    });
//                }
                break;
        }
        return super.onTouchEvent(event);
    }
}
