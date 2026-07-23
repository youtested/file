package com.distriqt.extension.inappbilling.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import com.adobe.fre.FREBitmapData;

/* loaded from: /home/codespace/chem/Chlab_rebuilt.apk_decompiled/build/apk/classes.dex */
public class FREImageUtils {
    public static Bitmap getFREObjectAsBitmap(FREBitmapData object) throws Exception {
        object.acquire();
        Bitmap bmp = Bitmap.createBitmap(object.getWidth(), object.getHeight(), Bitmap.Config.ARGB_8888);
        bmp.copyPixelsFromBuffer(object.getBits());
        object.release();
        float[] bgrToRgbColorTransform = {0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f};
        ColorMatrix colorMatrix = new ColorMatrix(bgrToRgbColorTransform);
        ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);
        Canvas canvas = new Canvas(bmp);
        Paint paint = new Paint();
        paint.setColorFilter(colorFilter);
        canvas.drawBitmap(bmp, 0.0f, 0.0f, paint);
        return bmp;
    }

    public static void drawBitmapToBitmapData(Bitmap bitmap, FREBitmapData bitmapData) {
        float[] bgrToRgbColorTransform = {0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f};
        ColorMatrix colorMatrix = new ColorMatrix(bgrToRgbColorTransform);
        ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);
        try {
            Bitmap encodingBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(encodingBitmap);
            try {
                Paint paint = new Paint();
                try {
                    paint.setColorFilter(colorFilter);
                    canvas.drawBitmap(bitmap, 0.0f, 0.0f, paint);
                    bitmapData.acquire();
                    encodingBitmap.copyPixelsToBuffer(bitmapData.getBits());
                    bitmapData.release();
                } catch (Exception e) {
                    e = e;
                    e.printStackTrace();
                }
            } catch (Exception e2) {
                e = e2;
            }
        } catch (Exception e3) {
            e = e3;
        }
    }
}
