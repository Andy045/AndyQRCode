package com.handy.qrcode.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.handy.qrcode.ScanActivity;
import com.handy.qrcode.utils.BitmapUtils;

public class MainActivity extends Activity {

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (data != null && data.getExtras() != null && data.getExtras().size() > 0) {
                Log.d("++++++++++++++++++", data.getStringExtra("Result"));
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.zxing).setOnClickListener(v -> {
            ((TextView) findViewById(R.id.result)).setText("");
            ((ImageView) findViewById(R.id.image)).setImageBitmap(null);

            ScanActivity.doIntent(MainActivity.this, new Bundle(), (rawResult, barcode, scaleFactor) -> {
                BitmapUtils.drawResultPoints(MainActivity.this, barcode, scaleFactor, rawResult);

                ((TextView) findViewById(R.id.result)).setText("扫描结果：" + rawResult.getText());
                ((ImageView) findViewById(R.id.image)).setImageBitmap(barcode);
            }, 1, false);
        });
    }
}
