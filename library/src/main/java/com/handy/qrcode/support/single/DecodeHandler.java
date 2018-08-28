/*
 * Copyright (C) 2010 ZXing authors
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

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.DecodeHandlerJni;
import com.google.zxing.common.HybridBinarizer;
import com.handy.qrcode.R;
import com.handy.qrcode.module.ScanConfig;
import com.handy.qrcode.module.single.ScanSingleActivity;

import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;

import java.io.ByteArrayOutputStream;
import java.util.Map;

final class DecodeHandler extends Handler {

    private final ScanSingleActivity activity;
    private final MultiFormatReader multiFormatReader;
    private boolean running = true;

    DecodeHandler(ScanSingleActivity activity, Map<DecodeHintType, Object> hints) {
        multiFormatReader = new MultiFormatReader();
        multiFormatReader.setHints(hints);
        this.activity = activity;
    }

    private static void bundleThumbnail(PlanarYUVLuminanceSource source, Bundle bundle) {
        int[] pixels = source.renderThumbnail();
        int width = source.getThumbnailWidth();
        int height = source.getThumbnailHeight();
        Bitmap bitmap = Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.ARGB_8888);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
        bundle.putByteArray(DecodeThread.BARCODE_BITMAP, out.toByteArray());
        bundle.putFloat(DecodeThread.BARCODE_SCALED_FACTOR, (float) width / source.getWidth());
    }

    @Override
    public void handleMessage(Message message) {
        if (message == null || !running) {
            return;
        }
        if (message.what == R.id.handy_qrcode_decode) {
            decode((byte[]) message.obj, message.arg1, message.arg2);
        } else if (message.what == R.id.handy_qrcode_quit) {
            running = false;
            Looper.myLooper().quit();
        }
    }

    /**
     * Decode the data within the viewfinder rectangle, and time how long it took. For efficiency,
     * reuse the same reader objects from one decode to the next.
     *
     * @param data   The YUV preview frame.
     * @param width  The width of the preview frame.
     * @param height The height of the preview frame.
     */
    private void decode(byte[] data, int width, int height) {
        if (width < height) {
            data = DecodeHandlerJni.dataHandler(data, data.length, height, width);
        }

        Bundle bundle = null;
        Result rawResult = null;

        if (ScanConfig.KEY_SCAN_TYPE == ScanConfig.ScanType.Zxing) {
            PlanarYUVLuminanceSource source = activity.getCameraManager().buildLuminanceSource(data, width, height);
            if (source != null) {
                BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
                try {
                    rawResult = multiFormatReader.decodeWithState(bitmap);
                } catch (ReaderException re) {
                    re.printStackTrace();
                } finally {
                    multiFormatReader.reset();
                }
                bundle = new Bundle();
                bundleThumbnail(source, bundle);
            }

        } else if (ScanConfig.KEY_SCAN_TYPE == ScanConfig.ScanType.Zbar) {
            Image barcode = new Image(width, height, "Y800");
            barcode.setData(data);
            if (!ScanConfig.KEY_SCAN_FULLSCREEN) {
                // 如果非全屏扫描，则根据扫描框大小进行裁剪
                Rect rect = activity.getCameraManager().getFramingRectInPreview();
                if (rect != null) {
                    barcode.setCrop(rect.left, rect.top, rect.width(), rect.height());
                }
            }
            String resultQRcode = "";
            ImageScanner mImageScanner = new ImageScanner();
            if (mImageScanner.scanImage(barcode) != 0) {
                for (Symbol symbol : mImageScanner.getResults()) {
                    resultQRcode = symbol.getData();
                }
            }
            if (!TextUtils.isEmpty(resultQRcode)) {
                rawResult = new Result(resultQRcode, data, new ResultPoint[0], BarcodeFormat.QR_CODE);
            }
        }

        Handler handler = activity.getHandler();
        if (rawResult != null) {
            if (handler != null) {
                Message message = Message.obtain(handler, R.id.handy_qrcode_decode_succeeded, rawResult);
                message.setData(bundle);
                message.sendToTarget();
            }
        } else {
            if (handler != null) {
                Message message = Message.obtain(handler, R.id.handy_qrcode_decode_failed);
                message.sendToTarget();
            }
        }
    }
}
