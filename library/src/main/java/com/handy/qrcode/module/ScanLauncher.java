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
public class ScanLauncher {

    public ScanLauncher() {
        ScanConfig.scanResultListener = null;
        ScanConfig.scanResultsListener = null;
    }

    /**
     * 启动扫描界面
     *
     * @param scanResultListener 扫描结果回调接口
     */
    public void startSingle(@NonNull Activity activity, @NonNull ScanConfig.ScanResultListener scanResultListener) {
        ScanConfig.scanResultListener = null;
        ScanConfig.scanResultListener = scanResultListener;
        activity.startActivity(new Intent(activity, ScanSingleActivity.class));
    }

    /**
     * 启动扫描界面
     *
     * @param scanResultsListener 扫描结果回调接口
     */
    public void startMultiple(@NonNull Activity activity, @NonNull ScanConfig.ScanResultsListener scanResultsListener) {
        ScanConfig.scanResultsListener = null;
        ScanConfig.scanResultsListener = scanResultsListener;
        activity.startActivity(new Intent(activity, ScanMultipleActivity.class));
    }
}
