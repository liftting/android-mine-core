package com.xm.cygcore.javatest;

import java.util.Set;

/**
 * Created by wm on 15/6/12.
 */
public class ThOuter {

    // 这里就讲集合中得ThreadTest 发布出去了，因为发布了集合的引用
    public static Set<ThreadTest> knowObj;

    public Set<ThreadTest> getSecure() {
        return knowObj;
    }




}
