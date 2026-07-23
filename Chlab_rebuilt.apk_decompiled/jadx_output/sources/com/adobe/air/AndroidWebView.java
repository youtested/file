package com.adobe.air;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import com.adobe.air.AndroidActivityWrapper;
import com.adobe.air.wand.view.CompanionView;
import com.distriqt.extension.inappbilling.BuildConfig;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;

/* loaded from: /home/codespace/chem/Chlab_rebuilt.apk_decompiled/build/apk/classes.dex */
public class AndroidWebView implements AndroidActivityWrapper.StateChangeCallback {
    private static final int ERROR_OTHER = 0;
    private static final int ERROR_PROTOCOL_UNSUPPORTED = 3229;
    private static final int FOCUS_DOWN = 3;
    private static final int FOCUS_NONE = 1;
    private static final int FOCUS_UP = 2;
    private static final String LOG_TAG = "AndroidWebView";
    private AIRWindowSurfaceView mAIRSurface;
    private WebViewCustomView mCustomViewHolder;
    private RelativeLayout mLayout;
    private Rect mBounds = null;
    private Rect mGlobalBounds = null;
    private String mUrl = null;
    private long mInternalReference = 0;
    private Context mContext = AndroidActivityWrapper.GetAndroidActivityWrapper().getActivity();
    private AndroidWebViewImpl mView = new AndroidWebViewImpl(this.mContext);

    /* JADX INFO: Access modifiers changed from: private */
    public native void dispatchFocusIn(long j, int i);

    /* JADX INFO: Access modifiers changed from: private */
    public native void dispatchFocusOut(long j, int i);

    private native void dispatchLoadComplete(long j);

    private native void dispatchLoadError(long j, String str, int i);

    private native boolean dispatchLocationChange(long j);

    private native boolean dispatchLocationChanging(long j, String str);

    /* loaded from: /home/codespace/chem/Chlab_rebuilt.apk_decompiled/build/apk/classes.dex */
    public class WebViewCustomView {
        private WebChromeClient.CustomViewCallback mCallback;
        private FrameLayout mCustomViewHolder;

        public WebViewCustomView() {
        }

        public void onShowCustomView(View view, WebChromeClient.CustomViewCallback customViewCallback) {
            if (this.mCallback != null) {
                customViewCallback.onCustomViewHidden();
                return;
            }
            this.mCallback = customViewCallback;
            View view2 = AndroidActivityWrapper.GetAndroidActivityWrapper().getView();
            view2.setVisibility(8);
            RelativeLayout overlaysLayout = AndroidActivityWrapper.GetAndroidActivityWrapper().getOverlaysLayout(false);
            if (overlaysLayout != null) {
                overlaysLayout.setVisibility(8);
            }
            this.mCustomViewHolder = new FrameLayout(AndroidWebView.this.mContext) { // from class: com.adobe.air.AndroidWebView.WebViewCustomView.1
                @Override // android.view.ViewGroup, android.view.View
                public boolean dispatchKeyEvent(KeyEvent keyEvent) {
                    if (super.dispatchKeyEvent(keyEvent)) {
                        return true;
                    }
                    if (keyEvent.getAction() == 0 && keyEvent.getKeyCode() == 4) {
                        WebViewCustomView.this.onHideCustomView();
                        return true;
                    }
                    return false;
                }
            };
            this.mCustomViewHolder.setBackgroundColor(-16777216);
            ((ViewGroup) view2.getParent()).addView(this.mCustomViewHolder, new FrameLayout.LayoutParams(-1, -1));
            this.mCustomViewHolder.addView(view, new FrameLayout.LayoutParams(-1, -1, 17));
            this.mCustomViewHolder.bringToFront();
            this.mCustomViewHolder.requestFocus();
        }

        public void onHideCustomView() {
            if (this.mCallback != null && this.mCustomViewHolder != null) {
                AndroidActivityWrapper.GetAndroidActivityWrapper().getView().setVisibility(0);
                RelativeLayout overlaysLayout = AndroidActivityWrapper.GetAndroidActivityWrapper().getOverlaysLayout(false);
                if (overlaysLayout != null) {
                    overlaysLayout.setVisibility(0);
                }
                ((ViewGroup) this.mCustomViewHolder.getParent()).removeView(this.mCustomViewHolder);
                this.mCustomViewHolder = null;
                this.mCallback.onCustomViewHidden();
                this.mCallback = null;
            }
        }
    }

    /* loaded from: /home/codespace/chem/Chlab_rebuilt.apk_decompiled/build/apk/classes.dex */
    public class AndroidWebViewImpl extends WebView {
        private int mLastFocusDirection;
        private View m_focusedChildView;
        private boolean m_hasFocus;
        private boolean m_inRequestChildFocus;

        public AndroidWebViewImpl(Context context) {
            super(context);
            this.m_inRequestChildFocus = false;
            this.m_focusedChildView = null;
            this.m_hasFocus = false;
            this.mLastFocusDirection = 0;
        }

        public AndroidWebViewImpl(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            this.m_inRequestChildFocus = false;
            this.m_focusedChildView = null;
            this.m_hasFocus = false;
            this.mLastFocusDirection = 0;
        }

        public AndroidWebViewImpl(Context context, AttributeSet attributeSet, int i) {
            super(context, attributeSet, i);
            this.m_inRequestChildFocus = false;
            this.m_focusedChildView = null;
            this.m_hasFocus = false;
            this.mLastFocusDirection = 0;
        }

        @Override // android.view.ViewGroup, android.view.View
        public boolean dispatchTouchEvent(MotionEvent motionEvent) {
            if (!this.m_hasFocus) {
                requestFocus();
            }
            return super.dispatchTouchEvent(motionEvent);
        }

        @Override // android.view.ViewGroup, android.view.ViewParent
        public void requestChildFocus(View view, View view2) {
            this.m_inRequestChildFocus = true;
            if (!this.m_hasFocus) {
                setRealFocus(true);
            }
            try {
                this.m_focusedChildView = view;
                super.requestChildFocus(view, view2);
            } finally {
                this.m_inRequestChildFocus = false;
            }
        }

        @Override // android.view.ViewGroup, android.view.ViewParent
        public void clearChildFocus(View view) {
            this.m_inRequestChildFocus = true;
            try {
                super.clearChildFocus(view);
            } finally {
                this.m_inRequestChildFocus = false;
            }
        }

        @Override // android.view.ViewGroup, android.view.View
        public void clearFocus() {
            if (this.m_focusedChildView != null) {
                this.m_focusedChildView.clearFocus();
                if (AutoCompleteTextView.class.isInstance(this.m_focusedChildView)) {
                    AndroidWebView.this.mView.removeView(this.m_focusedChildView);
                }
                this.m_focusedChildView = null;
                setRealFocus(false);
                return;
            }
            super.clearFocus();
        }

        private void setRealFocus(boolean z) {
            super.onFocusChanged(z, 0, new Rect());
            invalidate();
            dispatchFocusEvent(z, 0);
        }

        public boolean isInTextEditingMode() {
            return this.m_hasFocus && !(this.m_focusedChildView == null && getChildCount() == 0);
        }

        private void dispatchFocusEvent(boolean z, int i) {
            int i2 = 2;
            if (this.m_hasFocus != z) {
                this.m_hasFocus = z;
                AndroidWebView androidWebView = AndroidWebView.this;
                if (androidWebView.mInternalReference != 0) {
                    if (AndroidWebView.this.mAIRSurface != null) {
                        AndroidWebView.this.mAIRSurface.updateFocusedStageWebView(androidWebView, this.m_hasFocus);
                    }
                    if ((i & 2) == 2) {
                        i2 = 3;
                    } else if ((i & 1) != 1) {
                        i2 = 1;
                    }
                    if (z) {
                        androidWebView.dispatchFocusIn(AndroidWebView.this.mInternalReference, i2);
                    } else {
                        androidWebView.dispatchFocusOut(AndroidWebView.this.mInternalReference, i2);
                    }
                }
            }
        }

        @Override // android.webkit.WebView, android.view.View
        protected void onFocusChanged(boolean z, int i, Rect rect) {
            super.onFocusChanged(z, i, rect);
            if (!this.m_inRequestChildFocus || this.m_focusedChildView == null || !this.m_hasFocus) {
                if (i == 0) {
                    i = this.mLastFocusDirection;
                }
                this.mLastFocusDirection = 0;
                dispatchFocusEvent(z, i);
            }
        }

        @Override // android.webkit.WebView, android.view.ViewGroup, android.view.View
        public boolean dispatchKeyEvent(KeyEvent keyEvent) {
            boolean dispatchKeyEvent = super.dispatchKeyEvent(keyEvent);
            if (!dispatchKeyEvent && keyEvent.getAction() == 0) {
                int i = 0;
                if (keyEvent.getKeyCode() == 19) {
                    i = 33;
                } else if (keyEvent.getKeyCode() == 20) {
                    i = 130;
                }
                if (i != 0) {
                    AndroidWebView androidWebView = AndroidWebView.this;
                    this.mLastFocusDirection = i;
                    androidWebView.setStageFocus(i);
                    return true;
                }
            }
            if (!dispatchKeyEvent) {
                return AndroidWebView.this.mAIRSurface.dispatchKeyEvent(keyEvent);
            }
            return dispatchKeyEvent;
        }
    }

    public AndroidWebView() {
        WebSettings settings = this.mView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setBuiltInZoomControls(true);
        settings.setNeedInitialFocus(true);
        boolean webContentsDebuggingEnabled = AndroidActivityWrapper.GetAndroidActivityWrapper().getWebContentsDebuggingEnabled();
        if (webContentsDebuggingEnabled && Build.VERSION.SDK_INT >= 19) {
            AndroidWebViewImpl androidWebViewImpl = this.mView;
            AndroidWebViewImpl.setWebContentsDebuggingEnabled(webContentsDebuggingEnabled);
        }
        if (Build.VERSION.SDK_INT < 18) {
            settings.setPluginState(WebSettings.PluginState.ON);
        }
        if (Build.VERSION.SDK_INT > 15) {
            settings.setAllowUniversalAccessFromFileURLs(true);
        }
        this.mView.setScrollbarFadingEnabled(true);
        this.mView.setScrollBarStyle(CompanionView.kTouchMetaStateIsPen);
        this.mView.setWebChromeClient(new WebChromeClient() { // from class: com.adobe.air.AndroidWebView.1
            private ValueCallback<Uri> mUploadMessage;

            @Override // android.webkit.WebChromeClient
            public void onShowCustomView(View view, WebChromeClient.CustomViewCallback customViewCallback) {
                if (AndroidWebView.this.mCustomViewHolder == null) {
                    AndroidWebView.this.mCustomViewHolder = new WebViewCustomView();
                }
                AndroidWebView.this.mCustomViewHolder.onShowCustomView(view, customViewCallback);
            }

            @Override // android.webkit.WebChromeClient
            public void onHideCustomView() {
                if (AndroidWebView.this.mCustomViewHolder != null) {
                    AndroidWebView.this.mCustomViewHolder.onHideCustomView();
                }
            }

            public void openFileChooser(ValueCallback<Uri> valueCallback) {
                if (this.mUploadMessage == null) {
                    this.mUploadMessage = valueCallback;
                    final AndroidActivityWrapper GetAndroidActivityWrapper = AndroidActivityWrapper.GetAndroidActivityWrapper();
                    GetAndroidActivityWrapper.addActivityResultListener(new AndroidActivityWrapper.ActivityResultCallback() { // from class: com.adobe.air.AndroidWebView.1.1
                        @Override // com.adobe.air.AndroidActivityWrapper.ActivityResultCallback
                        public void onActivityResult(int i, int i2, Intent intent) {
                            if (i == 5) {
                                if (AnonymousClass1.this.mUploadMessage != null) {
                                    AnonymousClass1.this.mUploadMessage.onReceiveValue((intent == null || i2 != -1) ? null : intent.getData());
                                    AnonymousClass1.this.mUploadMessage = null;
                                }
                                GetAndroidActivityWrapper.removeActivityResultListener(this);
                            }
                        }
                    });
                    Intent intent = new Intent("android.intent.action.GET_CONTENT");
                    intent.addCategory("android.intent.category.OPENABLE");
                    intent.setType("*/*");
                    GetAndroidActivityWrapper.getActivity().startActivityForResult(Intent.createChooser(intent, BuildConfig.FLAVOR), 5);
                }
            }
        });
        this.mView.setWebViewClient(new WebViewClient() { // from class: com.adobe.air.AndroidWebView.2
            private String mUrl = null;
            private String mLastPageStartedUrl = null;
            private String mNoCompleteForUrl = null;

            @Override // android.webkit.WebViewClient
            public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
                super.onPageStarted(webView, str, bitmap);
                this.mUrl = str;
                this.mLastPageStartedUrl = str;
            }

            @Override // android.webkit.WebViewClient
            public boolean shouldOverrideUrlLoading(WebView webView, String str) {
                boolean onLocationChanging = this.onLocationChanging(str);
                if (onLocationChanging) {
                    this.mNoCompleteForUrl = str;
                }
                return onLocationChanging;
            }

            @Override // android.webkit.WebViewClient
            public void onPageFinished(WebView webView, String str) {
                if (str != null && this.mNoCompleteForUrl != null && str.equals(this.mNoCompleteForUrl)) {
                    this.mNoCompleteForUrl = null;
                } else {
                    this.mUrl = str;
                    this.onLoadComplete(str);
                }
            }

            @Override // android.webkit.WebViewClient
            public void doUpdateVisitedHistory(WebView webView, String str, boolean z) {
                if (this.mLastPageStartedUrl != null && this.mLastPageStartedUrl.equals(str)) {
                    this.onLocationChange(str);
                }
            }

            @Override // android.webkit.WebViewClient
            public void onReceivedError(WebView webView, int i, String str, String str2) {
                this.mNoCompleteForUrl = str2;
                if (i == -10) {
                    this.onLoadError(str2, "[" + i + "] " + str + ": " + str2, AndroidWebView.ERROR_PROTOCOL_UNSUPPORTED);
                } else {
                    this.onLoadError(str2, "[" + i + "] " + str + ": " + str2, 0);
                }
            }
        });
    }

    @Override // com.adobe.air.AndroidActivityWrapper.StateChangeCallback
    public void onActivityStateChanged(AndroidActivityWrapper.ActivityState activityState) {
        try {
            Method method = WebView.class.getMethod("isPaused", new Class[0]);
            Method method2 = WebView.class.getMethod("onResume", new Class[0]);
            Method method3 = WebView.class.getMethod("onPause", new Class[0]);
            boolean booleanValue = ((Boolean) method.invoke(this.mView, new Object[0])).booleanValue();
            switch (activityState) {
                case RESUMED:
                    if (booleanValue) {
                        method2.invoke(this.mView, new Object[0]);
                        this.mView.resumeTimers();
                        break;
                    }
                    break;
                case PAUSED:
                    if (!booleanValue) {
                        this.mView.pauseTimers();
                        method3.invoke(this.mView, new Object[0]);
                        break;
                    }
                    break;
            }
        } catch (Exception e) {
        }
        switch (activityState) {
            case STARTED:
                if (this.mCustomViewHolder == null) {
                    this.mCustomViewHolder = new WebViewCustomView();
                    return;
                }
                return;
            case DESTROYED:
                if (this.mCustomViewHolder != null) {
                    this.mCustomViewHolder.onHideCustomView();
                    this.mCustomViewHolder = null;
                    return;
                }
                return;
            default:
                return;
        }
    }

    @Override // com.adobe.air.AndroidActivityWrapper.StateChangeCallback
    public void onConfigurationChanged(Configuration configuration) {
    }

    public void setInternalReference(long j) {
        this.mInternalReference = j;
    }

    public void destroyInternals() {
        removedFromStage();
        this.mInternalReference = 0L;
        this.mView.destroy();
        this.mView = null;
    }

    public void stop() {
        this.mView.stopLoading();
    }

    public void reload() {
        this.mView.reload();
    }

    public void setMediaPlaybackRequiresUserAction(boolean z) {
        Log.d(LOG_TAG, "Java - setting val = " + z);
        this.mView.getSettings().setMediaPlaybackRequiresUserGesture(z);
    }

    public boolean getMediaPlaybackRequiresUserAction() {
        return this.mView.getSettings().getMediaPlaybackRequiresUserGesture();
    }

    public void goBack() {
        this.mView.goBack();
    }

    public void goForward() {
        this.mView.goForward();
    }

    public boolean canGoBack() {
        return this.mView.canGoBack();
    }

    public boolean canGoForward() {
        return this.mView.canGoForward();
    }

    public void addedToStage(AIRWindowSurfaceView aIRWindowSurfaceView) {
        if (this.mLayout != null) {
            removedFromStage();
        }
        this.mAIRSurface = aIRWindowSurfaceView;
        AndroidActivityWrapper activityWrapper = aIRWindowSurfaceView.getActivityWrapper();
        activityWrapper.addActivityStateChangeListner(this);
        this.mLayout = activityWrapper.getOverlaysLayout(true);
        this.mLayout.addView(this.mView, new RelativeLayout.LayoutParams(0, 0));
    }

    public void removedFromStage() {
        if (this.mLayout != null) {
            this.mLayout.removeView(this.mView);
            this.mLayout = null;
            AndroidActivityWrapper activityWrapper = this.mAIRSurface.getActivityWrapper();
            activityWrapper.didRemoveOverlay();
            activityWrapper.removeActivityStateChangeListner(this);
            this.mAIRSurface.updateFocusedStageWebView(this, false);
        }
        this.mAIRSurface = null;
    }

    public void setVisibility(boolean z) {
        int i = z ? 0 : 4;
        if (this.mView.getVisibility() != i) {
            this.mView.setVisibility(i);
        }
    }

    public long updateViewBoundsWithKeyboard(int i) {
        int i2 = 0;
        this.mGlobalBounds = this.mBounds;
        if (this.mAIRSurface != null && isInTextEditingMode()) {
            Rect rect = new Rect(0, 0, this.mAIRSurface.getVisibleBoundWidth(), this.mAIRSurface.getVisibleBoundHeight());
            if (!rect.contains(this.mBounds)) {
                int min = Math.min(Math.max(0, this.mBounds.top), i);
                int min2 = Math.min(Math.max(0, this.mBounds.bottom), i);
                if (min != min2 && (i2 = min2 - rect.bottom) > 0) {
                    if (i2 > min) {
                        this.mGlobalBounds = new Rect(this.mBounds);
                        this.mGlobalBounds.bottom = rect.bottom + min;
                        i2 = min;
                    }
                }
                return 0L;
            }
        }
        refreshGlobalBounds();
        return i2;
    }

    public void resetGlobalBounds() {
        this.mGlobalBounds = this.mBounds;
        refreshGlobalBounds();
    }

    private void refreshGlobalBounds() {
        if (this.mView != null) {
            this.mView.post(new Runnable() { // from class: com.adobe.air.AndroidWebView.3
                @Override // java.lang.Runnable
                public void run() {
                    if (AndroidWebView.this.mView != null) {
                        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(AndroidWebView.this.mGlobalBounds.width(), AndroidWebView.this.mGlobalBounds.height());
                        layoutParams.leftMargin = AndroidWebView.this.mGlobalBounds.left;
                        layoutParams.topMargin = AndroidWebView.this.mGlobalBounds.top;
                        AndroidWebView.this.mView.setLayoutParams(layoutParams);
                        AndroidWebView.this.mView.requestLayout();
                    }
                }
            });
        }
    }

    public void adjustViewBounds(double d, double d2, double d3, double d4) {
        this.mBounds = new Rect((int) d, (int) d2, (int) (d + d3), (int) (d2 + d4));
        this.mGlobalBounds = this.mBounds;
        refreshGlobalBounds();
    }

    public void loadString(String str, String str2) {
        byte[] bArr;
        try {
            byte[] bytes = str.getBytes("utf-8");
            if (bytes.length >= 3 && bytes[0] == 239 && bytes[1] == 187 && bytes[2] == 191) {
                bArr = bytes;
            } else {
                bArr = new byte[bytes.length + 3];
                System.arraycopy(bytes, 0, bArr, 3, bytes.length);
                bArr[0] = -17;
                bArr[1] = -69;
                bArr[2] = -65;
            }
            this.mView.loadData(Base64.encodeToString(bArr, 0, bArr.length, 2), "text/html", "base64");
        } catch (UnsupportedEncodingException e) {
        }
    }

    public void loadURL(String str) {
        if (str != null) {
            this.mView.loadUrl(str);
        }
    }

    public boolean onLocationChange(String str) {
        if (this.mInternalReference == 0) {
            return true;
        }
        this.mUrl = str;
        return dispatchLocationChange(this.mInternalReference);
    }

    private String decodeURL(String str) {
        try {
            return new String(URLUtil.decode(str.getBytes()));
        } catch (IllegalArgumentException e) {
            return str;
        }
    }

    public boolean onLocationChanging(String str) {
        if (this.mInternalReference == 0) {
            return true;
        }
        return dispatchLocationChanging(this.mInternalReference, decodeURL(str));
    }

    public void onLoadComplete(String str) {
        if (this.mInternalReference != 0) {
            this.mUrl = str;
            dispatchLoadComplete(this.mInternalReference);
        }
    }

    public void onLoadError(String str, String str2, int i) {
        if (this.mInternalReference != 0) {
            this.mUrl = str;
            dispatchLoadError(this.mInternalReference, str2, i);
        }
    }

    public String getCurrentLocation() {
        String str = this.mUrl;
        if (str == null) {
            return "about:blank";
        }
        return decodeURL(str);
    }

    public String getPageTitle() {
        String title = this.mView.getTitle();
        if (title == null) {
            return BuildConfig.FLAVOR;
        }
        return title;
    }

    public void assignFocus(int i) {
        switch (i) {
            case 1:
                this.mView.requestFocus();
                return;
            case 2:
                this.mView.pageUp(true);
                this.mView.requestFocus(33);
                return;
            case 3:
                this.mView.pageDown(true);
                this.mView.requestFocus(130);
                return;
            default:
                return;
        }
    }

    public void clearFocus() {
        this.mView.clearFocus();
        this.mAIRSurface.requestFocus();
    }

    public void setStageFocus(int i) {
        this.mView.clearFocus();
        this.mAIRSurface.requestFocus(i);
    }

    public Bitmap captureSnapshot(int i, int i2) {
        Bitmap bitmap = null;
        if (i >= 0 && i2 >= 0 && (i != 0 || i2 != 0)) {
            bitmap = Bitmap.createBitmap(i, i2, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            canvas.translate(-this.mView.getScrollX(), -this.mView.getScrollY());
            boolean isHorizontalScrollBarEnabled = this.mView.isHorizontalScrollBarEnabled();
            boolean isVerticalScrollBarEnabled = this.mView.isVerticalScrollBarEnabled();
            this.mView.setHorizontalScrollBarEnabled(false);
            this.mView.setVerticalScrollBarEnabled(false);
            try {
                this.mView.draw(canvas);
            } catch (Exception e) {
                Picture capturePicture = this.mView.capturePicture();
                float scale = this.mView.getScale();
                canvas.scale(scale, scale);
                capturePicture.draw(canvas);
            }
            this.mView.setHorizontalScrollBarEnabled(isHorizontalScrollBarEnabled);
            this.mView.setVerticalScrollBarEnabled(isVerticalScrollBarEnabled);
        }
        return bitmap;
    }

    public boolean isInTextEditingMode() {
        return this.mView.isInTextEditingMode();
    }
}
