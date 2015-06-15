package com.xm.cygcore.javatest.lock;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by wm on 15/6/15.
 */
public class CLHLock implements Lock {

    // 原子变量指向队尾
    private AtomicReference<QNode> tail;


    // 两个指针，一个指向自己的Node,一个指向前一个Node
    ThreadLocal<QNode> myNode;
    ThreadLocal<QNode> myPreNode;

    public CLHLock() {
        tail = new AtomicReference<QNode>(new QNode());


        myNode = new ThreadLocal<QNode>() {
            protected QNode initialValue() {
                return new QNode();
            }
        };


        myPreNode = new ThreadLocal<QNode>() {
            protected QNode initialValue() {
                return null;
            }
        };
    }

    @Override
    public void lock() {
        QNode node = myNode.get();// 多个去获取node，获取的都是不同的对象实例
        System.out.println("lock get QNode is:" + node.toString());
        node.lock = true; // 修改自己线程的lock boolean

        // tail是原子的独立的实例，多个thread去get时，如果获取到了，则将自己thread之前设置的QNode，设置给tail

        // CAS原子操作，保证原子性
        QNode preNode = tail.getAndSet(node);

        System.out.println("");

        myPreNode.set(preNode);
        // volatile变量，能保证锁释放及时通知
        // 只对前一个节点的状态自旋，减少缓存一致性流量
        while (preNode.lock) {
            System.out.print(Thread.currentThread() + " is wait preNode");
        }
    }

    @Override
    public void unlock() {
        QNode node = myNode.get();
        node.lock = false;
        // 把myNode指向preNode，目的是保证同一个线程下次还能使用这个锁，因为myNode原来指向的节点有它的后一个节点的preNode引用
        // 防止这个线程下次lock时myNode.get获得原来的节点
        myNode.set(myPreNode.get());
    }

    public static class QNode {
        volatile boolean lock;
    }

}
