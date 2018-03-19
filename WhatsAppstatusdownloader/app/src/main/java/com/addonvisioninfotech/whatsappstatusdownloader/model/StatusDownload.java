package com.addonvisioninfotech.whatsappstatusdownloader.model;

import android.graphics.Bitmap;

import java.io.File;

/**
 * Created by AddonVision infotech on 3/12/2018.
 */

public class StatusDownload {
    private static final String MP4 = ".mp4";
    private String title, subtitle;
    private Bitmap thumbnail;
    private boolean isVideo;
    private File file;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public void setVideo(boolean video) {
        isVideo = video;
    }

    public Bitmap getThumbnail() {

        return thumbnail;
    }

    public void setThumbnail(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
    }

    public StatusDownload(File file, String title, String absolutePath) {
        this.file = file;
        this.title = title;
        this.subtitle = absolutePath;
        this.isVideo = file.getName().endsWith(MP4);

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }
}
