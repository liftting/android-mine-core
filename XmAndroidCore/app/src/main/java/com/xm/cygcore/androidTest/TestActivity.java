package com.xm.cygcore.androidTest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.xm.cygcore.R;
import com.xm.zxing.activity.CaptureActivity;

/**
 *
 */
public class TestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main_test);

        //构造Thread poll
//        Executors.newFixedThreadPool(12);
//        Executors.newSingleThreadExecutor();

    }

    public void onConvertQrcode(View v) {
        Intent intent = new Intent();
        intent.setClass(this, CaptureActivity.class);
        startActivity(intent);
    }
}
