package com.addonvisioninfotech.whatsappstatusdownloader.adapter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.addonvisioninfotech.whatsappstatusdownloader.R;
import com.addonvisioninfotech.whatsappstatusdownloader.ui.DownloadFragment;
import com.addonvisioninfotech.whatsappstatusdownloader.ui.PhotoStatusesFragment;
import com.addonvisioninfotech.whatsappstatusdownloader.ui.StatusFragment;
import com.addonvisioninfotech.whatsappstatusdownloader.ui.VideoStatusesFragment;
import com.addonvisioninfotech.whatsappstatusdownloader.ui.WebViewFragment;
import com.addonvisioninfotech.whatsappstatusdownloader.ui.WikiWebViewFragment;


/**
 * Created by theapache64 on 17/7/17.
 */

public class PagerAdapter extends FragmentPagerAdapter {

    private final PhotoStatusesFragment photoStatusesFragment;
    private final VideoStatusesFragment videoStatusesFragment;
    private final DownloadFragment downloadFragment;
    private final StatusFragment statusFragment;
    Activity activity;

    public PagerAdapter(FragmentManager fm, Activity activity) {
        super(fm);
        photoStatusesFragment = new PhotoStatusesFragment();
        videoStatusesFragment = new VideoStatusesFragment();
        downloadFragment = new DownloadFragment();
        statusFragment = new StatusFragment();
        this.activity = activity;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return photoStatusesFragment;
        } else if (position == 2) {
            return videoStatusesFragment;
        } else if (position == 1) {
            return new WebViewFragment();
        } else if (position == 4) {
            return downloadFragment;
        } else if (position == 5) {
            return statusFragment;
        } else if (position == 3) {
            return new WikiWebViewFragment();
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return activity.getResources().getString(R.string.tab1);
        } else if (position == 2) {
            return activity.getResources().getString(R.string.tab2);
        } else if (position == 4) {
            return activity.getResources().getString(R.string.tab3);
        } else if (position == 5) {
            return activity.getResources().getString(R.string.tab4);
        } else if (position == 1) {
            return "Trending";
        } else if (position == 3) {
            return "Wiki Feed";
        }
        return "";
    }

    @Override
    public int getCount() {
        return 5;
    }
}
