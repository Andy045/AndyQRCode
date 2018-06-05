# 功能描述

基于Zxing 3.3.3版本源码精简与优化。移除了分享、WIFI、书签、URL识别等功能，保留了条码、二维码扫描功能。并增加了可自定义扫描界面和扫描配置。支持音量调整焦距、全屏扫描、扫描结果图片水印、压缩、绘制识别点等功能。

# 效果展示

![](https://github.com/Handy045/HandyQRCode/blob/master/document/UiDemo.png?raw=true)

# 自定义扫描界面及配置

## 自定义UI

请在项目中重写以下资源名的属性值。

### 修改扫描界面

|资源属性|属性描述|默认值
|:---|:---|:---
|handy_titlebar_background|扫描界面标题栏背景颜色|#00000000
|handy_titlebar_bottomLine_background|扫描界面标题栏底部分割线背景颜色|#FFFFFFFF
|handy_qrcode_points_color|二维码扫描时，识别点描点|#C0FFBD21
|handy_result_points_color|二维码扫描成功后，二维码Bitmap图片识别点描点|#C099CC00
|handy_viewfinder_mask_color|扫描界面，扫描框外的区域背景|#60000000
|handy_scan_corner_color|扫描框四个角的背景色|#65E102
|handy_scan_centerLine_color|扫描线颜色|#98C360
|handy_scan_focusLine_color|扫描框四周边线颜色|#FFFFFF
|handy_scan_hit_color|底部提示内容字体颜色|#FFFFFF
|handy_scan_snackbar_background|扫描结果提示框背景颜色|#424242
|handy_scan_snackbar_message_color|扫描结果提示框字体颜色|#FFFFFF
|handy_scan_snackbar_action_color|扫描结果提示框按钮字体颜色|#7CB342
|handy_scan_corner_width|扫描框四个边角长度|16dp
|handy_scan_corner_thick|扫描框四个边角厚度|4dp
|handy_scan_focusLine_thick|扫描框四周边线宽度|1dp
|handy_scan_hit_marginTop|底部提示文字离扫描框距离值|22dp
|handy_scan_hit_size|底部提示文字字体大小|13sp

### 修改界面文字内容

|资源属性|属性描述|默认值
|:---|:---|:---
|handy_scan_titlebar_connect|扫描界面标题栏名称|二维码扫描
|handy_scan_error_title|扫描失败提示框标题|启动失败
|handy_scan_error_dialog_btn|扫描失败提示框按钮|确定
|handy_scan_error_dialog_message|扫描失败提示框内容|很遗憾，Android相机出现问题。请检查此应用权限中是否允许使用相机并重启应用。
|handy_scan_hint_content|扫描界面提示内容|请将条码置于取景框内扫描

## 自定义扫描配置

相关配置属性是以静态变量的方式存放在ScanSingleConfig类中，可直接重写属性值自定义配置。

### 一维码：商品
``` java
public static boolean KEY_DECODE_1D_PRODUCT = false;
```
### 一维码：工业
``` java
public static boolean KEY_DECODE_1D_INDUSTRIAL = false;
```
### 二维码
``` java
public static boolean KEY_DECODE_QR = true;
```
### Data Matrix
``` java
public static boolean KEY_DECODE_DATA_MATRIX = false;
```
### Aztec
``` java
public static boolean KEY_DECODE_AZTEC = false;
```
### PDF417 (测试)
``` java
public static boolean KEY_DECODE_PDF417 = false;
```
### 播放提示音
``` java
public static boolean KEY_PLAY_BEEP = true;
```
### 振动
``` java
public static boolean KEY_VIBRATE = true;
```
### 闪光灯 (设置闪光灯模式)
``` java
public static boolean KEY_USE_LIGHT = false;
```
### 自动对焦
``` java
public static boolean KEY_AUTO_FOCUS = true;
```
### 反色
``` java
public static boolean KEY_INVERT_SCAN = false;
```
### 不持续对焦 (使用标准对焦模式)
``` java
public static boolean KEY_DISABLE_CONTINUOUS_FOCUS = false;
```
### 不曝光
``` java
public static boolean KEY_DISABLE_EXPOSURE = true;
```
### 不使用距离测量
``` java
public static boolean KEY_DISABLE_METERING = true;
```
### 不进行条形码场景匹配
``` java
public static boolean KEY_DISABLE_BARCODE_SCENE_MODE = true;
```
### 使用全屏扫描 (默认识别扫描框内的图形)
``` java
public static boolean KEY_SCAN_FULLSCREEN = true;
```
### 屏幕自动旋转
``` java
public static boolean KEY_AUTO_ORIENTATION = true;
```
### 屏幕竖屏旋转方向 (默认竖屏)
``` java
public static int KEY_SCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
```

# 使用方式

## Github地址

https://github.com/Handy045/HandyQRCode

## Gradle引用

1. 在根目录的build.gradle文件中，找到allProjects属性并增加maven仓库地址。

    ```
    allprojects {
    	repositories {
    		...
    		maven { url 'https://jitpack.io' }
    	}
    }
    ```

2. 在使用Module的build.gradle文件中，添加引用语句。

    ```
    dependencies {
        implementation 'com.github.Handy045:HandyQRCode:最新版本号'
    }
    ```

## 最新版本号

[![](https://jitpack.io/v/Handy045/HandyQRCode.svg)](https://jitpack.io/#Handy045/HandyQRCode)

## 内置扫描界面使用

### 单个二维码或条码扫描

```` java
new ScanLauncher().startSingle(MainActivity.this, new ScanConfig.ScanResultListener() {
    @Override
    public void resultListener(Result rawResult, Bundle bundle) {
        Bitmap barcode = null;
        float scaleFactor = 1.0f;
        if (bundle != null) {
            byte[] compressedBitmap = bundle.getByteArray(DecodeThread.BARCODE_BITMAP);
            if (compressedBitmap != null) {
                barcode = BitmapFactory.decodeByteArray(compressedBitmap, 0, compressedBitmap.length, null);
                // Mutable copy:
                barcode = barcode.copy(Bitmap.Config.ARGB_8888, true);
            }
            scaleFactor = bundle.getFloat(DecodeThread.BARCODE_SCALED_FACTOR);
        }
        if (barcode != null) {
            Bitmap bitmap;
            bitmap = BitmapUtils.drawResultPoints(MainActivity.this, barcode, scaleFactor, rawResult, true);
            bitmap = BitmapUtils.compressByScale(bitmap, 360, 640, true);
            bitmap = BitmapUtils.addTextWatermark(MainActivity.this, bitmap, "HandyQRCode\nhttps://www.handy045.com", 13, Color.BLUE, 4, 4, true);
            ((ImageView) findViewById(R.id.image)).setImageBitmap(bitmap);
        }
        ((TextView) findViewById(R.id.result)).setText("扫描结果: \n" + rawResult.getText());
    }
});
````

### 多个二维码同时扫描

```` java
new ScanLauncher().startMultiple(MainActivity.this, new ScanConfig.ScanResultsListener() {
    @Override
    public void resultListener(List<Result> rawResults, Bundle bundle) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < rawResults.size(); i++) {
            Result result = rawResults.get(i);
            str.append("第").append(i + 1).append("个: ").append(result.getText());
            if (i < rawResults.size() - 1) {
                str.append("\n");
            }
        }
        ((TextView) findViewById(R.id.result)).setText("扫描结果：\n" + str.toString());
    }
});
````

# 库中含有的工具类及自定义控件

## 工具类功能描述

|类名|功能描述|
|:---|:---|
|BitmapUtils|图片处理工具类|
|QRCodeUtils|二维码生成工具类|
|LogUtils|日志打印工具类|
|SnackBarUtils|底部卡点击提示工具类|

## 自定义控件描述

|类名|功能描述|
|:---|:---|
|MarqueeTextView|带跑马灯效果的TextView|
|TitleBar|标题栏|