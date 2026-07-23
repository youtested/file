package com.adobe.air;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.net.Uri;
import com.adobe.air.utils.Utils;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: /home/codespace/chem/Chlab_rebuilt.apk_decompiled/build/apk/classes.dex */
public final class ResourceFileManager {
    private final Context mAppContext;
    private final Resources mAppResources;
    private ResourceIdMap mResourceIdMap;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ResourceFileManager(Context context) {
        this.mResourceIdMap = null;
        this.mAppContext = context;
        this.mAppResources = context.getResources();
        try {
            this.mResourceIdMap = new ResourceIdMap(this.mAppContext.getClassLoader().loadClass(this.mAppContext.getPackageName() + ".R"));
        } catch (ClassNotFoundException e) {
        }
    }

    public InputStream getFileStreamFromRawRes(int i) throws Resources.NotFoundException {
        InputStream openRawResource = this.mAppResources.openRawResource(i);
        if (openRawResource == null) {
        }
        return openRawResource;
    }

    public String readFileFromRawRes(int i) {
        String str = new String();
        try {
            InputStream fileStreamFromRawRes = getFileStreamFromRawRes(i);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            Utils.writeThrough(fileStreamFromRawRes, byteArrayOutputStream);
            return new String(byteArrayOutputStream.toByteArray(), "UTF-8");
        } catch (Exception e) {
            return str;
        }
    }

    public InputStream getStream(int i) throws Resources.NotFoundException {
        return this.mAppResources.openRawResource(i);
    }

    public void extractResource(int i, File file) throws IOException {
        InputStream stream = getStream(i);
        Utils.writeOut(stream, file);
        stream.close();
    }

    public boolean resExists(int i) {
        if (i <= 0) {
            return false;
        }
        try {
            return this.mAppResources.openRawResource(i) != null;
        } catch (Exception e) {
            return false;
        }
    }

    public int lookupResId(String str) {
        try {
            if (this.mResourceIdMap != null) {
                return this.mResourceIdMap.getId(str);
            }
        } catch (Resources.NotFoundException e) {
        }
        return -1;
    }

    private String remapSpecialFileNames(String str) {
        if (str.equals("ss.cfg") || str.equals("ss.sgn") || str.equals("mms.cfg")) {
            return str.replace('.', '_');
        }
        return null;
    }

    private boolean resExists(String str) {
        String remapSpecialFileNames = remapSpecialFileNames(str);
        if (remapSpecialFileNames == null) {
            return false;
        }
        return resExists(lookupResId("raw." + remapSpecialFileNames));
    }

    public AssetFileDescriptor GetAssetFileDescriptor(String str) {
        try {
            String remapSpecialFileNames = remapSpecialFileNames(str);
            if (remapSpecialFileNames != null) {
                str = remapSpecialFileNames;
            }
            return this.mAppContext.getContentResolver().openAssetFileDescriptor(Uri.parse("android.resource://" + this.mAppContext.getPackageName() + "/raw/" + str), "r");
        } catch (Exception e) {
            return null;
        }
    }

    public String getResourceName(int i) {
        try {
            return this.mAppResources.getResourceName(i);
        } catch (Exception e) {
            return "null";
        }
    }

    public String getResourceEntryName(int i) {
        try {
            return this.mAppResources.getResourceEntryName(i);
        } catch (Exception e) {
            return "null";
        }
    }
}
