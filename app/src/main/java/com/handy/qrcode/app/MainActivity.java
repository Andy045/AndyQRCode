package com.handy.qrcode.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.handy.base.app.BaseActivity;
import com.handy.qrcode.CaptureActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.zxing).setOnClickListener(v -> {
            CaptureActivity.doIntent(activity, new Bundle(), (rawResult, barcode, scaleFactor) -> {
                Log.d("==============", rawResult.getText());
                Toast.makeText(activity, rawResult.getText(), Toast.LENGTH_SHORT).show();
            }, 1, false);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (data != null && data.getExtras() != null && data.getExtras().size() > 0) {
                Log.d("++++++++++++++++++", data.getStringExtra("Result"));
            }
        }
    }
}
