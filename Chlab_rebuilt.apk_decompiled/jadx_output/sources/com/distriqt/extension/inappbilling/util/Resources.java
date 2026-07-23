package com.distriqt.extension.inappbilling.util;

import android.util.Log;
import java.lang.reflect.Field;

/* loaded from: /home/codespace/chem/Chlab_rebuilt.apk_decompiled/build/apk/classes.dex */
public class Resources {
    /* JADX WARN: Code restructure failed: missing block: B:10:0x006d, code lost:
    
        if (java.lang.Class.forName(r6 + ".R").getClasses()[r1] == null) goto L10;
     */
    /* JADX WARN: Code restructure failed: missing block: B:11:0x006f, code lost:
    
        r2 = java.lang.Class.forName(r6 + ".R").getClasses()[r1].getField(r8).getInt(java.lang.Class.forName(r6 + ".R").getClasses()[r1]);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static int getResourceIdByName(java.lang.String r6, java.lang.String r7, java.lang.String r8) {
        /*
            r2 = 0
            r1 = 0
        L2:
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch: java.lang.Exception -> Lb7
            r3.<init>()     // Catch: java.lang.Exception -> Lb7
            java.lang.StringBuilder r3 = r3.append(r6)     // Catch: java.lang.Exception -> Lb7
            java.lang.String r4 = ".R"
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch: java.lang.Exception -> Lb7
            java.lang.String r3 = r3.toString()     // Catch: java.lang.Exception -> Lb7
            java.lang.Class r3 = java.lang.Class.forName(r3)     // Catch: java.lang.Exception -> Lb7
            java.lang.Class[] r3 = r3.getClasses()     // Catch: java.lang.Exception -> Lb7
            int r3 = r3.length     // Catch: java.lang.Exception -> Lb7
            if (r1 >= r3) goto Lb1
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch: java.lang.Exception -> Lb7
            r3.<init>()     // Catch: java.lang.Exception -> Lb7
            java.lang.StringBuilder r3 = r3.append(r6)     // Catch: java.lang.Exception -> Lb7
            java.lang.String r4 = ".R"
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch: java.lang.Exception -> Lb7
            java.lang.String r3 = r3.toString()     // Catch: java.lang.Exception -> Lb7
            java.lang.Class r3 = java.lang.Class.forName(r3)     // Catch: java.lang.Exception -> Lb7
            java.lang.Class[] r3 = r3.getClasses()     // Catch: java.lang.Exception -> Lb7
            r3 = r3[r1]     // Catch: java.lang.Exception -> Lb7
            java.lang.String r3 = r3.getName()     // Catch: java.lang.Exception -> Lb7
            java.lang.String r4 = "\\$"
            java.lang.String[] r3 = r3.split(r4)     // Catch: java.lang.Exception -> Lb7
            r4 = 1
            r3 = r3[r4]     // Catch: java.lang.Exception -> Lb7
            boolean r3 = r3.equals(r7)     // Catch: java.lang.Exception -> Lb7
            if (r3 == 0) goto Lb3
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch: java.lang.Exception -> Lb7
            r3.<init>()     // Catch: java.lang.Exception -> Lb7
            java.lang.StringBuilder r3 = r3.append(r6)     // Catch: java.lang.Exception -> Lb7
            java.lang.String r4 = ".R"
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch: java.lang.Exception -> Lb7
            java.lang.String r3 = r3.toString()     // Catch: java.lang.Exception -> Lb7
            java.lang.Class r3 = java.lang.Class.forName(r3)     // Catch: java.lang.Exception -> Lb7
            java.lang.Class[] r3 = r3.getClasses()     // Catch: java.lang.Exception -> Lb7
            r3 = r3[r1]     // Catch: java.lang.Exception -> Lb7
            if (r3 == 0) goto Lb1
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch: java.lang.Exception -> Lb7
            r3.<init>()     // Catch: java.lang.Exception -> Lb7
            java.lang.StringBuilder r3 = r3.append(r6)     // Catch: java.lang.Exception -> Lb7
            java.lang.String r4 = ".R"
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch: java.lang.Exception -> Lb7
            java.lang.String r3 = r3.toString()     // Catch: java.lang.Exception -> Lb7
            java.lang.Class r3 = java.lang.Class.forName(r3)     // Catch: java.lang.Exception -> Lb7
            java.lang.Class[] r3 = r3.getClasses()     // Catch: java.lang.Exception -> Lb7
            r3 = r3[r1]     // Catch: java.lang.Exception -> Lb7
            java.lang.reflect.Field r3 = r3.getField(r8)     // Catch: java.lang.Exception -> Lb7
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch: java.lang.Exception -> Lb7
            r4.<init>()     // Catch: java.lang.Exception -> Lb7
            java.lang.StringBuilder r4 = r4.append(r6)     // Catch: java.lang.Exception -> Lb7
            java.lang.String r5 = ".R"
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch: java.lang.Exception -> Lb7
            java.lang.String r4 = r4.toString()     // Catch: java.lang.Exception -> Lb7
            java.lang.Class r4 = java.lang.Class.forName(r4)     // Catch: java.lang.Exception -> Lb7
            java.lang.Class[] r4 = r4.getClasses()     // Catch: java.lang.Exception -> Lb7
            r4 = r4[r1]     // Catch: java.lang.Exception -> Lb7
            int r2 = r3.getInt(r4)     // Catch: java.lang.Exception -> Lb7
        Lb1:
            r3 = r2
        Lb2:
            return r3
        Lb3:
            int r1 = r1 + 1
            goto L2
        Lb7:
            r0 = move-exception
            r3 = -1
            goto Lb2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.distriqt.extension.inappbilling.util.Resources.getResourceIdByName(java.lang.String, java.lang.String, java.lang.String):int");
    }

    public static void listResources(String packageName) {
        for (int i = 0; i < Class.forName(packageName + ".R").getClasses().length; i++) {
            try {
                Field[] fields = Class.forName(packageName + ".R").getClasses()[i].getFields();
                for (Field field : fields) {
                    Log.d("Resources", Class.forName(packageName + ".R").getClasses()[i].getName() + "::" + field.getName());
                }
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
    }
}
