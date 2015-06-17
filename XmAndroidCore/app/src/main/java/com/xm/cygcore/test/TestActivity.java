package com.xm.cygcore.test;

import android.app.Activity;
import android.os.Bundle;

import com.xm.cygcore.R;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;

/**
 * Created by wm on 15/6/10.
 */
public class TestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main_test);

        //构造Thread poll
        Executors.newFixedThreadPool(12);
        Executors.newSingleThreadExecutor();

    }
}
