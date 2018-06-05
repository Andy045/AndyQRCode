package com.handy.qrcode.app;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.Result;
import com.handy.qrcode.module.ScanBuild;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.zxing).setOnClickListener(v -> {
            ((TextView) findViewById(R.id.result)).setText("");
            ((ImageView) findViewById(R.id.image)).setImageBitmap(null);

            new ScanBuild().startMultiple(MainActivity.this, (rawResults, bundle) -> {
                StringBuilder str = new StringBuilder();
                for (int i = 0; i < rawResults.size(); i++) {
                    Result result = rawResults.get(i);
                    str.append("第").append(i + 1).append("个: ").append(result.getText());
                    if (i < rawResults.size() - 1) {
                        str.append("\n");
                    }
                }

                ((TextView) findViewById(R.id.result)).setText("扫描结果：" + str.toString());
            });
        });
    }
}
