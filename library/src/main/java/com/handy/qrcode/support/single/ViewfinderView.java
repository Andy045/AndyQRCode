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

package com.handy.qrcode.support.single;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.google.zxing.ResultPoint;
import com.handy.qrcode.R;
import com.handy.qrcode.support.single.camera.CameraManager;

import java.util.ArrayList;
import java.util.List;

/**
 * This view is overlaid on top of the camera preview. It adds the viewfinder rectangle and partial
 * transparency outside it, as well as the laser scanner animation and result points.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class ViewfinderView extends View {

    private static final int[] SCANNER_ALPHA = {0, 64, 128, 192, 255, 192, 128, 64};
    private static final long ANIMATION_DELAY = 100L;
    private static final int CURRENT_POINT_OPACITY = 0xA0;
    private static final int MAX_RESULT_POINTS = 20;
    private static final int POINT_SIZE = 6;
    private final Paint paint;
    private final int maskColor;
    private final int resultPointColor;
    private final int mFocusLineColor;
    private CameraManager cameraManager;
    private Bitmap resultBitmap;
    private int scannerAlpha;
    private List<ResultPoint> possibleResultPoints;

    private Paint mCornerPaint;
    private Paint mFocusFramePaint;
    private Paint mTipPaint;
    private Paint mLaserPaint;

    private int mCornerLength;
    private int mCornerThick;
    private int mTipPaddingTop;
    private int mFocusLineThick;

    public ViewfinderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        scannerAlpha = 0;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        possibleResultPoints = new ArrayList<>(5);

        mCornerPaint = new Paint();
        mFocusFramePaint = new Paint();
        mTipPaint = new Paint();
        mLaserPaint = new Paint();

        Resources resources = getResources();
        maskColor = resources.getColor(R.color.handy_viewfinder_mask_color);
        resultPointColor = resources.getColor(R.color.handy_qrcode_points_color);
        mFocusLineColor = resources.getColor(R.color.handy_scan_focusLine_color);

        mCornerPaint.setAntiAlias(true);
        mCornerPaint.setColor(resources.getColor(R.color.handy_scan_corner_color));

        mFocusFramePaint.setAntiAlias(true);
        mFocusFramePaint.setColor(mFocusLineColor);

        mTipPaint.setAntiAlias(true);
        mTipPaint.setTextSize(resources.getDimensionPixelSize(R.dimen.handy_scan_hit_size));
        mTipPaint.setColor(resources.getColor(R.color.handy_scan_hit_color));

        mLaserPaint.setAntiAlias(true);
        mLaserPaint.setColor(resources.getColor(R.color.handy_scan_centerLine_color));

        mCornerLength = resources.getDimensionPixelSize(R.dimen.handy_scan_corner_width);
        mCornerThick = resources.getDimensionPixelSize(R.dimen.handy_scan_corner_thick);
        mTipPaddingTop = resources.getDimensionPixelSize(R.dimen.handy_scan_hit_marginTop);
        mFocusLineThick = resources.getDimensionPixelSize(R.dimen.handy_scan_focusLine_thick);
    }

    public void setCameraManager(CameraManager cameraManager) {
        this.cameraManager = cameraManager;
    }

    @SuppressLint("DrawAllocation")
    @Override
    public void onDraw(Canvas canvas) {
        if (cameraManager == null) {
            return; // not ready yet, early draw before done configuring
        }
        Rect frame = cameraManager.getFramingRect();
        Rect previewFrame = cameraManager.getFramingRectInPreview();
        if (frame == null || previewFrame == null) {
            return;
        }
        int width = canvas.getWidth();
        int height = canvas.getHeight();

        // Draw the exterior (i.e. outside the framing rect) darkened
        paint.setColor(maskColor);
        canvas.drawRect(0, 0, width, frame.top, paint);
        canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);
        canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1, paint);
        canvas.drawRect(0, frame.bottom + 1, width, height, paint);
        //绘制矩形框的四个角
        drawCorner(canvas, frame);
        //绘制聚焦框
        drawFocusRect(canvas, frame);
        //绘制激光线
        drawLaser(canvas, frame);
        //绘制提示语
        drawTipText(canvas, getResources().getDisplayMetrics().widthPixels, frame.bottom + mTipPaddingTop);
        //实现动画效果
        postInvalidateDelayed(ANIMATION_DELAY, frame.left, frame.top, frame.right, frame.bottom);
    }

    public void drawViewfinder() {
        Bitmap resultBitmap = this.resultBitmap;
        this.resultBitmap = null;
        if (resultBitmap != null) {
            resultBitmap.recycle();
        }
        invalidate();
    }

    /**
     * Draw a bitmap with the result points highlighted instead of the live scanning display.
     *
     * @param barcode An image of the decoded barcode.
     */
    public void drawResultBitmap(Bitmap barcode) {
        resultBitmap = barcode;
        invalidate();
    }

    public void addPossibleResultPoint(ResultPoint point) {
        List<ResultPoint> points = possibleResultPoints;
        synchronized (points) {
            points.add(point);
            int size = points.size();
            if (size > MAX_RESULT_POINTS) {
                // trim it
                points.subList(0, size - MAX_RESULT_POINTS / 2).clear();
            }
        }
    }

    /**
     * 绘制矩形框的四个角
     *
     * @param canvas
     * @param rect
     */
    private void drawCorner(Canvas canvas, Rect rect) {
        if (rect == null) {
            return;
        }
        //绘制左上角
        canvas.drawRect(rect.left, rect.top, rect.left + mCornerLength, rect.top + mCornerThick, mCornerPaint);
        canvas.drawRect(rect.left, rect.top, rect.left + mCornerThick, rect.top + mCornerLength, mCornerPaint);

        //绘制左下角
        canvas.drawRect(rect.left, rect.bottom - mCornerThick, rect.left + mCornerLength, rect.bottom, mCornerPaint);
        canvas.drawRect(rect.left, rect.bottom - mCornerLength, rect.left + mCornerThick, rect.bottom, mCornerPaint);

        //绘制右上角
        canvas.drawRect(rect.right - mCornerLength, rect.top, rect.right, rect.top + mCornerThick, mCornerPaint);
        canvas.drawRect(rect.right - mCornerThick, rect.top, rect.right, rect.top + mCornerLength, mCornerPaint);

        //绘制右下角
        canvas.drawRect(rect.right - mCornerLength, rect.bottom - mCornerThick, rect.right, rect.bottom, mCornerPaint);
        canvas.drawRect(rect.right - mCornerThick, rect.bottom - mCornerLength, rect.right, rect.bottom, mCornerPaint);
    }

    /**
     * 绘制聚焦框
     */
    private void drawFocusRect(Canvas canvas, Rect rect) {
        canvas.drawRect(rect.left + mCornerLength, rect.top, rect.right - mCornerLength, rect.top + mFocusLineThick, mFocusFramePaint);
        canvas.drawRect(rect.right - mFocusLineThick, rect.top + mCornerLength, rect.right, rect.bottom - mCornerLength, mFocusFramePaint);
        canvas.drawRect(rect.left + mCornerLength, rect.bottom - mFocusLineThick, rect.right - mCornerLength, rect.bottom, mFocusFramePaint);
        canvas.drawRect(rect.left, rect.top + mCornerLength, rect.left + mFocusLineThick, rect.bottom - mCornerLength, mFocusFramePaint);
    }

    /**
     * 绘制激光线
     */
    private void drawLaser(Canvas canvas, Rect rect) {
        mLaserPaint.setAlpha(SCANNER_ALPHA[scannerAlpha]);
        scannerAlpha = (scannerAlpha + 1) % SCANNER_ALPHA.length;
        int middle = rect.height() / 2 + rect.top;
        canvas.drawRect(rect.left + 2, middle - 1, rect.right - 1, middle + 2, mLaserPaint);
    }

    /**
     * 绘制提示语
     */
    private void drawTipText(Canvas canvas, int w, int h) {
        String tip = getResources().getString(R.string.handy_scan_hint_content);
        float l = (w - tip.length() * mTipPaint.getTextSize()) / 2;
        canvas.drawText(tip, l, h, mTipPaint);
    }
}
