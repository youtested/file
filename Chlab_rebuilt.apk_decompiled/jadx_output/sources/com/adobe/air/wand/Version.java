package com.adobe.air.wand;

/* loaded from: /home/codespace/chem/Chlab_rebuilt.apk_decompiled/build/apk/classes.dex */
public class Version {
    public static final String CURRENT = "1.1.0";
    public static final String V1_0_0 = "1.0.0";
    public static final String V1_1_0 = "1.1.0";

    public static boolean isGreaterThan(String str, String str2) throws Exception {
        String[] split = str.split("\\.");
        String[] split2 = str2.split("\\.");
        int i = 0;
        while (i < 2 && split[i].equals(split2[i])) {
            i++;
        }
        return Integer.valueOf(split[i]).intValue() > Integer.valueOf(split2[i]).intValue();
    }

    public static boolean isGreaterThanEqualTo(String str, String str2) throws Exception {
        return isGreaterThan(str, str2) || str.equals(str2);
    }
}
