package com.handy.qrcode.utils;

import android.util.Log;

/**
 * 类名
 *
 * @author LiuJie https://www.Handy045.com
 * @description 类功能内容
 * @date Created in 2018/5/30 上午10:45
 * @modified By LiuJie
 */
public class LogUtils {
    public static int LOG_LEVEL = 0;

    private static final int ERROR = 1;
    private static final int WARN = 2;
    private static final int INFO = 3;
    private static final int DEBUG = 4;
    private static final int VERBOS = 5;


    public static void e(String tag, String msg) {
        if (LOG_LEVEL > ERROR) {
            Log.e(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (LOG_LEVEL > WARN) {
            Log.w(tag, msg);
        }
    }

    public static void w(String tag, Throwable msg) {
        if (LOG_LEVEL > WARN) {
            Log.w(tag, msg);
        }
    }

    public static void w(String tag, String msg, Throwable t) {
        if (LOG_LEVEL > WARN) {
            Log.w(tag, msg, t);
        }
    }

    public static void i(String tag, String msg) {
        if (LOG_LEVEL > INFO) {
            Log.i(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (LOG_LEVEL > DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void v(String tag, String msg) {
        if (LOG_LEVEL > VERBOS) {
            Log.v(tag, msg);
        }
    }
}