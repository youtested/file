package com.aiwu;

import air.thix.sciencesense.chemist.AppEntry;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/* loaded from: /home/codespace/chem/Chlab_rebuilt.apk_decompiled/build/apk/classes.dex */
public class Splash extends Activity {
    public int getStartCount(Context c) {
        SharedPreferences sp = c.getSharedPreferences("25az", 0);
        int StartCount = sp.getInt("StartCount", 0) + 1;
        sp.edit().putInt("StartCount", StartCount).commit();
        return StartCount;
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        RelativeLayout.LayoutParams params;
        AiwuInjectSave(this, "iSFeN", "/data/data/air.thix.sciencesense.chemist/", 1);
        if (getStartCount(this) == 1) {
        }
        super.onCreate(bundle);
        getWindow().setFlags(1024, 1024);
        requestWindowFeature(1);
        RelativeLayout relativeLayout = new RelativeLayout(this);
        relativeLayout.setBackgroundColor(Color.parseColor("#00a0e9"));
        ImageView aiwuLogo = GetAssetsImage("25az.bin");
        ViewGroup.LayoutParams Logoparams = new RelativeLayout.LayoutParams(-1, -1);
        aiwuLogo.setLayoutParams(Logoparams);
        ImageView LoadingImage = GetAssetsImage("25az_loading.bin");
        int DeviceWidth = getWindowManager().getDefaultDisplay().getWidth();
        int DeviceHeight = getWindowManager().getDefaultDisplay().getHeight();
        if (DeviceWidth < DeviceHeight) {
            params = new RelativeLayout.LayoutParams(DeviceWidth / 7, DeviceWidth / 7);
            int MarginsLeft = (DeviceWidth / 2) - (DeviceWidth / 14);
            int MarginsTop = (int) (DeviceHeight * 0.75d);
            params.setMargins(MarginsLeft, MarginsTop, 0, 0);
            LoadingImage.setLayoutParams(params);
        } else {
            params = new RelativeLayout.LayoutParams(DeviceHeight / 8, DeviceHeight / 8);
            int MarginsLeft2 = (DeviceWidth / 2) - (DeviceHeight / 16);
            int MarginsTop2 = (int) (DeviceHeight * 0.8d);
            params.setMargins(MarginsLeft2, MarginsTop2, 0, 0);
            LoadingImage.setLayoutParams(params);
        }
        relativeLayout.addView(aiwuLogo);
        relativeLayout.addView(LoadingImage);
        setContentView(relativeLayout);
        LoadingImage.setLayoutParams(params);
        RotateAnimation Animation_Loading = new RotateAnimation(0.0f, 360.0f, 1, 0.5f, 1, 0.5f);
        Animation_Loading.setDuration(3500L);
        LoadingImage.startAnimation(Animation_Loading);
        final Intent localIntent = new Intent(this, (Class<?>) AppEntry.class);
        new Timer().schedule(new TimerTask() { // from class: com.aiwu.Splash.1
            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                Splash.this.startActivity(localIntent);
                Splash.this.finish();
            }
        }, 2500L);
    }

    private ImageView GetAssetsImage(String ImagePath) {
        ImageView localImageView = new ImageView(this);
        try {
            localImageView.setImageDrawable(Drawable.createFromStream(getAssets().open(ImagePath), null));
            return localImageView;
        } catch (IOException e) {
            return null;
        }
    }

    private void AiwuInjectSave(Context context, String ZipPath, String OutPath, int SaveNo) {
        SharedPreferences sp = context.getSharedPreferences("25az", 0);
        if (sp.getBoolean("FirstStart" + SaveNo, true)) {
            SharedPreferences.Editor EditorSp = sp.edit();
            EditorSp.putBoolean("FirstStart" + SaveNo, false);
            EditorSp.commit();
            File file = new File(OutPath);
            if (!file.exists()) {
                file.mkdirs();
            }
            try {
                InputStream SaveData = context.getAssets().open(ZipPath);
                ByteArrayOutputStream SaveDataByte = new ByteArrayOutputStream();
                while (true) {
                    int count = SaveData.read();
                    if (count == -1) {
                        break;
                    } else {
                        SaveDataByte.write(count);
                    }
                }
                byte[] bs = SaveDataByte.toByteArray();
                SaveDataByte.close();
                for (int i = 0; i < bs.length; i++) {
                    bs[i] = (byte) (bs[i] ^ 25);
                }
                InputStream SaveData2 = new ByteArrayInputStream(bs);
                ZipInputStream zip = new ZipInputStream(SaveData2);
                while (true) {
                    ZipEntry Entry = zip.getNextEntry();
                    if (Entry == null) {
                        zip.close();
                        zip.close();
                        zip.close();
                        return;
                    }
                    File localFile = new File(String.valueOf(OutPath) + Entry.getName());
                    if (!localFile.exists()) {
                        if (Entry.isDirectory()) {
                            localFile.mkdirs();
                        } else {
                            String FilePath = localFile.toString().substring(0, localFile.toString().lastIndexOf("/") + 1);
                            File localDirectory = new File(FilePath);
                            if (!localDirectory.exists()) {
                                localDirectory.mkdirs();
                            }
                            try {
                                byte[] arrayOfByte = new byte[2048];
                                BufferedOutputStream localBufferedOutputStream = new BufferedOutputStream(new FileOutputStream(localFile), 2048);
                                while (true) {
                                    int i2 = zip.read(arrayOfByte, 0, 2048);
                                    if (i2 == -1) {
                                        break;
                                    } else {
                                        localBufferedOutputStream.write(arrayOfByte, 0, i2);
                                    }
                                }
                                localBufferedOutputStream.flush();
                                localBufferedOutputStream.close();
                            } catch (Exception e) {
                            }
                        }
                    }
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }
}
