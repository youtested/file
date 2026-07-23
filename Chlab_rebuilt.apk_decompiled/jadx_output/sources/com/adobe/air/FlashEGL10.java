package com.adobe.air;

import android.app.Activity;
import android.opengl.GLES10;
import android.os.Build;
import android.view.SurfaceView;
import com.adobe.flashruntime.air.VideoViewAIR;
import java.util.Scanner;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;

/* loaded from: /home/codespace/chem/Chlab_rebuilt.apk_decompiled/build/apk/classes.dex */
public class FlashEGL10 implements FlashEGL {
    private static String TAG = "FlashEGL10";
    private static int EGL_CONTEXT_CLIENT_VERSION = 12440;
    private static int EGL_OPENGL_ES2_BIT = 4;
    private static int EGL_COVERAGE_BUFFERS_NV = 12512;
    private static int EGL_COVERAGE_SAMPLES_NV = 12513;
    private static int EGL_SWAP_BEHAVIOR = 12435;
    private static int EGL_BUFFER_PRESERVED = 12436;
    private static int EGL_BUFFER_DESTROYED = 12437;
    private static int[] cfgAttrs = {12339, -1, 12325, -1, 12326, -1, 12352, EGL_OPENGL_ES2_BIT, 12344};
    private static int[] fbWindowSurfaceOnAttrs = {EGL_SWAP_BEHAVIOR, EGL_BUFFER_PRESERVED, 12344};
    private static int[] fbWindowSurfaceOffAttrs = {EGL_SWAP_BEHAVIOR, EGL_BUFFER_DESTROYED, 12344};
    private static int[] fbPBufferSurfaceAttrs = {12375, 64, 12374, 64, 12344};
    private int kSurfaceTypes = 0;
    private int kConfigId = 1;
    private int kRedBits = 2;
    private int kGreenBits = 3;
    private int kBlueBits = 4;
    private int kAlphaBits = 5;
    private int kColorBits = 6;
    private int kDepthBits = 7;
    private int kStencilBits = 8;
    private int kMsaaSamp = 9;
    private int kCsaaSamp = 10;
    private int kSwapPreserve = 11;
    private int kNumElements = 12;
    private int kSwapPreserveDefault = 0;
    private int kSwapPreserveOn = 1;
    private int kSwapPreserveOff = 2;
    private EGL10 mEgl = null;
    private EGLDisplay mEglDisplay = EGL10.EGL_NO_DISPLAY;
    private EGLSurface mEglSurface = EGL10.EGL_NO_SURFACE;
    private EGLSurface mEglWindowSurface = EGL10.EGL_NO_SURFACE;
    private EGLSurface mEglPbufferSurface = EGL10.EGL_NO_SURFACE;
    private EGLConfig mEglConfig = null;
    private EGLConfig[] mEglConfigList = null;
    private int[] mEglVersion = null;
    private int mEglConfigCount = 0;
    private int mWindowConfigCount = 0;
    private int mPixmapConfigCount = 0;
    private int mPbufferConfigCount = 0;
    volatile EGLContext mEglContext = EGL10.EGL_NO_CONTEXT;
    private boolean mIsARGBSurface = false;
    private boolean mIsGPUOOM = false;
    private boolean mIsBufferPreserve = false;
    private boolean mIsES3Device = false;

    public void FlashEGL10() {
        this.mEgl = null;
        this.mEglDisplay = EGL10.EGL_NO_DISPLAY;
        this.mEglConfig = null;
        this.mEglContext = EGL10.EGL_NO_CONTEXT;
        this.mEglSurface = EGL10.EGL_NO_SURFACE;
        this.mEglWindowSurface = EGL10.EGL_NO_SURFACE;
        this.mEglPbufferSurface = EGL10.EGL_NO_SURFACE;
        this.mIsARGBSurface = false;
    }

    private int XX(int i, int i2) {
        return (this.kNumElements * i) + i2;
    }

    @Override // com.adobe.air.FlashEGL
    public boolean HasGLContext() {
        return this.mEglContext != EGL10.EGL_NO_CONTEXT;
    }

    @Override // com.adobe.air.FlashEGL
    public int GetSurfaceWidth() {
        int[] iArr = new int[1];
        this.mEgl.eglQuerySurface(this.mEglDisplay, this.mEglSurface, 12375, iArr);
        return iArr[0];
    }

    @Override // com.adobe.air.FlashEGL
    public int GetSurfaceHeight() {
        int[] iArr = new int[1];
        this.mEgl.eglQuerySurface(this.mEglDisplay, this.mEglSurface, 12374, iArr);
        return iArr[0];
    }

    @Override // com.adobe.air.FlashEGL
    public boolean IsEmulator() {
        return Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic");
    }

    public boolean ChooseConfig(EGLDisplay eGLDisplay, int[] iArr, EGLConfig[] eGLConfigArr, int i, int[] iArr2) {
        if (!IsEmulator()) {
            return this.mEgl.eglChooseConfig(eGLDisplay, iArr, eGLConfigArr, i, iArr2);
        }
        int[] iArr3 = new int[1];
        this.mEgl.eglGetConfigs(eGLDisplay, null, 0, iArr3);
        int i2 = iArr3[0];
        EGLConfig[] eGLConfigArr2 = new EGLConfig[i2];
        this.mEgl.eglGetConfigs(eGLDisplay, eGLConfigArr2, i2, iArr3);
        int length = iArr.length;
        if (iArr.length % 2 != 0) {
            length = iArr.length - 1;
        }
        int i3 = 0;
        for (int i4 = 0; i4 < i2; i4++) {
            int i5 = 0;
            while (i5 < length) {
                if (iArr[i5 + 1] != -1) {
                    int[] iArr4 = new int[1];
                    this.mEgl.eglGetConfigAttrib(eGLDisplay, eGLConfigArr2[i4], iArr[i5], iArr4);
                    if ((iArr4[0] & iArr[i5 + 1]) != iArr[i5 + 1]) {
                        break;
                    }
                }
                i5 += 2;
            }
            if (i5 == length) {
                if (eGLConfigArr != null && i3 < i) {
                    eGLConfigArr[i3] = eGLConfigArr2[i4];
                }
                i3++;
            }
        }
        iArr2[0] = i3;
        return true;
    }

    @Override // com.adobe.air.FlashEGL
    public int[] GetNumConfigs() {
        int[] iArr = new int[1];
        ChooseConfig(this.mEglDisplay, cfgAttrs, null, 0, iArr);
        int i = iArr[0];
        this.mEglConfigCount = i;
        cfgAttrs[1] = 4;
        ChooseConfig(this.mEglDisplay, cfgAttrs, null, 0, iArr);
        int i2 = iArr[0];
        this.mWindowConfigCount = i2;
        cfgAttrs[1] = 2;
        ChooseConfig(this.mEglDisplay, cfgAttrs, null, 0, iArr);
        int i3 = iArr[0];
        this.mPixmapConfigCount = i3;
        cfgAttrs[1] = 1;
        ChooseConfig(this.mEglDisplay, cfgAttrs, null, 0, iArr);
        int i4 = iArr[0];
        int[] iArr2 = {i, i2, i3, i4};
        this.mPbufferConfigCount = i4;
        cfgAttrs[1] = -1;
        return iArr2;
    }

    @Override // com.adobe.air.FlashEGL
    public int[] GetConfigs(boolean z, boolean z2) {
        int[] iArr = new int[this.mEglConfigCount * this.kNumElements];
        int[] iArr2 = new int[1];
        int[] iArr3 = new int[1];
        this.mEglConfigList = new EGLConfig[this.mEglConfigCount];
        checkEglError("Before eglChooseConfig");
        cfgAttrs[1] = 5;
        ChooseConfig(this.mEglDisplay, cfgAttrs, this.mEglConfigList, this.mEglConfigCount, iArr2);
        checkEglError("After eglChooseConfig");
        cfgAttrs[1] = -1;
        int i = iArr2[0];
        this.mEglConfigCount = i;
        for (int i2 = 0; i2 < i; i2++) {
            this.mEgl.eglGetConfigAttrib(this.mEglDisplay, this.mEglConfigList[i2], 12339, iArr3);
            iArr[XX(i2, this.kSurfaceTypes)] = iArr3[0];
            iArr[XX(i2, this.kConfigId)] = i2;
            this.mEgl.eglGetConfigAttrib(this.mEglDisplay, this.mEglConfigList[i2], 12324, iArr3);
            iArr[XX(i2, this.kRedBits)] = iArr3[0];
            this.mEgl.eglGetConfigAttrib(this.mEglDisplay, this.mEglConfigList[i2], 12323, iArr3);
            iArr[XX(i2, this.kGreenBits)] = iArr3[0];
            this.mEgl.eglGetConfigAttrib(this.mEglDisplay, this.mEglConfigList[i2], 12322, iArr3);
            iArr[XX(i2, this.kBlueBits)] = iArr3[0];
            this.mEgl.eglGetConfigAttrib(this.mEglDisplay, this.mEglConfigList[i2], 12321, iArr3);
            iArr[XX(i2, this.kAlphaBits)] = iArr3[0];
            this.mEgl.eglGetConfigAttrib(this.mEglDisplay, this.mEglConfigList[i2], 12320, iArr3);
            iArr[XX(i2, this.kColorBits)] = iArr3[0];
            this.mEgl.eglGetConfigAttrib(this.mEglDisplay, this.mEglConfigList[i2], 12325, iArr3);
            iArr[XX(i2, this.kDepthBits)] = iArr3[0];
            this.mEgl.eglGetConfigAttrib(this.mEglDisplay, this.mEglConfigList[i2], 12326, iArr3);
            iArr[XX(i2, this.kStencilBits)] = iArr3[0];
            iArr[XX(i2, this.kCsaaSamp)] = 0;
            iArr[XX(i2, this.kMsaaSamp)] = 0;
            if (z) {
                this.mEgl.eglGetConfigAttrib(this.mEglDisplay, this.mEglConfigList[i2], EGL_COVERAGE_SAMPLES_NV, iArr3);
                if (iArr3[0] != 1) {
                    iArr[XX(i2, this.kCsaaSamp)] = iArr3[0];
                }
            } else {
                this.mEgl.eglGetConfigAttrib(this.mEglDisplay, this.mEglConfigList[i2], 12337, iArr3);
                if (iArr3[0] != 1) {
                    iArr[XX(i2, this.kMsaaSamp)] = iArr3[0];
                }
            }
            if (z2) {
                iArr[XX(i2, this.kSwapPreserve)] = ((this.mEglVersion[0] > 1 || this.mEglVersion[1] > 3) && (iArr[XX(i2, this.kSurfaceTypes)] & EGL_BUFFER_PRESERVED) != 0) ? 1 : 0;
            } else {
                iArr[XX(i2, this.kSwapPreserve)] = 0;
            }
        }
        return iArr;
    }

    @Override // com.adobe.air.FlashEGL
    public void SetConfig(int i) {
        this.mEglConfig = this.mEglConfigList[i];
        int[] iArr = new int[1];
        this.mEgl.eglGetConfigAttrib(this.mEglDisplay, this.mEglConfig, 12324, iArr);
        int i2 = iArr[0];
        this.mEgl.eglGetConfigAttrib(this.mEglDisplay, this.mEglConfig, 12323, iArr);
        int i3 = iArr[0];
        this.mEgl.eglGetConfigAttrib(this.mEglDisplay, this.mEglConfig, 12322, iArr);
        int i4 = iArr[0];
        this.mEgl.eglGetConfigAttrib(this.mEglDisplay, this.mEglConfig, 12321, iArr);
        int i5 = iArr[0];
        this.mEgl.eglGetConfigAttrib(this.mEglDisplay, this.mEglConfig, 12325, iArr);
        int i6 = iArr[0];
        this.mEgl.eglGetConfigAttrib(this.mEglDisplay, this.mEglConfig, 12326, iArr);
        int i7 = iArr[0];
        this.mEgl.eglGetConfigAttrib(this.mEglDisplay, this.mEglConfig, 12337, iArr);
        int i8 = iArr[0];
        this.mEgl.eglGetConfigAttrib(this.mEglDisplay, this.mEglConfig, 12338, iArr);
        int i9 = iArr[0];
        this.mEgl.eglGetConfigAttrib(this.mEglDisplay, this.mEglConfig, 12339, iArr);
        int i10 = iArr[0];
    }

    @Override // com.adobe.air.FlashEGL
    public int CreateDummySurfaceAndContext() {
        boolean z;
        int i;
        if (this.mEglDisplay == EGL10.EGL_NO_DISPLAY) {
            return 12296;
        }
        if (this.mEglContext != EGL10.EGL_NO_CONTEXT) {
            if (this.mEglWindowSurface != EGL10.EGL_NO_SURFACE) {
                this.mEgl.eglMakeCurrent(this.mEglDisplay, this.mEglWindowSurface, this.mEglWindowSurface, this.mEglContext);
                return 12288;
            }
            if (this.mEglPbufferSurface != EGL10.EGL_NO_SURFACE) {
                this.mEgl.eglMakeCurrent(this.mEglDisplay, this.mEglPbufferSurface, this.mEglPbufferSurface, this.mEglContext);
                return 12288;
            }
            this.mEgl.eglMakeCurrent(this.mEglDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
            this.mEgl.eglDestroyContext(this.mEglDisplay, this.mEglContext);
            this.mEglContext = EGL10.EGL_NO_CONTEXT;
        }
        int[] iArr = new int[1];
        EGLConfig[] eGLConfigArr = new EGLConfig[1];
        cfgAttrs[1] = 1;
        ChooseConfig(this.mEglDisplay, cfgAttrs, eGLConfigArr, 1, iArr);
        cfgAttrs[1] = -1;
        if (iArr[0] == 0) {
            return 12294;
        }
        boolean z2 = Build.VERSION.SDK_INT >= 18;
        int[] iArr2 = {EGL_CONTEXT_CLIENT_VERSION, 2, 12344};
        if (z2) {
            this.mEglContext = this.mEgl.eglCreateContext(this.mEglDisplay, eGLConfigArr[0], EGL10.EGL_NO_CONTEXT, iArr2);
            if (this.mEglContext == EGL10.EGL_NO_CONTEXT) {
                return 12294;
            }
            checkEglError("After creating dummy context for checking gl version");
            EGLSurface eglCreatePbufferSurface = this.mEgl.eglCreatePbufferSurface(this.mEglDisplay, eGLConfigArr[0], fbPBufferSurfaceAttrs);
            checkEglError("After eglCreatePbufferSurface for checking gl version");
            if (eglCreatePbufferSurface == EGL10.EGL_NO_SURFACE) {
                return 12294;
            }
            this.mEgl.eglMakeCurrent(this.mEglDisplay, eglCreatePbufferSurface, eglCreatePbufferSurface, this.mEglContext);
            checkEglError("After eglMakeCurrent for checking gl version");
            String glGetString = GLES10.glGetString(7938);
            if (glGetString != null) {
                Scanner scanner = new Scanner(glGetString);
                scanner.useDelimiter("[^\\w']+");
                while (scanner.hasNext()) {
                    if (scanner.hasNextInt()) {
                        i = scanner.nextInt();
                        break;
                    }
                    if (scanner.hasNext()) {
                        scanner.next();
                    }
                }
            }
            i = 0;
            z = i >= 3;
            this.mEgl.eglMakeCurrent(this.mEglDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
            this.mEgl.eglDestroySurface(this.mEglDisplay, eglCreatePbufferSurface);
            this.mEgl.eglDestroyContext(this.mEglDisplay, this.mEglContext);
            this.mEglContext = EGL10.EGL_NO_CONTEXT;
        } else {
            z = false;
        }
        if (z) {
            iArr2[1] = 3;
            checkEglError("Before eglCreateContext es3");
            this.mEglContext = this.mEgl.eglCreateContext(this.mEglDisplay, eGLConfigArr[0], EGL10.EGL_NO_CONTEXT, iArr2);
            checkEglError("After eglCreateContext es3");
            if (this.mEglContext != EGL10.EGL_NO_CONTEXT) {
                this.mIsES3Device = true;
            }
        }
        if (this.mEglContext == EGL10.EGL_NO_CONTEXT) {
            iArr2[1] = 2;
            checkEglError("Before eglCreateContext es2");
            this.mEglContext = this.mEgl.eglCreateContext(this.mEglDisplay, eGLConfigArr[0], EGL10.EGL_NO_CONTEXT, iArr2);
            checkEglError("After eglCreateContext es2");
            if (this.mEglContext == EGL10.EGL_NO_CONTEXT) {
                return 12294;
            }
        }
        checkEglError("Before eglCreatePbufferSurface");
        this.mEglPbufferSurface = this.mEgl.eglCreatePbufferSurface(this.mEglDisplay, eGLConfigArr[0], fbPBufferSurfaceAttrs);
        checkEglError("After eglCreatePbufferSurface");
        if (this.mEglPbufferSurface == EGL10.EGL_NO_SURFACE) {
            return 12294;
        }
        checkEglError("Before eglMakeCurrent");
        this.mEgl.eglMakeCurrent(this.mEglDisplay, this.mEglPbufferSurface, this.mEglPbufferSurface, this.mEglContext);
        checkEglError("After eglMakeCurrent");
        return 12288;
    }

    @Override // com.adobe.air.FlashEGL
    public int InitEGL() {
        if (this.mEglContext != EGL10.EGL_NO_CONTEXT) {
            return 12288;
        }
        this.mEgl = (EGL10) EGLContext.getEGL();
        checkEglError("Before eglGetDisplay");
        this.mEglDisplay = this.mEgl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
        int checkEglError = checkEglError("After eglGetDisplay");
        if (12288 == checkEglError) {
            this.mEglVersion = new int[2];
            checkEglError("Before eglInitialize");
            this.mEgl.eglInitialize(this.mEglDisplay, this.mEglVersion);
            int checkEglError2 = checkEglError("After eglInitialize");
            if (12288 == checkEglError2) {
                return 12288;
            }
            return checkEglError2;
        }
        return checkEglError;
    }

    @Override // com.adobe.air.FlashEGL
    public boolean DestroyGLContext() {
        if (this.mEglContext == EGL10.EGL_NO_CONTEXT || this.mEglDisplay == EGL10.EGL_NO_DISPLAY) {
            return false;
        }
        checkEglError("DestroyGLContext: Before eglMakeCurrent for noSurface");
        this.mEgl.eglMakeCurrent(this.mEglDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
        checkEglError("DestroyGLContext: After eglMakeCurrent");
        if (this.mEglPbufferSurface != EGL10.EGL_NO_SURFACE) {
            this.mEgl.eglDestroySurface(this.mEglDisplay, this.mEglPbufferSurface);
            this.mEglPbufferSurface = EGL10.EGL_NO_SURFACE;
        }
        checkEglError("Before eglDestroyContext");
        boolean eglDestroyContext = this.mEgl.eglDestroyContext(this.mEglDisplay, this.mEglContext);
        checkEglError("After eglDestroyContext");
        this.mEglContext = EGL10.EGL_NO_CONTEXT;
        return eglDestroyContext;
    }

    @Override // com.adobe.air.FlashEGL
    public int CreateGLContext(boolean z) {
        if (this.mEglConfig == null) {
            return 12293;
        }
        if (this.mEglContext != EGL10.EGL_NO_CONTEXT && !z) {
            return 12288;
        }
        int[] iArr = {EGL_CONTEXT_CLIENT_VERSION, this.mIsES3Device ? 3 : 2, 12344};
        if (z) {
            EGLContext eGLContext = this.mEglContext;
            checkEglError("Before eglCreateContext");
            this.mEglContext = this.mEgl.eglCreateContext(this.mEglDisplay, this.mEglConfig, eGLContext, iArr);
            checkEglError("After eglCreateContext");
            this.mEgl.eglDestroyContext(this.mEglDisplay, eGLContext);
            checkEglError("After eglDestroyContext");
        } else {
            checkEglError("Before eglCreateContext");
            this.mEglContext = this.mEgl.eglCreateContext(this.mEglDisplay, this.mEglConfig, EGL10.EGL_NO_CONTEXT, iArr);
            checkEglError("After eglCreateContext");
        }
        if (this.mEglContext == EGL10.EGL_NO_CONTEXT) {
            return 12294;
        }
        if (EGL10.EGL_NO_SURFACE == this.mEglPbufferSurface) {
            checkEglError("Before eglCreatePbufferSurface");
            this.mEglPbufferSurface = this.mEgl.eglCreatePbufferSurface(this.mEglDisplay, this.mEglConfig, fbPBufferSurfaceAttrs);
            checkEglError("After eglCreatePbufferSurface");
        }
        return 12288;
    }

    @Override // com.adobe.air.FlashEGL
    public void TerminateEGL() {
        if (this.mEgl != null && this.mEglDisplay != EGL10.EGL_NO_DISPLAY) {
            this.mEgl.eglTerminate(this.mEglDisplay);
        }
        this.mEglDisplay = EGL10.EGL_NO_DISPLAY;
    }

    @Override // com.adobe.air.FlashEGL
    public void ReleaseGPUResources() {
        if (this.mEglContext != EGL10.EGL_NO_CONTEXT) {
            checkEglError("Before eglMakeCurrent");
            this.mEgl.eglMakeCurrent(this.mEglDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
            checkEglError("After eglMakeCurrent");
            synchronized (this.mEgl) {
                checkEglError("Before eglDestroySurface");
                if (this.mEglWindowSurface != EGL10.EGL_NO_SURFACE) {
                    this.mEgl.eglDestroySurface(this.mEglDisplay, this.mEglWindowSurface);
                    this.mEglWindowSurface = EGL10.EGL_NO_SURFACE;
                }
                checkEglError("After eglDestroySurface (window)");
            }
            if (this.mEglPbufferSurface != EGL10.EGL_NO_SURFACE) {
                checkEglError("Before eglDestroySurface (pbuffer)");
                this.mEgl.eglDestroySurface(this.mEglDisplay, this.mEglPbufferSurface);
                checkEglError("After eglDestroySurface (pbuffer)");
                this.mEglPbufferSurface = EGL10.EGL_NO_SURFACE;
            }
            checkEglError("Before eglDestroyContext");
            this.mEgl.eglDestroyContext(this.mEglDisplay, this.mEglContext);
            checkEglError("After eglDestroyContext");
            this.mEglContext = EGL10.EGL_NO_CONTEXT;
            this.mEglSurface = EGL10.EGL_NO_SURFACE;
        }
    }

    @Override // com.adobe.air.FlashEGL
    public void SwapEGLBuffers() {
        if (12288 == MakeGLCurrent()) {
            checkEglError("Before eglSwapBuffers");
            this.mEgl.eglSwapBuffers(this.mEglDisplay, this.mEglSurface);
            checkEglError("After eglSwapBuffers");
        }
    }

    @Override // com.adobe.air.FlashEGL
    public int MakeGLCurrent() {
        if (this.mEglContext == EGL10.EGL_NO_CONTEXT) {
            return 12294;
        }
        if (this.mEglSurface == EGL10.EGL_NO_SURFACE) {
            return 12301;
        }
        if (this.mEglDisplay == EGL10.EGL_NO_DISPLAY) {
            return 12296;
        }
        if (Build.VERSION.SDK_INT == 23 && Build.MODEL.equals("Nexus 5") && this.mEglSurface == this.mEglPbufferSurface) {
            return 12301;
        }
        checkEglError("Before eglMakeCurrent");
        this.mEgl.eglMakeCurrent(this.mEglDisplay, this.mEglSurface, this.mEglSurface, this.mEglContext);
        return checkEglError("After eglMakeCurrent");
    }

    @Override // com.adobe.air.FlashEGL
    public int CreateWindowSurface(SurfaceView surfaceView, int i) {
        boolean z;
        if (this.mIsGPUOOM) {
            return 12291;
        }
        boolean z2 = surfaceView instanceof AIRWindowSurfaceView;
        if (!(surfaceView instanceof VideoViewAIR) && !(surfaceView instanceof AIRStage3DSurfaceView) && !z2) {
            return 12301;
        }
        if (this.mEglWindowSurface != EGL10.EGL_NO_SURFACE) {
            this.mEglSurface = this.mEglWindowSurface;
            return MakeGLCurrent();
        }
        if (i == this.kSwapPreserveOn) {
            checkEglError("Before eglCreateWindowSurface");
            this.mEglWindowSurface = this.mEgl.eglCreateWindowSurface(this.mEglDisplay, this.mEglConfig, surfaceView.getHolder(), fbWindowSurfaceOnAttrs);
            if (this.mEglWindowSurface == EGL10.EGL_NO_SURFACE) {
                checkEglError("After eglCreateWindowSurface");
                z = false;
            }
            z = true;
        } else {
            if (i == this.kSwapPreserveOff) {
                checkEglError("Before eglCreateWindowSurface");
                this.mEglWindowSurface = this.mEgl.eglCreateWindowSurface(this.mEglDisplay, this.mEglConfig, surfaceView.getHolder(), fbWindowSurfaceOffAttrs);
                if (this.mEglWindowSurface == EGL10.EGL_NO_SURFACE) {
                    checkEglError("After eglCreateWindowSurface");
                    z = false;
                }
            }
            z = true;
        }
        if (this.mEglWindowSurface == EGL10.EGL_NO_SURFACE) {
            checkEglError("Before eglCreateWindowSurface");
            this.mEglWindowSurface = this.mEgl.eglCreateWindowSurface(this.mEglDisplay, this.mEglConfig, surfaceView.getHolder(), null);
            int checkEglError = checkEglError("After eglCreateWindowSurface");
            if (checkEglError != 12288) {
                return checkEglError;
            }
        }
        if (this.mEglWindowSurface == EGL10.EGL_NO_SURFACE) {
            return 12301;
        }
        this.mEglSurface = this.mEglWindowSurface;
        if (z2) {
            ((AIRWindowSurfaceView) surfaceView).setFlashEGL(this);
            Activity activity = ((AIRWindowSurfaceView) surfaceView).getActivityWrapper().getActivity();
            if (activity != null) {
                activity.getWindow().setSoftInputMode(34);
            }
        }
        int[] iArr = {0};
        this.mIsBufferPreserve = false;
        if (z && this.mEgl.eglQuerySurface(this.mEglDisplay, this.mEglSurface, EGL_SWAP_BEHAVIOR, iArr)) {
            this.mIsBufferPreserve = iArr[0] == EGL_BUFFER_PRESERVED;
        }
        return MakeGLCurrent();
    }

    @Override // com.adobe.air.FlashEGL
    public boolean DestroyWindowSurface() {
        if (this.mEglWindowSurface == EGL10.EGL_NO_SURFACE) {
            return false;
        }
        checkEglError("Before eglMakeCurrent");
        this.mEgl.eglMakeCurrent(this.mEglDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
        if (12288 != checkEglError("After eglMakeCurrent")) {
            return false;
        }
        checkEglError("Before eglDestroySurface (window)");
        this.mEgl.eglDestroySurface(this.mEglDisplay, this.mEglWindowSurface);
        if (12288 != checkEglError("After eglDestroySurface (window)")) {
            return false;
        }
        if (this.mEglSurface == this.mEglWindowSurface) {
            this.mEglSurface = EGL10.EGL_NO_SURFACE;
        }
        this.mEglWindowSurface = EGL10.EGL_NO_SURFACE;
        if (this.mEglPbufferSurface != EGL10.EGL_NO_SURFACE && this.mEglContext != EGL10.EGL_NO_CONTEXT) {
            this.mEglSurface = this.mEglPbufferSurface;
            this.mEgl.eglMakeCurrent(this.mEglDisplay, this.mEglSurface, this.mEglSurface, this.mEglContext);
            if (12288 != checkEglError("After eglMakeCurrent")) {
                return false;
            }
        }
        return true;
    }

    @Override // com.adobe.air.FlashEGL
    public boolean IsARGBSurface() {
        return this.mIsARGBSurface;
    }

    @Override // com.adobe.air.FlashEGL
    public boolean IsBufferPreserve() {
        return this.mIsBufferPreserve;
    }

    private int checkEglError(String str) {
        int eglGetError = this.mEgl.eglGetError();
        if (eglGetError != 12288 && !this.mIsGPUOOM && eglGetError == 12291) {
            if (this.mEglWindowSurface != EGL10.EGL_NO_SURFACE) {
                this.mEgl.eglDestroySurface(this.mEglDisplay, this.mEglWindowSurface);
                int eglGetError2 = this.mEgl.eglGetError();
                this.mEglWindowSurface = EGL10.EGL_NO_SURFACE;
                this.mEglSurface = EGL10.EGL_NO_SURFACE;
                if (eglGetError2 != 12288) {
                }
                this.mEglWindowSurface = EGL10.EGL_NO_SURFACE;
                this.mEgl.eglMakeCurrent(this.mEglDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
                if (this.mEgl.eglGetError() != 12288) {
                }
            }
            if (this.mEglPbufferSurface != EGL10.EGL_NO_SURFACE && this.mEglContext != EGL10.EGL_NO_CONTEXT) {
                this.mEglSurface = this.mEglPbufferSurface;
                this.mEgl.eglMakeCurrent(this.mEglDisplay, this.mEglSurface, this.mEglSurface, this.mEglContext);
                if (this.mEgl.eglGetError() != 12288) {
                }
            }
            this.mIsGPUOOM = true;
        }
        return eglGetError;
    }
}
