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

    private ScanResultListener scanResultListener = null;

    public ScanSingleBuild() {
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
     *
     * @param activity 当前Activity
     */
    public void start(@NonNull Activity activity) {
        start(activity, false);
    }

    /**
     * 启动扫描界面
     *
     * @param activity 当前Activity
     * @param isFinish 是否结束当前Activity
     */
    public void start(@NonNull Activity activity, boolean isFinish) {
        ScanSingleConfig.scanResultListener = null;
        if (this.scanResultListener != null) {
            ScanSingleConfig.scanResultListener = scanResultListener;
        }
        activity.startActivityForResult(new Intent(activity, ScanSingleActivity.class), ScanSingleConfig.CODE_SCAN_REQUEST);
        if (isFinish) {
            activity.finish();
        }
    }
}
