package com.distriqt.extension.inappbilling.functions;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.android.vending.billing.util.SkuDetails;
import com.distriqt.extension.inappbilling.BuildConfig;
import com.distriqt.extension.inappbilling.InAppBillingContext;
import com.distriqt.extension.inappbilling.events.InAppBillingEvent;
import com.distriqt.extension.inappbilling.util.FREUtils;
import java.util.Map;

/* loaded from: /home/codespace/chem/Chlab_rebuilt.apk_decompiled/build/apk/classes.dex */
public class ActiveProductsFunction implements FREFunction {
    private static String TAG = ActiveProductsFunction.class.getSimpleName();

    @Override // com.adobe.fre.FREFunction
    public FREObject call(FREContext context, FREObject[] args) {
        FREUtils.log(TAG, "call", new Object[0]);
        try {
            InAppBillingContext ctx = (InAppBillingContext) context;
            String productsString = BuildConfig.FLAVOR;
            if (ctx.v) {
                Map<String, SkuDetails> products = ctx.controller().activeProducts();
                productsString = InAppBillingEvent.formatProductsForEvent(products);
            }
            FREObject result = FREObject.newObject(productsString);
            return result;
        } catch (Exception e) {
            FREUtils.handleException(context, e);
            return null;
        }
    }
}
