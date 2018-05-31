package com.handy.qrcode.module.single;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;

/**
 * 扫描功能创建者
 *
 * @author LiuJie https://www.Handy045.com
 * @description 初始化配置并启动扫描功能
 * @date Created in 2018/5/30 下午3:48
 * @modified By LiuJie
 */
public class ScanSingleBuild {

    private ScanSingleConfig.ScanResultListener scanResultListener = null;

    public ScanSingleBuild(ScanSingleConfig.ScanResultListener scanResultListener) {
        this.scanResultListener = scanResultListener;
    }

    /**
     * 启动扫描界面
     *
     * @param activity 当前Activity
     */
    public void start(@NonNull Activity activity) throws Exception {
        start(activity, false);
    }

    /**
     * 启动扫描界面
     *
     * @param activity 当前Activity
     * @param isFinish 是否结束当前Activity
     */
    public void start(@NonNull Activity activity, boolean isFinish) throws Exception {
        ScanSingleConfig.scanResultListener = null;
        if (this.scanResultListener != null) {
            ScanSingleConfig.scanResultListener = scanResultListener;
        } else {
            throw new Exception("NullPointerException: ScanSingleConfig.scanResultListener is NULL! you mast setScanResultListener in this Class.");
        }
        activity.startActivity(new Intent(activity, ScanSingleActivity.class));
        if (isFinish) {
            activity.finish();
        }
    }
}
