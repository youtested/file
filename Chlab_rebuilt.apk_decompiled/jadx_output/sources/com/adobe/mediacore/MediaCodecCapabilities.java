package com.adobe.mediacore;

import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.os.Build;
import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/* loaded from: /home/codespace/chem/Chlab_rebuilt.apk_decompiled/build/apk/classes.dex */
public class MediaCodecCapabilities {
    private static final String TAG = "MediaCodecCapabilities(j)";
    private static ArrayList<MediaCodecInfo> codecInfoList = new ArrayList<>();
    private static Map<String, MediaCodecInfo.CodecCapabilities> capabilitiesMap = new HashMap();

    public static void PreloadCodecInfoList(String str) {
        if (Build.VERSION.SDK_INT >= 19) {
            Log.w(TAG, "PreloadCodecInfoList for mime" + str);
            if (codecInfoList.isEmpty()) {
                getCodecInfos(str);
            }
            if (codecInfoList.isEmpty()) {
                Log.w(TAG, "no codeInfo for mime " + str);
                return;
            }
            int i = 0;
            while (true) {
                int i2 = i;
                if (i2 < codecInfoList.size()) {
                    MediaCodecInfo mediaCodecInfo = codecInfoList.get(i2);
                    try {
                        capabilitiesMap.put(mediaCodecInfo.getName(), mediaCodecInfo.getCapabilitiesForType(str));
                    } catch (Exception e) {
                        Log.w(TAG, "caught exception");
                    }
                    i = i2 + 1;
                } else {
                    return;
                }
            }
        }
    }

    public static boolean IsProfileLevelSupported(String str, int i, int i2) {
        boolean z = false;
        if (codecInfoList.isEmpty()) {
            getCodecInfos(str);
        }
        if (codecInfoList.isEmpty()) {
            Log.w(TAG, "no codeInfo for mime " + str);
        } else {
            if (capabilitiesMap.keySet().isEmpty()) {
                boolean z2 = false;
                for (int i3 = 0; i3 < codecInfoList.size(); i3++) {
                    MediaCodecInfo mediaCodecInfo = codecInfoList.get(i3);
                    MediaCodecInfo.CodecCapabilities codecCapabilities = null;
                    try {
                        codecCapabilities = mediaCodecInfo.getCapabilitiesForType(new String("video/avc"));
                    } catch (Exception e) {
                        Log.w(TAG, "caught exception");
                    }
                    if (codecCapabilities != null && isProfileLevelSupportedByCapacities(codecCapabilities, i, i2, mediaCodecInfo.getName())) {
                        z2 = true;
                    }
                }
                z = z2;
            } else {
                boolean z3 = false;
                for (String str2 : capabilitiesMap.keySet()) {
                    MediaCodecInfo.CodecCapabilities codecCapabilities2 = capabilitiesMap.get(str2);
                    if (codecCapabilities2 != null) {
                        z3 = isProfileLevelSupportedByCapacities(codecCapabilities2, i, i2, str2) ? true : z3;
                    }
                }
                z = z3;
            }
            if (!z) {
                Log.w(TAG, "profile " + i + " and level " + i2 + "not supported");
            }
        }
        return z;
    }

    public static boolean IsABRSupportedByMediaCodec(String str) {
        if (Build.VERSION.SDK_INT < 19) {
            return false;
        }
        if (!codecInfoList.isEmpty() && !capabilitiesMap.keySet().isEmpty() && str.equalsIgnoreCase("video/avc")) {
            for (int i = 0; i < codecInfoList.size(); i++) {
                MediaCodecInfo mediaCodecInfo = codecInfoList.get(i);
                MediaCodecInfo.CodecCapabilities codecCapabilities = capabilitiesMap.get(mediaCodecInfo.getName());
                if (codecCapabilities != null) {
                    boolean isFeatureSupported = codecCapabilities.isFeatureSupported("adaptive-playback");
                    Log.w(TAG, "IsCodecSupported (video/avc) : info = " + mediaCodecInfo.getName() + "AdaptivePlayback supported = " + isFeatureSupported);
                    if (isFeatureSupported) {
                        return true;
                    }
                }
            }
            return false;
        }
        int codecCount = MediaCodecList.getCodecCount();
        for (int i2 = 0; i2 < codecCount; i2++) {
            MediaCodecInfo codecInfoAt = MediaCodecList.getCodecInfoAt(i2);
            if (!codecInfoAt.isEncoder()) {
                String[] supportedTypes = codecInfoAt.getSupportedTypes();
                for (int i3 = 0; i3 < supportedTypes.length; i3++) {
                    if (supportedTypes[i3].equalsIgnoreCase(str)) {
                        boolean isFeatureSupported2 = codecInfoAt.getCapabilitiesForType(supportedTypes[i3]).isFeatureSupported("adaptive-playback");
                        Log.w(TAG, "IsCodecSupported (video/avc) : info = " + codecInfoAt.getName() + "AdaptivePlayback supported = " + isFeatureSupported2);
                        if (isFeatureSupported2) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public static void PrintCodecList() {
        int codecCount = MediaCodecList.getCodecCount();
        Log.w(TAG, "PrintCodecList ---------");
        for (int i = 0; i < codecCount; i++) {
            MediaCodecInfo codecInfoAt = MediaCodecList.getCodecInfoAt(i);
            if (!codecInfoAt.isEncoder()) {
                Log.w(TAG, "    Name = [" + codecInfoAt.getName() + "]");
                String[] supportedTypes = codecInfoAt.getSupportedTypes();
                for (int i2 = 0; i2 < supportedTypes.length; i2++) {
                    Log.w(TAG, "        SupportedType[" + i2 + "] = [" + supportedTypes[i2] + "]");
                }
            }
        }
        Log.w(TAG, "-------------------------------");
    }

    public static boolean IsCodecSupported(String str) {
        int codecCount = MediaCodecList.getCodecCount();
        for (int i = 0; i < codecCount; i++) {
            MediaCodecInfo codecInfoAt = MediaCodecList.getCodecInfoAt(i);
            if (!codecInfoAt.isEncoder()) {
                for (String str2 : codecInfoAt.getSupportedTypes()) {
                    if (str2.equalsIgnoreCase(str)) {
                        Log.w(TAG, "IsCodecSupported (" + str + ") : info = " + codecInfoAt.getName() + " : yes, it's supported");
                        return true;
                    }
                }
            }
        }
        Log.w(TAG, "IsCodecSupported (" + str + ") : no, it's not supported");
        return false;
    }

    private static void getCodecInfos(String str) {
        int codecCount = MediaCodecList.getCodecCount();
        for (int i = 0; i < codecCount; i++) {
            MediaCodecInfo codecInfoAt = MediaCodecList.getCodecInfoAt(i);
            if (!codecInfoAt.isEncoder()) {
                for (String str2 : codecInfoAt.getSupportedTypes()) {
                    if (str2.equalsIgnoreCase(str)) {
                        codecInfoList.add(codecInfoAt);
                        Log.w(TAG, "info = " + codecInfoAt.getName());
                    }
                }
            }
        }
    }

    private static boolean isProfileLevelSupportedByCapacities(MediaCodecInfo.CodecCapabilities codecCapabilities, int i, int i2, String str) {
        StringBuilder sb = new StringBuilder();
        boolean z = false;
        for (int i3 = 0; i3 < codecCapabilities.profileLevels.length; i3++) {
            if (i3 > 0) {
                sb.append(", ");
            }
            sb.append(codecCapabilities.profileLevels[i3].profile + " / " + codecCapabilities.profileLevels[i3].level);
            if (codecCapabilities.profileLevels[i3].profile >= i && codecCapabilities.profileLevels[i3].level >= i2) {
                Log.w(TAG, "IsProfileLevelSupported suppported: caps.profileLevels[k].profile: " + codecCapabilities.profileLevels[i3].profile + " >= profile: " + i + " and caps.profileLevels[k].level: " + codecCapabilities.profileLevels[i3].level + " >= level: " + i2);
                z = true;
            }
        }
        Log.d(TAG, "codecName " + str + "    profiles/levels: " + ((Object) sb));
        return z;
    }
}
