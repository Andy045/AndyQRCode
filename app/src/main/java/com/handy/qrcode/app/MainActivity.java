package com.handy.qrcode.app;

import android.os.Bundle;

import com.handy.base.app.BaseActivity;
import com.handy.base.utils.IntentUtils;
import com.handy.qrcode.CaptureActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.zxing).setOnClickListener(v -> IntentUtils.openActivity(activity, CaptureActivity.class, false));
    }
}
