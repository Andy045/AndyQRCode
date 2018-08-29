package com.handy.qrcode.module;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;

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
    }

    /**
     * 启动扫描界面
     *
     * @param scanResultListener 扫描结果回调接口
     */
    public void startSingle(@NonNull Activity activity, @NonNull ScanConfig.ScanResultListener scanResultListener) {
        if (!checkDeniedPermissions(activity, true)) {
            ScanConfig.scanResultListener = null;
            ScanConfig.scanResultListener = scanResultListener;
            activity.startActivity(new Intent(activity, ScanSingleActivity.class));
        }
    }

    /**
     * 扫描权限集合 如果发现未启用权限，停止扫描并启动权限动态请求。
     *
     * @param activity  发起请求的Activity
     * @param isRequest 是否发起系统权限请求
     * @return 如果有未启用的权限返回true
     */
    public static boolean checkDeniedPermissions(Activity activity, boolean isRequest) {
        ArrayList<String> permissions = new ArrayList<String>() {{
            add(Manifest.permission.CAMERA);
            add(Manifest.permission.VIBRATE);
        }};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (int index = 0; index < permissions.size(); index++) {
                if (ContextCompat.checkSelfPermission(activity, permissions.get(index)) == PackageManager.PERMISSION_DENIED) {
                    if (isRequest) {
                        ActivityCompat.requestPermissions(activity, new String[]{permissions.get(index)}, index);
                    }
                    return true;
                }
            }
        }
        return false;
    }
}
