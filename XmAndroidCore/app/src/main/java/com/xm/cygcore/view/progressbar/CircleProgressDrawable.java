package com.xm.cygcore.view.progressbar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;

import com.xm.log.base.XmLogger;
import com.xm.log.server.XmLoggerFactory;

/**
 * Created by wm on 15/5/4.
 */
public class CircleProgressDrawable extends View {

    private XmLogger logger = XmLoggerFactory.getLogger("CircleProgressDrawable");

    private int mProgressRadius = 48; // 默认大小
    private int barWidth = 8;
    private int rimWidth = 8;



    //Colors (with defaults)
    private int barColor = 0xAA000000;
    private int rimColor = 0x00FFFFFF;


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

    //设置绘制边界，边界是否填充在布局文件中设置的整个矩形，
    private boolean fillRadius = true;


    public CircleProgressDrawable(Context context) {
        this(context, null, 0);
    }

    public CircleProgressDrawable(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressDrawable(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();

    }

    private void init() {
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

        logger.d("progress:onMeasure height:" + height + "  and  width:" + width);

        setMeasuredDimension(width, height);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        logger.d("progress:onSizeChanged and w:" + w + " -- h:" + h);
        setupBounds(w, h);
        setupPaints();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        logger.d("progress:onDraw");

        super.onDraw(canvas);

        boolean mustInvalidate = true;

        canvas.drawArc(circleBounds, 360, 360, false, rimPaint);

//Draw the spinning bar
        mustInvalidate = true;

        long deltaTime = (SystemClock.uptimeMillis() - lastTimeAnimated);
        float deltaNormalized = deltaTime * spinSpeed / 1000.0f;

        updateBarLength(deltaTime);

        mProgress += deltaNormalized;
        if (mProgress > 360) { //
            mProgress -= 360f;

            // A full turn has been completed
            // we run the callback with -1 in case we want to
            // do something, like changing the color
//            runCallback(-1.0f);
        }
        lastTimeAnimated = SystemClock.uptimeMillis();

        float from = mProgress - 90;  // 主要是修改from
        float length = barLength + barExtraLength; //

        if (isInEditMode()) {
            from = 0;
            length = 135;
        }

        //  绘制时，从哪个角度开始，经历的范围角度是多少，
        // from 存储开始角度，
        // length  计算需要旋转多少角度
        logger.d("progress:onDraw drawArc:from" + from + " and length:" + length);
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

    /**
     * Set the bounds of the component
     */
    private void setupBounds(int layout_width, int layout_height) {
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();

        if (!fillRadius) {
            // Width should equal to Height, find the min value to setup the circle
            int minValue = Math.min(layout_width - paddingLeft - paddingRight,
                    layout_height - paddingBottom - paddingTop);

            int circleDiameter = Math.min(minValue, mProgressRadius * 2 - barWidth * 2);

            // Calc the Offset if needed for centering the wheel in the available space
            int xOffset = (layout_width - paddingLeft - paddingRight - circleDiameter) / 2 + paddingLeft;
            int yOffset = (layout_height - paddingTop - paddingBottom - circleDiameter) / 2 + paddingTop;

            circleBounds = new RectF(xOffset + barWidth,
                    yOffset + barWidth,
                    xOffset + circleDiameter - barWidth,
                    yOffset + circleDiameter - barWidth);

            logger.d("progress: fillRadius is false : " + circleBounds);
        } else {
            circleBounds = new RectF(paddingLeft + barWidth,
                    paddingTop + barWidth,
                    layout_width - paddingRight - barWidth,
                    layout_height - paddingBottom - barWidth);
            logger.d("progress: fillRadius is true : " + circleBounds);
        }
    }

    /**
     * Set the properties of the paints we're using to
     * draw the progress wheel
     */
    private void setupPaints() {
        barPaint.setColor(barColor);
        barPaint.setAntiAlias(true);
        barPaint.setStyle(Paint.Style.STROKE);
        barPaint.setStrokeWidth(barWidth);

        rimPaint.setColor(rimColor);
        rimPaint.setAntiAlias(true);
        rimPaint.setStyle(Paint.Style.STROKE);
        rimPaint.setStrokeWidth(rimWidth);
    }


}
