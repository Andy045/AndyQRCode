package com.handy.qrcode.utils;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

/**
 * 日志工具类
 *
 * @author LiuJie https://www.Handy045.com
 * @description 是否在控制台打印日志
 * @date Created in 2018/5/30 上午10:45
 * @modified By LiuJie
 */
public class LogUtils {

    private static Application application;

    public static String TAG = "HandyQRCode";

    private LogUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static void e(String msg) {
        if (isAppDebug(application.getPackageName())) {
            Log.e(TAG, msg);
        }
    }

    public static void w(String msg) {
        if (isAppDebug(application.getPackageName())) {
            Log.w(TAG, msg);
        }
    }

    public static void w(Throwable msg) {
        if (isAppDebug(application.getPackageName())) {
            Log.w(TAG, msg);
        }
    }

    public static void w(String msg, Throwable t) {
        if (isAppDebug(application.getPackageName())) {
            Log.w(TAG, msg, t);
        }
    }

    public static void i(String msg) {
        if (isAppDebug(application.getPackageName())) {
            Log.i(TAG, msg);
        }
    }

    public static void d(String msg) {
        if (isAppDebug(application.getPackageName())) {
            Log.d(TAG, msg);
        }
    }

    public static void v(String msg) {
        if (isAppDebug(application.getPackageName())) {
            Log.v(TAG, msg);
        }
    }

    public static void init(Application mApplication) {
        application = mApplication;
    }

    public static boolean isAppDebug(final String packageName) {
        if (isSpace(packageName)) {
            return false;
        }
        try {
            PackageManager pm = application.getPackageManager();
            ApplicationInfo ai = pm.getApplicationInfo(packageName, 0);
            return ai != null && (ai.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean isSpace(final String s) {
        if (s == null) {
            return true;
        }
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}