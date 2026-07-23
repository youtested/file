package com.adobe.air;

/* compiled from: ApplicationFileManager.java */
/* loaded from: /home/codespace/chem/Chlab_rebuilt.apk_decompiled/build/apk/classes.dex */
class FileInfo {
    public long mFileSize;
    public boolean mIsDirectory;
    public boolean mIsFile;

    /* JADX INFO: Access modifiers changed from: package-private */
    public FileInfo(long j, boolean z, boolean z2) {
        this.mFileSize = j;
        this.mIsFile = z;
        this.mIsDirectory = z2;
    }
}
