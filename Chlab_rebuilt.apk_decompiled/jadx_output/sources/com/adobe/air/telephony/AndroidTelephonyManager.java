package com.adobe.air.telephony;

import android.content.Context;
import android.telephony.TelephonyManager;

/* loaded from: /home/codespace/chem/Chlab_rebuilt.apk_decompiled/build/apk/classes.dex */
public class AndroidTelephonyManager {
    private static AndroidTelephonyManager sAndroidTelephonyManager = null;
    private Context mContext;
    private TelephonyManager mTelephonyMgr;
    private AndroidPhoneStateListener mPhoneStateListener = null;
    private boolean mListening = false;

    public static AndroidTelephonyManager CreateAndroidTelephonyManager(Context context) {
        if (sAndroidTelephonyManager == null) {
            sAndroidTelephonyManager = new AndroidTelephonyManager(context);
        }
        return sAndroidTelephonyManager;
    }

    public static AndroidTelephonyManager GetAndroidTelephonyManager() {
        return sAndroidTelephonyManager;
    }

    private AndroidTelephonyManager(Context context) {
        this.mContext = null;
        this.mTelephonyMgr = null;
        this.mContext = context;
        if (this.mContext != null) {
            this.mTelephonyMgr = (TelephonyManager) this.mContext.getSystemService("phone");
        }
    }

    public void listen(boolean z) {
        if (this.mTelephonyMgr != null) {
            if (z) {
                try {
                    if (!this.mListening) {
                        if (this.mPhoneStateListener == null) {
                            this.mPhoneStateListener = new AndroidPhoneStateListener();
                        }
                        this.mTelephonyMgr.listen(this.mPhoneStateListener, 32);
                        this.mListening = true;
                        this.mPhoneStateListener.onCallStateChanged(this.mTelephonyMgr.getCallState(), "0");
                        return;
                    }
                } catch (Exception e) {
                    return;
                }
            }
            if (!z && this.mListening && this.mPhoneStateListener != null) {
                this.mTelephonyMgr.listen(this.mPhoneStateListener, 0);
                this.mPhoneStateListener = null;
                this.mListening = false;
            }
        }
    }
}
