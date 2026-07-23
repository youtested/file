package com.distriqt.extension.inappbilling.functions;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.adobe.fre.FREWrongThreadException;
import com.distriqt.extension.inappbilling.InAppBillingContext;

/* loaded from: /home/codespace/chem/Chlab_rebuilt.apk_decompiled/build/apk/classes.dex */
public class VersionFunction implements FREFunction {
    @Override // com.adobe.fre.FREFunction
    public FREObject call(FREContext context, FREObject[] passedArgs) {
        try {
            FREObject result = FREObject.newObject(InAppBillingContext.VERSION);
            return result;
        } catch (FREWrongThreadException e) {
            return null;
        }
    }
}
