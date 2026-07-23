package com.adobe.air;

import android.app.Activity;
import android.app.Application;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.provider.MediaStore;
import com.adobe.air.AndroidActivityWrapper;
import com.distriqt.extension.inappbilling.BuildConfig;

/* loaded from: /home/codespace/chem/Chlab_rebuilt.apk_decompiled/build/apk/classes.dex */
public class AndroidMediaManager {
    public static final int ERROR_ACTIVITY_DESTROYED = 2;
    public static final int ERROR_IMAGE_PICKER_NOT_FOUND = 1;
    private static long MediaManagerObjectPointer = 0;
    private static final String PHONE_STORAGE = "phoneStorage";
    private boolean mCallbacksRegistered = false;
    private AndroidActivityWrapper.ActivityResultCallback mActivityResultCB = null;

    public native void useImagePickerData(long j, boolean z, boolean z2, String str, String str2, String str3);

    public native void useStreamData(long j, boolean z, boolean z2, String str);

    public AndroidMediaManager() {
        MediaManagerObjectPointer = 0L;
    }

    public void registerCallbacks() {
        doCallbackRegistration(true);
    }

    public void unregisterCallbacks() {
        doCallbackRegistration(false);
    }

    private void doCallbackRegistration(boolean z) {
        this.mCallbacksRegistered = z;
        if (z) {
            if (this.mActivityResultCB == null) {
                this.mActivityResultCB = new AndroidActivityWrapper.ActivityResultCallback() { // from class: com.adobe.air.AndroidMediaManager.1
                    @Override // com.adobe.air.AndroidActivityWrapper.ActivityResultCallback
                    public void onActivityResult(int i, int i2, Intent intent) {
                        if (i == 2 && AndroidMediaManager.MediaManagerObjectPointer != 0 && AndroidMediaManager.this.mCallbacksRegistered) {
                            AndroidMediaManager.this.onBrowseImageResult(i2, intent, AndroidActivityWrapper.GetAndroidActivityWrapper().getActivity());
                            AndroidMediaManager.this.unregisterCallbacks();
                        }
                    }
                };
            }
            AndroidActivityWrapper.GetAndroidActivityWrapper().addActivityResultListener(this.mActivityResultCB);
        } else if (this.mActivityResultCB != null) {
            AndroidActivityWrapper.GetAndroidActivityWrapper().removeActivityResultListener(this.mActivityResultCB);
            this.mActivityResultCB = null;
        }
    }

    public static boolean AddImage(Application application, Bitmap bitmap, boolean z) {
        String str = null;
        if (application != null) {
            ContentResolver contentResolver = application.getContentResolver();
            try {
                str = MediaStore.Images.Media.insertImage(contentResolver, bitmap, (String) null, (String) null);
            } catch (Exception e) {
            }
            String SaveImage = str == null ? SaveImage(PHONE_STORAGE, contentResolver, bitmap, z) : str;
            if (SaveImage != null) {
                try {
                    Cursor query = contentResolver.query(Uri.parse(SaveImage), new String[]{"_data"}, null, null, null);
                    if (query != null) {
                        int columnIndexOrThrow = query.getColumnIndexOrThrow("_data");
                        query.moveToFirst();
                        MediaScannerConnection.scanFile(AndroidActivityWrapper.GetAndroidActivityWrapper().getDefaultContext(), new String[]{query.getString(columnIndexOrThrow)}, null, null);
                    }
                } catch (Exception e2) {
                }
                return true;
            }
        }
        return false;
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x008f  */
    /* JADX WARN: Removed duplicated region for block: B:9:0x0065  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static java.lang.String SaveImage(java.lang.String r11, android.content.ContentResolver r12, android.graphics.Bitmap r13, boolean r14) {
        /*
            r9 = 0
            android.content.ContentValues r0 = new android.content.ContentValues     // Catch: java.lang.Exception -> L72
            r0.<init>()     // Catch: java.lang.Exception -> L72
            if (r14 == 0) goto L6a
            java.lang.String r1 = "mime_type"
            java.lang.String r2 = "image/jpeg"
            r0.put(r1, r2)     // Catch: java.lang.Exception -> L72
        Lf:
            java.util.Date r1 = new java.util.Date     // Catch: java.lang.Exception -> L72
            r1.<init>()     // Catch: java.lang.Exception -> L72
            java.lang.String r2 = "datetaken"
            long r4 = r1.getTime()     // Catch: java.lang.Exception -> L72
            java.lang.Long r3 = java.lang.Long.valueOf(r4)     // Catch: java.lang.Exception -> L72
            r0.put(r2, r3)     // Catch: java.lang.Exception -> L72
            java.lang.String r2 = "date_added"
            long r4 = r1.getTime()     // Catch: java.lang.Exception -> L72
            r6 = 1000(0x3e8, double:4.94E-321)
            long r4 = r4 / r6
            java.lang.Long r1 = java.lang.Long.valueOf(r4)     // Catch: java.lang.Exception -> L72
            r0.put(r2, r1)     // Catch: java.lang.Exception -> L72
            android.net.Uri r1 = android.provider.MediaStore.Images.Media.getContentUri(r11)     // Catch: java.lang.Exception -> L72
            android.net.Uri r0 = r12.insert(r1, r0)     // Catch: java.lang.Exception -> L72
            if (r0 == 0) goto L63
            java.io.OutputStream r10 = r12.openOutputStream(r0)     // Catch: java.lang.Exception -> L88
            android.graphics.Bitmap$CompressFormat r1 = android.graphics.Bitmap.CompressFormat.JPEG     // Catch: java.lang.Exception -> L7b java.lang.Throwable -> L8a
            r2 = 90
            r13.compress(r1, r2, r10)     // Catch: java.lang.Exception -> L7b java.lang.Throwable -> L8a
            long r4 = android.content.ContentUris.parseId(r0)     // Catch: java.lang.Exception -> L7b java.lang.Throwable -> L8a
            r6 = 1134559232(0x43a00000, float:320.0)
            r7 = 1131413504(0x43700000, float:240.0)
            r8 = 1
            r1 = r11
            r2 = r12
            r3 = r13
            android.graphics.Bitmap r3 = SaveThumbnail(r1, r2, r3, r4, r6, r7, r8)     // Catch: java.lang.Exception -> L7b java.lang.Throwable -> L8a
            r6 = 1112014848(0x42480000, float:50.0)
            r7 = 1112014848(0x42480000, float:50.0)
            r8 = 3
            r1 = r11
            r2 = r12
            SaveThumbnail(r1, r2, r3, r4, r6, r7, r8)     // Catch: java.lang.Exception -> L7b java.lang.Throwable -> L8a
            r10.close()     // Catch: java.lang.Exception -> L88
        L63:
            if (r0 == 0) goto L8f
            java.lang.String r0 = r0.toString()
        L69:
            return r0
        L6a:
            java.lang.String r1 = "mime_type"
            java.lang.String r2 = "image/png"
            r0.put(r1, r2)     // Catch: java.lang.Exception -> L72
            goto Lf
        L72:
            r0 = move-exception
            r0 = r9
        L74:
            if (r0 == 0) goto L63
            r12.delete(r0, r9, r9)
            r0 = r9
            goto L63
        L7b:
            r1 = move-exception
            if (r0 == 0) goto L84
            r1 = 0
            r2 = 0
            r12.delete(r0, r1, r2)     // Catch: java.lang.Throwable -> L8a
            r0 = r9
        L84:
            r10.close()     // Catch: java.lang.Exception -> L88
            goto L63
        L88:
            r1 = move-exception
            goto L74
        L8a:
            r1 = move-exception
            r10.close()     // Catch: java.lang.Exception -> L88
            throw r1     // Catch: java.lang.Exception -> L88
        L8f:
            r0 = r9
            goto L69
        */
        throw new UnsupportedOperationException("Method not decompiled: com.adobe.air.AndroidMediaManager.SaveImage(java.lang.String, android.content.ContentResolver, android.graphics.Bitmap, boolean):java.lang.String");
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x0078  */
    /* JADX WARN: Removed duplicated region for block: B:16:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static final android.graphics.Bitmap SaveThumbnail(java.lang.String r9, android.content.ContentResolver r10, android.graphics.Bitmap r11, long r12, float r14, float r15, int r16) {
        /*
            r7 = 0
            if (r11 != 0) goto L5
            r0 = r7
        L4:
            return r0
        L5:
            android.graphics.Matrix r5 = new android.graphics.Matrix
            r5.<init>()
            int r0 = r11.getWidth()     // Catch: java.lang.Exception -> L7a
            float r0 = (float) r0     // Catch: java.lang.Exception -> L7a
            float r0 = r14 / r0
            int r1 = r11.getHeight()     // Catch: java.lang.Exception -> L7a
            float r1 = (float) r1     // Catch: java.lang.Exception -> L7a
            float r1 = r15 / r1
            r5.setScale(r0, r1)     // Catch: java.lang.Exception -> L7a
            r1 = 0
            r2 = 0
            int r3 = r11.getWidth()     // Catch: java.lang.Exception -> L7a
            int r4 = r11.getHeight()     // Catch: java.lang.Exception -> L7a
            r6 = 1
            r0 = r11
            android.graphics.Bitmap r0 = android.graphics.Bitmap.createBitmap(r0, r1, r2, r3, r4, r5, r6)     // Catch: java.lang.Exception -> L7a
            android.content.ContentValues r1 = new android.content.ContentValues
            r2 = 4
            r1.<init>(r2)
            java.lang.String r2 = "kind"
            java.lang.Integer r3 = java.lang.Integer.valueOf(r16)
            r1.put(r2, r3)
            java.lang.String r2 = "image_id"
            int r3 = (int) r12
            java.lang.Integer r3 = java.lang.Integer.valueOf(r3)
            r1.put(r2, r3)
            java.lang.String r2 = "height"
            int r3 = r0.getHeight()
            java.lang.Integer r3 = java.lang.Integer.valueOf(r3)
            r1.put(r2, r3)
            java.lang.String r2 = "width"
            int r3 = r0.getWidth()
            java.lang.Integer r3 = java.lang.Integer.valueOf(r3)
            r1.put(r2, r3)
            android.net.Uri r2 = android.provider.MediaStore.Images.Thumbnails.getContentUri(r9)     // Catch: java.lang.Exception -> L7d
            android.net.Uri r1 = r10.insert(r2, r1)     // Catch: java.lang.Exception -> L7d
            if (r1 == 0) goto L76
            java.io.OutputStream r2 = r10.openOutputStream(r1)     // Catch: java.lang.Exception -> L86
            android.graphics.Bitmap$CompressFormat r3 = android.graphics.Bitmap.CompressFormat.JPEG     // Catch: java.lang.Exception -> L86
            r4 = 100
            r0.compress(r3, r4, r2)     // Catch: java.lang.Exception -> L86
            r2.close()     // Catch: java.lang.Exception -> L86
        L76:
            if (r1 != 0) goto L4
            r0 = r7
            goto L4
        L7a:
            r0 = move-exception
            r0 = r7
            goto L4
        L7d:
            r1 = move-exception
            r1 = r7
        L7f:
            if (r1 == 0) goto L76
            r10.delete(r1, r7, r7)
            r1 = r7
            goto L76
        L86:
            r2 = move-exception
            goto L7f
        */
        throw new UnsupportedOperationException("Method not decompiled: com.adobe.air.AndroidMediaManager.SaveThumbnail(java.lang.String, android.content.ContentResolver, android.graphics.Bitmap, long, float, float, int):android.graphics.Bitmap");
    }

    public int BrowseImage(long j) {
        int i = 0;
        try {
            AndroidActivityWrapper GetAndroidActivityWrapper = AndroidActivityWrapper.GetAndroidActivityWrapper();
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction("android.intent.action.PICK");
            if (GetAndroidActivityWrapper.getActivity() != null) {
                GetAndroidActivityWrapper.getActivity().startActivityForResult(Intent.createChooser(intent, BuildConfig.FLAVOR), 2);
            } else {
                i = 2;
            }
        } catch (ActivityNotFoundException e) {
            i = 1;
        }
        if (i == 0) {
            registerCallbacks();
            MediaManagerObjectPointer = j;
        }
        return i;
    }

    public void onBrowseImageResult(int i, Intent intent, Activity activity) {
        if (i == 0) {
            useImagePickerData(MediaManagerObjectPointer, false, true, BuildConfig.FLAVOR, BuildConfig.FLAVOR, BuildConfig.FLAVOR);
            return;
        }
        if (i == -1) {
            try {
                Cursor managedQuery = activity.managedQuery(intent.getData(), new String[]{"_data", "mime_type", "_display_name"}, null, null, null);
                if (managedQuery == null) {
                    useImagePickerData(MediaManagerObjectPointer, false, false, BuildConfig.FLAVOR, BuildConfig.FLAVOR, BuildConfig.FLAVOR);
                } else {
                    int columnIndexOrThrow = managedQuery.getColumnIndexOrThrow("_data");
                    int columnIndexOrThrow2 = managedQuery.getColumnIndexOrThrow("_display_name");
                    managedQuery.moveToFirst();
                    String string = managedQuery.getString(columnIndexOrThrow);
                    String string2 = managedQuery.getString(columnIndexOrThrow2);
                    if (string != null && !string.startsWith("http")) {
                        useImagePickerData(MediaManagerObjectPointer, true, true, string, "image", string2);
                    } else {
                        useStreamData(MediaManagerObjectPointer, true, true, intent.getDataString());
                    }
                }
            } catch (IllegalArgumentException e) {
                useImagePickerData(MediaManagerObjectPointer, false, false, BuildConfig.FLAVOR, BuildConfig.FLAVOR, BuildConfig.FLAVOR);
            } catch (Exception e2) {
                useImagePickerData(MediaManagerObjectPointer, false, false, BuildConfig.FLAVOR, BuildConfig.FLAVOR, BuildConfig.FLAVOR);
            }
        }
    }
}
