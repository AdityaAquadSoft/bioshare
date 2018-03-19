package com.addonvisioninfotech.whatsappstatusdownloader.application;

import com.addonvisioninfotech.whatsappstatusdownloader.model.Status;
import com.addonvisioninfotech.whatsappstatusdownloader.model.StatusDownload;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 18-Mar-18.
 */

public class SingleInstance {
    private static final SingleInstance ourInstance = new SingleInstance();

    public static SingleInstance getInstance() {
        return ourInstance;
    }

    private SingleInstance() {
    }

    private List<Status> statusImageList = new ArrayList<>();
    private List<Status> statusVideoList = new ArrayList<>();
    private File statusFile;

    public File getStatusFile() {
        return statusFile;
    }

    public void setStatusFile(File statusFile) {
        this.statusFile = statusFile;
    }

    private List<StatusDownload> statusDownload = new ArrayList<>();

    public List<StatusDownload> getStatusDownload() {
        return statusDownload;
    }

    public void setStatusDownload(List<StatusDownload> statusDownload) {
        this.statusDownload = statusDownload;
    }

    public List<Status> getStatusImageList() {
        return statusImageList;
    }

    public void setStatusImageList(List<Status> statusImageList) {
        this.statusImageList = statusImageList;
    }

    public List<Status> getStatusVideoList() {
        return statusVideoList;
    }

    public void setStatusVideoList(List<Status> statusVideoList) {
        this.statusVideoList = statusVideoList;
    }
}
