package com.addonvisioninfotech.whatsappstatusdownloader;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.addonvisioninfotech.whatsappstatusdownloader.application.MyApp;
import com.addonvisioninfotech.whatsappstatusdownloader.application.SingleInstance;
import com.addonvisioninfotech.whatsappstatusdownloader.custom.CustomActivity;
import com.addonvisioninfotech.whatsappstatusdownloader.model.Status;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class ImagePreview extends CustomActivity {
    private File statusFile;
    private static final String TYPE_PHOTO = "PHOTO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_priview);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupActionBar();
        statusFile = SingleInstance.getInstance().getStatusFile();
        AdRequest adRequest = new AdRequest.Builder().build();
        ((AdView) findViewById(R.id.adView)).loadAd(adRequest);
        MyApp.loader.displayImage(Uri.fromFile(statusFile).toString(), ((ImageView) findViewById(R.id.img_pre)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.share, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            try {
                File sd = statusFile;
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                Bitmap bitmap = BitmapFactory.decodeFile(sd.getAbsolutePath(), bmOptions);
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/jpeg");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                String path = MediaStore.Images.Media.insertImage(getContentResolver(),
                        bitmap, "Title", null);
                Uri imageUri = Uri.parse(path);
                share.putExtra(Intent.EXTRA_STREAM, imageUri);
                share.putExtra(Intent.EXTRA_SUBJECT, "WhatsAppStatusDownloader");
                share.putExtra(Intent.EXTRA_TEXT, "WhatsAppStatusDownloader\nhttps://play.google.com/store/apps/details?id=com.addonvisioninfotech.whatsappstatusdownloader");

                startActivity(Intent.createChooser(share, "Select"));

            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), R.string.No_viewer_found, Toast.LENGTH_SHORT).show();
            }

        }

        return false;
    }
}
