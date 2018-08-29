package com.handy.qrcode.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TitleBar titleBar = findViewById(R.id.titlebar);
        titleBar.setTitle("扫描库");
        titleBar.setTitleBarBackground(R.color.titlebar_background);
        titleBar.setImmersive(MainActivity.this, true);
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

        findViewById(R.id.zxing_scan).setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ScanActivity.class)));

        findViewById(R.id.zxing_img).setOnClickListener(v -> {
        });

        findViewById(R.id.zxing_create).setOnClickListener(v -> startActivity(new Intent(MainActivity.this, CreateActivity.class)));
    }
}
