package com.distriqt.extension.inappbilling;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREExtension;
import com.distriqt.extension.inappbilling.util.FREUtils;

/* loaded from: /home/codespace/chem/Chlab_rebuilt.apk_decompiled/build/apk/classes.dex */
public class InAppBillingExtension implements FREExtension {
    public static String ID = "com.distriqt.inappbilling";
    public static InAppBillingContext context;

    @Override // com.adobe.fre.FREExtension
    public FREContext createContext(String arg0) {
        context = new InAppBillingContext();
        FREUtils.ID = ID;
        FREUtils.context = context;
        return context;
    }

    @Override // com.adobe.fre.FREExtension
    public void dispose() {
        if (context != null) {
            context.dispose();
        }
        context = null;
    }

    @Override // com.adobe.fre.FREExtension
    public void initialize() {
    }
}
