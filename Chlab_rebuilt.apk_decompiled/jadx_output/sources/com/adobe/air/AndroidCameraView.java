package com.adobe.air;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: AndroidCamera.java */
/* loaded from: /home/codespace/chem/Chlab_rebuilt.apk_decompiled/build/apk/classes.dex */
public class AndroidCameraView extends SurfaceView implements SurfaceHolder.Callback {
    private AndroidActivityWrapper mActivityWrapper;
    private boolean mRecreating;

    public AndroidCameraView(Context context, AndroidActivityWrapper androidActivityWrapper) {
        super(context);
        this.mActivityWrapper = null;
        this.mRecreating = false;
        this.mActivityWrapper = androidActivityWrapper;
        getHolder().setType(3);
        setZOrderMediaOverlay(false);
        getHolder().addCallback(this);
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        this.mActivityWrapper.planeStepCascade();
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        this.mActivityWrapper.planeBreakCascade();
    }
}
