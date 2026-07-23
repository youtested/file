package com.android.vending.billing.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: /home/codespace/chem/Chlab_rebuilt.apk_decompiled/build/apk/classes.dex */
public class Inventory {
    Map<String, SkuDetails> mSkuMap = new HashMap();
    Map<String, Purchase> mPurchaseMap = new HashMap();

    public SkuDetails getSkuDetails(String sku) {
        return this.mSkuMap.get(sku);
    }

    public Purchase getPurchase(String sku) {
        return this.mPurchaseMap.get(sku);
    }

    public boolean hasPurchase(String sku) {
        return this.mPurchaseMap.containsKey(sku);
    }

    public boolean hasDetails(String sku) {
        return this.mSkuMap.containsKey(sku);
    }

    public List<String> getAllSkus() {
        return new ArrayList(this.mSkuMap.keySet());
    }

    public void erasePurchase(String sku) {
        if (this.mPurchaseMap.containsKey(sku)) {
            this.mPurchaseMap.remove(sku);
        }
    }

    List<String> getAllOwnedSkus() {
        return new ArrayList(this.mPurchaseMap.keySet());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<String> getAllOwnedSkus(String itemType) {
        List<String> result = new ArrayList<>();
        for (Purchase p : this.mPurchaseMap.values()) {
            if (p.getItemType().equals(itemType)) {
                result.add(p.getSku());
            }
        }
        return result;
    }

    List<Purchase> getAllPurchases() {
        return new ArrayList(this.mPurchaseMap.values());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addSkuDetails(SkuDetails d) {
        this.mSkuMap.put(d.getSku(), d);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addPurchase(Purchase p) {
        this.mPurchaseMap.put(p.getSku(), p);
    }
}
