package com.handy.qrcode.module.single;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.handy.qrcode.api.ScanResultListener;

/**
 * 扫描功能创建者
 *
 * @author LiuJie https://www.Handy045.com
 * @description 初始化配置并启动扫描功能
 * @date Created in 2018/5/30 下午3:48
 * @modified By LiuJie
 */
public class ScanSingleBuild {

    public static final String KEY_SCAN_RESULT_STRING = "KEY_SCAN_RESULT_STRING";
    public static final String KEY_SCAN_BITMAP_BYTEARRAY = "KEY_SCAN_BITMAP_BYTEARRAY";

    private int requestCode = 100;
    private boolean isFinish = false;

    private ScanResultListener scanResultListener = null;

    public ScanSingleBuild() {
    }

    /**
     * 带回调跳转请求编码（默认：100）
     */
    public ScanSingleBuild setRequestCode(int requestCode) {
        this.requestCode = requestCode;
        return this;
    }

    /**
     * 是否关闭当前Activity（默认：false）
     */
    public ScanSingleBuild setFinish(boolean finish) {
        this.isFinish = finish;
        return this;
    }

    /**
     * 扫描完成的回调接口（默认：NULL）
     */
    public ScanSingleBuild setScanResultListener(ScanResultListener scanResultListener) {
        this.scanResultListener = scanResultListener;
        return this;
    }

    /**
     * 启动扫描界面
     */
    public void start(@NonNull Activity activity) {
        ScanSingleConfig.scanResultListener = null;
        if (this.scanResultListener != null) {
            ScanSingleConfig.scanResultListener = scanResultListener;
        }
        activity.startActivityForResult(new Intent(activity, ScanSingleActivity.class), requestCode);
        if (isFinish) {
            activity.finish();
        }
    }
}
