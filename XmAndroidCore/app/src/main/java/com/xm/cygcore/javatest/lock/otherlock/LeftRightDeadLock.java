package com.xm.cygcore.javatest.lock.otherlock;

/**
 * Created by wm on 15/6/17.
 */
public class LeftRightDeadLock {

    private Object left = new Object();
    private Object right = new Object();

    private void leftRight() {
        synchronized (left) {
            synchronized (right) {
                doSomething();
            }
        }
    }

    private void rightLeft() {
        synchronized (right) {
            synchronized (left) {
                doSomething();
            }
        }
    }

    private void doSomething() {

    }

}
