package com.xm.cygcore.javatest.lock;

/**
 * Created by wm on 15/6/18.
 */
public class StripLockMap {

    private Node datas[];
    private Object lockArray[];

    private static final int N_LOCKS = 16;//分段数目

    public static class Node {
        public String key;
        public String value;
        public Node preNode;
        public Node nextNode;

        public Node next() {
            return nextNode;
        }
    }

    public StripLockMap(int dataNum) {
        buildNode(dataNum);

        lockArray = new Object[N_LOCKS];

        for (int i = 0; i < N_LOCKS; i++) {

            lockArray[i] = new Object();
        }

    }

    private void buildNode(int dataNum) {
        //构建node


    }

    private int hash(Object key) {
        return Math.abs(key.hashCode() % datas.length);
    }

    public Object get(Object key) {
        int hashCode = hash(key);
        synchronized (lockArray[hashCode % N_LOCKS]) { //分段lock
            for (Node n = datas[hashCode]; n != null; n.next()) {
                if (n.key.equals(key))
                    return n.value;
            }
        }

        return null;
    }






}
