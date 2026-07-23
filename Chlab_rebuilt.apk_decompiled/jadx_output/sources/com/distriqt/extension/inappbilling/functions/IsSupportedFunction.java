package com.distriqt.extension.inappbilling.functions;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.adobe.fre.FREWrongThreadException;
import com.distriqt.extension.inappbilling.InAppBillingContext;
import com.distriqt.extension.inappbilling.util.FREUtils;
import com.distriqt.extension.inappbilling.util.Resources;

/* loaded from: /home/codespace/chem/Chlab_rebuilt.apk_decompiled/build/apk/classes.dex */
public class IsSupportedFunction implements FREFunction {
    @Override // com.adobe.fre.FREFunction
    public FREObject call(FREContext context, FREObject[] passedArgs) {
        try {
            FREUtils.log("DEBUG", "LIST RESOURCES: %s", context.getActivity().getPackageName());
            Resources.listResources(context.getActivity().getPackageName());
            FREUtils.log("DEBUG", "LIST RESOURCES: %s", "android.support.v7.appcompat");
            Resources.listResources("android.support.v7.appcompat");
            Boolean isSupported = ((InAppBillingContext) context).controller().isSupported();
            FREObject result = FREObject.newObject(isSupported.booleanValue());
            return result;
        } catch (FREWrongThreadException e) {
            FREUtils.handleException(e);
            return null;
        }
    }
}
