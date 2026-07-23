package com.adobe.air;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.adobe.air.AndroidLocale;
import com.distriqt.extension.inappbilling.BuildConfig;

/* loaded from: /home/codespace/chem/Chlab_rebuilt.apk_decompiled/build/apk/classes.dex */
public final class RemoteDebuggerDialog {
    private final Activity mActivity;

    /* JADX INFO: Access modifiers changed from: package-private */
    public RemoteDebuggerDialog(Activity activity) {
        this.mActivity = activity;
    }

    public void createAndShowDialog(String str) {
        LinearLayout linearLayout = new LinearLayout(this.mActivity);
        linearLayout.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        linearLayout.setOrientation(1);
        TextView textView = new TextView(this.mActivity);
        String str2 = BuildConfig.FLAVOR;
        if (str.length() > 0) {
            str2 = AndroidLocale.GetLocalizedString(AndroidLocale.STRING_ID.IDA_DEBUGGER_ERROR_MESSAGE).replaceFirst("%1", str) + "\n";
        }
        textView.setText(str2 + AndroidLocale.GetLocalizedString(AndroidLocale.STRING_ID.IDA_DEBUGGER_ENTERIP_MESSAGE));
        textView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        textView.setPadding(20, 20, 20, 20);
        final EditText editText = new EditText(this.mActivity);
        editText.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        editText.setHeight(30);
        editText.setWidth(25);
        linearLayout.addView(textView);
        linearLayout.addView(editText);
        AlertDialog.Builder builder = new AlertDialog.Builder(this.mActivity);
        builder.setView(linearLayout);
        builder.setTitle(AndroidConstants.ADOBE_AIR);
        builder.setPositiveButton(AndroidLocale.GetLocalizedString(AndroidLocale.STRING_ID.IDA_OK), new DialogInterface.OnClickListener() { // from class: com.adobe.air.RemoteDebuggerDialog.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                RemoteDebuggerDialog.this.gotResultFromDialog(true, editText.getText().toString());
            }
        });
        builder.setNegativeButton(AndroidLocale.GetLocalizedString(AndroidLocale.STRING_ID.IDA_CANCEL), new DialogInterface.OnClickListener() { // from class: com.adobe.air.RemoteDebuggerDialog.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                RemoteDebuggerDialog.this.gotResultFromDialog(false, null);
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: com.adobe.air.RemoteDebuggerDialog.3
            @Override // android.content.DialogInterface.OnCancelListener
            public void onCancel(DialogInterface dialogInterface) {
                RemoteDebuggerDialog.this.gotResultFromDialog(false, null);
            }
        });
        builder.show();
    }

    public void gotResultFromDialog(boolean z, String str) {
        AndroidActivityWrapper.GetAndroidActivityWrapper().gotResultFromDialog(z, str);
    }
}
