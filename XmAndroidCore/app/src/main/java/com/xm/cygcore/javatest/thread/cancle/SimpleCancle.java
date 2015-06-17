package com.xm.cygcore.javatest.thread.cancle;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by wm on 15/6/16.
 */
public class SimpleCancle implements Runnable {

    private volatile boolean isCancel;
    private List<BigInteger> datas = new ArrayList<BigInteger>();

    @Override
    public void run() {
        BigInteger p = BigInteger.ONE;
        while (!isCancel) {
            p = p.nextProbablePrime();//素数生成
            synchronized (this) {
                datas.add(p);
            }
        }
    }


    public void cancel() {
        isCancel = true;
    }


}
