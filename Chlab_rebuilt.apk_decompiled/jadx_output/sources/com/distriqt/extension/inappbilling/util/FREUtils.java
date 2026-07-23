package com.distriqt.extension.inappbilling.util;

import android.R;
import android.content.res.Configuration;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import com.adobe.fre.FREArray;
import com.adobe.fre.FREContext;
import com.adobe.fre.FREObject;
import java.util.Locale;

/* loaded from: /home/codespace/chem/Chlab_rebuilt.apk_decompiled/build/apk/classes.dex */
public class FREUtils {
    public static String TAG = FREUtils.class.getSimpleName();
    public static Boolean DEBUG_ENABLED = true;
    public static Boolean DEBUG_OUTPUTS_ENABLED = true;
    public static String ID = "com.distriqt.EXTENSION";
    public static FREContext context = null;

    public static void log(String TAG2, String message, Object... args) {
        if (DEBUG_OUTPUTS_ENABLED.booleanValue()) {
            Log.d(ID, TAG2 + "::" + String.format(Locale.UK, message, args));
        }
    }

    public static void handleException(Exception e) {
        handleException(context, e);
    }

    public static void handleException(FREContext context2, Exception e) {
        if (DEBUG_ENABLED.booleanValue()) {
            e.printStackTrace();
        }
        if (context2 != null) {
            context2.dispatchStatusEventAsync("extension:error", e.getMessage());
        }
    }

    public static String[] GetObjectAsArrayOfStrings(FREArray values) {
        try {
            int length = (int) values.getLength();
            String[] retArray = new String[length];
            for (int i = 0; i < length; i++) {
                FREObject value = values.getObjectAt(i);
                retArray[i] = value.getAsString();
            }
            return retArray;
        } catch (Exception e) {
            return new String[0];
        }
    }

    public static int[] GetObjectAsArrayOfNumbers(FREArray values) {
        try {
            int length = (int) values.getLength();
            int[] retArray = new int[length];
            for (int i = 0; i < length; i++) {
                FREObject value = values.getObjectAt(i);
                retArray[i] = value.getAsInt();
            }
            return retArray;
        } catch (Exception e) {
            return new int[0];
        }
    }

    public static void listViews(ViewGroup v, String prefix) {
        String logPrefix = prefix + v.getClass().getName() + "/";
        for (int i = 0; i < v.getChildCount(); i++) {
            View a = v.getChildAt(i);
            log(TAG, "%s%s", logPrefix, a.getClass().getName());
            if (ViewGroup.class.isAssignableFrom(a.getClass())) {
                listViews((ViewGroup) a, logPrefix);
            }
        }
    }

    public static View findViewByClassName(ViewGroup v, String viewClassName) {
        View b;
        for (int i = 0; i < v.getChildCount(); i++) {
            View a = v.getChildAt(i);
            if (!a.getClass().getName().equals(viewClassName)) {
                if (ViewGroup.class.isAssignableFrom(a.getClass()) && (b = findViewByClassName((ViewGroup) a, viewClassName)) != null) {
                    return b;
                }
            } else {
                return a;
            }
        }
        return null;
    }

    public static View getAIRWindowSurfaceView() {
        if (context == null) {
            return null;
        }
        ViewGroup fl = (ViewGroup) context.getActivity().findViewById(R.id.content);
        return findViewByClassName(fl, "com.adobe.air.AIRWindowSurfaceView");
    }

    public static void addViewToAIR(View v, FrameLayout.LayoutParams params) {
        if (context != null) {
            ViewGroup fl = (ViewGroup) context.getActivity().findViewById(R.id.content);
            fl.addView(v, params);
        }
    }

    public static void moveToBack(View currentView) {
        ViewGroup vg = (ViewGroup) currentView.getParent();
        int index = vg.indexOfChild(currentView);
        for (int i = 0; i < index; i++) {
            vg.bringChildToFront(vg.getChildAt(0));
        }
    }

    public static int getDeviceDefaultOrientation() {
        if (context != null) {
            WindowManager windowManager = (WindowManager) context.getActivity().getSystemService("window");
            Configuration config = context.getActivity().getResources().getConfiguration();
            int rotation = windowManager.getDefaultDisplay().getRotation();
            if ((rotation == 0 || rotation == 2) && config.orientation == 2) {
                return 2;
            }
            return ((rotation == 1 || rotation == 3) && config.orientation == 1) ? 2 : 1;
        }
        return 0;
    }
}
