package com.addonvisioninfotech.whatsappstatusdownloader.ui;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.addonvisioninfotech.whatsappstatusdownloader.R;
import com.addonvisioninfotech.whatsappstatusdownloader.adapter.DownloadAdapter;
import com.addonvisioninfotech.whatsappstatusdownloader.adapter.StatusAdapter;
import com.addonvisioninfotech.whatsappstatusdownloader.application.MyApp;
import com.addonvisioninfotech.whatsappstatusdownloader.application.SingleInstance;
import com.addonvisioninfotech.whatsappstatusdownloader.custom.CustomFragment;
import com.addonvisioninfotech.whatsappstatusdownloader.model.Status;
import com.addonvisioninfotech.whatsappstatusdownloader.model.StatusDownload;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Created by AddonVision infotech on 3/12/2018.
 */

public class DownloadFragment extends CustomFragment {
    private static final File STATUS_DIRECTORY = new File(Environment.getExternalStorageDirectory() + File.separator + "WhatsApp/Media/.Statuses");
    private static final File STATUS_DOWNLOAD_DIRECTORY = new File(Environment.getExternalStorageDirectory() + File.separator + "WhatsAppStatusDownloader");
    //    private List<Status> imageStatuses, videoStatus;
    private Map<String, StatusDownload> downloadfile = new HashMap<>();
    private static final int THUMBSIZE = 128;
    RecyclerView rvStatuses;
    DownloadAdapter adapter;
    private InterstitialAd mInterstitialAd;
    SwipeRefreshLayout swipeRefreshLayout;
    ImageView img_whatsapp;
    LinearLayout lin_whatsapp, lin_see;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.whatsapp, null);
        setUpUi(v);
        return v;
    }

    private void setUpUi(View v) {
        img_whatsapp = v.findViewById(R.id.img_whatsapp);
        setClick(img_whatsapp);
        lin_see = v.findViewById(R.id.lin_see);
        lin_whatsapp = v.findViewById(R.id.lin_whatsapp);
//        MyApp.spinnerStart(getActivity(), "Please wait...");
        MobileAds.initialize(getActivity(),
                "ca-app-pub-2776560501294874~9155387566");
        AdRequest adRequest = new AdRequest.Builder().build();
        ((AdView) v.findViewById(R.id.adView)).loadAd(adRequest);


        mInterstitialAd = new InterstitialAd(getActivity());
        mInterstitialAd.setAdUnitId("ca-app-pub-2776560501294874/8836232481");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        rvStatuses = v.findViewById(R.id.rvStatuses);

        if (SingleInstance.getInstance().getStatusDownload().size() > 0) {
            List<StatusDownload> myList = SingleInstance.getInstance().getStatusDownload();
            for (int i = 0; i < myList.size(); i++) {
                downloadfile.put(myList.get(i).getTitle(), myList.get(i));
            }
            myList = new ArrayList<>();
            for (String s : downloadfile.keySet()) {
                myList.add(downloadfile.get(s));
            }
            SingleInstance.getInstance().setStatusDownload(myList);
            rvStatuses.setVisibility(View.VISIBLE);
            lin_whatsapp.setVisibility(View.GONE);
            lin_see.setVisibility(View.GONE);
            rvStatuses.setLayoutManager(new GridLayoutManager(getActivity(), 3));
            adapter = new DownloadAdapter(getActivity(), SingleInstance.getInstance().getStatusDownload());
            rvStatuses.setAdapter(adapter);
        } else {
            loadFile(false);
        }
        swipeRefreshLayout = v.findViewById(R.id.activity_main_swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark, R.color.green, R.color.blue);
                loadFile(false);
            }
        });


//        Handler h = new Handler();
//        h.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                {
//                    if (downloadfile != null && downloadfile.size() != 0) {
//                        rvStatuses.setLayoutManager(new GridLayoutManager(getActivity(), 3));
//                        adapter = new DownloadAdapter(getActivity(), downloadfile);
//                        rvStatuses.setAdapter(adapter);
//                    } else {
//                        rvStatuses.setVisibility(View.GONE);
//                        lin_whatsapp.setVisibility(View.VISIBLE);
//                        lin_see.setVisibility(View.VISIBLE);
//                    }
//                }
//            }
//        }, 3500);

    }

    @Override
    public void onResume() {
        super.onResume();
//        loadFile(true);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v == img_whatsapp) {
            try {
                try {
                    Intent i = getActivity().getPackageManager().getLaunchIntentForPackage("com.whatsapp");
                    getActivity().startActivity(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();

//                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
//                    try {
//                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "com.whatsapp")));
//                    } catch (android.content.ActivityNotFoundException anfe) {
//                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + "com.whatsapp")));
//                    }
            }

        }
    }

    private void loadFile(final boolean isResume) {
//        if (STATUS_DIRECTORY.exists()) {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    File[] statusFiles = STATUS_DIRECTORY.listFiles();
//                    imageStatuses = new ArrayList<>();
//                    videoStatus = new ArrayList<>();
//                    if (statusFiles != null && statusFiles.length > 0) {
//                        Arrays.sort(statusFiles, lastModifiedComparator);
//                        for (final File statusFile : statusFiles) {
//                            final Status status = new Status(
//                                    statusFile,
//                                    statusFile.getName(),
//                                    statusFile.getAbsolutePath()
//                            );
//                            status.setThumbnail(getThumbnail(status));
//
//                            if (status.isVideo()) {
//                                videoStatus.add(status);
//                            } else {
//                                imageStatuses.add(status);
//                            }
//                        }
//                    }
//
//
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (isResume) {
//
//                            } else {
//                                if (downloadfile != null && downloadfile.size() != 0) {
//                                    rvStatuses.setVisibility(View.VISIBLE);
//                                    lin_whatsapp.setVisibility(View.GONE);
//                                    lin_see.setVisibility(View.GONE);
//                                    rvStatuses.setLayoutManager(new GridLayoutManager(getActivity(), 3));
//                                    adapter = new DownloadAdapter(getActivity(), downloadfile);
//                                    rvStatuses.setAdapter(adapter);
//                                } else {
//                                    rvStatuses.setVisibility(View.GONE);
//                                    lin_whatsapp.setVisibility(View.VISIBLE);
//                                    lin_see.setVisibility(View.VISIBLE);
//                                }
//                            }
//
//                            swipeRefreshLayout.setRefreshing(false);
//                        }
//                    });
//                }
//            }).start();
//        }

        if (STATUS_DOWNLOAD_DIRECTORY.exists()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    File[] statusDownload = STATUS_DOWNLOAD_DIRECTORY.listFiles();
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
                                downloadfile.put(status.getTitle(), status);
                            }
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                List<StatusDownload> list = new ArrayList<>();
                                for (String s : downloadfile.keySet()) {
                                    list.add(downloadfile.get(s));
                                }
                                SingleInstance.getInstance().setStatusDownload(list);

//                                ArrayList<String> values = new ArrayList<String>();
//                                HashSet<String> hashSet = new HashSet<String>();
//                                hashSet.addAll(values);
//                                values.clear();
//                                values.addAll(hashSet);


                                adapter = new DownloadAdapter(getActivity(), list);
                                rvStatuses.setAdapter(adapter);
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        });
                    }

                }
            }).start();

        } else {
//            Toast.makeText(getActivity(),"WhatsApp Status directory not found",Toast.LENGTH_SHORT).show();
        }
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
