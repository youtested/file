package com.distriqt.extension.inappbilling;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import com.adobe.air.wand.view.CompanionView;
import com.android.vending.billing.util.IabHelper;
import com.android.vending.billing.util.IabResult;
import com.android.vending.billing.util.SkuDetails;
import com.distriqt.extension.inappbilling.activities.ProductViewActivity;
import com.distriqt.extension.inappbilling.activities.PurchaseActivity;
import com.distriqt.extension.inappbilling.events.InAppBillingEvent;
import com.distriqt.extension.inappbilling.events.ProductViewEvent;
import com.distriqt.extension.inappbilling.listeners.ConsumePurchaseListener;
import com.distriqt.extension.inappbilling.listeners.PurchaseListener;
import com.distriqt.extension.inappbilling.listeners.QueryInventoryListener;
import com.distriqt.extension.inappbilling.util.FREUtils;
import com.distriqt.extension.inappbilling.util.IEventDispatcher;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: /home/codespace/chem/Chlab_rebuilt.apk_decompiled/build/apk/classes.dex */
public class InAppBillingController {
    public static final String TAG = InAppBillingController.class.getSimpleName();
    private Activity _activity;
    private IEventDispatcher _dispatcher;
    private ArrayList<String> _productIds;
    public Boolean setupComplete = false;
    public IabHelper billingHelper = null;
    public PurchaseActivity purchaseActivity = null;
    public QueryInventoryListener queryInventoryListener = null;
    public PurchaseListener purchaseListener = null;

    public InAppBillingController(IEventDispatcher dispatcher, Activity activity) {
        this._productIds = null;
        this._activity = activity;
        this._dispatcher = dispatcher;
        this._productIds = new ArrayList<>();
    }

    public Boolean isSupported() {
        return true;
    }

    public Boolean canMakePayments() {
        return true;
    }

    public Boolean setup(String billingPublicKey) {
        if (billingPublicKey == null || billingPublicKey.length() == 0) {
            this._dispatcher.dispatchEvent(InAppBillingEvent.SETUP_FAILURE, "Invalid Key");
            return false;
        }
        if (this.billingHelper == null) {
            this.billingHelper = new IabHelper(this._activity.getApplicationContext(), billingPublicKey);
            this.billingHelper.enableDebugLogging(true);
            this.billingHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() { // from class: com.distriqt.extension.inappbilling.InAppBillingController.1
                @Override // com.android.vending.billing.util.IabHelper.OnIabSetupFinishedListener
                public void onIabSetupFinished(IabResult result) {
                    if (result.isSuccess()) {
                        InAppBillingController.this.setupComplete = true;
                        InAppBillingController.this._dispatcher.dispatchEvent(InAppBillingEvent.SETUP_SUCCESS, BuildConfig.FLAVOR);
                    } else {
                        InAppBillingController.this.setupComplete = false;
                        InAppBillingController.this._dispatcher.dispatchEvent(InAppBillingEvent.SETUP_FAILURE, InAppBillingEvent.formatErrorForEvent(result.getResponse(), result.getMessage()));
                    }
                }
            });
        }
        return true;
    }

    public void cleanup() {
        this.setupComplete = false;
        if (this.billingHelper != null) {
            this.billingHelper.dispose();
        }
        this.billingHelper = null;
    }

    public boolean getProducts(List<String> productIds, Boolean clearProducts) {
        if (clearProducts.booleanValue()) {
            this._productIds.clear();
        }
        for (String productId : productIds) {
            if (!this._productIds.contains(productId)) {
                this._productIds.add(productId);
            }
        }
        return getProducts(this._productIds, QueryInventoryListener.QUERY_GET_PRODUCTS);
    }

    public Map<String, SkuDetails> activeProducts() {
        return this.queryInventoryListener != null ? this.queryInventoryListener.getSkuMap() : new HashMap();
    }

    public ArrayList<String> activeProductIds() {
        return this._productIds;
    }

    public boolean restorePurchases() {
        if (!this.setupComplete.booleanValue() || this.queryInventoryListener == null || this.queryInventoryListener.getProductIds() == null) {
            return false;
        }
        return getProducts(this.queryInventoryListener.getProductIds(), QueryInventoryListener.QUERY_RESTORE_PURCHASES);
    }

    private boolean getProducts(List<String> productIds, String query) {
        if (!this.setupComplete.booleanValue()) {
            return false;
        }
        FREUtils.log(TAG, String.format("getProducts( %s )", productIds.toString()), new Object[0]);
        if (this.queryInventoryListener == null) {
            this.queryInventoryListener = new QueryInventoryListener();
            this.queryInventoryListener.setDispatcher(this._dispatcher);
        }
        this.queryInventoryListener.setProductIds(productIds);
        this.queryInventoryListener.query = query;
        this.billingHelper.queryInventoryAsync(true, productIds, this.queryInventoryListener);
        return true;
    }

    public boolean isProductViewSupported() {
        PackageManager pm = this._activity.getPackageManager();
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setData(Uri.parse("market://details?id="));
        return pm.queryIntentActivities(intent, 0).size() > 0;
    }

    public boolean showProductView(String productId) {
        if (!isProductViewSupported()) {
            return false;
        }
        Intent productViewIntent = new Intent(this._activity.getApplicationContext(), (Class<?>) ProductViewActivity.class);
        productViewIntent.putExtra("productId", productId);
        this._activity.startActivity(productViewIntent);
        this._dispatcher.dispatchEvent(ProductViewEvent.LOADED, BuildConfig.FLAVOR);
        return true;
    }

    public boolean makePurchase(PurchaseRequest request) {
        if (!this.setupComplete.booleanValue()) {
            return false;
        }
        FREUtils.log(TAG, "makePurchase( %s )", request.toString());
        if (this.queryInventoryListener == null || this.queryInventoryListener.getProductIds() == null) {
            return false;
        }
        if (this.purchaseListener == null) {
            this.purchaseListener = new PurchaseListener();
            this.purchaseListener.setDispatcher(this._dispatcher);
        }
        String productType = IabHelper.ITEM_TYPE_INAPP;
        SkuDetails details = this.queryInventoryListener.getSkuDetails(request.productId);
        if (details != null) {
            productType = details.getItemType();
        }
        Intent purchaseActivityIntent = new Intent(this._activity.getApplicationContext(), (Class<?>) PurchaseActivity.class);
        purchaseActivityIntent.addFlags(CompanionView.kTouchMetaStateIsEraser);
        purchaseActivityIntent.putExtra("quantity", request.quantity);
        purchaseActivityIntent.putExtra("payload", request.developerPayload);
        purchaseActivityIntent.putExtra("productId", request.productId);
        purchaseActivityIntent.putExtra("productType", productType);
        this._activity.startActivity(purchaseActivityIntent);
        return true;
    }

    public boolean finishPurchase() {
        return true;
    }

    public Boolean consumePurchase(String productId) {
        if (!this.setupComplete.booleanValue()) {
            return false;
        }
        String[] productIds = {productId};
        ConsumePurchaseListener listener = new ConsumePurchaseListener();
        listener.productId = productId;
        listener.billingHelper = this.billingHelper;
        listener.setDispatcher(this._dispatcher);
        this.billingHelper.queryInventoryAsync(true, Arrays.asList(productIds), listener);
        return true;
    }

    public void finishActivePurchaseActivity() {
        if (this.purchaseActivity != null) {
            this.purchaseActivity.finish();
            this.purchaseActivity = null;
        }
    }
}
