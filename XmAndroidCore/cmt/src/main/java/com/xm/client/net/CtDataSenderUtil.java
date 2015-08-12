package com.xm.client.net;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Date;

/**
 * Created by wm on 15/7/9.
 */
public class CtDataSenderUtil {

    public static void writeStr2Stream(String str, BufferedOutputStream writer) throws IOException {
        try {
            System.out.println("cliet:" + getNowTime() + "begin to write data is-" + str);
            // add buffered writer
            // write
            writer.write(str.getBytes());

            writer.flush();
        } catch (IOException ex) {
            //System.out.println(SocketUtil.getNowTime() + ex);
            throw ex;
        }
    }

    public static String readStrFromStream(BufferedInputStream in) throws IOException {
        StringBuffer result = new StringBuffer("");

        // build buffered reader
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        // read 1024 bytes per time
        char[] chars = new char[2048];
        int len;

        try {
            while ((len = reader.read(chars)) != -1) {
                // if the length of array is 1M
                if (2048 == len) {
                    //then append all chars of the array
                    result.append(chars);
                }
                // if the length of array is less then 1M
                else {
                    //then append the valid chars
                    for (int i = 0; i < len; i++) {
                        result.append(chars[i]);
                    }
                    break;
                }
            }

        } catch (IOException e) {
            System.out.println(e);
            throw e;
        }
        //System.out.println("end reading string from stream");
        return result.toString();
    }

    public static String getNowTime() {
        return new Date().toString();
    }

}
