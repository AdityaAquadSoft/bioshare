package com.addonvisioninfotech.whatsappstatusdownloader.ui;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.addonvisioninfotech.whatsappstatusdownloader.R;
import com.addonvisioninfotech.whatsappstatusdownloader.adapter.PagerAdapter;
import com.addonvisioninfotech.whatsappstatusdownloader.custom.CustomFragment;
import com.addonvisioninfotech.whatsappstatusdownloader.utils.PermissionUtils;


/**
 * Created by AddonVision infotech on 2/27/2018.
 */

public class StatusFragment extends CustomFragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.statusview, null);
        return v;
    }
}
