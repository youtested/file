package com.adobe.air;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.SystemClock;
import com.adobe.air.AndroidActivityWrapper;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: /home/codespace/chem/Chlab_rebuilt.apk_decompiled/build/apk/classes.dex */
public class ConfigDownloadListener {
    private static ConfigDownloadListener sListener = null;
    private static String LOG_TAG = "ConfigDownloadListener";
    private BroadcastReceiver mDownloadConfigRecv = new BroadcastReceiver() { // from class: com.adobe.air.ConfigDownloadListener.1
        private String LOG_TAG = "ConfigDownloadListenerBR";

        /* JADX WARN: Code restructure failed: missing block: B:8:0x0028, code lost:
        
            if (r6.this$0.lastPauseTime < r2.getLong(com.adobe.air.AIRService.EXTRA_DOWNLOAD_TIME)) goto L10;
         */
        @Override // android.content.BroadcastReceiver
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void onReceive(android.content.Context r7, android.content.Intent r8) {
            /*
                r6 = this;
                r0 = 1
                java.lang.String r1 = r8.getAction()
                java.lang.String r2 = "com.adobe.air.DownloadConfigComplete"
                boolean r1 = r1.equals(r2)
                if (r1 == 0) goto L35
                r1 = 0
                boolean r2 = r6.isInitialStickyBroadcast()
                if (r2 == 0) goto L2a
                android.os.Bundle r2 = r8.getExtras()
                if (r2 == 0) goto L36
                java.lang.String r3 = "com.adobe.air.DownloadConfigCompleteTime"
                long r2 = r2.getLong(r3)
                com.adobe.air.ConfigDownloadListener r4 = com.adobe.air.ConfigDownloadListener.this
                long r4 = com.adobe.air.ConfigDownloadListener.access$000(r4)
                int r2 = (r4 > r2 ? 1 : (r4 == r2 ? 0 : -1))
                if (r2 >= 0) goto L36
            L2a:
                if (r0 == 0) goto L35
                com.adobe.air.AndroidActivityWrapper r0 = com.adobe.air.AndroidActivityWrapper.GetAndroidActivityWrapper()
                if (r0 == 0) goto L35
                r0.applyDownloadedConfig()
            L35:
                return
            L36:
                r0 = r1
                goto L2a
            */
            throw new UnsupportedOperationException("Method not decompiled: com.adobe.air.ConfigDownloadListener.AnonymousClass1.onReceive(android.content.Context, android.content.Intent):void");
        }
    };
    private AndroidActivityWrapper.StateChangeCallback mActivityStateCB = new AndroidActivityWrapper.StateChangeCallback() { // from class: com.adobe.air.ConfigDownloadListener.2
        @Override // com.adobe.air.AndroidActivityWrapper.StateChangeCallback
        public void onActivityStateChanged(AndroidActivityWrapper.ActivityState activityState) {
            Activity activity = AndroidActivityWrapper.GetAndroidActivityWrapper().getActivity();
            if (activityState == AndroidActivityWrapper.ActivityState.PAUSED) {
                activity.unregisterReceiver(ConfigDownloadListener.this.mDownloadConfigRecv);
                ConfigDownloadListener.this.lastPauseTime = SystemClock.uptimeMillis();
            } else if (activityState == AndroidActivityWrapper.ActivityState.RESUMED) {
                activity.registerReceiver(ConfigDownloadListener.this.mDownloadConfigRecv, new IntentFilter(AIRService.INTENT_CONFIG_DOWNLOADED));
            }
        }

        @Override // com.adobe.air.AndroidActivityWrapper.StateChangeCallback
        public void onConfigurationChanged(Configuration configuration) {
        }
    };
    private long lastPauseTime = SystemClock.uptimeMillis();

    private ConfigDownloadListener() {
        AndroidActivityWrapper.GetAndroidActivityWrapper().addActivityStateChangeListner(this.mActivityStateCB);
    }

    public static ConfigDownloadListener GetConfigDownloadListener() {
        if (sListener == null) {
            sListener = new ConfigDownloadListener();
        }
        return sListener;
    }
}
