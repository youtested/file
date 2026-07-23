package com.distriqt.extension.inappbilling.functions.purchases;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.distriqt.extension.inappbilling.InAppBillingContext;
import com.distriqt.extension.inappbilling.util.FREUtils;

/* loaded from: /home/codespace/chem/Chlab_rebuilt.apk_decompiled/build/apk/classes.dex */
public class FinishPurchaseFunction implements FREFunction {
    @Override // com.adobe.fre.FREFunction
    public FREObject call(FREContext context, FREObject[] args) {
        try {
            InAppBillingContext ctx = (InAppBillingContext) context;
            Boolean success = false;
            if (ctx.v) {
                success = Boolean.valueOf(ctx.controller().finishPurchase());
            }
            FREObject result = FREObject.newObject(success.booleanValue());
            return result;
        } catch (Exception e) {
            FREUtils.handleException(context, e);
            return null;
        }
    }
}
