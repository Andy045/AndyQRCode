package com.handy.qrcode.module;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.handy.qrcode.module.multiple.ScanMultipleActivity;
import com.handy.qrcode.module.single.ScanSingleActivity;

/**
 * 扫描功能创建者
 *
 * @author LiuJie https://www.Handy045.com
 * @description 初始化配置并启动扫描功能
 * @date Created in 2018/5/30 下午3:48
 * @modified By LiuJie
 */
public class ScanBuild {

    private ScanConfig.ScanResultListener scanResultListener = null;

    public ScanBuild(ScanConfig.ScanResultListener scanResultListener) {
        this.scanResultListener = scanResultListener;
    }

    /**
     * 启动扫描界面
     *
     * @param activity 当前Activity
     */
    public void startSingle(@NonNull Activity activity) throws Exception {
        startSingle(activity, false);
    }

    /**
     * 启动扫描界面
     *
     * @param activity 当前Activity
     * @param isFinish 是否结束当前Activity
     */
    public void startSingle(@NonNull Activity activity, boolean isFinish) throws Exception {
        ScanConfig.scanResultListener = null;
        if (this.scanResultListener != null) {
            ScanConfig.scanResultListener = scanResultListener;
        } else {
            throw new Exception("NullPointerException: ScanConfig.scanResultListener is NULL! you mast setScanResultListener in this Class.");
        }
        activity.startActivity(new Intent(activity, ScanSingleActivity.class));
        if (isFinish) {
            activity.finish();
        }
    }

    /**
     * 启动扫描界面
     *
     * @param activity 当前Activity
     */
    public void startMultiple(@NonNull Activity activity) throws Exception {
        startMultiple(activity, false);
    }

    /**
     * 启动扫描界面
     *
     * @param activity 当前Activity
     * @param isFinish 是否结束当前Activity
     */
    public void startMultiple(@NonNull Activity activity, boolean isFinish) throws Exception {
        ScanConfig.scanResultListener = null;
        if (this.scanResultListener != null) {
            ScanConfig.scanResultListener = scanResultListener;
        } else {
            throw new Exception("NullPointerException: ScanConfig.scanResultListener is NULL! you mast setScanResultListener in this Class.");
        }
        activity.startActivity(new Intent(activity, ScanMultipleActivity.class));
        if (isFinish) {
            activity.finish();
        }
    }
}
