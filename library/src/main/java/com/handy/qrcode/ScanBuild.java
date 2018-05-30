package com.handy.qrcode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.handy.qrcode.support.ScanResultListener;

/**
 * 扫描功能创建者
 *
 * @author LiuJie https://www.Handy045.com
 * @description 初始化配置并启动扫描功能
 * @date Created in 2018/5/30 下午3:48
 * @modified By LiuJie
 */
public class ScanBuild {
    private int requestCode = 100;
    private boolean isFinish = false;
    private boolean isRecord = false;
    private ScanResultListener scanResultListener = null;

    /**
     * 带回调跳转请求编码（默认100）
     */
    public ScanBuild setRequestCode(int requestCode) {
        this.requestCode = requestCode;
        return this;
    }

    /**
     * 是否关闭当前Activity
     */
    public ScanBuild setFinish(boolean finish) {
        isFinish = finish;
        return this;
    }

    /**
     * 是否将扫描结果记录到缓存里
     */
    public ScanBuild setRecord(boolean record) {
        isRecord = record;
        return this;
    }

    /**
     * 扫描完成的回调接口
     */
    public ScanBuild setScanResultListener(ScanResultListener scanResultListener) {
        this.scanResultListener = scanResultListener;
        return this;
    }

    /**
     * 启动扫描界面
     */
    public void start(@NonNull Activity activity) {
        Intent intent = new Intent(activity, ScanSingleActivity.class);

        Bundle bundle = new Bundle();
        bundle.putBoolean("isRecord", isRecord);
        intent.putExtras(bundle);

        ScanSingleActivity.scanResultListener = scanResultListener;

        activity.startActivityForResult(intent, requestCode);
        if (isFinish) {
            activity.finish();
        }
    }
}
