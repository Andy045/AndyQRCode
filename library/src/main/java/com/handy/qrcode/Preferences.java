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
    public static final boolean KEY_DECODE_1D_PRODUCT = true;
    /**
     * 一维码：工业
     */
    public static final boolean KEY_DECODE_1D_INDUSTRIAL = true;
    /**
     * 二维码
     */
    public static final boolean KEY_DECODE_QR = true;
    /**
     * Data Matrix
     */
    public static final boolean KEY_DECODE_DATA_MATRIX = true;
    /**
     * Aztec
     */
    public static final boolean KEY_DECODE_AZTEC = true;
    /**
     * PDF417 (测试)
     */
    public static final boolean KEY_DECODE_PDF417 = true;
    /**
     * 自定义搜索网址
     */
    public static final String KEY_CUSTOM_PRODUCT_SEARCH = "preferences_custom_product_search";
    /**
     * 播放提示音
     */
    public static final boolean KEY_PLAY_BEEP = true;
    /**
     * 振动
     */
    public static final boolean KEY_VIBRATE = true;
    /**
     * 复制到剪贴板
     */
    public static final boolean KEY_COPY_TO_CLIPBOARD = true;
    /**
     * 设置闪光灯模式
     */
    public static final boolean KEY_FRONT_LIGHT_MODE = true;
    /**
     * 批量扫描模式
     */
    public static final boolean KEY_BULK_MODE = true;
    /**
     * 保存重复记录
     */
    public static final boolean KEY_REMEMBER_DUPLICATES = true;
    /**
     * 存入历史记录
     */
    public static final boolean KEY_ENABLE_HISTORY = true;
    /**
     * 检索更多信息
     */
    public static final boolean KEY_SUPPLEMENTAL = true;
    /**
     * 自动对焦
     */
    public static final boolean KEY_AUTO_FOCUS = true;
    /**
     * 反色
     */
    public static final boolean KEY_INVERT_SCAN = true;
    /**
     * 搜索引擎国别
     */
    public static final boolean KEY_SEARCH_COUNTRY = true;
    /**
     * 不自动旋转
     */
    public static final boolean KEY_DISABLE_AUTO_ORIENTATION = true;
    /**
     * 不持续对焦
     */
    public static final boolean KEY_DISABLE_CONTINUOUS_FOCUS = true;
    /**
     * 不曝光
     */
    public static final boolean KEY_DISABLE_EXPOSURE = true;
    /**
     * 不使用距离测量
     */
    public static final boolean KEY_DISABLE_METERING = true;
    /**
     * 不进行条形码场景匹配
     */
    public static final boolean KEY_DISABLE_BARCODE_SCENE_MODE = true;
    /**
     * 自动打开网页
     */
    public static final boolean KEY_AUTO_OPEN_WEB = true;
}
