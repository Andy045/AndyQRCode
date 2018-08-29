package com.handy.qrcode.app;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.handy.qrcode.module.ScanConfig;
import com.handy.qrcode.module.ScanLauncher;
import com.handy.titlebar.HandyTitleBar;

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

        HandyTitleBar titleBar = findViewById(R.id.titlebar);
        titleBar.setMainText("扫描条码和二维码");
        titleBar.showCustomStatusBar(ScanActivity.this);
        titleBar.addLeftAction(new HandyTitleBar.BaseAction(titleBar) {
            {
                setImageSrc(com.handy.qrcode.R.drawable.handy_qrcode_select_titlebar_back);
            }

            @Override
            public void onClick() {
                finish();
            }
        });

        findViewById(R.id.zxing_single).setOnClickListener(v -> {
            ((TextView) findViewById(R.id.result)).setText("");

            ScanConfig.KEY_DECODE_1D_INDUSTRIAL = true;
            ScanConfig.KEY_DECODE_1D_PRODUCT = true;

            new ScanLauncher().startSingle(ScanActivity.this, result -> {
                ((TextView) findViewById(R.id.result)).setText("扫描结果: \n" + result);
            });
        });
    }
}

