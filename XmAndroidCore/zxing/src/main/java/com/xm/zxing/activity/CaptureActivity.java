
package com.xm.zxing.activity;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.xm.zxing.QrcodeUtil;
import com.xm.zxing.R;
import com.xm.zxing.camera.BeepManager;
import com.xm.zxing.camera.CameraManager;
import com.xm.zxing.decoding.CaptureActivityHandler;
import com.xm.zxing.decoding.DecodeFormatManager;
import com.xm.zxing.processor.BarcodeScannerProcessor;
import com.xm.zxing.view.ViewfinderView;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import org.json.JSONObject;

/**
 * 条码扫描
 *
 * @author xiaohu 2013-08-24
 */
public class CaptureActivity extends Activity implements Callback {

    private CaptureActivityHandler captureActivityHandler;

    private ViewfinderView viewfinderView;

//    private ProcessDialog mDialog;

    private View mVerifyPb;

    private static final int MESSAGE_QRCODE_ORDER_LOAD = 100;
    private static final int REQUEST_QRCODE_LIST = 101; //请求跳转列表

    private boolean isCaptureActive;

    /**
     * 扫描成功后响铃
     */
    private BeepManager beepManager;

    /**
     * 支持扫描的条码类型
     */
    private Vector<BarcodeFormat> decodeFormats = new Vector<BarcodeFormat>();

    /**
     * 告诉处理器扫描到的条码类型
     */
    private int scanType = BarcodeScannerProcessor.ScanParamConstants.RESULT_TYPE_UNHANDLED;

    /**
     * 扫描到的内容
     */
    private String scanContent;

    /**
     * 还未经处理器处理的扫描内容
     */
    private String scanContentBeforeDealed;

    /**
     * 正在加载的商品id
     */
    private String mLoadingBabyId;

    private String mLoadingBabyTokenUrl;

    private SurfaceHolder surfaceHolder;

    private boolean isSurfaceExists;

    private LinearLayout mBarContainer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        int actionBarHeight = intent.getIntExtra("action_bar_height", 0);

        setContentView(R.layout.zx_capture);
        setTitle("验证二维码");

        // 修改titleBar的高度
        if (actionBarHeight > 0) {
            mBarContainer = (LinearLayout) findViewById(R.id.ly_bar_container);
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mBarContainer.getLayoutParams();
            params.height = actionBarHeight;
            mBarContainer.setLayoutParams(params);
        }


        CameraManager.init(getApplication());

        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);

        surfaceHolder = surfaceView.getHolder();
        beepManager = new BeepManager(this);

        // 支持两个编码集合
        // QR code
        decodeFormats.addAll(DecodeFormatManager.QR_CODE_FORMATS);
        // 一维条码集合
        decodeFormats.addAll(DecodeFormatManager.ONE_D_FORMATS);

//        // 验证中对话框
//        mDialog = new ProcessDialog(this);
        mVerifyPb = findViewById(R.id.progressbg);

    }

    @Override
    protected void onResume() {
        super.onResume();

        isCaptureActive = true;
//        logger.i("onResume called");

        if (isSurfaceExists) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        beepManager.updatePrefs();
    }

    @Override
    protected void onPause() {
        super.onPause();

//        logger.i("onPause called");
        isCaptureActive = false;

        if (captureActivityHandler != null) {
            captureActivityHandler.quitSynchronously();
            captureActivityHandler = null;
        }

        // 卸载处理器
        BarcodeScannerProcessor.unloadProcessor();

        CameraManager.get().closeDriver();
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }

//        logger.i("initCamera called");

        if (captureActivityHandler != null) {
            captureActivityHandler.quitSynchronously();
        }
        captureActivityHandler = new CaptureActivityHandler(this, decodeFormats, null);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
//        logger.d("surfaceCreated called, isCaptureActive=" + isCaptureActive);

        if (isCaptureActive) {
            initCamera(holder);
        }
        isSurfaceExists = true;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
//        logger.d("surface destroyed");

        isSurfaceExists = false;
    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return captureActivityHandler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();
    }

    /**
     * 扫描成功后回调
     *
     * @param obj
     * @param barcode
     */
    public void handleDecode(Result obj, Bitmap barcode) {
        if (null != beepManager) {
            beepManager.playBeepSound();
        }

        BarcodeFormat barcodeFormat = obj.getBarcodeFormat();

        scanContent = obj.getText();
//        logger.i("scan finished, barcodeFormat=" + barcodeFormat + ", scanContent=" + scanContent);

        scanType = BarcodeScannerProcessor.ScanParamConstants.SCAN_TYPE_UNKNOWN;
        if (DecodeFormatManager.ONE_D_FORMATS.contains(barcodeFormat)) {
            scanType = BarcodeScannerProcessor.ScanParamConstants.SCAN_TYPE_ONE_D_CODE;
        } else if (DecodeFormatManager.QR_CODE_FORMATS.contains(barcodeFormat)) {
            scanType = BarcodeScannerProcessor.ScanParamConstants.SCAN_TYPE_TWO_D_CODE;
        }

        doHandleDecode(barcode);
    }

    public void doHandleDecode(Bitmap resultBitmap) {

        mVerifyPb.setVisibility(View.VISIBLE);

        // 扫描的结果串
        scanContentBeforeDealed = new String(scanContent);
        if (null == scanContent) {
            /**
             * 如果要是返回为空，要么是扫描器内部处理有问题，要么是处理器下载失败
             */
            QrcodeUtil.makeToast(this, "扫描失败，请保证网络通畅，再试一次！");
            mVerifyPb.setVisibility(View.GONE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    sendResetCameraMessage();
                }
            }, 1000);
            return;
        }

        // 扫描成功获得字符串
//        viewfinderView.drawResultBitmap(resultBitmap);
        viewfinderView.setScanFinished(true);

        handleOrderRequest(scanContent);


    }

    public void sendResetCameraMessage() {
        if (null != captureActivityHandler) {
            Message message = Message.obtain(captureActivityHandler, R.id.restart_preview, null);
            captureActivityHandler.sendMessage(message);
        }

    }

    /**
     * 解析成功后数据请求
     *
     * @param qrCode
     */
    private void handleOrderRequest(String qrCode) {


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_QRCODE_LIST) {
            if (resultCode == RESULT_OK) {
                QrcodeUtil.makeToast(this, "交易完成");
            } else {

            }
        }

    }

    private void resetCapture(String showTip) {
        QrcodeUtil.makeToast(this, showTip);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sendResetCameraMessage();
            }
        }, 1000);
    }
}
