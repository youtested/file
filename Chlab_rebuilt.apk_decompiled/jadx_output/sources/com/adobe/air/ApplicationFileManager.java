package com.adobe.air;

import android.content.pm.PackageManager;
import android.os.Bundle;
import com.distriqt.extension.inappbilling.BuildConfig;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/* loaded from: /home/codespace/chem/Chlab_rebuilt.apk_decompiled/build/apk/classes.dex */
public final class ApplicationFileManager {
    private static final String APP_PREFIX = "app";
    private static final String APP_XML_PATH = "META-INF/AIR/application.xml";
    private static final String ASSET_STRING = "assets";
    public static String sAndroidPackageName;
    public static String sApkPath;
    public static String sAppDataPath;
    public static String sInitialContentName;
    private final int BUFFER_SIZE = 8192;
    private final int DEFAULT_SIZE = -1;
    private HashMap<Object, Object> mFileInfoMap = new HashMap<>();

    public static void setAndroidPackageName(String str) {
        sAndroidPackageName = str;
    }

    public static void setAndroidAPKPath(String str) {
        sApkPath = str;
    }

    private static void setAndroidDataPath(String str) {
        sAppDataPath = str;
    }

    public static String getAndroidApkPath() {
        return sApkPath;
    }

    public static String getAndroidAppDataPath() {
        return sAppDataPath;
    }

    public static String getAppXMLRoot() {
        return getAndroidUnzipContentPath() + File.separatorChar + APP_XML_PATH;
    }

    public static String getAppRoot() {
        return getAndroidUnzipContentPath() + File.separatorChar + ASSET_STRING;
    }

    public static String getAndroidUnzipContentPath() {
        return sAppDataPath;
    }

    private File getApkPathFile() {
        return new File(getAndroidApkPath());
    }

    private static void setInitialContentName(String str) {
        sInitialContentName = str;
    }

    ApplicationFileManager() {
        procZipContents(getApkPathFile());
    }

    public static boolean deleteUnzippedContents(String str) {
        File file = new File(str);
        if (file.isDirectory()) {
            for (File file2 : file.listFiles()) {
                deleteUnzippedContents(file2.getAbsolutePath());
            }
        }
        return file.delete();
    }

    public void deleteFile(String str) {
        new File(str).delete();
    }

    public void procZipContents(File file) {
        try {
            ZipFile zipFile = new ZipFile(file);
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry nextElement = entries.nextElement();
                String name = nextElement.getName();
                if (name.substring(0, ASSET_STRING.length()).equals(ASSET_STRING)) {
                    this.mFileInfoMap.put(name, new FileInfo(nextElement.getSize(), true, false));
                    for (File file2 = new File(name); file2.getParent() != null && ((FileInfo) this.mFileInfoMap.get(file2.getParent())) == null; file2 = new File(file2.getParent())) {
                        this.mFileInfoMap.put(file2.getParent(), new FileInfo(-1L, false, true));
                    }
                }
            }
            zipFile.close();
        } catch (Exception e) {
        }
    }

    public boolean fileExists(String str) {
        return this.mFileInfoMap.containsKey(!str.equals(BuildConfig.FLAVOR) ? new StringBuilder().append(ASSET_STRING).append(File.separator).append(str).toString() : ASSET_STRING);
    }

    public boolean isDirectory(String str) {
        FileInfo fileInfo = (FileInfo) this.mFileInfoMap.get(!str.equals(BuildConfig.FLAVOR) ? ASSET_STRING + File.separator + str : ASSET_STRING);
        return fileInfo != null && fileInfo.mIsDirectory;
    }

    public long getLSize(String str) {
        FileInfo fileInfo = (FileInfo) this.mFileInfoMap.get(ASSET_STRING + File.separator + str);
        if (fileInfo == null || fileInfo.mFileSize == -1) {
            return 0L;
        }
        return fileInfo.mFileSize;
    }

    public boolean addToCache(String str) {
        return (sInitialContentName == null || str.indexOf(sInitialContentName) == -1) ? false : true;
    }

    public boolean readFileName(String str) {
        ZipFile zipFile;
        Throwable th;
        String str2 = ASSET_STRING + File.separator + str;
        String str3 = getAndroidUnzipContentPath() + File.separatorChar;
        File file = new File(str3 + str2);
        if (!file.exists()) {
            ZipFile zipFile2 = null;
            try {
                zipFile = new ZipFile(getApkPathFile());
            } catch (Exception e) {
            } catch (Throwable th2) {
                zipFile = null;
                th = th2;
            }
            try {
                Enumeration<? extends ZipEntry> entries = zipFile.entries();
                while (true) {
                    if (!entries.hasMoreElements()) {
                        break;
                    }
                    ZipEntry nextElement = entries.nextElement();
                    String name = nextElement.getName();
                    if (name.substring(0, ASSET_STRING.length()).equals(ASSET_STRING)) {
                        if (name.equals(str2)) {
                            InputStream inputStream = zipFile.getInputStream(nextElement);
                            new File(file.getParent()).mkdirs();
                            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file), 8192);
                            byte[] bArr = new byte[8192];
                            while (true) {
                                int read = inputStream.read(bArr);
                                if (read == -1) {
                                    break;
                                }
                                bufferedOutputStream.write(bArr, 0, read);
                            }
                            closeInputStream(inputStream);
                            closeOutputStream(bufferedOutputStream);
                        } else if (name.startsWith(str2 + "/")) {
                            new File(str3 + str2).mkdirs();
                            break;
                        }
                    }
                }
                if (zipFile != null) {
                    try {
                        zipFile.close();
                    } catch (Exception e2) {
                    }
                }
            } catch (Exception e3) {
                zipFile2 = zipFile;
                if (zipFile2 != null) {
                    try {
                        zipFile2.close();
                    } catch (Exception e4) {
                    }
                }
                return true;
            } catch (Throwable th3) {
                th = th3;
                if (zipFile != null) {
                    try {
                        zipFile.close();
                    } catch (Exception e5) {
                    }
                }
                throw th;
            }
        }
        return true;
    }

    public void copyFolder(String str) {
        String str2 = !str.equals(BuildConfig.FLAVOR) ? ASSET_STRING + File.separator + str : ASSET_STRING;
        String str3 = getAndroidUnzipContentPath() + File.separatorChar;
        try {
            ZipInputStream zipInputStream = new ZipInputStream(new BufferedInputStream(new FileInputStream(getApkPathFile()), 8192));
            while (true) {
                ZipEntry nextEntry = zipInputStream.getNextEntry();
                if (nextEntry != null) {
                    String name = nextEntry.getName();
                    if (name.substring(0, ASSET_STRING.length()).equals(ASSET_STRING) && name.startsWith(str2)) {
                        File file = new File(str3 + name);
                        new File(file.getParent()).mkdirs();
                        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file), 8192);
                        byte[] bArr = new byte[8192];
                        while (true) {
                            int read = zipInputStream.read(bArr, 0, 8192);
                            if (read == -1) {
                                break;
                            } else {
                                bufferedOutputStream.write(bArr, 0, read);
                            }
                        }
                        closeOutputStream(bufferedOutputStream);
                    }
                } else {
                    closeInputStream(zipInputStream);
                    return;
                }
            }
        } catch (Exception e) {
        }
    }

    public String[] appDirectoryNameList(String str) {
        String str2 = !str.equals(BuildConfig.FLAVOR) ? ASSET_STRING + File.separator + str : ASSET_STRING;
        Iterator<Object> it = this.mFileInfoMap.keySet().iterator();
        ArrayList arrayList = new ArrayList();
        while (it.hasNext()) {
            String str3 = (String) it.next();
            if (!str3.equals(str2) && str3.startsWith(str2) && -1 == str3.indexOf(File.separator, str2.length() + 1)) {
                arrayList.add(str3.substring(str2.length() + 1));
            }
        }
        return (String[]) arrayList.toArray(new String[0]);
    }

    public boolean[] appDirectoryTypeList(String str) {
        String str2 = !str.equals(BuildConfig.FLAVOR) ? ASSET_STRING + File.separator + str : ASSET_STRING;
        Iterator<Object> it = this.mFileInfoMap.keySet().iterator();
        ArrayList arrayList = new ArrayList();
        while (it.hasNext()) {
            String str3 = (String) it.next();
            if (!str3.equals(str2) && str3.startsWith(str2) && -1 == str3.indexOf(File.separator, str2.length() + 1)) {
                arrayList.add(new Boolean(((FileInfo) this.mFileInfoMap.get(str3)).mIsFile));
            }
        }
        boolean[] zArr = new boolean[arrayList.size()];
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 < zArr.length) {
                zArr[i2] = ((Boolean) arrayList.get(i2)).booleanValue();
                i = i2 + 1;
            } else {
                return zArr;
            }
        }
    }

    private static void RefreshAppCache(String str, String str2) {
        if (!new File(str + File.separator + str2).exists()) {
            deleteDir(new File(str));
        }
    }

    public static boolean deleteDir(File file) {
        if (file.isDirectory()) {
            for (File file2 : file.listFiles()) {
                if (!deleteDir(file2)) {
                    return false;
                }
            }
        }
        return file.delete();
    }

    public static void processAndroidDataPath(String str) {
        String str2;
        String str3;
        String str4;
        String str5 = APP_PREFIX;
        String str6 = str + File.separator + APP_PREFIX;
        String str7 = null;
        try {
            Bundle bundle = AndroidActivityWrapper.GetAndroidActivityWrapper().getActivity().getPackageManager().getActivityInfo(AndroidActivityWrapper.GetAndroidActivityWrapper().getActivity().getComponentName(), 128).metaData;
            if (bundle == null) {
                str3 = null;
                str4 = APP_PREFIX;
            } else {
                str2 = (String) bundle.get("uniqueappversionid");
                try {
                    str5 = AndroidActivityWrapper.IsGamePreviewMode() ? UUID.randomUUID().toString() : str2;
                    RefreshAppCache(str6, str5);
                    str3 = (String) bundle.get("initialcontent");
                    str4 = str5;
                } catch (PackageManager.NameNotFoundException e) {
                } catch (NullPointerException e2) {
                }
            }
            String str8 = str3;
            str2 = str4;
            str7 = str8;
        } catch (PackageManager.NameNotFoundException e3) {
            str2 = str5;
        } catch (NullPointerException e4) {
            str2 = str5;
        }
        setAndroidDataPath(str6 + File.separator + str2);
        new File(str6 + File.separator + str2).mkdirs();
        setInitialContentName(str7);
    }

    private void closeInputStream(InputStream inputStream) throws Exception {
        inputStream.close();
    }

    private void closeOutputStream(OutputStream outputStream) throws Exception {
        outputStream.flush();
        outputStream.close();
    }

    public static void checkAndCreateAppDataDir() {
        File file = new File(sAppDataPath);
        if (!file.exists()) {
            try {
                file.mkdirs();
            } catch (SecurityException e) {
            }
        }
    }
}
