package com.distriqt.extension.inappbilling.functions;

import com.adobe.fre.FREArray;
import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.distriqt.extension.inappbilling.InAppBillingContext;
import com.distriqt.extension.inappbilling.util.FREUtils;
import java.util.ArrayList;

/* loaded from: /home/codespace/chem/Chlab_rebuilt.apk_decompiled/build/apk/classes.dex */
public class ActiveProductIdsFunction implements FREFunction {
    private static String TAG = CleanupFunction.class.getSimpleName();

    @Override // com.adobe.fre.FREFunction
    public FREObject call(FREContext context, FREObject[] args) {
        FREUtils.log(TAG, "call", new Object[0]);
        try {
            InAppBillingContext ctx = (InAppBillingContext) context;
            if (ctx.v) {
                ArrayList<String> productIds = ctx.controller().activeProductIds();
                FREArray freProductIds = FREArray.newArray(productIds.size());
                for (int i = 0; i < productIds.size(); i++) {
                    freProductIds.setObjectAt(i, FREObject.newObject(productIds.get(i)));
                }
                return freProductIds;
            }
            FREArray freEmptyProductIds = FREArray.newArray(0);
            return freEmptyProductIds;
        } catch (Exception e) {
            FREUtils.handleException(context, e);
            return null;
        }
    }
}
