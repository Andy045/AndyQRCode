# 条码和二维码扫描库

[使用说明](https://handy045.com/2018/05/31/Android/Handy/HandyQRCode/)

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
|handy_titlebar_mainTextColor|扫描界面标题栏文字颜色|#FFFFFFFF|
|handy_status_background|扫描界面状态栏背景颜色|#00000000|
|handy_titlebar_background|扫描界面标题栏背景颜色|#00000000|
|handy_titlebar_bottomLine_background|扫描界面标题栏底部分割线背景颜色|#FFFFFFFF||||
|||
|handy_viewfinder_mask_color|扫描界面，扫描框外的区域背景|#60000000|
|handy_scan_corner_color|扫描框四个角的背景色|#65E102|
|handy_scan_centerLine_color|扫描线颜色|#98C360|
|handy_scan_focusLine_color|扫描框四周边线颜色|#FFFFFF|
|handy_scan_hit_color|底部提示内容字体颜色|#FFFFFF|
|handy_scan_snackbar_background|扫描结果提示框背景颜色|#424242|
|handy_scan_snackbar_message_color|扫描结果提示框字体颜色|#FFFFFF|
|handy_scan_snackbar_action_color|扫描结果提示框按钮字体颜色|#7CB342|
|||
|handy_scan_corner_width|扫描框四个边角长度|16dp|
|handy_scan_corner_thick|扫描框四个边角厚度|4dp|
|handy_scan_focusLine_thick|扫描框四周边线宽度|1dp|
|handy_scan_hit_marginTop|底部提示文字离扫描框距离值|22dp|
|handy_scan_hit_size|底部提示文字字体大小|13sp|

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

### 一维码：商品【解析方式为Zxing时生效，Zbar默认支持条码和二维码】
``` java
public static boolean KEY_DECODE_1D_PRODUCT = true;
```

### 一维码：工业【解析方式为Zxing时生效，Zbar默认支持条码和二维码】
``` java
public static boolean KEY_DECODE_1D_INDUSTRIAL = true;
```

### 二维码【解析方式为Zxing时生效，Zbar默认支持条码和二维码】
``` java
public static boolean KEY_DECODE_QR = true;
```

### Data Matrix【解析方式为Zxing时生效】
``` java
public static boolean KEY_DECODE_DATA_MATRIX = false;
```

### Aztec【解析方式为Zxing时生效】
``` java
public static boolean KEY_DECODE_AZTEC = false;
```

### PDF417 (测试)【解析方式为Zxing时生效】
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

### 不持续对焦 (使用标准对焦模式)
``` java
public static boolean KEY_DISABLE_CONTINUOUS_FOCUS = true;
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
public static boolean KEY_DISABLE_BARCODE_SCENE_MODE = false;
```

### 使用全屏扫描 (默认识别扫描框内的图形)
``` java
public static boolean KEY_SCAN_FULLSCREEN = true;
```

### 扫描解析方式（zxing或zbar解析，优先使用效率最高的Zbar解码，无法解码时再使用兼容性更高的Zxing）
``` java
public static ScanType KEY_SCAN_TYPE = ScanType.All;
```

### 屏幕自动旋转
``` java
public static boolean KEY_AUTO_ORIENTATION = false;
```

### 屏幕竖屏旋转方向 (默认竖屏)
``` java
public static int KEY_SCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
```

### 通过底部弹出的SncakBar，手动确认扫描结果
``` java
public static boolean KEY_VERIFY_RESULT = true;
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

## 更新记录

[点击查看](https://github.com/Handy045/HandyQRCode/releases)

## 内置扫描界面使用

### 单个二维码或条码扫描

```` java
View.setOnClickListener(v -> {
    // 全局配置设置
    ScanConfig.KEY_DECODE_1D_INDUSTRIAL = false;
    ScanConfig.KEY_DECODE_1D_PRODUCT = false;
    // 调用二维码扫描功能模块
    new ScanLauncher().startSingle(ScanActivity.this, new ScanConfig.ScanResultListener() {
        @Override
        public void resultListener(String result) {
            // 二维码扫描结果返回
            Toast.makeText(activity, "扫描结果: \n" + result, Toast.LENGTH_SHORT).show();
        }
    });
});
````

# 库中含有的工具类及自定义控件

## 工具类功能描述

|类名|功能描述|其他
|:---|:---|:---|
|QRCodeUtils|二维码生成工具类|生成普通二维码和带Image标识的二维码

## 自定义控件描述

|类名|功能描述|其他
|:---|:---|---|
|HandyTitleBar|自定义标题栏控件|[查看地址](https://github.com/Handy045/HandyTitleBar)

# 相关技术分享

[使用ZXing扫描多个二维码，条形码](https://github.com/sunlightAndroid/ZXingMoreResult)
[Zxing二维码扫描(一)：初始项目搭建与精简](https://handy045.com/2018/05/22/Android/Zxing/zxing_1/)