package com.addonvisioninfotech.whatsappstatusdownloader.adapter;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.addonvisioninfotech.whatsappstatusdownloader.ImagePreview;
import com.addonvisioninfotech.whatsappstatusdownloader.R;
import com.addonvisioninfotech.whatsappstatusdownloader.VideoPreview;
import com.addonvisioninfotech.whatsappstatusdownloader.application.SingleInstance;
import com.addonvisioninfotech.whatsappstatusdownloader.model.Status;
import com.addonvisioninfotech.whatsappstatusdownloader.model.StatusDownload;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

/**
 * Created by AddonVision infotech on 3/13/2018.
 */

public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.ViewHolder> {
    private List<StatusDownload> download;
    private final LayoutInflater inflater;
    private Activity context;

    public DownloadAdapter(final Activity context, List<StatusDownload> download) {
        this.download = download;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }


    @Override
    public DownloadAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View rowLayout = inflater.inflate(R.layout.status_row, parent, false);
        return new DownloadAdapter.ViewHolder(rowLayout);
    }

    @Override
    public void onBindViewHolder(DownloadAdapter.ViewHolder holder, final int position) {
        final StatusDownload statusDownload = download.get(position);
        holder.ivThumbnail.setImageBitmap(statusDownload.getThumbnail());
        holder.ibSaveToGallery.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        try {
            return download.size();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView ivThumbnail;
        ImageButton ibSaveToGallery;

        ViewHolder(View itemView) {
            super(itemView);
            this.ivThumbnail = itemView.findViewById(R.id.ivThumbnail);
            ivThumbnail.setOnClickListener(this);
            this.ibSaveToGallery = itemView.findViewById(R.id.ibSaveToGallery);
        }

        @Override
        public void onClick(View v) {
            if (v == ivThumbnail) {
                try {
                    File sd = download.get(getLayoutPosition()).getFile();
                    if (download.get(getLayoutPosition()).isVideo()) {
                        String uri = String.valueOf((Uri.fromFile(download.get(getLayoutPosition()).getFile())));
//                        context.startActivity(intent);
                        context.startActivity(new Intent(context, VideoPreview.class).putExtra("path", uri));
//                        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
//                        sharingIntent.setType("video/mp4");
//                        Uri uri = Uri.fromFile(sd);
//                        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
//                        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "WhatsAppStatusDownloader");
//                        sharingIntent.putExtra(Intent.EXTRA_TEXT, "WhatsAppStatusDownloader\nhttps://play.google.com/store/apps/details?id=com.addonvisioninfotech.whatsappstatusdownloader");
//                        context.startActivity(Intent.createChooser(sharingIntent, "Select"));
                    } else {
//                        ImagePreview.status = download.get(getLayoutPosition());
                        SingleInstance.getInstance().setStatusFile(download.get(getLayoutPosition()).getFile());
                        context.startActivity(new Intent(context, ImagePreview.class));
//                        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//                        Bitmap bitmap = BitmapFactory.decodeFile(sd.getAbsolutePath(), bmOptions);
//                        Intent share = new Intent(Intent.ACTION_SEND);
//                        share.setType("image/jpeg");
//                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//                        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(),
//                                bitmap, "Title", null);
//                        Uri imageUri = Uri.parse(path);
//                        share.putExtra(Intent.EXTRA_STREAM, imageUri);
//                        share.putExtra(Intent.EXTRA_SUBJECT, "WhatsAppStatusDownloader");
//                        share.putExtra(Intent.EXTRA_TEXT, "WhatsAppStatusDownloader\nhttps://play.google.com/store/apps/details?id=com.addonvisioninfotech.whatsappstatusdownloader");
//                        context.startActivity(Intent.createChooser(share, "Select"));
                    }
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(context, R.string.No_viewer_found, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}

