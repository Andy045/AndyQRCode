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

package com.handy.qrcode.support.multiple;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.client.android.DecodeHandlerJni;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.multi.qrcode.QRCodeMultiReader;
import com.handy.qrcode.R;
import com.handy.qrcode.module.multiple.ScanMultipleActivity;
import com.handy.qrcode.utils.LogUtils;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

final class DecodeHandler extends Handler {

    private final ScanMultipleActivity activity;
    private boolean running = true;

    private QRCodeMultiReader qrCodeMultiReader;
    private Map<DecodeHintType, Object> hints;

    DecodeHandler(ScanMultipleActivity activity, Map<DecodeHintType, Object> hints) {
        this.hints = hints;
        this.activity = activity;
        this.qrCodeMultiReader = new QRCodeMultiReader();
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

        Result[] rawResults = null;
        PlanarYUVLuminanceSource source = activity.getCameraManager().buildLuminanceSource(data, width, height);
        if (source != null) {
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            try {
                rawResults = qrCodeMultiReader.decodeMultiple(bitmap, hints);
            } catch (ReaderException re) {
                // continue
                re.printStackTrace();
            } finally {
                qrCodeMultiReader.reset();
            }
        }

        Handler handler = activity.getHandler();
        if (rawResults != null && rawResults.length > 0) {
            // Don't log the barcode contents for security.
            if (handler != null) {
                List<Result> results = new ArrayList<>(Arrays.asList(rawResults));
                LogUtils.d("results.size()=" + results.size());
                Message message = Message.obtain(handler, R.id.handy_qrcode_decode_succeeded, results);
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
