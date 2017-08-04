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
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.example.adityavats.bioshare.fargments.CompanySignup;
import com.example.adityavats.bioshare.fargments.IndividualSignup;

public class SignupActivity extends AppCompatActivity {

    TextView backToLoginTextView;
    Button signUpButton;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_individual);

        signUpButton=(Button)findViewById(R.id.signupIndividualSubmitButton);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignupActivity.this, PhoneAuthActivity.class));
            }
        });

        backToLoginTextView=(TextView)findViewById(R.id.signUp_loginTextView);
        backToLoginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            }
        });

        viewPager = (ViewPager) findViewById(R.id.individualSignupPageViewer);
       /* TabLayout tabLayout=(TabLayout)findViewById(R.id.tabBar);
        tabLayout.setupWithViewPager(viewPager,true);*/

        viewPager.setAdapter(new SignupActivity.SamplePagerAdapter(getSupportFragmentManager()));
        PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabBar);
        // Attach the view pager to the tab strip
        tabsStrip.setViewPager(viewPager);
    }

    public class SamplePagerAdapter extends FragmentPagerAdapter {
        final int PAGE_COUNT=2;
        private String tabTitles[] = new String[] { "As Individual", "As Company"};
        public SamplePagerAdapter(FragmentManager fragmentManager) {

            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            if(position==0){

                return new IndividualSignup();
            }
            else
            {
                return new CompanySignup();
            }
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            return tabTitles[position];
        }
    }
}