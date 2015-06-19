
package com.xm.zxing.processor;

import android.annotation.SuppressLint;
import android.content.Context;

import org.json.JSONObject;


/**
 * 条码扫描处理器
 */
@SuppressLint("NewApi")
public class BarcodeScannerProcessor {

    /**
     * 无法处理的内容
     */
    public static final int RESULT_TYPE_UNHANDLED = 0;

    /**
     * 商品编码
     */
    public static final int RESULT_TYPE_PRODUCT_ID = 1;

    /**
     * 可识别url
     */
    public static final int RESULT_TYPE_RECOGNIZED_URL = 2;

    private static final String PROC_CLASS_NAME = "com.geili.koudai.Processor";

    private static final String PROC_METHOD_NAME = "getScanInfo";

    private static final String PARSE_METHOD_NAME = "parseShortUrl";

    private static Class<?> processor;

    public static void unloadProcessor() {
        processor = null;
    }

    /**
     * 获取处理器处理的结果
     *
     * @param scanType 编码类型
     * @param cont     扫描到的内容
     * @return
     */
    public static JSONObject getScanInfo(Context context, int scanType, String cont) {


        return null;
    }

    /**
     * 扫描结果处理器中的参数常量值集合，此类与处理器中的ScanParamConstants应保持一致
     *
     * @author weijianglong
     */
    public class ScanParamConstants {
        /**
         * 扫描类型：未知类型
         */
        public static final int SCAN_TYPE_UNKNOWN = 0;

        /**
         * 扫描类型：一维码
         */
        public static final int SCAN_TYPE_ONE_D_CODE = 1;

        /**
         * 扫描类型：二维码
         */
        public static final int SCAN_TYPE_TWO_D_CODE = 2;

        /**
         * 无法处理的内容，例如纯文本等
         */
        public static final int RESULT_TYPE_UNHANDLED = 0;

        /**
         * 商品编码
         */
        public static final int RESULT_TYPE_PRODUCT_ID = 1;

        /**
         * 可识别url
         */
        public static final int RESULT_TYPE_RECOGNIZED_URL = 2;

        /**
         * 不可识别url，口袋内部无法处理的链接，如http://www.baidu.com
         */
        public static final int RESULT_TYPE_UNRECOGNIZED_URL = 3;

        /**
         * 淘宝短链类型url
         */
        public static final int RESULT_TYPE_SHORT_URL = 4;

    }
}
