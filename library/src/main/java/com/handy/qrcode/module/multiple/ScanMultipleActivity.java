/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.handy.qrcode.module.multiple;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.OrientationEventListener;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.zxing.Result;
import com.handy.qrcode.R;
import com.handy.qrcode.module.ScanConfig;
import com.handy.qrcode.support.multiple.BeepManager;
import com.handy.qrcode.support.multiple.FinishListener;
import com.handy.qrcode.support.multiple.InactivityTimer;
import com.handy.qrcode.support.multiple.ScanActivityHandler;
import com.handy.qrcode.support.multiple.ViewfinderView;
import com.handy.qrcode.support.multiple.camera.CameraManager;
import com.handy.qrcode.utils.LogUtils;
import com.handy.qrcode.utils.SnackBarUtils;
import com.handy.qrcode.widget.TitleBar;

import java.io.IOException;
import java.util.List;

/**
 * This activity opens the camera and does the actual scanning on a background thread. It draws a
 * viewfinder to help the user place the barcode correctly, shows feedback as the image processing
 * is happening, and then overlays the results when a scan is successful.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 * @author Sean Owen
 */
public final class ScanMultipleActivity extends Activity implements SurfaceHolder.Callback {

    private static final String TAG = ScanMultipleActivity.class.getSimpleName();
    private TitleBar titleBar;
    private SurfaceView surfaceView;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private BeepManager beepManager;
    private CameraManager cameraManager;
    private ScanActivityHandler handler;
    private InactivityTimer inactivityTimer;
    private MyOrientationDetector myOrientationDetector;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.handy_activity_scan_multiple);

        surfaceView = findViewById(R.id.preview_view);
        viewfinderView = findViewById(R.id.viewfinder_view);
        titleBar = findViewById(R.id.common_titlebar);
        if (titleBar != null) {
            if (ScanConfig.KEY_SCREEN_ORIENTATION == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                titleBar.setTitle(getResources().getString(R.string.handy_scan_titlebar_connect));
                titleBar.setTitleBackground(getResources().getColor(R.color.handy_titlebar_background));
                titleBar.setBottomLineHeight(1);
            } else if (ScanConfig.KEY_SCREEN_ORIENTATION == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                titleBar.setTitle("");
                titleBar.setBackgroundColor(Color.TRANSPARENT);
                titleBar.setBottomLineHeight(0);
            }

            titleBar.setImmersive(ScanMultipleActivity.this, true);
            titleBar.setBottomLineBackground(R.color.handy_titlebar_bottomLine_background);
            titleBar.addLeftAction(new TitleBar.Action() {
                @Override
                public void onClick() {
                    finish();
                }

                @Override
                public int setDrawable() {
                    return R.drawable.handy_qrcode_select_titlebar_back;
                }
            });
            titleBar.addRightAction(new TitleBar.Action() {
                @Override
                public void onClick() {
                    ScanConfig.KEY_USE_LIGHT = !ScanConfig.KEY_USE_LIGHT;
                    cameraManager.setTorch(ScanConfig.KEY_USE_LIGHT);

                    titleBar.removeAllRightActions();
                    titleBar.addRightAction(this);
                }

                @Override
                public int setDrawable() {
                    return ScanConfig.KEY_USE_LIGHT ? R.drawable.handy_qrcode_icon_light_c : R.drawable.handy_qrcode_icon_light_n;
                }
            });
        }

        hasSurface = false;
        beepManager = new BeepManager(this);
        inactivityTimer = new InactivityTimer(this);
        myOrientationDetector = new MyOrientationDetector(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler = null;

        cameraManager = new CameraManager(getApplication());
        viewfinderView.setCameraManager(cameraManager);

        setRequestedOrientation(ScanConfig.KEY_SCREEN_ORIENTATION);
        if (ScanConfig.KEY_AUTO_ORIENTATION) {
            //启用监听
            myOrientationDetector.enable();
        }

        beepManager.updatePrefs();
        inactivityTimer.onResume();

        SurfaceView surfaceView = findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
        }
    }

    @Override
    protected void onPause() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        inactivityTimer.onPause();
        beepManager.close();
        cameraManager.closeDriver();
        myOrientationDetector.disable();
        if (!hasSurface) {
            SurfaceHolder surfaceHolder = surfaceView.getHolder();
            surfaceHolder.removeCallback(this);
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                cameraManager.zoomOut();
                return true;
            case KeyEvent.KEYCODE_VOLUME_UP:
                cameraManager.zoomIn();
                return true;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {
            LogUtils.w(TAG, "initCamera() while already open -- late SurfaceView callback?");
            return;
        }
        try {
            cameraManager.openDriver(surfaceHolder);
            // Creating the handler starts the preview, which can also throw a RuntimeException.
            if (handler == null) {
                handler = new ScanActivityHandler(this, null, null, "utf-8", cameraManager);
            }
        } catch (IOException ioe) {
            LogUtils.w(TAG, ioe);
            displayFrameworkBugMessageAndExit();
        } catch (RuntimeException e) {
            // Barcode Scanner has seen crashes in the wild of this variety:
            // java.?lang.?RuntimeException: Fail to connect to camera service
            LogUtils.w(TAG, "Unexpected error initializing camera", e);
            displayFrameworkBugMessageAndExit();
        }
    }

    /**
     * A valid barcode has been found, so give an indication of success and show the results.
     *
     * @param rawResults The contents of the barcodes.
     * @param bundle     result data
     */
    public void handleDecode(List<Result> rawResults, Bundle bundle) {
        inactivityTimer.onActivity();
        beepManager.playBeepSoundAndVibrate();

        SnackBarUtils snackBarUtils = SnackBarUtils.with(findViewById(R.id.parent_layout));
        View view = LayoutInflater.from(ScanMultipleActivity.this).inflate(R.layout.handy_view_scan_multiple_snackbar, null);
        TextView message = view.findViewById(R.id.snackbar_message);
        Button again = view.findViewById(R.id.snackbar_again);
        Button commit = view.findViewById(R.id.snackbar_commit);

        message.setText("共识别到" + rawResults.size() + "个二维码");
        again.setOnClickListener(v -> {
            SnackBarUtils.dismiss();
            new CountDownTimer(1000, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    if (handler != null) {
                        handler.restartPreviewAndDecode(false);
                    }
                }
            }.start();
        });
        commit.setOnClickListener(v -> {
            SnackBarUtils.dismiss();
            if (ScanConfig.scanResultsListener != null) {
                ScanConfig.scanResultsListener.resultListener(rawResults, bundle);
                ScanConfig.scanResultsListener = null;
            }
            finish();
        });
        snackBarUtils.setDuration(SnackBarUtils.LENGTH_INDEFINITE);
        snackBarUtils.show();
        SnackBarUtils.addView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (holder == null) {
            LogUtils.e(TAG, "*** WARNING *** surfaceCreated() gave us a null surface!");
        }
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // do nothing
    }

    private void displayFrameworkBugMessageAndExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.handy_scan_error_title));
        builder.setMessage(getString(R.string.handy_scan_error_dialog_message));
        builder.setPositiveButton(R.string.handy_scan_error_dialog_btn, new FinishListener(this));
        builder.setOnCancelListener(new FinishListener(this));
        builder.show();
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();
    }

    private class MyOrientationDetector extends OrientationEventListener {


        MyOrientationDetector(Context context) {
            super(context);
        }

        @Override
        public void onOrientationChanged(int orientation) {
            LogUtils.d(TAG, "orientation:" + orientation);
            if (orientation < 45 || orientation > 315) {
                orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
            } else if (orientation > 225 && orientation < 315) {
                orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
            } else {
                orientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;
            }

            if ((orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT && ScanConfig.KEY_SCREEN_ORIENTATION == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) || (orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE && ScanConfig.KEY_SCREEN_ORIENTATION == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)) {
                LogUtils.i(TAG, "orientation:" + orientation);
                ScanConfig.KEY_SCREEN_ORIENTATION = orientation;
                Intent intent = getIntent();
                finish();
                startActivity(intent);
                LogUtils.i(TAG, "SUCCESS");
            }
        }
    }
}
