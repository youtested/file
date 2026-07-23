package com.adobe.air.utils;

import com.distriqt.extension.inappbilling.BuildConfig;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: /home/codespace/chem/Chlab_rebuilt.apk_decompiled/build/apk/classes.dex */
public class DeviceInfo {
    static String getHardwareInfo() {
        byte[] bArr;
        InputStream inputStream;
        int read;
        String str;
        int indexOf;
        int indexOf2;
        try {
            bArr = new byte[1024];
            inputStream = new ProcessBuilder("/system/bin/cat", "/proc/cpuinfo").start().getInputStream();
            read = inputStream.read(bArr, 0, 1024);
        } catch (IOException e) {
        }
        if (read >= 0 && (indexOf = (str = new String(bArr, 0, read)).indexOf("Hardware")) >= 0 && (indexOf2 = str.indexOf(58, indexOf)) >= 0) {
            return str.substring(indexOf2 + 1, str.indexOf(10, indexOf2 + 1)).trim();
        }
        inputStream.close();
        return new String(BuildConfig.FLAVOR);
    }

    static String getTotalMemory() {
        byte[] bArr;
        InputStream inputStream;
        int read;
        String str;
        int indexOf;
        int indexOf2;
        try {
            bArr = new byte[1024];
            inputStream = new ProcessBuilder("/system/bin/cat", "/proc/meminfo").start().getInputStream();
            read = inputStream.read(bArr, 0, 1024);
        } catch (IOException e) {
        }
        if (read >= 0 && (indexOf = (str = new String(bArr, 0, read)).indexOf("MemTotal")) >= 0 && (indexOf2 = str.indexOf(58, indexOf)) >= 0) {
            return str.substring(indexOf2 + 1, str.indexOf(10, indexOf2 + 1)).trim();
        }
        inputStream.close();
        return new String(BuildConfig.FLAVOR);
    }

    static String getCPUCount() {
        byte[] bArr;
        InputStream inputStream;
        int read;
        try {
            bArr = new byte[1024];
            inputStream = new ProcessBuilder("/system/bin/cat", "/sys/devices/system/cpu/present").start().getInputStream();
            read = inputStream.read(bArr, 0, 1024);
        } catch (IOException e) {
        }
        if (read >= 0) {
            String str = new String(bArr, 0, read);
            int indexOf = str.indexOf("-");
            if (indexOf >= 0) {
                return Integer.toString(Integer.parseInt(str.substring(indexOf + 1, indexOf + 2)) + 1);
            }
            return Integer.toString(Integer.parseInt(str.substring(0, 1)) + 1);
        }
        inputStream.close();
        return new String(BuildConfig.FLAVOR);
    }
}
