package com.xm.cygcore.javatest.lock;

import java.util.Random;

/**
 * Created by wm on 15/6/15.
 * 回退
 */
public class ThSleep {

    private final int minDelay, maxDelay;

    private int limit;

    final Random random;

    public ThSleep(int min, int max){
        this.minDelay = min;
        this.maxDelay = max;
        limit = minDelay;
        random = new Random();
    }

    // 回退，线程等待一段时间
    public void taskSleep() throws InterruptedException{
        int delay = random.nextInt(limit);
        limit = Math.min(maxDelay, 2 * limit);
        Thread.sleep(delay);
    }

}
