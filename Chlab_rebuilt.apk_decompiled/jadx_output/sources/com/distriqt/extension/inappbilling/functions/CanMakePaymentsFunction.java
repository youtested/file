package com.distriqt.extension.inappbilling.functions;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.distriqt.extension.inappbilling.InAppBillingContext;
import com.distriqt.extension.inappbilling.util.FREUtils;

/* loaded from: /home/codespace/chem/Chlab_rebuilt.apk_decompiled/build/apk/classes.dex */
public class CanMakePaymentsFunction implements FREFunction {
    @Override // com.adobe.fre.FREFunction
    public FREObject call(FREContext context, FREObject[] args) {
        try {
            Boolean canMakePayments = ((InAppBillingContext) context).controller().canMakePayments();
            FREObject result = FREObject.newObject(canMakePayments.booleanValue());
            return result;
        } catch (Exception e) {
            FREUtils.handleException(e);
            return null;
        }
    }
}
