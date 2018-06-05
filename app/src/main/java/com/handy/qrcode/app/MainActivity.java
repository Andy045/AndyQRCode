package com.handy.qrcode.app;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.Result;
import com.handy.qrcode.module.ScanLauncher;
import com.handy.qrcode.support.single.DecodeThread;
import com.handy.qrcode.utils.BitmapUtils;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.zxing_single).setOnClickListener(v -> {
            ((TextView) findViewById(R.id.result)).setText("");
            ((ImageView) findViewById(R.id.image)).setImageBitmap(null);

            new ScanLauncher().startSingle(MainActivity.this, (rawResult, bundle) -> {
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
            });
        });

        findViewById(R.id.zxing_multiple).setOnClickListener(v -> {
            ((TextView) findViewById(R.id.result)).setText("");
            ((ImageView) findViewById(R.id.image)).setImageBitmap(null);

            new ScanLauncher().startMultiple(MainActivity.this, (rawResults, bundle) -> {
                StringBuilder str = new StringBuilder();
                for (int i = 0; i < rawResults.size(); i++) {
                    Result result = rawResults.get(i);
                    str.append("第").append(i + 1).append("个: ").append(result.getText());
                    if (i < rawResults.size() - 1) {
                        str.append("\n");
                    }
                }

                ((TextView) findViewById(R.id.result)).setText("扫描结果：\n" + str.toString());
            });
        });
    }
}
