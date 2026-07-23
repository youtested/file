package com.distriqt.extension.inappbilling.functions;

import com.adobe.fre.FREArray;
import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.distriqt.extension.inappbilling.InAppBillingContext;
import com.distriqt.extension.inappbilling.util.FREUtils;
import java.util.Arrays;

/* loaded from: /home/codespace/chem/Chlab_rebuilt.apk_decompiled/build/apk/classes.dex */
public class GetProductsFunction implements FREFunction {
    private static String TAG = GetProductsFunction.class.getSimpleName();

    @Override // com.adobe.fre.FREFunction
    public FREObject call(FREContext context, FREObject[] args) {
        FREUtils.log(TAG, "call", new Object[0]);
        try {
            InAppBillingContext ctx = (InAppBillingContext) context;
            boolean success = false;
            if (ctx.v) {
                String[] productIds = FREUtils.GetObjectAsArrayOfStrings((FREArray) args[0]);
                Boolean clearProducts = Boolean.valueOf(args[1].getAsBool());
                success = ctx.controller().getProducts(Arrays.asList(productIds), clearProducts);
            }
            FREObject result = FREObject.newObject(success);
            return result;
        } catch (Exception e) {
            FREUtils.handleException(context, e);
            return null;
        }
    }
}
