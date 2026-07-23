package com.distriqt.extension.inappbilling;

import java.util.Locale;

/* loaded from: /home/codespace/chem/Chlab_rebuilt.apk_decompiled/build/apk/classes.dex */
public class PurchaseRequest {
    public String productId = BuildConfig.FLAVOR;
    public int quantity = 1;
    public String developerPayload = BuildConfig.FLAVOR;
    public String applicationUsername = BuildConfig.FLAVOR;

    public String toString() {
        return String.format(Locale.UK, "[%s, %d]", this.productId, Integer.valueOf(this.quantity));
    }
}
