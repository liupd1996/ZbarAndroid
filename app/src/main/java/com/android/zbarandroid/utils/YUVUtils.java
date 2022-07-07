package com.android.zbarandroid.utils;

import android.graphics.Bitmap;

public class YUVUtils {
    public static byte[] getNV21(int inputWidth, int inputHeight, Bitmap srcBitmap) {
        int[] argb = new int[inputWidth * inputHeight];
        if (null != srcBitmap) {
            try {
                srcBitmap.getPixels(argb, 0, inputWidth, 0, 0, inputWidth, inputHeight);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

            if (null != srcBitmap && !srcBitmap.isRecycled()) {
                srcBitmap.recycle();
                srcBitmap = null;
            }
            return colorconvertRGB_YUV_NV21(argb, inputWidth, inputHeight);
        } else return null;
    }

    public static byte[] colorconvertRGB_YUV_NV21(int[] aRGB, int width, int height) {
        final int frameSize = width * height;
        int yIndex = 0;
        int uvIndex = frameSize;
        byte[] yuv = new byte[width * height * 3 / 2];

        int a, R, G, B, Y, U, V;
        int index = 0;
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                //a = (aRGB[index] & 0xff000000) >> 24; //not using it right now
                R = (aRGB[index] & 0xff0000) >> 16;
                G = (aRGB[index] & 0xff00) >> 8;
                B = (aRGB[index] & 0xff) >> 0;

                Y = ((66 * R + 129 * G + 25 * B + 128) >> 8) + 16;
                U = ((-38 * R - 74 * G + 112 * B + 128) >> 8) + 128;
                V = ((112 * R - 94 * G - 18 * B + 128) >> 8) + 128;

                yuv[yIndex++] = (byte) ((Y < 0) ? 0 : ((Y > 255) ? 255 : Y));

                if (j % 2 == 0 && index % 2 == 0) {
                    yuv[uvIndex++] = (byte) ((V < 0) ? 0 : ((V > 255) ? 255 : V));
                    yuv[uvIndex++] = (byte) ((U < 0) ? 0 : ((U > 255) ? 255 : U));
                }
                index++;
            }
        }
        return yuv;
    }

}
