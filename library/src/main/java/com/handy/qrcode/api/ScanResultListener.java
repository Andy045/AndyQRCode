package com.handy.qrcode.api;

import android.graphics.Bitmap;

import com.google.zxing.Result;

/**
 * 二维码扫描成功回调接口
 *
 * @author LiuJie https://www.Handy045.com
 * @description 回调接口
 * @date Created in 2018/5/29 下午4:23
 * @modified By LiuJie
 */
public interface ScanResultListener {
    void resultListener(Result rawResult, Bitmap barcode, float scaleFactor);
}
