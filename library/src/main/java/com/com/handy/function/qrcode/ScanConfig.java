/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.com.handy.function.qrcode;

import android.content.pm.ActivityInfo;

/**
 * The main settings activity.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 * @author Sean Owen
 */
public final class ScanConfig {

    public enum ScanType {
        /**
         * Zxing解析
         */
        Zxing,
        /**
         * Zbar解析
         */
        Zbar,
        /**
         * 优先使用Zbar，未识别成功再使用Zxing
         */
        All
    }

    /**
     * 一维码：商品【解析方式为Zxing时生效】
     */
    public static boolean KEY_DECODE_1D_PRODUCT = true;
    /**
     * 一维码：工业【解析方式为Zxing时生效】
     */
    public static boolean KEY_DECODE_1D_INDUSTRIAL = true;
    /**
     * 二维码【解析方式为Zxing时生效】
     */
    public static boolean KEY_DECODE_QR = true;
    /**
     * Data Matrix【解析方式为Zxing时生效】
     */
    public static boolean KEY_DECODE_DATA_MATRIX = false;
    /**
     * Aztec【解析方式为Zxing时生效】
     */
    public static boolean KEY_DECODE_AZTEC = false;
    /**
     * PDF417 (测试)【解析方式为Zxing时生效】
     */
    public static boolean KEY_DECODE_PDF417 = false;
    /**
     * 播放提示音
     */
    public static boolean KEY_PLAY_BEEP = true;
    /**
     * 振动
     */
    public static boolean KEY_VIBRATE = true;
    /**
     * 闪光灯 (设置闪光灯模式)
     */
    public static boolean KEY_USE_LIGHT = false;
    /**
     * 自动对焦
     */
    public static boolean KEY_AUTO_FOCUS = true;
    /**
     * 不持续对焦 (使用标准对焦模式)
     */
    public static boolean KEY_DISABLE_CONTINUOUS_FOCUS = true;
    /**
     * 不曝光
     */
    public static boolean KEY_DISABLE_EXPOSURE = true;
    /**
     * 不使用距离测量
     */
    public static boolean KEY_DISABLE_METERING = true;
    /**
     * 不进行条形码场景匹配
     */
    public static boolean KEY_DISABLE_BARCODE_SCENE_MODE = false;
    /**
     * 使用全屏扫描 (默认识别扫描框内的图形)
     */
    public static boolean KEY_SCAN_FULLSCREEN = true;
    /**
     * 扫描解析方式（zxing或zbar解析）
     */
    public static ScanType KEY_SCAN_TYPE = ScanType.All;
    /**
     * 屏幕自动旋转
     */
    public static boolean KEY_AUTO_ORIENTATION = false;
    /**
     * 屏幕竖屏旋转方向 (默认竖屏)
     */
    public static int KEY_SCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
    /**
     * 通过底部弹出的SncakBar，手动确认扫描结果
     */
    public static boolean KEY_VERIFY_RESULT = true;

    /**
     * 扫描成功回调自定义接口（单个条码或二维码扫描）
     */
    public static ScanResultListener scanResultListener = null;

    /**
     * 扫描结果回调接口（单个条码或二维码扫描）
     */
    public interface ScanResultListener {
        void resultListener(String result);
    }
}
