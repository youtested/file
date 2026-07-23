package com.distriqt.extension.inappbilling.functions;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.distriqt.extension.inappbilling.InAppBillingContext;
import com.distriqt.extension.inappbilling.util.FREUtils;

/* loaded from: /home/codespace/chem/Chlab_rebuilt.apk_decompiled/build/apk/classes.dex */
public class IsProductViewSupportedFunction implements FREFunction {
    public static final String TAG = IsProductViewSupportedFunction.class.getSimpleName();

    @Override // com.adobe.fre.FREFunction
    public FREObject call(FREContext context, FREObject[] args) {
        FREUtils.log(TAG, "call", new Object[0]);
        try {
            InAppBillingContext ctx = (InAppBillingContext) context;
            boolean isSupported = false;
            if (ctx.v) {
                isSupported = ctx.controller().isProductViewSupported();
            }
            FREObject result = FREObject.newObject(isSupported);
            return result;
        } catch (Exception e) {
            FREUtils.handleException(context, e);
            return null;
        }
    }
}
