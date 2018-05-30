package com.handy.qrcode.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.handy.qrcode.R;

/**
 * 图片工具类
 *
 * @author LiuJie https://www.Handy045.com
 * @description 二维码图片绘制标识点颜色
 * @date Created in 2018/5/30 下午2:38
 * @modified By LiuJie
 */
public class BitmapUtils {

    /**
     * 按目标宽高所计算的宽高比压缩图片
     *
     * @param bitmap       原图片
     * @param targetWidth  目标宽度（建议值：360）
     * @param targetHeight 目标高度（建议值：640）
     * @param recycle      是否回收
     * @return 压缩后的图片
     */
    public static Bitmap compressByScale(@NonNull Bitmap bitmap, @NonNull int targetWidth, @NonNull int targetHeight, boolean recycle) {
        if (isEmptyBitmap(bitmap)) {
            return null;
        }
        //压缩图片
        int scale;
        if (bitmap.getWidth() < bitmap.getHeight()) {
            if (bitmap.getWidth() <= targetWidth || bitmap.getHeight() <= targetHeight) {
                scale = 1;
            } else {
                int widthScale = bitmap.getWidth() / targetWidth;
                int heightScale = bitmap.getHeight() / targetHeight;
                scale = widthScale < heightScale ? widthScale : heightScale;
            }
        } else {
            if (bitmap.getWidth() <= targetHeight || bitmap.getHeight() <= targetWidth) {
                scale = 1;
            } else {
                int widthScale = bitmap.getWidth() / targetHeight;
                int heightScale = bitmap.getHeight() / targetWidth;
                scale = widthScale < heightScale ? widthScale : heightScale;
            }
        }
        // 如果缩放倍数大于1则对Bitmap实例进行压缩，如果小于1则无需压缩
        if (scale > 1) {
            int imgWidth = bitmap.getWidth() / (scale + 1);
            int imgHeight = bitmap.getHeight() / (scale + 1);

            Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, imgWidth, imgHeight, true);
            if (recycle && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
            return newBitmap;
        }
        return null;
    }

    /**
     * 添加文字水印，支持换行
     *
     * @param src      源图片
     * @param content  水印文本
     * @param textSize 水印字体大小（sp）
     * @param color    水印字体颜色
     * @param x        起始坐标x
     * @param y        起始坐标y
     * @param recycle  是否回收
     * @return 带有文字水印的图片
     */
    public static Bitmap addTextWatermark(Context context, Bitmap src, String content, float textSize, int color, float x, float y, boolean recycle) {
        if (isEmptyBitmap(src) || content == null) {
            return null;
        }
        Bitmap ret = src.copy(src.getConfig(), true);
        TextPaint paint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        Canvas canvas = new Canvas(ret);
        paint.setColor(color);
        paint.setTextSize(sp2px(context, textSize));
        Rect bounds = new Rect();
        paint.getTextBounds(content, 0, content.length(), bounds);
        StaticLayout layout = new StaticLayout(content, paint, (int) (ret.getWidth() - x), Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
        canvas.translate(x, y);
        layout.draw(canvas);
        if (recycle && !src.isRecycled()) {
            src.recycle();
        }
        return ret;
    }

    /**
     * 扫描结果的二维码图片中，给二维码的识别点绘制颜色
     */
    public static void drawResultPoints(Context context, Bitmap barcode, float scaleFactor, Result rawResult) {
        ResultPoint[] points = rawResult.getResultPoints();
        if (points != null && points.length > 0) {
            Canvas canvas = new Canvas(barcode);
            Paint paint = new Paint();
            paint.setColor(context.getResources().getColor(R.color.handy_result_points_color));
            if (points.length == 2) {
                paint.setStrokeWidth(4.0f);
                drawLine(canvas, paint, points[0], points[1], scaleFactor);
            } else if (points.length == 4 && (rawResult.getBarcodeFormat() == BarcodeFormat.UPC_A || rawResult.getBarcodeFormat() == BarcodeFormat.EAN_13)) {
                drawLine(canvas, paint, points[0], points[1], scaleFactor);
                drawLine(canvas, paint, points[2], points[3], scaleFactor);
            } else {
                paint.setStrokeWidth(10.0f);
                for (ResultPoint point : points) {
                    if (point != null) {
                        canvas.drawPoint(scaleFactor * point.getX(), scaleFactor * point.getY(), paint);
                    }
                }
            }
        }
    }

    private static void drawLine(Canvas canvas, Paint paint, ResultPoint a, ResultPoint b, float scaleFactor) {
        if (a != null && b != null) {
            canvas.drawLine(scaleFactor * a.getX(), scaleFactor * a.getY(), scaleFactor * b.getX(), scaleFactor * b.getY(), paint);
        }
    }

    /**
     * 判断bitmap对象是否为空
     *
     * @param src 源图片
     * @return {@code true}: 是<br>{@code false}: 否
     */
    private static boolean isEmptyBitmap(Bitmap src) {
        return src == null || src.getWidth() == 0 || src.getHeight() == 0;
    }

    /**
     * sp 转 px
     *
     * @param spValue sp 值
     * @return px 值
     */
    private static float sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return spValue * fontScale + 0.5f;
    }
}
