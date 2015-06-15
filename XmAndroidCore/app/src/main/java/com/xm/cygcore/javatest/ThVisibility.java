package com.xm.cygcore.javatest;

/**
 * Created by wm on 15/6/12.
 */
public class ThVisibility {

    // 会出现的问题

    private static boolean ready;
    private static int number;

    public static class NumThread extends Thread {

        @Override
        public void run() {
            while (!ready) {
                Thread.yield(); // out the cpu
            }
            System.out.print(number);
        }

    }

    // 1,正常：会显示出42，
    // 2,可能NumThread会一直运行下去，应为它读取的值一直是以前的默认值，没有读取到更新的ready = true的值
    // 3，可能因为重排序问题，导致他直接显示了0，即看到了ready设置了值，但是没有看到number设置值问题

    // 在没有同步的情况下，编译器 运行时肯可能都会对操作顺序进行一些调整操作，导致意外情况发生
    public static void main(String[] args) {
        new NumThread().start();
        number = 42;
        ready = true;
    }


    // 失效数据
    // 这里再多线程时，可能是set的时候，如果别的thread 调用get就会导致数据的错位，出现脏数据
    private int noSeeData;
    public void setData(int data){
        noSeeData = data;
    }

    public int getData(){
        return noSeeData;
    }


    //  非原子的64位操作 long  double
    // 非volatile的64位变量，java内存模型中，jvm允许将64位的读操作或写操作会被拆分成两个32位的操作，如果进行读写时，那么久导致了
    // 高32位  和  低32位的数据错位问题


    //加锁与可见性
    //加锁操作，要保证同步是，保存两个线程访问的时相同的锁对象

    // volatile
    // 弱的同步机制，保证数据更新后通知到其他的thread
    // 但是这种类型变量使用时，要注意，因为它没有保证像有些操作的原子性  volatile  a++

    // 总结： 加锁机制可以确保原子性和可见性 ，而volatile只是确保原子性



}
