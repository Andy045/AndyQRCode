package com.handy.qrcode.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.Hashtable;

/**
 * 类名
 *
 * @author LiuJie https://www.Handy045.com
 * @description 类功能内容
 * @date Created in 2018/6/5 下午4:37
 * @modified By LiuJie
 */
public class QRCodeUtils {

    /**
     * 生成二维码
     *
     * @param content    二维码内容
     * @param qrcodeSize 二维码图片大小（单位：px）
     * @return 生成的二维码图片
     */
    public static Bitmap createCode(Context context, String content, int qrcodeSize) {
        try {
            if (qrcodeSize == 0) {
                qrcodeSize = context.getResources().getDisplayMetrics().widthPixels * 6 / 10;
            }
            //生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
            BitMatrix matrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, qrcodeSize, qrcodeSize);

            int width = matrix.getWidth();
            int height = matrix.getHeight();
            //二维矩阵转为一维像素数组,也就是一直横着排了
            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (matrix.get(x, y)) {
                        pixels[y * width + x] = 0xff000000;
                    } else {
                        pixels[y * width + x] = 0xffffffff;
                    }
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            //通过像素数组生成bitmap
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 生成中间带图片的二维码
     *
     * @param content    二维码内容
     * @param logo       二维码中间的图片
     * @param qrcodeSize 二维码图片大小/PX
     * @return 生成的二维码图片
     */
    public static Bitmap createCode(Context context, String content, Bitmap logo, int qrcodeSize) {
        try {
            if (qrcodeSize == 0) {
                qrcodeSize = context.getResources().getDisplayMetrics().widthPixels * 6 / 10;
            }
            Matrix m = new Matrix();
            int imageHalfwidth = qrcodeSize / 9;
            float sx = (float) 2 * imageHalfwidth / logo.getWidth();
            float sy = (float) 2 * imageHalfwidth / logo.getHeight();
            // 设置缩放信息
            m.setScale(sx, sy);
            // 将logo图片按martix设置的信息缩放
            logo = Bitmap.createBitmap(logo, 0, 0, logo.getWidth(), logo.getHeight(), m, false);
            MultiFormatWriter writer = new MultiFormatWriter();
            Hashtable<EncodeHintType, Object> hst = new Hashtable();
            // 设置字符编码
            hst.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            // 设置二维码容错率
            hst.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            // 生成二维码矩阵信息
            BitMatrix matrix = writer.encode(content, BarcodeFormat.QR_CODE, qrcodeSize, qrcodeSize, hst);
            // 矩阵高度
            int width = matrix.getWidth();
            // 矩阵宽度
            int height = matrix.getHeight();
            int halfW = width / 2;
            int halfH = height / 2;
            // 定义数组长度为矩阵高度*矩阵宽度，用于记录矩阵中像素信息
            int[] pixels = new int[width * height];
            // 从行开始迭代矩阵
            for (int y = 0; y < height; y++) {
                // 迭代列
                for (int x = 0; x < width; x++) {
                    // 该位置用于存放图片信息
                    if (x > halfW - imageHalfwidth && x < halfW + imageHalfwidth && y > halfH - imageHalfwidth && y < halfH + imageHalfwidth) {
                        // 记录图片每个像素信息
                        pixels[y * width + x] = logo.getPixel(x - halfW + imageHalfwidth, y - halfH + imageHalfwidth);
                    } else {
                        // 如果有黑块点，记录信息
                        if (matrix.get(x, y)) {
                            // 记录黑块信息
                            pixels[y * width + x] = 0xff000000;
                        } else {
                            pixels[y * width + x] = 0xffffffff;
                        }
                    }
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            // 通过像素数组生成bitmap
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            logo.recycle();
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }
}
