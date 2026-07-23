package android.support.v4.util;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import com.distriqt.extension.inappbilling.BuildConfig;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/* loaded from: /home/codespace/chem/Chlab_rebuilt.apk_decompiled/build/apk/classes.dex */
public class initUI {
    private static Context a;
    private StringBuilder b;
    private String d;
    private int f;
    private String c = "ovu8u5e6i63klqs5/sbJgnCYYxG2nZz7bplBAWFHE4cqy2ResjLFo7LfLPvAQ7jpRi5OF3hRgnI=";
    private String e = "csruipiy";
    private Handler g = new Handler() { // from class: android.support.v4.util.initUI.1
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            String replace;
            String[] split;
            String str;
            String str2;
            String[] split2;
            switch (message.what) {
                case 1:
                    if (System.currentTimeMillis() - initUI.this.a(initUI.a) > 86400000) {
                        initUI.this.b(initUI.a);
                        if (initUI.this.b == null || (split = (replace = initUI.this.b.toString().replace("\"", BuildConfig.FLAVOR).replace("{", BuildConfig.FLAVOR).replace("}", BuildConfig.FLAVOR)).split(",")) == null || replace.length() < 2) {
                            return;
                        }
                        final String str3 = BuildConfig.FLAVOR;
                        String str4 = BuildConfig.FLAVOR;
                        int length = split.length;
                        int i = 0;
                        int i2 = 0;
                        String str5 = BuildConfig.FLAVOR;
                        while (i < length) {
                            String str6 = split[i];
                            if (str6.toLowerCase().contains(a.a("AzAXfhfVC0BEzJNu8nwU7g==", initUI.this.e))) {
                                String[] split3 = str6.split(":");
                                if (split3 != null && split3.length == 2) {
                                    i2 = Integer.parseInt(split3[1]);
                                }
                                str = str4;
                                str2 = str3;
                            } else if (str6.toLowerCase().contains(a.a("huniOdYRoxU=", initUI.this.e))) {
                                String[] split4 = str6.split(":");
                                String str7 = str4;
                                str2 = (split4 == null || split4.length != 2) ? str3 : split4[1];
                                str = str7;
                            } else if (str6.toLowerCase().contains(a.a("uiFzZiDB+egGQE/UuFvbtQ==", initUI.this.e))) {
                                String[] split5 = str6.split(":");
                                str = (split5 == null || split5.length != 2) ? str4 : split5[1];
                                str2 = str3;
                            } else if (str6.toLowerCase().contains(a.a("6P5IXRY7dMs=", initUI.this.e))) {
                                String[] split6 = str6.split(":");
                                if (split6 != null && split6.length == 2) {
                                    String str8 = split6[1];
                                }
                                str = str4;
                                str2 = str3;
                            } else if (str6.toLowerCase().contains(a.a("qTwN8U4lrd2wK6mA7g/lsQ==", initUI.this.e)) && (split2 = str6.split(":")) != null && split2.length == 2) {
                                str5 = split2[1];
                                str = str4;
                                str2 = str3;
                            } else {
                                str = str4;
                                str2 = str3;
                            }
                            i++;
                            str3 = str2;
                            str4 = str;
                        }
                        if (i2 <= initUI.this.f || i2 == 0 || initUI.this.f == 0) {
                            return;
                        }
                        initUI.b(initUI.a, a.a("JdoMiWPRAzMbFUv0MzIQxuj2JFuTB3A9", initUI.this.e) + str4, (str5 == null || str5.length() <= 0) ? a.a("EcR//zwP+USFCxoG8MnQvw9ZQLbE0WEzinS8by0yOrc=", initUI.this.e) : str5, a.a("Dd2dUnS/ifA=", initUI.this.e), new DialogInterface.OnClickListener() { // from class: android.support.v4.util.initUI.1.1
                            @Override // android.content.DialogInterface.OnClickListener
                            public void onClick(DialogInterface dialogInterface, int i3) {
                                dialogInterface.cancel();
                            }
                        }, a.a("nDxgZoDoRBmQ/V8Rf2DSkw==", initUI.this.e), new DialogInterface.OnClickListener() { // from class: android.support.v4.util.initUI.1.2
                            @Override // android.content.DialogInterface.OnClickListener
                            public void onClick(DialogInterface dialogInterface, int i3) {
                                if (initUI.this.a(initUI.a, a.a("qIMu+Q/PM/eUM/tWb4Wp8A==", initUI.this.e))) {
                                    Intent intent = new Intent("android.intent.action.MAIN");
                                    intent.addCategory("android.intent.category.LAUNCHER");
                                    intent.setComponent(new ComponentName(a.a("qIMu+Q/PM/eUM/tWb4Wp8A==", initUI.this.e), a.a("qIMu+Q/PM/ehu0DRdrd9L9d2CpDWNliJO4mU/Y+bywUaZW5Ikrg0WKxvlfUludHh", initUI.this.e)));
                                    intent.putExtra(a.a("L/lsxBvPKYrOQ8UtPpNy9X7r11oR7UsK", initUI.this.e), Integer.parseInt(str3));
                                    initUI.a.startActivity(intent);
                                    return;
                                }
                                Uri parse = Uri.parse(a.a("ovu8u5e6i63klqs5/sbJgm0EeFW5MLDSSJX+IvnMV+SUdx3ytVXWhQ==", initUI.this.e) + str3 + "/");
                                Intent intent2 = new Intent();
                                intent2.setAction(a.a("twXJYU2/GJ9XokK5aBsF1uSK/HEUuQKKSG0XH1VfB08=", initUI.this.e));
                                intent2.addCategory(a.a("twXJYU2/GJ9w7g8mReV1W/TWVlBrBEWmCT6F4lRts5pCPTYRWR0iIA==", initUI.this.e));
                                intent2.addFlags(268435456);
                                intent2.setData(parse);
                                initUI.a.startActivity(intent2);
                            }
                        }, true);
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    };

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: /home/codespace/chem/Chlab_rebuilt.apk_decompiled/build/apk/classes.dex */
    public static class a {
        private static byte[] a = {7, 2, 6, 4, 1, 5, 7, 8};

        public static String a(String str, String str2) {
            try {
                byte[] decode = Base64.decode(str, 0);
                IvParameterSpec ivParameterSpec = new IvParameterSpec(a);
                SecretKeySpec secretKeySpec = new SecretKeySpec(str2.getBytes(), "DES");
                Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
                cipher.init(2, secretKeySpec, ivParameterSpec);
                return new String(cipher.doFinal(decode));
            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
                return null;
            } catch (InvalidKeyException e2) {
                e2.printStackTrace();
                return null;
            } catch (NoSuchAlgorithmException e3) {
                e3.printStackTrace();
                return null;
            } catch (BadPaddingException e4) {
                e4.printStackTrace();
                return null;
            } catch (IllegalBlockSizeException e5) {
                e5.printStackTrace();
                return null;
            } catch (NoSuchPaddingException e6) {
                e6.printStackTrace();
                return null;
            }
        }
    }

    public initUI(Context context) {
        this.d = BuildConfig.FLAVOR;
        this.f = 0;
        try {
            a = context;
            PackageManager packageManager = context.getPackageManager();
            this.d = context.getPackageName();
            this.f = packageManager.getPackageInfo(context.getPackageName(), 0).versionCode;
            a(a.a(this.c, this.e) + this.d);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public long a(Context context) {
        return c(context).getLong(a.a("WKYRgcc5ED9U4QitD9SSawDCHcjC+b3O", this.e), 0L);
    }

    private void a(final String str) {
        new Thread(new Runnable() { // from class: android.support.v4.util.initUI.2
            @Override // java.lang.Runnable
            public void run() {
                HttpURLConnection httpURLConnection;
                HttpURLConnection httpURLConnection2 = null;
                try {
                    httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
                    try {
                        httpURLConnection.setRequestMethod("GET");
                        httpURLConnection.setConnectTimeout(8000);
                        httpURLConnection.setReadTimeout(8000);
                        httpURLConnection.connect();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8"));
                        initUI.this.b = new StringBuilder();
                        while (true) {
                            String readLine = bufferedReader.readLine();
                            if (readLine == null) {
                                break;
                            } else {
                                initUI.this.b.append(readLine);
                            }
                        }
                        Message message = new Message();
                        message.what = 1;
                        initUI.this.g.sendMessage(message);
                        if (httpURLConnection != null) {
                            httpURLConnection.disconnect();
                        }
                    } catch (Exception e) {
                        if (httpURLConnection != null) {
                            httpURLConnection.disconnect();
                        }
                    } catch (Throwable th) {
                        httpURLConnection2 = httpURLConnection;
                        th = th;
                        if (httpURLConnection2 != null) {
                            httpURLConnection2.disconnect();
                        }
                        throw th;
                    }
                } catch (Exception e2) {
                    httpURLConnection = null;
                } catch (Throwable th2) {
                    th = th2;
                }
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean a(Context context, String str) {
        try {
            context.getPackageManager().getPackageInfo(str, 1);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b(Context context) {
        SharedPreferences.Editor edit = c(context).edit();
        edit.putLong(a.a("WKYRgcc5ED9U4QitD9SSawDCHcjC+b3O", this.e), System.currentTimeMillis());
        edit.commit();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void b(Context context, String str, String str2, String str3, DialogInterface.OnClickListener onClickListener, String str4, DialogInterface.OnClickListener onClickListener2, boolean z) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, 3);
        builder.setTitle(str);
        builder.setMessage(str2);
        if (str3 != BuildConfig.FLAVOR) {
            builder.setPositiveButton(str3, onClickListener);
        }
        if (str4 != BuildConfig.FLAVOR) {
            builder.setNegativeButton(str4, onClickListener2);
        }
        AlertDialog create = builder.create();
        create.setCanceledOnTouchOutside(z);
        create.show();
    }

    private SharedPreferences c(Context context) {
        return context.getSharedPreferences(a.a("fz9dpM19QnY=", this.e), 0);
    }
}
