package com.adobe.flashruntime.air;

import android.graphics.SurfaceTexture;
import android.os.Build;
import android.util.Log;
import android.view.Surface;

/* loaded from: /home/codespace/chem/Chlab_rebuilt.apk_decompiled/build/apk/classes.dex */
public class VideoTextureSurface implements SurfaceTexture.OnFrameAvailableListener {
    private static final String TAG = "VideoSurfaceTexture";
    private boolean mAmCreated;
    private long mCPPInstance;
    private Surface mSurface;
    private int mTextureId;
    private SurfaceTexture mVideoTexture;
    private boolean mPlanePositionSet = false;
    private boolean mFrameAvailable = false;

    private native void nativeSetJavaTextureSurfaceReady(long j, boolean z);

    public VideoTextureSurface(int i) {
        this.mAmCreated = false;
        this.mSurface = null;
        this.mTextureId = i;
        this.mVideoTexture = new SurfaceTexture(this.mTextureId);
        this.mVideoTexture.setOnFrameAvailableListener(this);
        this.mSurface = new Surface(this.mVideoTexture);
        this.mAmCreated = true;
    }

    public boolean updateSurfaceTextureTexImage() {
        if (!this.mFrameAvailable) {
            return false;
        }
        this.mVideoTexture.updateTexImage();
        this.mFrameAvailable = false;
        return true;
    }

    @Override // android.graphics.SurfaceTexture.OnFrameAvailableListener
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        this.mFrameAvailable = true;
        notifyNativeReadyForVideoTexture();
    }

    public void VideoPlaybackRestarted() {
    }

    public void setFPInstance(long j) {
        Log.d(TAG, "Changing FP Instance from " + this.mCPPInstance + " to " + j);
        this.mCPPInstance = j;
    }

    public long getFPInstance() {
        return this.mCPPInstance;
    }

    public boolean useOverlay() {
        return Build.VERSION.SDK_INT >= 14;
    }

    void setNativeInstance(long j) {
    }

    public Surface getSurface() {
        if (this.mAmCreated && useOverlay()) {
            return this.mSurface;
        }
        return null;
    }

    public void notifyNativeReadyForVideoTexture() {
        if (this.mCPPInstance != 0) {
            nativeSetJavaTextureSurfaceReady(this.mCPPInstance, this.mAmCreated);
        }
    }
}
