package com.adobe.air;

import android.os.Handler;
import android.os.Looper;

/* loaded from: /home/codespace/chem/Chlab_rebuilt.apk_decompiled/build/apk/classes.dex */
public class WorkerLooper {
    private Handler mHandler;
    private Looper mLooper;

    public WorkerLooper() {
        Looper.prepare();
        this.mHandler = new customHandler();
        this.mLooper = Looper.myLooper();
    }

    public void start() {
        Looper.loop();
    }

    public Handler getHandler() {
        return this.mHandler;
    }

    public void quit() {
        this.mLooper.quit();
    }
}
