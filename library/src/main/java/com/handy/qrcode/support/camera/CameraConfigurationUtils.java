/*
 * Copyright (C) 2014 ZXing authors
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

package com.handy.qrcode.support.camera;

import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Build;
import android.util.Log;

import com.handy.qrcode.utils.LogUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Utility methods for configuring the Android camera.
 *
 * @author Sean Owen
 */
@SuppressWarnings("deprecation") // camera APIs
public final class CameraConfigurationUtils {

    private static final Pattern SEMICOLON = Pattern.compile(";");

    /**
     * normal screen
     */
    private static final int MIN_PREVIEW_PIXELS = 480 * 320;
    private static final float MAX_EXPOSURE_COMPENSATION = 1.5f;
    private static final float MIN_EXPOSURE_COMPENSATION = 0.0f;
    private static final double MAX_ASPECT_DISTORTION = 0.15;
    private static final int MIN_FPS = 10;
    private static final int MAX_FPS = 20;
    private static final int AREA_PER_1000 = 400;

    private CameraConfigurationUtils() {
    }

    public static void setFocus(Camera.Parameters parameters, boolean autoFocus, boolean disableContinuous, boolean safeMode) {
        List<String> supportedFocusModes = parameters.getSupportedFocusModes();
        String focusMode = null;
        if (autoFocus) {
            if (safeMode || disableContinuous) {
                focusMode = findSettableValue("focus mode", supportedFocusModes, Camera.Parameters.FOCUS_MODE_AUTO);
            } else {
                focusMode = findSettableValue("focus mode", supportedFocusModes, Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE, Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO, Camera.Parameters.FOCUS_MODE_AUTO);
            }
        }
        // Maybe selected auto-focus but not available, so fall through here:
        if (!safeMode && focusMode == null) {
            focusMode = findSettableValue("focus mode", supportedFocusModes, Camera.Parameters.FOCUS_MODE_MACRO, Camera.Parameters.FOCUS_MODE_EDOF);
        }
        if (focusMode != null) {
            if (focusMode.equals(parameters.getFocusMode())) {
                LogUtils.i("Focus mode already set to " + focusMode);
            } else {
                parameters.setFocusMode(focusMode);
            }
        }
    }

    public static void setTorch(Camera.Parameters parameters, boolean on) {
        List<String> supportedFlashModes = parameters.getSupportedFlashModes();
        String flashMode;
        if (on) {
            flashMode = findSettableValue("flash mode", supportedFlashModes, Camera.Parameters.FLASH_MODE_TORCH, Camera.Parameters.FLASH_MODE_ON);
        } else {
            flashMode = findSettableValue("flash mode", supportedFlashModes, Camera.Parameters.FLASH_MODE_OFF);
        }
        if (flashMode != null) {
            if (flashMode.equals(parameters.getFlashMode())) {
                LogUtils.i("Flash mode already set to " + flashMode);
            } else {
                LogUtils.i("Setting flash mode to " + flashMode);
                parameters.setFlashMode(flashMode);
            }
        }
    }

    public static void setBestExposure(Camera.Parameters parameters, boolean lightOn) {
        int minExposure = parameters.getMinExposureCompensation();
        int maxExposure = parameters.getMaxExposureCompensation();
        float step = parameters.getExposureCompensationStep();
        if ((minExposure != 0 || maxExposure != 0) && step > 0.0f) {
            // Set low when light is on
            float targetCompensation = lightOn ? MIN_EXPOSURE_COMPENSATION : MAX_EXPOSURE_COMPENSATION;
            int compensationSteps = Math.round(targetCompensation / step);
            float actualCompensation = step * compensationSteps;
            // Clamp value:
            compensationSteps = Math.max(Math.min(compensationSteps, maxExposure), minExposure);
            if (parameters.getExposureCompensation() == compensationSteps) {
                LogUtils.i("Exposure compensation already set to " + compensationSteps + " / " + actualCompensation);
            } else {
                LogUtils.i("Setting exposure compensation to " + compensationSteps + " / " + actualCompensation);
                parameters.setExposureCompensation(compensationSteps);
            }
        } else {
            LogUtils.i("Camera does not support exposure compensation");
        }
    }

    public static void setBestPreviewFPS(Camera.Parameters parameters) {
        setBestPreviewFPS(parameters, MIN_FPS, MAX_FPS);
    }

    public static void setBestPreviewFPS(Camera.Parameters parameters, int minFPS, int maxFPS) {
        List<int[]> supportedPreviewFpsRanges = parameters.getSupportedPreviewFpsRange();
        LogUtils.i("Supported FPS ranges: " + toString(supportedPreviewFpsRanges));
        if (supportedPreviewFpsRanges != null && !supportedPreviewFpsRanges.isEmpty()) {
            int[] suitableFPSRange = null;
            for (int[] fpsRange : supportedPreviewFpsRanges) {
                int thisMin = fpsRange[Camera.Parameters.PREVIEW_FPS_MIN_INDEX];
                int thisMax = fpsRange[Camera.Parameters.PREVIEW_FPS_MAX_INDEX];
                if (thisMin >= minFPS * 1000 && thisMax <= maxFPS * 1000) {
                    suitableFPSRange = fpsRange;
                    break;
                }
            }
            if (suitableFPSRange == null) {
                LogUtils.i("No suitable FPS range?");
            } else {
                int[] currentFpsRange = new int[2];
                parameters.getPreviewFpsRange(currentFpsRange);
                if (Arrays.equals(currentFpsRange, suitableFPSRange)) {
                    LogUtils.i("FPS range already set to " + Arrays.toString(suitableFPSRange));
                } else {
                    LogUtils.i("Setting FPS range to " + Arrays.toString(suitableFPSRange));
                    parameters.setPreviewFpsRange(suitableFPSRange[Camera.Parameters.PREVIEW_FPS_MIN_INDEX], suitableFPSRange[Camera.Parameters.PREVIEW_FPS_MAX_INDEX]);
                }
            }
        }
    }

    public static void setFocusArea(Camera.Parameters parameters) {
        if (parameters.getMaxNumFocusAreas() > 0) {
            String str = parameters.get("focus-areas").replace(" ", "");
            parameters.set("focus-areas", str);
            LogUtils.i("Old focus areas: " + toString(parameters.getFocusAreas()));
            List<Camera.Area> middleArea = buildMiddleArea(AREA_PER_1000);
            LogUtils.i("Setting focus area to : " + toString(middleArea));
            parameters.setFocusAreas(middleArea);
        } else {
            LogUtils.i("Device does not support focus areas");
        }
    }

    public static void setMetering(Camera.Parameters parameters) {
        if (parameters.getMaxNumMeteringAreas() > 0) {
            String str = parameters.get("metering-areas").replace(" ", "");
            parameters.set("metering-areas", str);
            LogUtils.i("Old metering areas: " + parameters.getMeteringAreas());
            List<Camera.Area> middleArea = buildMiddleArea(AREA_PER_1000);
            LogUtils.i("Setting metering area to : " + toString(middleArea));
            parameters.setMeteringAreas(middleArea);
        } else {
            LogUtils.i("Device does not support metering areas");
        }
    }

    private static List<Camera.Area> buildMiddleArea(int areaPer1000) {
        return Collections.singletonList(new Camera.Area(new Rect(-areaPer1000, -areaPer1000, areaPer1000, areaPer1000), 1));
    }

    public static void setVideoStabilization(Camera.Parameters parameters) {
        if (parameters.isVideoStabilizationSupported()) {
            if (parameters.getVideoStabilization()) {
                LogUtils.i("Video stabilization already enabled");
            } else {
                LogUtils.i("Enabling video stabilization...");
                parameters.setVideoStabilization(true);
            }
        } else {
            LogUtils.i("This device does not support video stabilization");
        }
    }

    public static void setBarcodeSceneMode(Camera.Parameters parameters) {
        if (Camera.Parameters.SCENE_MODE_BARCODE.equals(parameters.getSceneMode())) {
            LogUtils.i("Barcode scene mode already set");
            return;
        }
        String sceneMode = findSettableValue("scene mode", parameters.getSupportedSceneModes(), Camera.Parameters.SCENE_MODE_BARCODE);
        if (sceneMode != null) {
            parameters.setSceneMode(sceneMode);
        }
    }

    public static void setZoom(Camera.Parameters parameters, double targetZoomRatio) {
        if (parameters.isZoomSupported()) {
            Integer zoom = indexOfClosestZoom(parameters, targetZoomRatio);
            if (zoom == null) {
                return;
            }
            if (parameters.getZoom() == zoom) {
                LogUtils.i("Zoom is already set to " + zoom);
            } else {
                LogUtils.i("Setting zoom to " + zoom);
                parameters.setZoom(zoom);
            }
        } else {
            LogUtils.i("Zoom is not supported");
        }
    }

    private static Integer indexOfClosestZoom(Camera.Parameters parameters, double targetZoomRatio) {
        List<Integer> ratios = parameters.getZoomRatios();
        LogUtils.i("Zoom ratios: " + ratios);
        int maxZoom = parameters.getMaxZoom();
        if (ratios == null || ratios.isEmpty() || ratios.size() != maxZoom + 1) {
            LogUtils.w("Invalid zoom ratios!");
            return null;
        }
        double target100 = 100.0 * targetZoomRatio;
        double smallestDiff = Double.POSITIVE_INFINITY;
        int closestIndex = 0;
        for (int i = 0; i < ratios.size(); i++) {
            double diff = Math.abs(ratios.get(i) - target100);
            if (diff < smallestDiff) {
                smallestDiff = diff;
                closestIndex = i;
            }
        }
        LogUtils.i("Chose zoom ratio of " + (ratios.get(closestIndex) / 100.0));
        return closestIndex;
    }

    public static void setInvertColor(Camera.Parameters parameters) {
        if (Camera.Parameters.EFFECT_NEGATIVE.equals(parameters.getColorEffect())) {
            LogUtils.i("Negative effect already set");
            return;
        }
        String colorMode = findSettableValue("color effect", parameters.getSupportedColorEffects(), Camera.Parameters.EFFECT_NEGATIVE);
        if (colorMode != null) {
            parameters.setColorEffect(colorMode);
        }
    }

    public static Point findBestPreviewSizeValue(Camera.Parameters parameters, Point screenResolution) {

        List<Camera.Size> rawSupportedSizes = parameters.getSupportedPreviewSizes();
        if (rawSupportedSizes == null) {
            LogUtils.w("Device returned no supported preview sizes; using default");
            Camera.Size defaultSize = parameters.getPreviewSize();
            if (defaultSize == null) {
                throw new IllegalStateException("Parameters contained no preview size!");
            }
            return new Point(defaultSize.width, defaultSize.height);
        }

        if (Log.isLoggable(LogUtils.TAG, Log.INFO)) {
            StringBuilder previewSizesString = new StringBuilder();
            for (Camera.Size size : rawSupportedSizes) {
                previewSizesString.append(size.width).append('x').append(size.height).append(' ');
            }
            LogUtils.i("Supported preview sizes: " + previewSizesString);
        }

        double screenAspectRatio = screenResolution.x / (double) screenResolution.y;

        // Find a suitable size, with max resolution
        int maxResolution = 0;
        Camera.Size maxResPreviewSize = null;
        for (Camera.Size size : rawSupportedSizes) {
            int realWidth = size.width;
            int realHeight = size.height;
            int resolution = realWidth * realHeight;
            if (resolution < MIN_PREVIEW_PIXELS) {
                continue;
            }

            boolean isCandidatePortrait = realWidth < realHeight;
            int maybeFlippedWidth = isCandidatePortrait ? realHeight : realWidth;
            int maybeFlippedHeight = isCandidatePortrait ? realWidth : realHeight;
            double aspectRatio = maybeFlippedWidth / (double) maybeFlippedHeight;
            double distortion = Math.abs(aspectRatio - screenAspectRatio);
            if (distortion > MAX_ASPECT_DISTORTION) {
                continue;
            }

            if (maybeFlippedWidth == screenResolution.x && maybeFlippedHeight == screenResolution.y) {
                Point exactPoint = new Point(realWidth, realHeight);
                LogUtils.i("Found preview size exactly matching screen size: " + exactPoint);
                return exactPoint;
            }

            // Resolution is suitable; record the one with max resolution
            if (resolution > maxResolution) {
                maxResolution = resolution;
                maxResPreviewSize = size;
            }
        }

        // If no exact match, use largest preview size. This was not a great idea on older devices because
        // of the additional computation needed. We're likely to get here on newer Android 4+ devices, where
        // the CPU is much more powerful.
        if (maxResPreviewSize != null) {
            Point largestSize = new Point(maxResPreviewSize.width, maxResPreviewSize.height);
            LogUtils.i("Using largest suitable preview size: " + largestSize);
            return largestSize;
        }

        // If there is nothing at all suitable, return current preview size
        Camera.Size defaultPreview = parameters.getPreviewSize();
        if (defaultPreview == null) {
            throw new IllegalStateException("Parameters contained no preview size!");
        }
        Point defaultSize = new Point(defaultPreview.width, defaultPreview.height);
        LogUtils.i("No suitable preview sizes, using default: " + defaultSize);
        return defaultSize;
    }

    private static String findSettableValue(String name, Collection<String> supportedValues, String... desiredValues) {
        LogUtils.i("Requesting " + name + " value from among: " + Arrays.toString(desiredValues));
        LogUtils.i("Supported " + name + " values: " + supportedValues);
        if (supportedValues != null) {
            for (String desiredValue : desiredValues) {
                if (supportedValues.contains(desiredValue)) {
                    LogUtils.i("Can set " + name + " to: " + desiredValue);
                    return desiredValue;
                }
            }
        }
        LogUtils.i("No supported values match");
        return null;
    }

    private static String toString(Collection<int[]> arrays) {
        if (arrays == null || arrays.isEmpty()) {
            return "[]";
        }
        StringBuilder buffer = new StringBuilder();
        buffer.append('[');
        Iterator<int[]> it = arrays.iterator();
        while (it.hasNext()) {
            buffer.append(Arrays.toString(it.next()));
            if (it.hasNext()) {
                buffer.append(", ");
            }
        }
        buffer.append(']');
        return buffer.toString();
    }

    private static String toString(Iterable<Camera.Area> areas) {
        if (areas == null) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        for (Camera.Area area : areas) {
            result.append(area.rect).append(':').append(area.weight).append(' ');
        }
        return result.toString();
    }

    public static String collectStats(Camera.Parameters parameters) {
        return collectStats(parameters.flatten());
    }

    public static String collectStats(CharSequence flattenedParams) {
        StringBuilder result = new StringBuilder(1000);

        result.append("BOARD=").append(Build.BOARD).append('\n');
        result.append("BRAND=").append(Build.BRAND).append('\n');
        result.append("CPU_ABI=").append(Build.CPU_ABI).append('\n');
        result.append("DEVICE=").append(Build.DEVICE).append('\n');
        result.append("DISPLAY=").append(Build.DISPLAY).append('\n');
        result.append("FINGERPRINT=").append(Build.FINGERPRINT).append('\n');
        result.append("HOST=").append(Build.HOST).append('\n');
        result.append("ID=").append(Build.ID).append('\n');
        result.append("MANUFACTURER=").append(Build.MANUFACTURER).append('\n');
        result.append("MODEL=").append(Build.MODEL).append('\n');
        result.append("PRODUCT=").append(Build.PRODUCT).append('\n');
        result.append("TAGS=").append(Build.TAGS).append('\n');
        result.append("TIME=").append(Build.TIME).append('\n');
        result.append("TYPE=").append(Build.TYPE).append('\n');
        result.append("USER=").append(Build.USER).append('\n');
        result.append("VERSION.CODENAME=").append(Build.VERSION.CODENAME).append('\n');
        result.append("VERSION.INCREMENTAL=").append(Build.VERSION.INCREMENTAL).append('\n');
        result.append("VERSION.RELEASE=").append(Build.VERSION.RELEASE).append('\n');
        result.append("VERSION.SDK_INT=").append(Build.VERSION.SDK_INT).append('\n');

        if (flattenedParams != null) {
            String[] params = SEMICOLON.split(flattenedParams);
            Arrays.sort(params);
            for (String param : params) {
                result.append(param).append('\n');
            }
        }

        return result.toString();
    }
}
