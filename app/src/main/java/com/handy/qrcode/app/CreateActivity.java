package com.handy.qrcode.app;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.handy.qrcode.utils.QRCodeUtils;

/**
 * 类名
 *
 * @author LiuJie https://www.Handy045.com
 * @description 类功能内容
 * @date Created in 2018/6/5 下午4:51
 * @modified By LiuJie
 */
public class CreateActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        EditText edt = findViewById(R.id.edt);
        Button create = findViewById(R.id.create);
        ImageView qrcode_1 = findViewById(R.id.qrcode_1);
        ImageView qrcode_2 = findViewById(R.id.qrcode_2);

        create.setOnClickListener(v -> {
            qrcode_1.setImageBitmap(null);
            qrcode_2.setImageBitmap(null);

            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm == null) {
                return;
            }
            imm.hideSoftInputFromWindow(findViewById(R.id.parent_layout).getWindowToken(), 0);

            String str = edt.getText().toString();
            if ("".equals(str)) {
                Toast.makeText(CreateActivity.this, "请输入二维码内容", Toast.LENGTH_SHORT).show();
            } else {
                Bitmap qrcode1 = QRCodeUtils.createCode(CreateActivity.this, str, qrcode_1.getHeight());

                Bitmap img = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                Bitmap qrcode2 = QRCodeUtils.createCode(CreateActivity.this, str, img, qrcode_1.getHeight());

                qrcode_1.setImageBitmap(qrcode1);
                qrcode_2.setImageBitmap(qrcode2);
            }
        });
    }
}
