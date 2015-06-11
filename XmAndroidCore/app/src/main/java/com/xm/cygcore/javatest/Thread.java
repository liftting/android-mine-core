package com.xm.cygcore.javatest;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by wm on 15/6/11.
 */
public class Thread {



    private int count;

    //这个是不安全，一个++操作，有三个步骤 读取--修改 --写入
    public void test(){
        count++;
    }

    // 延迟初始化操作，
    public static Thread instance;

    public static Thread getInstance(){
        if(instance == null){
            instance = new Thread(); // 如果这里的构造费时操作，那么当多个thread同时get判断时，就会出现判断错误
        }
        return instance;
    }

    /// ----  对于同步，一些 function

    // 1， 原子操作
    // add new thread safe  使得上面的++操作是原子性的
    private AtomicInteger atomicInteger;
    public void testAtmoic(){
        atomicInteger.incrementAndGet();
    }


    // 2，同步块 内置锁


    // 3，加锁 lock

}
