package com.xm.cygcore.javatest.lock;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by wm on 15/6/15.
 */
public class ThridLock implements Lock {

    private final int MIN_DELAY, MAX_DELAY;

    public ThridLock(int min, int max) {
        MIN_DELAY = min;
        MAX_DELAY = max;
    }

    private AtomicBoolean mutex = new AtomicBoolean(false);


    @Override
    public void lock() {

        // 增加回退对象
        ThSleep backoff = new ThSleep(MIN_DELAY, MAX_DELAY);
        while (true) {
            // 第一步使用读操作，尝试获取锁，当mutex为false时退出循环，表示可以获取锁
            while (mutex.get()) {

            }


            // 第二部使用getAndSet方法来尝试获取锁
            if (!mutex.getAndSet(true)) {
                return;
            } else { //
                //回退
                try {
                    backoff.taskSleep();
                } catch (InterruptedException e) {
                }
            }

        }

    }

    @Override
    public void unlock() {

    }
}
