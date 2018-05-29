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

package com.handy.qrcode;

import android.content.pm.ActivityInfo;

/**
 * The main settings activity.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 * @author Sean Owen
 */
public final class Preferences {
    /**
     * 一维码：商品
     */
    public static boolean KEY_DECODE_1D_PRODUCT = false;
    /**
     * 一维码：工业
     */
    public static boolean KEY_DECODE_1D_INDUSTRIAL = false;
    /**
     * 二维码
     */
    public static boolean KEY_DECODE_QR = true;
    /**
     * Data Matrix
     */
    public static boolean KEY_DECODE_DATA_MATRIX = false;
    /**
     * Aztec
     */
    public static boolean KEY_DECODE_AZTEC = false;
    /**
     * PDF417 (测试)
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
     * 反色
     */
    public static boolean KEY_INVERT_SCAN = false;
    /**
     * 不持续对焦 (使用标准对焦模式)
     */
    public static boolean KEY_DISABLE_CONTINUOUS_FOCUS = false;
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
    public static boolean KEY_DISABLE_BARCODE_SCENE_MODE = true;
    /**
     * 使用全屏扫描 (默认识别扫描框内的图形)
     */
    public static boolean KEY_SCAN_FULLSCREEN = false;
    /**
     * 屏幕自动旋转
     */
    public static boolean KEY_AUTO_ORIENTATION = false;
    /**
     * 屏幕竖屏旋转方向 (默认竖屏)
     */
    public static int KEY_SCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
}
