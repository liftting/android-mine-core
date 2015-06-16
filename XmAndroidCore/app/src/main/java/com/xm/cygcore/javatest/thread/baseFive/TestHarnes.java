package com.xm.cygcore.javatest.thread.baseFive;

import java.util.concurrent.CountDownLatch;

/**
 * Created by wm on 15/6/16.
 */
public class TestHarnes {

    public static long timeTasks(int nThreads, final Runnable task) throws InterruptedException {
        final CountDownLatch startGate = new CountDownLatch(1);
        final CountDownLatch endGate = new CountDownLatch(nThreads);

        for (int i = 0; i < nThreads; i++) {
            Thread t = new Thread() {
                @Override
                public void run() {
                    try {
                        startGate.await();// 初始时，所有thread会等待，直到，countDown，
                        try {
                            task.run();
                        } finally {
                            endGate.countDown();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

            };
            t.start();
        }

        long start = System.nanoTime();
        startGate.countDown();
        endGate.await();// 主线程执行到这会等待阻塞，知道值减为0
        long end = System.nanoTime();
        return end - start;
    }

    private static Runnable task = new Runnable() {
        @Override
        public void run() {
            System.out.println("enter run task");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    public static void main(String[] args) {

        try {
            long time = timeTasks(3, task);
            System.out.println(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
            // 发生中断时，处理，一般上传或者取消任务
        }

    }

}
