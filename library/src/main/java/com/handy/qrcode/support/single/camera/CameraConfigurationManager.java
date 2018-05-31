/*
 * Copyright (C) 2010 ZXing authors
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

package com.handy.qrcode.support.single.camera;

import android.content.Context;
import android.graphics.Point;
import android.hardware.Camera;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

import com.handy.qrcode.ScanSingleConfig;
import com.handy.qrcode.support.single.camera.open.CameraFacing;
import com.handy.qrcode.support.single.camera.open.OpenCamera;
import com.handy.qrcode.utils.LogUtils;

/**
 * A class which deals with reading, parsing, and setting the camera parameters which are used to
 * configure the camera hardware.
 */
@SuppressWarnings("deprecation") // camera APIs
final class CameraConfigurationManager {

    private static final String TAG = "CameraConfiguration";

    private final Context context;
    private int cwNeededRotation;
    private int cwRotationFromDisplayToCamera;
    private Point screenResolution;
    private Point cameraResolution;
    private Point bestPreviewSize;
    private Point previewSizeOnScreen;

    CameraConfigurationManager(Context context) {
        this.context = context;
    }

    /**
     * Reads, one time, values from the camera that are needed by the app.
     */
    void initFromCameraParameters(OpenCamera camera) {
        Camera.Parameters parameters = camera.getCamera().getParameters();
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();

        int displayRotation = display.getRotation();
        int cwRotationFromNaturalToDisplay;
        switch (displayRotation) {
            case Surface.ROTATION_0:
                cwRotationFromNaturalToDisplay = 0;
                break;
            case Surface.ROTATION_90:
                cwRotationFromNaturalToDisplay = 90;
                break;
            case Surface.ROTATION_180:
                cwRotationFromNaturalToDisplay = 180;
                break;
            case Surface.ROTATION_270:
                cwRotationFromNaturalToDisplay = 270;
                break;
            default:
                // Have seen this return incorrect values like -90
                if (displayRotation % 90 == 0) {
                    cwRotationFromNaturalToDisplay = (360 + displayRotation) % 360;
                } else {
                    throw new IllegalArgumentException("Bad rotation: " + displayRotation);
                }
        }
        LogUtils.i(TAG, "Display at: " + cwRotationFromNaturalToDisplay);

        int cwRotationFromNaturalToCamera = camera.getOrientation();
        LogUtils.i(TAG, "Camera at: " + cwRotationFromNaturalToCamera);

        // Still not 100% sure about this. But acts like we need to flip this:
        if (camera.getFacing() == CameraFacing.FRONT) {
            cwRotationFromNaturalToCamera = (360 - cwRotationFromNaturalToCamera) % 360;
            LogUtils.i(TAG, "Front camera overriden to: " + cwRotationFromNaturalToCamera);
        }

        cwRotationFromDisplayToCamera = (360 + cwRotationFromNaturalToCamera - cwRotationFromNaturalToDisplay) % 360;
        LogUtils.i(TAG, "Final display orientation: " + cwRotationFromDisplayToCamera);
        if (camera.getFacing() == CameraFacing.FRONT) {
            LogUtils.i(TAG, "Compensating rotation for front camera");
            cwNeededRotation = (360 - cwRotationFromDisplayToCamera) % 360;
        } else {
            cwNeededRotation = cwRotationFromDisplayToCamera;
        }
        LogUtils.i(TAG, "Clockwise rotation from display to camera: " + cwNeededRotation);

        Point theScreenResolution = new Point();
        display.getSize(theScreenResolution);
        screenResolution = theScreenResolution;
        LogUtils.i(TAG, "Screen resolution in current orientation: " + screenResolution);
        cameraResolution = CameraConfigurationUtils.findBestPreviewSizeValue(parameters, screenResolution);
        LogUtils.i(TAG, "Camera resolution: " + cameraResolution);
        bestPreviewSize = CameraConfigurationUtils.findBestPreviewSizeValue(parameters, screenResolution);
        LogUtils.i(TAG, "Best available preview size: " + bestPreviewSize);

        boolean isScreenPortrait = screenResolution.x < screenResolution.y;
        boolean isPreviewSizePortrait = bestPreviewSize.x < bestPreviewSize.y;

        if (isScreenPortrait == isPreviewSizePortrait) {
            previewSizeOnScreen = bestPreviewSize;
        } else {
            previewSizeOnScreen = new Point(bestPreviewSize.y, bestPreviewSize.x);
        }
        LogUtils.i(TAG, "Preview size on screen: " + previewSizeOnScreen);
    }

    void setDesiredCameraParameters(OpenCamera camera, boolean safeMode) {

        Camera theCamera = camera.getCamera();
        Camera.Parameters parameters = theCamera.getParameters();

        if (parameters == null) {
            LogUtils.w(TAG, "Device error: no camera parameters are available. Proceeding without configuration.");
            return;
        }

        LogUtils.i(TAG, "Initial camera parameters: " + parameters.flatten());

        if (safeMode) {
            LogUtils.w(TAG, "In camera config safe mode -- most settings will not be honored");
        }

        doSetTorch(parameters, ScanSingleConfig.KEY_USE_LIGHT, safeMode);

        CameraConfigurationUtils.setFocus(parameters, ScanSingleConfig.KEY_AUTO_FOCUS, ScanSingleConfig.KEY_DISABLE_CONTINUOUS_FOCUS, safeMode);

        if (!safeMode) {
            if (ScanSingleConfig.KEY_INVERT_SCAN) {
                CameraConfigurationUtils.setInvertColor(parameters);
            }

            if (!ScanSingleConfig.KEY_DISABLE_BARCODE_SCENE_MODE) {
                CameraConfigurationUtils.setBarcodeSceneMode(parameters);
            }

            if (!ScanSingleConfig.KEY_DISABLE_METERING) {
                CameraConfigurationUtils.setVideoStabilization(parameters);
                CameraConfigurationUtils.setFocusArea(parameters);
                CameraConfigurationUtils.setMetering(parameters);
            }

            //SetRecordingHint to true also a workaround for low framerate on Nexus 4
            //https://stackoverflow.com/questions/14131900/extreme-camera-lag-on-nexus-4
            parameters.setRecordingHint(true);

        }

        parameters.setPreviewSize(bestPreviewSize.x, bestPreviewSize.y);

        theCamera.setParameters(parameters);

        theCamera.setDisplayOrientation(cwRotationFromDisplayToCamera);

        Camera.Parameters afterParameters = theCamera.getParameters();
        Camera.Size afterSize = afterParameters.getPreviewSize();
        if (afterSize != null && (bestPreviewSize.x != afterSize.width || bestPreviewSize.y != afterSize.height)) {
            LogUtils.w(TAG, "Camera said it supported preview size " + bestPreviewSize.x + 'x' + bestPreviewSize.y + ", but after setting it, preview size is " + afterSize.width + 'x' + afterSize.height);
            bestPreviewSize.x = afterSize.width;
            bestPreviewSize.y = afterSize.height;
        }
    }

    Point getBestPreviewSize() {
        return bestPreviewSize;
    }

    Point getPreviewSizeOnScreen() {
        return previewSizeOnScreen;
    }

    Point getCameraResolution() {
        return cameraResolution;
    }

    Point getScreenResolution() {
        return screenResolution;
    }

    int getCWNeededRotation() {
        return cwNeededRotation;
    }

    boolean getTorchState(Camera camera) {
        if (camera != null) {
            Camera.Parameters parameters = camera.getParameters();
            if (parameters != null) {
                String flashMode = parameters.getFlashMode();
                return Camera.Parameters.FLASH_MODE_ON.equals(flashMode) || Camera.Parameters.FLASH_MODE_TORCH.equals(flashMode);
            }
        }
        return false;
    }

    void setTorch(Camera camera, boolean newSetting) {
        Camera.Parameters parameters = camera.getParameters();
        doSetTorch(parameters, newSetting, false);
        camera.setParameters(parameters);
    }

    private void doSetTorch(Camera.Parameters parameters, boolean newSetting, boolean safeMode) {
        CameraConfigurationUtils.setTorch(parameters, newSetting);
        if (!safeMode && !ScanSingleConfig.KEY_DISABLE_EXPOSURE) {
            CameraConfigurationUtils.setBestExposure(parameters, newSetting);
        }
    }
}
