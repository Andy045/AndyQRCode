package com.handy.qrcode.app;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.Result;
import com.handy.qrcode.module.ScanConfig;
import com.handy.qrcode.module.ScanLauncher;
import com.handy.qrcode.support.single.DecodeThread;
import com.handy.qrcode.utils.BitmapUtils;
import com.handy.qrcode.widget.TitleBar;

/**
 * 类名
 *
 * @author LiuJie https://www.Handy045.com
 * @description 类功能内容
 * @date Created in 2018/6/5 下午4:58
 * @modified By LiuJie
 */
public class ScanActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        TitleBar titleBar = findViewById(R.id.titlebar);
        titleBar.setTitle("扫描条码和二维码");
        titleBar.setTitleBarBackground(R.color.titlebar_background);
        titleBar.setImmersive(ScanActivity.this, true);
        titleBar.addLeftAction(new TitleBar.Action() {
            @Override
            public void onClick() {
                finish();
            }

            @Override
            public int setDrawable() {
                return com.handy.qrcode.R.drawable.handy_qrcode_select_titlebar_back;
            }
        });

        findViewById(R.id.zxing_single).setOnClickListener(v -> {
            ((TextView) findViewById(R.id.result)).setText("");
            ((ImageView) findViewById(R.id.image)).setImageBitmap(null);

            ScanConfig.KEY_DECODE_1D_INDUSTRIAL = true;
            ScanConfig.KEY_DECODE_1D_PRODUCT = true;
            ScanConfig.KEY_DISABLE_BARCODE_SCENE_MODE = false;

            new ScanLauncher().startSingle(ScanActivity.this, (rawResult, bundle) -> {
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
                    bitmap = BitmapUtils.drawResultPoints(ScanActivity.this, barcode, scaleFactor, rawResult, true);
                    bitmap = BitmapUtils.compressByScale(bitmap, 360, 640, true);
                    bitmap = BitmapUtils.addTextWatermark(ScanActivity.this, bitmap, "HandyQRCode\nhttps://www.handy045.com", 13, Color.BLUE, 4, 4, true);
                    ((ImageView) findViewById(R.id.image)).setImageBitmap(bitmap);
                }
                ((TextView) findViewById(R.id.result)).setText("扫描结果: \n" + rawResult.getText());
            });
        });

        findViewById(R.id.zxing_multiple).setOnClickListener(v -> {

            ScanConfig.KEY_DECODE_1D_INDUSTRIAL = false;
            ScanConfig.KEY_DECODE_1D_PRODUCT = false;
            ScanConfig.KEY_DISABLE_BARCODE_SCENE_MODE = true;

            ((TextView) findViewById(R.id.result)).setText("");
            ((ImageView) findViewById(R.id.image)).setImageBitmap(null);

            new ScanLauncher().startMultiple(ScanActivity.this, (rawResults, bundle) -> {
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

