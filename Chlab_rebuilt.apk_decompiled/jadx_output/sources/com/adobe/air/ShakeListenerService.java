package com.adobe.air;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import com.adobe.air.ShakeListener;
import com.adobe.air.wand.WandActivity;
import java.lang.Thread;
import java.util.List;

/* loaded from: /home/codespace/chem/Chlab_rebuilt.apk_decompiled/build/apk/classes.dex */
public class ShakeListenerService extends Service {
    private ShakeListener mShakeListener;
    private Context mcontext;
    private BackgroundThread backGroundThread = null;
    private final String AIR_WAND_CLASS_NAME = "com.adobe.air.wand.WandActivity";

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        if (this.backGroundThread == null) {
            this.backGroundThread = new BackgroundThread(getApplicationContext());
        }
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int i, int i2) {
        if (this.backGroundThread == null) {
            this.backGroundThread = new BackgroundThread(getApplicationContext());
        }
        if (this.backGroundThread.getState() == Thread.State.NEW || this.backGroundThread.getState() == Thread.State.TERMINATED) {
            if (this.backGroundThread.getState() == Thread.State.TERMINATED) {
                this.backGroundThread = new BackgroundThread(getApplicationContext());
            }
            this.backGroundThread.start();
            return 1;
        }
        return 1;
    }

    @Override // android.app.Service
    public void onDestroy() {
        super.onDestroy();
        if (this.backGroundThread != null) {
            try {
                this.backGroundThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.backGroundThread = null;
        }
    }

    /* loaded from: /home/codespace/chem/Chlab_rebuilt.apk_decompiled/build/apk/classes.dex */
    class BackgroundThread extends Thread {
        public BackgroundThread(Context context) {
            ShakeListenerService.this.mcontext = context;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            try {
                ShakeListenerService.this.mShakeListener = new ShakeListener(ShakeListenerService.this.mcontext);
                ShakeListenerService.this.mShakeListener.registerListener(new ShakeListener.Listener() { // from class: com.adobe.air.ShakeListenerService.BackgroundThread.1
                    @Override // com.adobe.air.ShakeListener.Listener
                    public void onShake() {
                        List<ActivityManager.RunningTaskInfo> runningTasks = ((ActivityManager) ShakeListenerService.this.getApplicationContext().getSystemService("activity")).getRunningTasks(1);
                        if (!runningTasks.isEmpty()) {
                            ComponentName componentName = runningTasks.get(0).topActivity;
                            if (componentName.getPackageName().equals(ShakeListenerService.this.getApplicationContext().getPackageName()) && !componentName.getClassName().equalsIgnoreCase("com.adobe.air.wand.WandActivity")) {
                                Intent intent = new Intent(ShakeListenerService.this.getApplicationContext(), (Class<?>) WandActivity.class);
                                intent.setFlags(272629760);
                                ShakeListenerService.this.startActivity(intent);
                            }
                        }
                    }
                });
            } catch (Exception e) {
            }
        }
    }
}
