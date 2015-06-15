package com.xm.cygcore.javatest.lock;

/**
 * Created by wm on 15/6/15.
 */
public class MainLock {

    // CLHLock 无界队列 lock
//    private static Lock lock = new TimeCost(new CLHLock());

//    private static Lock lock = new CLHLock();

    private static Lock lock = new SecondLock();

//    private static Lock lock = new ThridLock(20, 50);

//    private static Lock lock = new ArrayLock(100);

    //private static TimeCost timeCost = new TimeCost(new TTASLock());

    private static int value = 0;

    public static void method() {
        lock.lock();
        System.out.println("Value: " + ++value);
        lock.unlock();
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            Thread t = new Thread(new Runnable() {

                @Override
                public void run() {
                    method();
                }

            });
            t.start();
        }
    }

}
