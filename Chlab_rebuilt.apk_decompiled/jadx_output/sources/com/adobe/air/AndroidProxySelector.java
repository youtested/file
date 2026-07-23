package com.adobe.air;

import com.distriqt.extension.inappbilling.BuildConfig;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/* loaded from: /home/codespace/chem/Chlab_rebuilt.apk_decompiled/build/apk/classes.dex */
public final class AndroidProxySelector {
    private static final int LIST_HEAD = 0;
    private static final String LOG_TAG = "AndroidProxySelector";

    public static String getProxyUrl(String str) {
        List<Proxy> select;
        try {
            select = ProxySelector.getDefault().select(new URI(str));
        } catch (IllegalArgumentException e) {
        } catch (IndexOutOfBoundsException e2) {
        } catch (URISyntaxException e3) {
        }
        if (select.isEmpty()) {
            return BuildConfig.FLAVOR;
        }
        Proxy proxy = select.get(0);
        if (Proxy.NO_PROXY != proxy && Proxy.Type.HTTP == proxy.type()) {
            return proxy.address().toString();
        }
        return BuildConfig.FLAVOR;
    }
}
