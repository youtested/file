package com.adobe.air;

import android.R;
import android.text.Editable;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.ExtractedText;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputMethodManager;
import com.adobe.air.utils.AIRLogger;
import com.distriqt.extension.inappbilling.BuildConfig;

/* loaded from: /home/codespace/chem/Chlab_rebuilt.apk_decompiled/build/apk/classes.dex */
public class AndroidInputConnection extends BaseInputConnection {
    private static final String LOG_TAG = "AndroidInputConnection";
    private CharSequence mComposedText;
    private ExtractedTextRequest mExtractRequest;
    private ExtractedText mExtractedText;
    private final AIRWindowSurfaceView mWindowSurfaceView;

    private native String nativeGetTextAfterCursor(int i);

    private native String nativeGetTextBeforeCursor(int i);

    private native int nativeGetTextBoxMaxChars();

    private native void nativeSetSelection(int i, int i2);

    public AndroidInputConnection(AIRWindowSurfaceView aIRWindowSurfaceView) {
        super(aIRWindowSurfaceView, true);
        this.mExtractRequest = null;
        this.mComposedText = null;
        this.mExtractedText = null;
        this.mWindowSurfaceView = aIRWindowSurfaceView;
    }

    @Override // android.view.inputmethod.BaseInputConnection, android.view.inputmethod.InputConnection
    public boolean deleteSurroundingText(int i, int i2) {
        for (int i3 = 0; i3 < i2; i3++) {
            AIRLogger.d(LOG_TAG, "[JP] deleteSurroundingText ");
            this.mWindowSurfaceView.nativeOnKeyListener(0, 22, 0, false, false, false);
            this.mWindowSurfaceView.nativeOnKeyListener(1, 22, 0, false, false, false);
        }
        int i4 = i2 + i;
        for (int i5 = 0; i5 < i4; i5++) {
            AIRLogger.d(LOG_TAG, "[JP] deleteSurroundingText 2 ");
            this.mWindowSurfaceView.nativeOnKeyListener(0, 67, 0, false, false, false);
            this.mWindowSurfaceView.nativeOnKeyListener(1, 67, 0, false, false, false);
        }
        return true;
    }

    @Override // android.view.inputmethod.BaseInputConnection, android.view.inputmethod.InputConnection
    public CharSequence getTextAfterCursor(int i, int i2) {
        return nativeGetTextAfterCursor(i);
    }

    @Override // android.view.inputmethod.BaseInputConnection, android.view.inputmethod.InputConnection
    public CharSequence getTextBeforeCursor(int i, int i2) {
        return nativeGetTextBeforeCursor(i);
    }

    @Override // android.view.inputmethod.BaseInputConnection, android.view.inputmethod.InputConnection
    public ExtractedText getExtractedText(ExtractedTextRequest extractedTextRequest, int i) {
        this.mWindowSurfaceView.setInputConnection(this);
        ExtractedText nativeGetTextContent = this.mWindowSurfaceView.nativeGetTextContent();
        if (nativeGetTextContent == null || nativeGetTextContent.text == null) {
            return null;
        }
        nativeGetTextContent.partialEndOffset = -1;
        nativeGetTextContent.partialStartOffset = -1;
        if ((i & 1) != 0) {
            nativeGetTextContent.startOffset = 0;
            this.mExtractedText = nativeGetTextContent;
            this.mExtractRequest = extractedTextRequest;
            this.mWindowSurfaceView.nativeShowOriginalRect();
            return nativeGetTextContent;
        }
        return nativeGetTextContent;
    }

    @Override // android.view.inputmethod.BaseInputConnection
    public Editable getEditable() {
        return null;
    }

    @Override // android.view.inputmethod.BaseInputConnection, android.view.inputmethod.InputConnection
    public boolean performEditorAction(int i) {
        if (i != 6) {
            return false;
        }
        this.mWindowSurfaceView.showSoftKeyboard(false);
        this.mWindowSurfaceView.nativeDispatchUserTriggeredSkDeactivateEvent();
        return true;
    }

    @Override // android.view.inputmethod.BaseInputConnection, android.view.inputmethod.InputConnection
    public boolean performContextMenuAction(int i) {
        int i2 = 0;
        switch (i) {
            case R.id.selectAll:
                break;
            case R.id.cut:
                if (this.mWindowSurfaceView.nativeIsTextSelected()) {
                    i2 = 1;
                    break;
                } else {
                    i2 = 2;
                    break;
                }
            case R.id.copy:
                if (this.mWindowSurfaceView.nativeIsTextSelected()) {
                    i2 = 3;
                    break;
                } else {
                    i2 = 4;
                    break;
                }
            case R.id.paste:
                i2 = 5;
                break;
            case R.id.copyUrl:
            case R.id.inputExtractEditText:
            case R.id.keyboardView:
            case R.id.closeButton:
            default:
                return false;
            case R.id.switchInputMethod:
                i2 = 6;
                break;
            case R.id.startSelectingText:
                i2 = 7;
                break;
            case R.id.stopSelectingText:
                i2 = 8;
                break;
        }
        return this.mWindowSurfaceView.onTextBoxContextMenuItem(i2);
    }

    @Override // android.view.inputmethod.BaseInputConnection, android.view.inputmethod.InputConnection
    public boolean setComposingRegion(int i, int i2) {
        if (i <= i2) {
            i2 = i;
            i = i2;
        }
        ExtractedText nativeGetTextContent = this.mWindowSurfaceView.nativeGetTextContent();
        if (i <= nativeGetTextContent.text.length() && i2 >= 0) {
            this.mComposedText = nativeGetTextContent.text.subSequence(i2, i);
            return true;
        }
        String charSequence = nativeGetTextContent.text.toString();
        this.mComposedText = charSequence.substring(charSequence.lastIndexOf(32) + 1);
        return true;
    }

    @Override // android.view.inputmethod.BaseInputConnection, android.view.inputmethod.InputConnection
    public boolean setComposingText(CharSequence charSequence, int i) {
        int nativeGetTextBoxMaxChars = nativeGetTextBoxMaxChars();
        if (nativeGetTextBoxMaxChars != 0 && charSequence.length() > 0) {
            ExtractedText nativeGetTextContent = this.mWindowSurfaceView.nativeGetTextContent();
            int min = Math.min((this.mComposedText != null ? this.mComposedText.length() : 0) + (((nativeGetTextBoxMaxChars - nativeGetTextContent.text.length()) + nativeGetTextContent.selectionEnd) - nativeGetTextContent.selectionStart), charSequence.length());
            if (min > 0) {
                charSequence = charSequence.subSequence(0, min);
            } else {
                charSequence = null;
            }
        }
        if (charSequence != null) {
            AIRLogger.d(LOG_TAG, "[JP] setComposingText " + ((Object) charSequence));
            writeText(charSequence);
            this.mComposedText = charSequence;
            if (i <= 0) {
                int length = charSequence.length() + Math.abs(i);
                for (int i2 = 0; i2 < length; i2++) {
                    AIRLogger.d(LOG_TAG, "[JP] setComposingText " + ((Object) charSequence));
                    this.mWindowSurfaceView.nativeOnKeyListener(0, 21, 0, false, false, false);
                    this.mWindowSurfaceView.nativeOnKeyListener(1, 21, 0, false, false, false);
                }
            } else if (i > 1) {
                int i3 = i - 1;
                for (int i4 = 0; i4 < i3; i4++) {
                    AIRLogger.d(LOG_TAG, "[JP] setComposingText 2 " + ((Object) charSequence));
                    this.mWindowSurfaceView.nativeOnKeyListener(0, 22, 0, false, false, false);
                    this.mWindowSurfaceView.nativeOnKeyListener(1, 22, 0, false, false, false);
                }
            }
        }
        return true;
    }

    @Override // android.view.inputmethod.BaseInputConnection, android.view.inputmethod.InputConnection
    public boolean finishComposingText() {
        this.mComposedText = null;
        if (this.mWindowSurfaceView.getIsFullScreen() && !this.mWindowSurfaceView.IsSurfaceChangedForSoftKeyboard()) {
            this.mWindowSurfaceView.nativeShowOriginalRect();
            return true;
        }
        return true;
    }

    @Override // android.view.inputmethod.BaseInputConnection, android.view.inputmethod.InputConnection
    public boolean setSelection(int i, int i2) {
        nativeSetSelection(i, i2);
        return true;
    }

    @Override // android.view.inputmethod.BaseInputConnection, android.view.inputmethod.InputConnection
    public boolean commitText(CharSequence charSequence, int i) {
        AIRLogger.d(LOG_TAG, "[JP] setComposingText " + ((Object) charSequence));
        writeText(charSequence);
        this.mComposedText = null;
        return true;
    }

    private void writeText(CharSequence charSequence) {
        int i;
        int length = charSequence.length();
        if (this.mComposedText == null) {
            i = 0;
        } else {
            int length2 = this.mComposedText.length();
            int min = Math.min(length, length2);
            i = 0;
            while (i < min && charSequence.charAt(i) == this.mComposedText.charAt(i)) {
                i++;
            }
            for (int i2 = i; i2 < length2; i2++) {
                AIRLogger.d(LOG_TAG, "[JP] writeText " + ((Object) charSequence));
                this.mWindowSurfaceView.nativeOnKeyListener(0, 67, 0, false, false, false);
                this.mWindowSurfaceView.nativeOnKeyListener(1, 67, 0, false, false, false);
            }
        }
        while (i < length) {
            AIRLogger.d(LOG_TAG, "[JP] writeText 2 " + ((Object) charSequence));
            this.mWindowSurfaceView.nativeOnKeyListener(0, 0, charSequence.charAt(i), false, false, false);
            this.mWindowSurfaceView.nativeOnKeyListener(1, 0, charSequence.charAt(i), false, false, false);
            i++;
        }
        updateIMEText();
    }

    public void updateIMEText() {
        InputMethodManager inputMethodManager;
        if (this.mExtractRequest != null && (inputMethodManager = this.mWindowSurfaceView.getInputMethodManager()) != null && inputMethodManager.isActive(this.mWindowSurfaceView)) {
            ExtractedText nativeGetTextContent = this.mWindowSurfaceView.nativeGetTextContent();
            boolean equals = nativeGetTextContent.text.toString().equals(this.mExtractedText.text.toString());
            if (!equals || nativeGetTextContent.selectionStart != this.mExtractedText.selectionStart || nativeGetTextContent.selectionEnd != this.mExtractedText.selectionEnd || nativeGetTextContent.flags != this.mExtractedText.flags) {
                nativeGetTextContent.startOffset = 0;
                if (equals) {
                    nativeGetTextContent.partialStartOffset = 0;
                    nativeGetTextContent.partialEndOffset = 0;
                    nativeGetTextContent.text = BuildConfig.FLAVOR;
                    this.mExtractedText.flags = nativeGetTextContent.flags;
                    this.mExtractedText.selectionStart = nativeGetTextContent.selectionStart;
                    this.mExtractedText.selectionEnd = nativeGetTextContent.selectionEnd;
                } else {
                    nativeGetTextContent.partialStartOffset = -1;
                    nativeGetTextContent.partialEndOffset = -1;
                    this.mExtractedText = nativeGetTextContent;
                }
                inputMethodManager.updateExtractedText(this.mWindowSurfaceView, this.mExtractRequest.token, nativeGetTextContent);
            }
        }
    }

    public void Reset() {
        this.mComposedText = null;
        this.mExtractRequest = null;
        this.mExtractedText = null;
    }
}
