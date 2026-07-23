package com.distriqt.extension.inappbilling.listeners;

import com.android.vending.billing.util.IabHelper;
import com.android.vending.billing.util.IabResult;
import com.android.vending.billing.util.Inventory;
import com.android.vending.billing.util.Purchase;
import com.distriqt.extension.inappbilling.events.InAppBillingEvent;
import com.distriqt.extension.inappbilling.events.PurchaseEvent;
import com.distriqt.extension.inappbilling.util.FREUtils;
import com.distriqt.extension.inappbilling.util.IEventDispatcher;

/* loaded from: /home/codespace/chem/Chlab_rebuilt.apk_decompiled/build/apk/classes.dex */
public class ConsumePurchaseListener implements IabHelper.QueryInventoryFinishedListener {
    private static String TAG = ConsumePurchaseListener.class.getSimpleName();
    public String productId;
    public IabHelper billingHelper = null;
    private IEventDispatcher _dispatcher = null;

    public IEventDispatcher getDispatcher() {
        return this._dispatcher;
    }

    public void setDispatcher(IEventDispatcher dispatcher) {
        this._dispatcher = dispatcher;
    }

    @Override // com.android.vending.billing.util.IabHelper.QueryInventoryFinishedListener
    public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
        FREUtils.log(TAG, "onQueryInventoryFinished()", new Object[0]);
        if (this._dispatcher != null) {
            if (result.isSuccess()) {
                Purchase purchase = inventory.getPurchase(this.productId);
                FREUtils.log(TAG, "onQueryInventoryFinished(): success:: found valid purchase = " + (purchase != null), new Object[0]);
                if (purchase != null) {
                    try {
                        this.billingHelper.consumeAsync(purchase, new IabHelper.OnConsumeFinishedListener() { // from class: com.distriqt.extension.inappbilling.listeners.ConsumePurchaseListener.1
                            @Override // com.android.vending.billing.util.IabHelper.OnConsumeFinishedListener
                            public void onConsumeFinished(Purchase purchase2, IabResult result2) {
                                if (ConsumePurchaseListener.this._dispatcher != null) {
                                    if (result2.isSuccess()) {
                                        ConsumePurchaseListener.this._dispatcher.dispatchEvent(InAppBillingEvent.CONSUME_SUCCESS, PurchaseEvent.formatPurchaseForEvent(purchase2));
                                    } else {
                                        ConsumePurchaseListener.this._dispatcher.dispatchEvent(InAppBillingEvent.CONSUME_FAILED, PurchaseEvent.formatErrorForEvent(result2.getResponse(), result2.getMessage()));
                                    }
                                }
                            }
                        });
                        return;
                    } catch (IllegalStateException e) {
                        this._dispatcher.dispatchEvent(InAppBillingEvent.CONSUME_FAILED, PurchaseEvent.formatErrorForEvent(result.getResponse(), e.getLocalizedMessage()));
                        return;
                    }
                }
                FREUtils.log(TAG, "onQueryInventoryFinished(): Purchase could not be located, consume has failed", new Object[0]);
                this._dispatcher.dispatchEvent(InAppBillingEvent.CONSUME_FAILED, PurchaseEvent.formatErrorForEvent(result.getResponse(), result.getMessage()));
                return;
            }
            this._dispatcher.dispatchEvent(InAppBillingEvent.CONSUME_FAILED, PurchaseEvent.formatErrorForEvent(result.getResponse(), result.getMessage()));
        }
    }
}
