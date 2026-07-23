package com.adobe.air.microphone;

/* loaded from: /home/codespace/chem/Chlab_rebuilt.apk_decompiled/build/apk/classes.dex */
public class MobileAEC {
    public static final short AECM_ENABLE = 1;
    public static final short AECM_UNABLE = 0;
    private AecmConfig mAecmConfig;
    private int mAecmHandler;
    private SamplingFrequency mSampFreq = null;
    private boolean mIsInit = false;

    private static native int nativeAecmProcess(int i, short[] sArr, short[] sArr2, short[] sArr3, short s, short s2);

    private static native int nativeBufferFarend(int i, short[] sArr, int i2);

    private static native int nativeCreateAecmInstance();

    private static native int nativeFreeAecmInstance(int i);

    private static native int nativeInitializeAecmInstance(int i, int i2);

    private static native int nativeSetConfig(int i, AecmConfig aecmConfig);

    /* loaded from: /home/codespace/chem/Chlab_rebuilt.apk_decompiled/build/apk/classes.dex */
    public static final class SamplingFrequency {
        private final long mSamplingFrequency;
        public static final SamplingFrequency FS_8000Hz = new SamplingFrequency(8000);
        public static final SamplingFrequency FS_16000Hz = new SamplingFrequency(16000);

        public long getFS() {
            return this.mSamplingFrequency;
        }

        private SamplingFrequency(long j) {
            this.mSamplingFrequency = j;
        }
    }

    /* loaded from: /home/codespace/chem/Chlab_rebuilt.apk_decompiled/build/apk/classes.dex */
    public static final class AggressiveMode {
        private final int mMode;
        public static final AggressiveMode MILD = new AggressiveMode(0);
        public static final AggressiveMode MEDIUM = new AggressiveMode(1);
        public static final AggressiveMode HIGH = new AggressiveMode(2);
        public static final AggressiveMode AGGRESSIVE = new AggressiveMode(3);
        public static final AggressiveMode MOST_AGGRESSIVE = new AggressiveMode(4);

        public int getMode() {
            return this.mMode;
        }

        private AggressiveMode(int i) {
            this.mMode = i;
        }
    }

    public MobileAEC(SamplingFrequency samplingFrequency) {
        this.mAecmHandler = -1;
        this.mAecmConfig = null;
        setSampFreq(samplingFrequency);
        this.mAecmConfig = new AecmConfig();
        this.mAecmHandler = nativeCreateAecmInstance();
    }

    public void setSampFreq(SamplingFrequency samplingFrequency) {
        if (samplingFrequency == null) {
            this.mSampFreq = SamplingFrequency.FS_16000Hz;
        } else {
            this.mSampFreq = samplingFrequency;
        }
    }

    public MobileAEC farendBuffer(short[] sArr, int i) throws Exception {
        if (!this.mIsInit) {
            throw new Exception("setFarendBuffer() called on an unprepared AECM instance.");
        }
        if (nativeBufferFarend(this.mAecmHandler, sArr, i) == -1) {
            throw new Exception("setFarendBuffer() failed due to invalid arguments.");
        }
        return this;
    }

    public void echoCancellation(short[] sArr, short[] sArr2, short[] sArr3, short s, short s2) throws Exception {
        if (!this.mIsInit) {
            throw new Exception("echoCancelling() called on an unprepared AECM instance.");
        }
        if (nativeAecmProcess(this.mAecmHandler, sArr, sArr2, sArr3, s, s2) == -1) {
            throw new Exception("echoCancellation() failed due to invalid arguments.");
        }
    }

    public MobileAEC setAecmMode(AggressiveMode aggressiveMode) throws NullPointerException {
        if (aggressiveMode == null) {
            throw new NullPointerException("setAecMode() failed due to null argument.");
        }
        this.mAecmConfig.mAecmMode = (short) aggressiveMode.getMode();
        return this;
    }

    public short getAecmMode() {
        return this.mAecmConfig.mAecmMode;
    }

    public MobileAEC prepare() {
        if (this.mIsInit) {
            close();
            this.mAecmHandler = nativeCreateAecmInstance();
        }
        mInitAecmInstance((int) this.mSampFreq.getFS());
        this.mIsInit = true;
        nativeSetConfig(this.mAecmHandler, this.mAecmConfig);
        return this;
    }

    public void close() {
        if (this.mIsInit) {
            nativeFreeAecmInstance(this.mAecmHandler);
            this.mAecmHandler = -1;
            this.mIsInit = false;
        }
    }

    protected void finalize() throws Throwable {
        super.finalize();
        if (this.mIsInit) {
            close();
        }
    }

    private void mInitAecmInstance(int i) {
        if (!this.mIsInit) {
            nativeInitializeAecmInstance(this.mAecmHandler, i);
            this.mAecmConfig = new AecmConfig();
            nativeSetConfig(this.mAecmHandler, this.mAecmConfig);
            this.mIsInit = true;
        }
    }

    /* loaded from: /home/codespace/chem/Chlab_rebuilt.apk_decompiled/build/apk/classes.dex */
    public class AecmConfig {
        private short mAecmMode = (short) AggressiveMode.MOST_AGGRESSIVE.getMode();
        private short mCngMode = 1;

        public AecmConfig() {
        }
    }
}
