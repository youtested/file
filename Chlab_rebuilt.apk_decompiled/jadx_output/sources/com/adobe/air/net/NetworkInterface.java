package com.adobe.air.net;

import com.distriqt.extension.inappbilling.BuildConfig;
import java.util.Vector;

/* loaded from: /home/codespace/chem/Chlab_rebuilt.apk_decompiled/build/apk/classes.dex */
public class NetworkInterface {
    public boolean active = false;
    public String displayName = BuildConfig.FLAVOR;
    public String hardwareAddress = BuildConfig.FLAVOR;
    public int mtu = -1;
    public String name = BuildConfig.FLAVOR;
    public NetworkInterface parent = null;
    public NetworkInterface subInterfaces = null;
    private Vector<InterfaceAddress> addresses = new Vector<>();

    public void addAddress(InterfaceAddress interfaceAddress) {
        this.addresses.add(interfaceAddress);
    }

    public int GetAddressesCount() {
        return this.addresses.size();
    }

    public InterfaceAddress GetAddress(int i) {
        return this.addresses.elementAt(i);
    }
}
