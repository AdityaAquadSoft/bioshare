package com.example.adityavats.bioshare;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.adityavats.bioshare.fargments.FirstSlideTutor;
import com.example.adityavats.bioshare.fargments.SecondSlideTutor;
import com.example.adityavats.bioshare.fargments.ThirdSlideTutor;

public class TutorialActivity extends AppCompatActivity {

    ViewPager viewPager;
    Button getStarted;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        viewPager=(ViewPager)findViewById(R.id.tutorSliderPageViewer);
        TabLayout tabLayout=(TabLayout)findViewById(R.id.tabDots);
        tabLayout.setupWithViewPager(viewPager,true);
        viewPager.setAdapter(new SamplePagerAdapter(getSupportFragmentManager()));
        getStarted=(Button)findViewById(R.id.getStartedButton);
        getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity((new Intent(TutorialActivity.this,LoginActivity.class)));
            }
        });

    }
    public class SamplePagerAdapter extends FragmentPagerAdapter {
        public SamplePagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new FirstSlideTutor();
            }
            else if (position==1){
                return new SecondSlideTutor();
            }
            else
                return new ThirdSlideTutor();
        }

        @Override
        public int getCount() {

            return 3;
        }
    }
}
