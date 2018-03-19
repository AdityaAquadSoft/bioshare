package com.addonvisioninfotech.whatsappstatusdownloader;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.addonvisioninfotech.whatsappstatusdownloader.application.MyApp;
import com.addonvisioninfotech.whatsappstatusdownloader.custom.CustomActivity;
import com.addonvisioninfotech.whatsappstatusdownloader.utils.ExceptionHandler;
import com.addonvisioninfotech.whatsappstatusdownloader.utils.StaticData;
import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.akexorcist.roundcornerprogressbar.common.BaseRoundCornerProgressBar;

public class Splash extends CustomActivity {
    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
    RoundCornerProgressBar progress1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        String crash = getIntent()
                .getStringExtra(ExceptionHandler.CRASH_REPORT);

        progress1 = (RoundCornerProgressBar) findViewById(R.id.progress_two);
        progress1.setProgressBackgroundColor(getResources().getColor(R.color.white));
        if (crash == null) {
            updateProgressTwoColor(5);
//                startSplash();
        } else {
            showCrashDialog(crash);
        }
//        }

    }


    private void updateProgressTwoColor(int progress) {

        progress1.setProgress(progress);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (progress1.getProgress() >= 100) {
                    startSplash();
                    return;
                }
                updateProgressTwoColor((int) (progress1.getProgress() + 5));
                progress1.setProgressColor(getResources().getColor(R.color.colorPrimary));

            }
        }, 50);

    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    private void startSplash() {
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                MyApp.setStatus(StaticData.LOAD, true);
                startActivity(new Intent(Splash.this, MainActivity.class));
                finish();
            }
        }, 00);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivity(new Intent(Splash.this, MainActivity.class));
                    finish();
//                   startSplash();
                } else {
                    Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        finishAffinity();
                    }
                }
                return;
            }

        }
    }

    public void showCrashDialog(final String report) {
        android.support.v7.app.AlertDialog.Builder b = new android.support.v7.app.AlertDialog.Builder(this);
        b.setTitle("App Crashed");
        b.setMessage("Oops! The app crashed due to below reason:\n\n" + report);

        DialogInterface.OnClickListener ocl = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/html");
                    i.putExtra(Intent.EXTRA_EMAIL,
                            new String[]{"info@addonvision.com"});
                    i.putExtra(Intent.EXTRA_TEXT, report);
                    i.putExtra(Intent.EXTRA_SUBJECT, "App Crashed");
                    startActivity(Intent.createChooser(i, "Send Mail via:"));
                    finish();
                } else {
                    startSplash();
                }
                dialog.dismiss();
            }
        };
        b.setCancelable(false);
        b.setPositiveButton("Send Report", ocl);
        b.setNegativeButton("Restart", ocl);
        b.create().show();
    }

}
