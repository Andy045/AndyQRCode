package com.handy.qrcode.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.handy.titlebar.HandyTitleBar;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HandyTitleBar titleBar = findViewById(R.id.titlebar);
        titleBar.setMainText("扫描库");
        titleBar.showCustomStatusBar(MainActivity.this);
        titleBar.addLeftAction(new HandyTitleBar.BaseAction(titleBar) {
            {
                setImageSrc(com.handy.qrcode.R.drawable.handy_qrcode_select_titlebar_back);
            }

            @Override
            public void onClick() {
                finish();
            }
        });

        findViewById(R.id.zxing_scan).setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ScanActivity.class)));

        findViewById(R.id.zxing_img).setOnClickListener(v -> {
        });

        findViewById(R.id.zxing_create).setOnClickListener(v -> startActivity(new Intent(MainActivity.this, CreateActivity.class)));
    }
}
