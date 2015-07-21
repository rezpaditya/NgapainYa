package com.ngapainya.ngapainya.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ngapainya.ngapainya.R;
import com.ngapainya.ngapainya.activity.volunteer.ContainerActivity;
import com.ngapainya.ngapainya.fragment.child.GreetingSlideFragment;


public class GreetingActivity extends FragmentActivity implements ViewPager.OnPageChangeListener{
    private static final int NUM_PAGES = 5;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    //pager indicator variable
    private LinearLayout pager_indicator;
    private int dotsCount;
    private ImageView[] dots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_greeting);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setOnPageChangeListener(this);

        setUiPageViewController();
    }

    private void setUiPageViewController() {
        pager_indicator = (LinearLayout) findViewById(R.id.viewPagerCountDots);
        dotsCount = mPagerAdapter.getCount();
        dots = new ImageView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(4, 0, 4, 0);

            pager_indicator.addView(dots[i], params);
        }

        dots[0].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < dotsCount; i++) {
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
        }
        dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new GreetingSlideFragment();
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

    public void loginSocial(View view) {
        switch (view.getId()) {
            case R.id.twitter:
                Toast.makeText(getBaseContext(), "Social 1", Toast.LENGTH_SHORT).show();
                break;
            case R.id.facebook:
                Toast.makeText(getBaseContext(), "Social 2", Toast.LENGTH_SHORT).show();
                break;
            /*case R.id.google:
                Toast.makeText(getBaseContext(), "Social 3", Toast.LENGTH_SHORT).show();
                break;*/
        }

    }

    public void goTo (View view){
        Intent intent = new Intent(this, ContainerActivity.class);
        startActivity(intent);
        finish();
    }

}
