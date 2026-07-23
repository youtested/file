package com.distriqt.extension.inappbilling.activities;

import android.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.distriqt.extension.inappbilling.InAppBillingContext;
import com.distriqt.extension.inappbilling.InAppBillingExtension;
import com.distriqt.extension.inappbilling.util.FREUtils;

/* loaded from: /home/codespace/chem/Chlab_rebuilt.apk_decompiled/build/apk/classes.dex */
public class PurchaseActivity extends Activity {
    public static String TAG = PurchaseActivity.class.getSimpleName();
    public static int PURCHASE_REQUEST_CODE = 52;

    @Override // android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FREUtils.log(TAG, "onCreate()", new Object[0]);
        setTheme(R.style.Theme.Translucent.NoTitleBar);
        try {
            String productId = getIntent().getExtras().getString("productId");
            String productType = getIntent().getExtras().getString("productType");
            String payload = getIntent().getExtras().getString("payload");
            InAppBillingContext mContext = InAppBillingExtension.context;
            mContext.controller().purchaseActivity = this;
            FREUtils.log(TAG, String.format("launchPurchaseFlow( ..., %s, %s, %d, ..., %s )", productId, productType, Integer.valueOf(PURCHASE_REQUEST_CODE), payload), new Object[0]);
            mContext.controller().billingHelper.flagEndAsync();
            mContext.controller().billingHelper.launchPurchaseFlow(this, productId, productType, PURCHASE_REQUEST_CODE, mContext.controller().purchaseListener, payload);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override // android.app.Activity
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        FREUtils.log(TAG, String.format("onActivityResult(%d,%d,...)", Integer.valueOf(requestCode), Integer.valueOf(resultCode)), new Object[0]);
        super.onActivityResult(requestCode, resultCode, data);
        try {
            InAppBillingContext mContext = InAppBillingExtension.context;
            mContext.controller().billingHelper.handleActivityResult(requestCode, resultCode, data);
        } catch (Exception e) {
        }
        if (InAppBillingExtension.context != null) {
            InAppBillingExtension.context.controller().finishActivePurchaseActivity();
        }
    }
}
