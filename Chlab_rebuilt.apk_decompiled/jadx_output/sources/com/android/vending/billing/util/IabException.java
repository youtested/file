package com.android.vending.billing.util;

/* loaded from: /home/codespace/chem/Chlab_rebuilt.apk_decompiled/build/apk/classes.dex */
public class IabException extends Exception {
    private static final long serialVersionUID = 258468415364017860L;
    IabResult mResult;

    public IabException(IabResult r) {
        this(r, (Exception) null);
    }

    public IabException(int response, String message) {
        this(new IabResult(response, message));
    }

    public IabException(IabResult r, Exception cause) {
        super(r.getMessage(), cause);
        this.mResult = r;
    }

    public IabException(int response, String message, Exception cause) {
        this(new IabResult(response, message), cause);
    }

    public IabResult getResult() {
        return this.mResult;
    }
}
