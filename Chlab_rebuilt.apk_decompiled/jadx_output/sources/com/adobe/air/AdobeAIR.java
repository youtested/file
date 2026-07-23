package com.adobe.air;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.webkit.WebBackForwardList;

/* loaded from: /home/codespace/chem/Chlab_rebuilt.apk_decompiled/build/apk/classes.dex */
public class AdobeAIR extends Activity {
    private static final String PROPERTY_INITIAL_LAUNCH = "initialLaunch";
    private static final String TAG = "Adobe AIR";
    private AdobeAIRWebView mGameListingWebView = null;
    public String DYNAMIC_URL = "https://www.adobe.com/airgames/5/";
    private Context mCtx = null;

    @Override // android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (isWidgetShown()) {
            finish();
        }
        this.mCtx = this;
        if (isInitialLaunch()) {
            this.DYNAMIC_URL = AdobeAIRWebView.DYNAMIC_URL_CLOUDFRONT;
            updateSharedPrefForInitialLaunch();
        }
        this.mGameListingWebView = new AdobeAIRWebView(this.mCtx);
        this.mGameListingWebView.create();
        this.mGameListingWebView.loadUrl(this.DYNAMIC_URL);
        onNewIntent(getIntent());
    }

    private boolean isInitialLaunch() {
        return PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean(PROPERTY_INITIAL_LAUNCH, true);
    }

    private void updateSharedPrefForInitialLaunch() {
        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
        edit.putBoolean(PROPERTY_INITIAL_LAUNCH, false);
        edit.commit();
    }

    @Override // android.app.Activity
    public void onResume() {
        super.onResume();
        if (isWidgetShown()) {
            finish();
        }
        if (this.mGameListingWebView.isOffline()) {
            this.mGameListingWebView.setOffline(false);
            this.mGameListingWebView.loadUrl(this.DYNAMIC_URL);
        }
    }

    @Override // android.app.Activity
    public void onBackPressed() {
        if (Build.VERSION.SDK_INT >= 12 && this.mGameListingWebView.canGoBack()) {
            boolean z = true;
            WebBackForwardList copyBackForwardList = this.mGameListingWebView.copyBackForwardList();
            int currentIndex = copyBackForwardList.getCurrentIndex();
            if (currentIndex > 0) {
                if (this.DYNAMIC_URL.equals(copyBackForwardList.getItemAtIndex(currentIndex - 1).getUrl())) {
                    z = false;
                }
            }
            if (z) {
                this.mGameListingWebView.goBack();
                return;
            }
        }
        super.onBackPressed();
    }

    private boolean isWidgetShown() {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return defaultSharedPreferences.getBoolean("widgetShown", false) || defaultSharedPreferences.getBoolean("featuredWidgetShown", false);
    }
}
