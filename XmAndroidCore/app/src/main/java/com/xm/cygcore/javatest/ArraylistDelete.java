package com.xm.cygcore.javatest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wm on 15/5/15.
 */
public class ArraylistDelete {

    public static void main(String[] args){
        List<String> a = new ArrayList<String>();
        a.add("aa");
        a.add("bb");
        a.add("cc");

        for(String t : a){
            if(t.equals("aa")){
                a.remove(t);
            }
        }
    }

}
