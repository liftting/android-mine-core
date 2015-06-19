/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xm.zxing.view;

import java.util.Collection;
import java.util.HashSet;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.*;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.view.View;

import com.google.zxing.ResultPoint;
import com.xm.zxing.QrcodeUtil;
import com.xm.zxing.R;
import com.xm.zxing.camera.CameraManager;


/**
 * This view is overlaid on top of the camera preview. It adds the viewfinder
 * rectangle and partial transparency outside it, as well as the laser scanner
 * animation and result points.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class ViewfinderView extends View {
    /**
     * 重绘扫描窗口的时间间隔
     */
    private static final long ANIMATION_DELAY = 80L;

    private static final int[] SCANNER_ALPHA = {
            0, 64, 128, 192, 255, 192, 128, 64
    };

    private final Paint paint;

    private final int maskColor;

    private Collection<ResultPoint> possibleResultPoints;

    private final int laserColor;

    private final int resultMaskColor;

    private int scannerAlpha;

    private Context context;

    public boolean scanFinished;

    /**
     * 四个绿色边角对应的宽度
     */
    private int mCornerWidth = 6;

    /**
     * 四个绿色边角对应的长度
     */
    private int ScreenRate;

    private Bitmap resultBitmap;

    // This constructor is used when the class is built from an XML resource.
    public ViewfinderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        // Initialize these once for performance rather than calling them every
        // time in onDraw().
        paint = new Paint();
        Resources resources = getResources();
        maskColor = resources.getColor(R.color.zx_viewfinder_mask);
        resultMaskColor = resources.getColor(R.color.zx_capture_scanresult_mask);
        possibleResultPoints = new HashSet<ResultPoint>(5);
        laserColor = resources.getColor(R.color.zx_capture_scan_laser);

        //将像素转换成dp
        ScreenRate = QrcodeUtil.dip2px(context, 18);
        mCornerWidth = QrcodeUtil.dip2px(context, 4);

    }

    @Override
    public void onDraw(Canvas canvas) {
        Rect frame = CameraManager.get().getFramingRect();
        if (frame == null) {
            return;
        }

        // 获取屏幕的宽和高
        int width = canvas.getWidth();
        int height = canvas.getHeight();

        // 获取header高度
        int headerHeight = QrcodeUtil.getScreenHeight() - height;

        headerHeight = 0; // reset 0

        if (scanFinished) { // 如果成功扫描一次，则使用全屏遮罩
            paint.setColor(resultMaskColor);
            canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), paint);

        } else { // 如果是刚启用扫描，则用四块暗色调的矩形拼出一个正方形来，即为中间的扫描区
            paint.setColor(maskColor);
            canvas.drawRect(0, 0, width, frame.top - headerHeight, paint); // up
            canvas.drawRect(frame.right, frame.top - headerHeight, width, frame.bottom - headerHeight, paint); // right
            canvas.drawRect(0, frame.bottom - headerHeight, width, height, paint); // bottom
            canvas.drawRect(0, frame.top - headerHeight, frame.left, frame.bottom - headerHeight, paint); // left


            //画扫描框边上的角，总共8个部分
            paint.setColor(Color.GREEN);
            canvas.drawRect(frame.left, frame.top, frame.left + ScreenRate,
                    frame.top + mCornerWidth, paint);
            canvas.drawRect(frame.left, frame.top, frame.left + mCornerWidth, frame.top
                    + ScreenRate, paint);
            canvas.drawRect(frame.right - ScreenRate, frame.top, frame.right,
                    frame.top + mCornerWidth, paint);
            canvas.drawRect(frame.right - mCornerWidth, frame.top, frame.right, frame.top
                    + ScreenRate, paint);


            canvas.drawRect(frame.left, frame.bottom - mCornerWidth, frame.left
                    + ScreenRate, frame.bottom, paint);
            canvas.drawRect(frame.left, frame.bottom - ScreenRate,
                    frame.left + mCornerWidth, frame.bottom, paint);
            canvas.drawRect(frame.right - ScreenRate, frame.bottom - mCornerWidth,
                    frame.right, frame.bottom, paint);
            canvas.drawRect(frame.right - mCornerWidth, frame.bottom - ScreenRate,
                    frame.right, frame.bottom, paint);


            // 绘制激光，其实就是一个矩形
            paint.setColor(laserColor);
            paint.setAlpha(SCANNER_ALPHA[scannerAlpha]);
            scannerAlpha = (scannerAlpha + 1) % SCANNER_ALPHA.length;
            int middle = frame.height() / 2 + frame.top - headerHeight;
            canvas.drawRect(frame.left, middle - 1, frame.right, middle + 2, paint);

            // 绘制扫描区域上方的文字
            paint.setColor(getResources().getColor(R.color.zx_capture_scan_text));
            paint.setTextAlign(Align.CENTER);
            paint.setTextSize(QrcodeUtil.dip2px(context, 14));
            int vAxis = QrcodeUtil.getScreenWidth() / 2;
            int headerTextHeight = (frame.top - headerHeight) / 2;
            canvas.drawText(getResources().getString(R.string.zx_capture_scan_text_header), vAxis, headerTextHeight, paint);

            // 绘制扫描区域下方的文字
//            canvas.drawText(getResources().getString(R.string.bj_capture_scan_text_hint), vAxis, frame.bottom - headerHeight + 100, paint);

            // 重绘扫描区内的内容
            int repaintOffset = 6;
            postInvalidateDelayed(ANIMATION_DELAY, frame.left - repaintOffset, frame.top - headerHeight - repaintOffset,
                    frame.right + repaintOffset, frame.bottom + headerHeight + repaintOffset);
        }
    }

    public void drawViewfinder() {
        setScanFinished(false);
        resultBitmap = null;
        invalidate();
    }

    public void setScanFinished(boolean scanFinished) {
        this.scanFinished = scanFinished;
    }

    /**
     * Draw a bitmap with the result points highlighted instead of the live
     * scanning display.
     *
     * @param barcode An image of the decoded barcode.
     */
    public void drawResultBitmap(Bitmap barcode) {
//        resultBitmap = barcode;
        invalidate();
    }

    public void addPossibleResultPoint(ResultPoint point) {
        possibleResultPoints.add(point);
    }

}
