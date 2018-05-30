package com.handy.qrcode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

    private int requestCode = 100;
    private boolean isFinish = false;

    private boolean isRecord = false;
    public static final String KEY_SCAN_RESULT = "KEY_SCAN_RESULT";
    private String recordKey = "HandyQRCode";

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
     * 是否将扫描结果记录到缓存里（默认：false）
     */
    public ScanSingleBuild setRecord(boolean isRecord) {
        this.isRecord = isRecord;
        return this;
    }

    /**
     * 如果需要记录扫描结果，则需要传入记录值得关键字（默认：HandyQRCode）
     */
    public ScanSingleBuild setRecordKey(String recordKey) {
        this.recordKey = recordKey;
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
        Intent intent = new Intent(activity, ScanSingleActivity.class);

        Bundle bundle = new Bundle();
        bundle.putBoolean("isRecord", isRecord);
        bundle.putString("recordKey", recordKey);
        intent.putExtras(bundle);

        ScanSingleActivity.scanResultListener = scanResultListener;

        activity.startActivityForResult(intent, requestCode);
        if (isFinish) {
            activity.finish();
        }
    }
}
