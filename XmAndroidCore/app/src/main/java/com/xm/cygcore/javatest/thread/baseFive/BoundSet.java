package com.xm.cygcore.javatest.thread.baseFive;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Semaphore;

/**
 * Created by wm on 15/6/16.
 * <p/>
 * 构建有界的阻塞容器
 * sem有边界，add会进行减 操作
 */
public class BoundSet<T> {

    private Set<T> set;
    private Semaphore sem;

    public BoundSet(int bound) {
        set = Collections.synchronizedSet(new HashSet<T>());
        sem = new Semaphore(bound);
    }

    public boolean add(T o) throws InterruptedException {
        sem.acquire();

        boolean wasAdded = false;
        try {
            wasAdded = set.add(o);
            return wasAdded;
        } finally {
            if (!wasAdded) {
                sem.release();
            }
        }
    }

    public boolean remove(Object o) {
        boolean wasRemoved = set.remove(o);
        if (wasRemoved) {
            sem.release();
        }
        return wasRemoved;
    }


}
