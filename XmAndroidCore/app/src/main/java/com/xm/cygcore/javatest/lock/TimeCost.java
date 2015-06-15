package com.xm.cygcore.javatest.lock;

/**
 * Created by wm on 15/6/15.
 */
public class TimeCost implements Lock {
    private final Lock lock;

    public TimeCost(Lock lock) {
        this.lock = lock;
    }

    @Override
    public void lock() {
        long start = System.nanoTime();
        lock.lock();
        long duration = System.nanoTime() - start;
        System.out.println(lock.toString() + " time cost is " + duration + " ns");
    }

    @Override
    public void unlock() {
        lock.unlock();
    }
}
