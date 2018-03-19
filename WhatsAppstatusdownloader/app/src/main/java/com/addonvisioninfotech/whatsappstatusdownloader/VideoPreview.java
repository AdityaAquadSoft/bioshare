package com.addonvisioninfotech.whatsappstatusdownloader;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.addonvisioninfotech.whatsappstatusdownloader.application.MyApp;
import com.addonvisioninfotech.whatsappstatusdownloader.custom.CustomActivity;
import com.addonvisioninfotech.whatsappstatusdownloader.custom.CustomFragment;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * Created by AddonVision infotech on 3/17/2018.
 */

public class VideoPreview extends CustomActivity {
    VideoView videoView1;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.videopreview);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupActionBar();
        setUpUi();
      /*  MobileAds.initialize(ImagePreview.this,
                "ca-app-pub-2776560501294874~9155387566");
*/

//        AdRequest adRequest = new AdRequest.Builder().build();
//        ((AdView)findViewById(R.id.adView)).loadAd(adRequest);
    }

    private void setUpUi() {
        videoView1 = findViewById(R.id.videoView1);
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView1);
        uri = Uri.parse(getIntent().getStringExtra("path"));
        videoView1.setMediaController(mediaController);
        videoView1.setVideoURI(uri);
        videoView1.requestFocus();
        videoView1.start();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

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
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("video/mp4");
                sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "WhatsAppStatusDownloader");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, "WhatsAppStatusDownloader\nhttps://play.google.com/store/apps/details?id=com.addonvisioninfotech.whatsappstatusdownloader");
                startActivity(Intent.createChooser(sharingIntent, "Select"));
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), R.string.No_viewer_found, Toast.LENGTH_SHORT).show();
            }

        }

        return false;
    }
}
