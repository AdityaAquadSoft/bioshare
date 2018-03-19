package com.addonvisioninfotech.whatsappstatusdownloader.adapter;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.addonvisioninfotech.whatsappstatusdownloader.ui.PhotoStatusesFragment;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Created by theapache64 on 16/7/17.
 */
public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.ViewHolder> {
    private List<Status> statusList;
    //    private List<StatusDownload> download;
    LayoutInflater inflater;
    Activity context;

    List<Status> imageStatuses, videoStatus;
    List<StatusDownload> downloadfile;
    private static final int THUMBSIZE = 128;


    public StatusAdapter(final Activity context, List<Status> statusList) {
        try {
            this.statusList = statusList;
//            this.download = download;
            this.inflater = LayoutInflater.from(context);
            this.context = context;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View rowLayout = inflater.inflate(R.layout.status_row, parent, false);
        return new ViewHolder(rowLayout);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Status statusDownload = statusList.get(position);
        holder.ivThumbnail.setImageBitmap(statusDownload.getThumbnail());
        if (statusDownload.isDownload())
            holder.ibSaveToGallery.setVisibility(View.GONE);
        else {
            holder.ibSaveToGallery.setVisibility(View.VISIBLE);
        }

        holder.ibSaveToGallery.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                if (PhotoStatusesFragment.mInterstitialAd.isLoaded()) {
                    PhotoStatusesFragment.mInterstitialAd.show();
                    PhotoStatusesFragment.mInterstitialAd = new InterstitialAd(context);
                    PhotoStatusesFragment.mInterstitialAd.setAdUnitId("ca-app-pub-2776560501294874/8836232481");
                    PhotoStatusesFragment.mInterstitialAd.loadAd(new AdRequest.Builder().build());
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }

                String APP_DIR = android.os.Environment
                        .getExternalStorageDirectory()
                        + File.separator
                        + "WhatsAppStatusDownloader" + File.separator;
                final File appFolder = new File(APP_DIR);
                if (!appFolder.exists()) {
                    appFolder.mkdirs();
                }
                final File destFile = new File(appFolder + File.separator + statusList.get(position).getTitle());
                if (destFile.exists()) {
                    destFile.delete();
                }

                statusList.get(position).setDownload(true);
                notifyDataSetChanged();

                try {
                    copyFile(statusList.get(position).getFile(), destFile);
//                    Toast.makeText(context, "Download Started", Toast.LENGTH_SHORT).show();
                    Intent intent =
                            new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    intent.setData(Uri.fromFile(destFile));
                    context.sendBroadcast(intent);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(context, R.string.Failed_to_save_to_gallery, Toast.LENGTH_SHORT).show();
                }
                if (statusList.get(position).isVideo()) {
                    viewreset();
                } else {
                    viewreset();
                }
            }


        });
        holder.ivThumbnail.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                try {
                    if (statusList.get(position).isVideo()) {
//                        Intent intent = new Intent();
//                        intent.setAction(Intent.ACTION_VIEW);
//                        intent.setDataAndType(Uri.fromFile(statusList.get(position).getFile()), "video/mp4");
                        String uri = String.valueOf((Uri.fromFile(statusList.get(position).getFile())));
//                        context.startActivity(intent);
                        context.startActivity(new Intent(context, VideoPreview.class).putExtra("path", uri));

                    } else {
                        SingleInstance.getInstance().setStatusFile(statusList.get(position).getFile());
                        context.startActivity(new Intent(context, ImagePreview.class));
                    }
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(context, R.string.No_viewer_found, Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private static void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.getParentFile().exists())
            destFile.getParentFile().mkdirs();
        if (!destFile.exists()) {
            destFile.createNewFile();
        }
        FileChannel source = null;
        FileChannel destination = null;
        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }


    @Override
    public int getItemCount() {
        try {
            return statusList.size();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

//    public void setData(List<Status> imageStatuses, List<StatusDownload> downloadfile) {
//        this.statusList = imageStatuses;
//        this.download = downloadfile;
//    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivThumbnail;
        ImageButton ibSaveToGallery;

        ViewHolder(View itemView) {
            super(itemView);
            this.ivThumbnail = (ImageView) itemView.findViewById(R.id.ivThumbnail);
            this.ibSaveToGallery = itemView.findViewById(R.id.ibSaveToGallery);

        }


    }


    private void viewreset() {
        final File STATUS_DIRECTORY = new File(Environment.getExternalStorageDirectory() + File.separator + "WhatsApp/Media/.Statuses");
        final File STATUS_DOWNLOAD_DIRECTORY = new File(Environment.getExternalStorageDirectory() + File.separator + "WhatsAppStatusDownloader");
        int THUMBSIZE = 128;
        if (STATUS_DIRECTORY.exists()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    File[] statusFiles = STATUS_DIRECTORY.listFiles();
                    imageStatuses = new ArrayList<>();
                    videoStatus = new ArrayList<>();
                    if (statusFiles != null && statusFiles.length > 0) {
                        Arrays.sort(statusFiles, lastModifiedComparator);
                        for (final File statusFile : statusFiles) {

                            final Status status = new Status(
                                    statusFile,
                                    statusFile.getName(),
                                    statusFile.getAbsolutePath()
                            );
                            downloadfile = SingleInstance.getInstance().getStatusDownload();
                            for (int i = 0; i < downloadfile.size(); i++) {
                                if (downloadfile.get(i).getTitle().equals(status.getTitle())) {
                                    status.setDownload(true);
                                } else {
                                    status.setDownload(false);
                                }
                            }
                            status.setThumbnail(getThumbnail(status));
                            if (status.isVideo()) {
                                videoStatus.add(status);
                            } else {
                                imageStatuses.add(status);
                            }
                        }
                    }
                }
            }).start();

        }

        if (STATUS_DOWNLOAD_DIRECTORY.exists()) {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
                    File[] statusDownload = STATUS_DOWNLOAD_DIRECTORY.listFiles();
                    downloadfile = new ArrayList<>();
                    if (statusDownload != null && statusDownload.length > 0) {
                        Arrays.sort(statusDownload, lastModifiedComparator);
                        //Looping through each status
                        for (final File statusFile : statusDownload) {
                            if (!statusFile.getAbsolutePath().contains(".dthumb")) {
                                final StatusDownload status = new StatusDownload(
                                        statusFile,
                                        statusFile.getName(),
                                        statusFile.getAbsolutePath()
                                );
                                status.setThumbnail(getThumbnaildownload(status));
                                downloadfile.add(status);
                                SingleInstance.getInstance().setStatusDownload(downloadfile);
                                notifyDataSetChanged();
                            }
                        }
                    }

//                }
//            }).start();
        } else {
//            Toast.makeText(context, "WhatsApp Status directory not found", Toast.LENGTH_SHORT).show();
        }
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (isVideo) {
//                    statusList = new ArrayList<>();
//                    statusList = videoStatus;
//                } else {
//                    statusList = new ArrayList<>();
//                    statusList = imageStatuses;
//                }
//                download = new ArrayList<>();
//                download = downloadfile;
//                setData(statusList, download);
//                notifyDataSetChanged();
//            }
//        }, 2000);


    }

    private static Bitmap getThumbnail(Status status) {
        if (status.isVideo()) {
            return ThumbnailUtils.createVideoThumbnail(status.getFile().getAbsolutePath(), MediaStore.Video.Thumbnails.MICRO_KIND);
        } else {
            return ThumbnailUtils.extractThumbnail(
                    BitmapFactory.decodeFile(status.getFile().getAbsolutePath()),
                    THUMBSIZE,
                    THUMBSIZE);
        }
    }

    private static final Comparator lastModifiedComparator = new Comparator() {
        public int compare(Object o1, Object o2) {
            if (((File) o1).lastModified() > ((File) o2).lastModified()) {
                return -1;
            } else if (((File) o1).lastModified() < ((File) o2).lastModified()) {
                return +1;
            } else {
                return 0;
            }
        }
    };


    private static Bitmap getThumbnaildownload(StatusDownload status) {
        if (status.isVideo()) {
            return ThumbnailUtils.createVideoThumbnail(status.getFile().getAbsolutePath(), MediaStore.Video.Thumbnails.MICRO_KIND);
        } else {
            return ThumbnailUtils.extractThumbnail(
                    BitmapFactory.decodeFile(status.getFile().getAbsolutePath()),
                    THUMBSIZE,
                    THUMBSIZE);
        }
    }
}
