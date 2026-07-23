package com.distriqt.extension.inappbilling.helpers;

import android.util.Log;
import com.android.vending.billing.util.SkuDetails;
import com.distriqt.extension.inappbilling.BuildConfig;
import com.distriqt.extension.inappbilling.Purchase;
import com.distriqt.extension.inappbilling.util.FREUtils;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;
import org.json.JSONObject;

/* loaded from: /home/codespace/chem/Chlab_rebuilt.apk_decompiled/build/apk/classes.dex */
public class ResponseHelper {
    private static String TAG = ResponseHelper.class.getSimpleName();
    public static String STATE_UNKNOWN = Purchase.STATE_UNKNOWN;
    public static String STATE_PURCHASED = Purchase.STATE_PURCHASED;
    public static String STATE_FAILED = Purchase.STATE_FAILED;
    public static String STATE_REFUNDED = Purchase.STATE_REFUNDED;
    public static String STATE_CANCELLED = Purchase.STATE_CANCELLED;
    public static String STATE_RESTORED = Purchase.STATE_RESTORED;
    public static String RESPONSE_OK = "response:ok";
    public static String RESPONSE_USER_CANCELLED = "response:user:cancelled";
    public static String RESPONSE_UNKNOWN = "response:unknown";
    public static String RESPONSE_BILLING_UNAVAILABLE = "response:billing:unavailable";
    public static String RESPONSE_ITEM_UNAVAILABLE = "response:item:unavailable";
    public static String RESPONSE_DEVELOPER_ERROR = "response:developer:error";
    public static String RESPONSE_ERROR = "response:error";
    public static String RESPONSE_ITEM_ALREADY_OWNED = "response:item:already:owned";
    public static String RESPONSE_ITEM_NOT_OWNED = "response:item:not:owned";

    public static JSONObject encodeProduct(String productId, SkuDetails details) {
        FREUtils.log(TAG, "encodeProduct( %s ) : %s ", productId, details.toString());
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("id", productId);
            jsonObj.put("title", details.getTitle());
            jsonObj.put("description", details.getDescription());
            jsonObj.put("type", details.getType());
            jsonObj.put("price", Double.parseDouble(details.getPrice()) / 1000000.0d);
            jsonObj.put("priceString", details.getPriceString());
            jsonObj.put("locale", BuildConfig.FLAVOR);
            jsonObj.put("countryCode", BuildConfig.FLAVOR);
            jsonObj.put("currencySymbol", details.getPriceString().replaceAll("[\\d.,]+", BuildConfig.FLAVOR));
            jsonObj.put("internationalCurrencySymbol", BuildConfig.FLAVOR);
            jsonObj.put("currencyCode", details.getCurrencyCode());
            jsonObj.put("itemType", details.getItemType());
            jsonObj.put("source", details.getJson());
            return jsonObj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String encodeProductAsXml(String productId, SkuDetails details) {
        FREUtils.log(TAG, "encodeProductAsXml( " + productId + " )", new Object[0]);
        String priceString = details.getPrice();
        Number price = 0;
        String currency = BuildConfig.FLAVOR;
        try {
            currency = priceString.replaceAll("[\\d.,]+", BuildConfig.FLAVOR);
            price = NumberFormat.getInstance().parse(priceString.replaceAll("[^\\d.]+", BuildConfig.FLAVOR));
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        String productXML = (((((((((("<product><id>" + details.getSku() + "</id>") + "<title>" + details.getTitle() + "</title>") + "<description>" + details.getDescription() + "</description>") + "<currencySymbol>" + currency + "</currencySymbol>") + "<price>" + price.toString() + "</price>") + "<priceString>" + priceString + "</priceString>") + "<locale></locale>") + "<type>" + details.getType() + "</type>") + "<itemType>" + details.getItemType() + "</itemType>") + "<source><![CDATA[" + details.getJson() + "]]></source>") + "</product>";
        FREUtils.log(TAG, productXML, new Object[0]);
        return productXML;
    }

    public static JSONObject encodePurchase(String productId, com.android.vending.billing.util.Purchase purchase) {
        return encodePurchase(productId, purchase, false);
    }

    public static JSONObject encodePurchase(String productId, com.android.vending.billing.util.Purchase purchase, boolean restored) {
        try {
            if (purchase == null) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("productId", productId);
                jsonObj.put("error", "Invalid Purchase");
                jsonObj.put("errorCode", "-1");
                return jsonObj;
            }
            FREUtils.log(TAG, String.format("encodePurchase( %s )", purchase.getSku()), new Object[0]);
            JSONObject jsonObj2 = purchaseToObject(purchase);
            if (restored) {
                jsonObj2.put("transactionState", STATE_RESTORED);
                jsonObj2.put("originalTransaction", purchaseToObject(purchase));
            }
            jsonObj2.put("error", BuildConfig.FLAVOR);
            jsonObj2.put("errorCode", BuildConfig.FLAVOR);
            return jsonObj2;
        } catch (Exception e) {
            FREUtils.handleException(null, e);
            return null;
        }
    }

    public static JSONObject purchaseToObject(com.android.vending.billing.util.Purchase purchase) throws Exception {
        JSONObject jsonObj = new JSONObject();
        String transactionState = getPurchaseState(purchase.getPurchaseState());
        jsonObj.put("productId", purchase.getSku());
        jsonObj.put("quantity", 1);
        jsonObj.put("transactionDate", purchase.getPurchaseTime());
        jsonObj.put("transactionIdentifier", purchase.getToken());
        jsonObj.put("transactionState", transactionState);
        jsonObj.put("transactionReceipt", purchase.getOrderId());
        jsonObj.put("developerPayload", purchase.getDeveloperPayload());
        jsonObj.put("signature", purchase.getSignature());
        jsonObj.put("originalMessage", URLEncoder.encode(purchase.getOriginalJson(), "UTF-8"));
        return jsonObj;
    }

    public static String encodePurchaseAsXml(com.android.vending.billing.util.Purchase purchase) {
        if (purchase == null) {
            Log.d(TAG, "Invalid purchase");
            return "<transaction><error>Invalid Purchase|-1</error></transaction>";
        }
        Log.d(TAG, "encodePurchase( " + purchase.getSku() + " )");
        String transactionState = getPurchaseState(purchase.getPurchaseState());
        Date date = new Date(purchase.getPurchaseTime());
        Log.d(TAG, "encodePurchase( " + purchase.getSku() + " ) [" + purchase.getPurchaseState() + "] ::" + transactionState);
        String purchaseXML = "<transaction><productId>" + purchase.getSku() + "</productId>";
        return (((((((((purchaseXML + "<quantity>1</quantity>") + "<transactionDate>" + String.format(Locale.US, "%d", Long.valueOf(date.getTime())) + "</transactionDate>") + "<transactionIdentifier>" + purchase.getToken() + "</transactionIdentifier>") + "<transactionState>" + transactionState + "</transactionState>") + "<transactionReceipt>" + purchase.getOrderId() + "</transactionReceipt>") + "<developerPayload>" + purchase.getDeveloperPayload() + "</developerPayload>") + "<signature>" + purchase.getSignature() + "</signature>") + "<originalMessage><![CDATA[" + purchase.getOriginalJson() + "]]></originalMessage>") + "<error></error>") + "</transaction>";
    }

    public static String getResponseState(int code) {
        String responseState = RESPONSE_UNKNOWN;
        switch (code) {
            case 0:
                String responseState2 = RESPONSE_OK;
                return responseState2;
            case 1:
                String responseState3 = RESPONSE_USER_CANCELLED;
                return responseState3;
            case 2:
                String responseState4 = RESPONSE_UNKNOWN;
                return responseState4;
            case 3:
                String responseState5 = RESPONSE_BILLING_UNAVAILABLE;
                return responseState5;
            case 4:
                String responseState6 = RESPONSE_ITEM_UNAVAILABLE;
                return responseState6;
            case 5:
                String responseState7 = RESPONSE_DEVELOPER_ERROR;
                return responseState7;
            case 6:
                String responseState8 = RESPONSE_ERROR;
                return responseState8;
            case 7:
                String responseState9 = RESPONSE_ITEM_ALREADY_OWNED;
                return responseState9;
            case 8:
                String responseState10 = RESPONSE_ITEM_NOT_OWNED;
                return responseState10;
            default:
                return responseState;
        }
    }

    public static String getPurchaseState(int code) {
        String transactionState = STATE_UNKNOWN;
        switch (code) {
            case 0:
                String transactionState2 = STATE_PURCHASED;
                return transactionState2;
            case 1:
                String transactionState3 = STATE_CANCELLED;
                return transactionState3;
            case 2:
                String transactionState4 = STATE_REFUNDED;
                return transactionState4;
            default:
                return transactionState;
        }
    }
}
