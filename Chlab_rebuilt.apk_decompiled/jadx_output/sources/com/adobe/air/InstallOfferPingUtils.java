package com.adobe.air;

import android.app.Activity;
import android.app.UiModeManager;
import android.os.AsyncTask;
import android.os.Build;
import com.distriqt.extension.inappbilling.BuildConfig;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Locale;

/* loaded from: /home/codespace/chem/Chlab_rebuilt.apk_decompiled/build/apk/classes.dex */
public class InstallOfferPingUtils {
    private static final String LOG_TAG = "InstallOfferPingUtils";

    private static boolean isAndroidTV(Activity activity) {
        try {
            UiModeManager uiModeManager = (UiModeManager) activity.getSystemService("uimode");
            return uiModeManager.getCurrentModeType() == 4;
        } catch (Exception e) {
            return false;
        }
    }

    public static void PingAndExit(Activity activity, String baseUrl, boolean installClicked, boolean update, final boolean exit) {
        String urlQueryStr;
        String urlQueryStr2;
        try {
            if (update) {
                urlQueryStr = BuildConfig.FLAVOR + "installOffer=" + (installClicked ? "ua" : "ur");
            } else {
                urlQueryStr = BuildConfig.FLAVOR + "installOffer=" + (installClicked ? "a" : "r");
            }
            String urlQueryStr3 = (((urlQueryStr + "&appid=" + activity.getPackageName()) + "&runtimeType=s") + "&lang=" + Locale.getDefault().getLanguage()) + "&model=" + Build.MODEL;
            if (isAndroidTV(activity)) {
                urlQueryStr2 = urlQueryStr3 + "&os=atv";
            } else {
                urlQueryStr2 = urlQueryStr3 + "&os=a";
            }
            String urlStr = baseUrl + URLEncoder.encode((urlQueryStr2 + "&osVer=" + Build.VERSION.RELEASE) + "&arch=" + System.getProperty("os.arch"), "UTF-8");
            AsyncTask<String, Integer, Integer> PingAndExitTask = new AsyncTask<String, Integer, Integer>() { // from class: com.adobe.air.InstallOfferPingUtils.1
                /* JADX INFO: Access modifiers changed from: protected */
                @Override // android.os.AsyncTask
                public Integer doInBackground(String... urls) {
                    int resCode = 0;
                    try {
                        URL url = new URL(urls[0]);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setConnectTimeout(10000);
                        conn.setRequestMethod("GET");
                        HttpURLConnection.setFollowRedirects(true);
                        resCode = conn.getResponseCode();
                        if (exit) {
                            System.exit(0);
                        }
                    } catch (Exception e) {
                    }
                    return Integer.valueOf(resCode);
                }
            };
            PingAndExitTask.execute(urlStr);
            activity.finish();
        } catch (Exception e) {
        }
    }
}
