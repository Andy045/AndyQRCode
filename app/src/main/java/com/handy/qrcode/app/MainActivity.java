package com.handy.qrcode.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.zxing_scan).setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ScanActivity.class)));

        findViewById(R.id.zxing_img).setOnClickListener(v -> {
        });

        findViewById(R.id.zxing_create).setOnClickListener(v -> startActivity(new Intent(MainActivity.this, CreateActivity.class)));
    }
}
