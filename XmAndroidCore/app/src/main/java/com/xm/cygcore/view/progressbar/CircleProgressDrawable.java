package com.xm.cygcore.view.progressbar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by wm on 15/5/4.
 */
public class CircleProgressDrawable extends View {

    private int mProgressRadius = 28; // 默认大小
    private int mProgressBarWidth = 4; //


    //Paints
    private Paint barPaint = new Paint();
    private Paint rimPaint = new Paint();

    //Rectangles
    private RectF circleBounds = new RectF();

    private float mProgress = 0.0f; // 当前进展
    private float mTargetProgress = 0.0f; // 要达到的进展
    private boolean mustInvalidate;
    private long spinSpeed;
    private long lastTimeAnimated;

    private double timeStartGrowing = 0;
    private double barSpinCycleTime = 460;
    private float barExtraLength = 0;
    private boolean barGrowingFromFront = true;

    private long pausedTimeWithoutGrowing = 0;
    private final long pauseGrowingTime = 200;

    private final int barLength = 16;
    private final int barMaxLength = 270;


    public CircleProgressDrawable(Context context) {
        super(context);
    }

    public CircleProgressDrawable(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleProgressDrawable(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width, height;

        if (widthMode == MeasureSpec.AT_MOST) {
            width = Math.min(widthSize, mProgressRadius);
        } else if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            width = mProgressRadius;
        }

        if (heightMode == MeasureSpec.AT_MOST) {
            height = Math.min(heightSize, mProgressRadius);
        } else if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = mProgressRadius;
        }


        setMeasuredDimension(width, height);

    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        boolean mustInvalidate = true;

        canvas.drawArc(circleBounds, 360, 360, false, rimPaint);

//Draw the spinning bar
        mustInvalidate = true;

        long deltaTime = (SystemClock.uptimeMillis() - lastTimeAnimated);
        float deltaNormalized = deltaTime * spinSpeed / 1000.0f;

        updateBarLength(deltaTime);

        mProgress += deltaNormalized;
        if (mProgress > 360) {
            mProgress -= 360f;

            // A full turn has been completed
            // we run the callback with -1 in case we want to
            // do something, like changing the color
//            runCallback(-1.0f);
        }
        lastTimeAnimated = SystemClock.uptimeMillis();

        float from = mProgress - 90;
        float length = barLength + barExtraLength;

        if (isInEditMode()) {
            from = 0;
            length = 135;
        }

        //  绘制时，从哪个角度开始，经历的范围角度是多少，
        // from 存储开始角度，
        // length  计算需要旋转多少角度
        canvas.drawArc(circleBounds, from, length, false,
                barPaint);


        if (mustInvalidate) {
            // 需要不断绘制
            invalidate();
        }

    }

    private void updateBarLength(long deltaTimeInMilliSeconds) {
        if (pausedTimeWithoutGrowing >= pauseGrowingTime) {
            timeStartGrowing += deltaTimeInMilliSeconds;

            if (timeStartGrowing > barSpinCycleTime) {
                // We completed a size change cycle
                // (growing or shrinking)
                timeStartGrowing -= barSpinCycleTime;
                //if(barGrowingFromFront) {
                pausedTimeWithoutGrowing = 0;
                //}
                barGrowingFromFront = !barGrowingFromFront;
            }

            float distance = (float) Math.cos((timeStartGrowing / barSpinCycleTime + 1) * Math.PI) / 2 + 0.5f;
            float destLength = (barMaxLength - barLength);

            if (barGrowingFromFront) {
                barExtraLength = distance * destLength;
            } else {
                float newLength = destLength * (1 - distance);
                mProgress += (barExtraLength - newLength);
                barExtraLength = newLength;
            }
        } else {
            pausedTimeWithoutGrowing += deltaTimeInMilliSeconds;
        }
    }


}
