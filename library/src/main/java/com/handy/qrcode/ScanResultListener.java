package com.handy.qrcode;

import android.graphics.Bitmap;

import com.google.zxing.Result;

/**
 * 类名
 *
 * @author LiuJie https://www.Handy045.com
 * @description 类功能内容
 * @date Created in 2018/5/29 下午4:23
 * @modified By LiuJie
 */
public interface ScanResultListener {
    void resultListener(Result rawResult, Bitmap barcode, float scaleFactor);
}
