package com.ngapainya.ngapainya.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ngapainya.ngapainya.R;
import com.ngapainya.ngapainya.activity.volunteer.ContainerActivity;
import com.ngapainya.ngapainya.fragment.volunteer.child.GreetingSlideFragment;
import com.ngapainya.ngapainya.fragment.volunteer.child.LoginFragment;
import com.ngapainya.ngapainya.fragment.volunteer.child.RegisterFragment;
import com.ngapainya.ngapainya.helper.Config;
import com.ngapainya.ngapainya.helper.JSONParser;
import com.ngapainya.ngapainya.helper.SessionManager;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class GreetingActivity extends FragmentActivity implements ViewPager.OnPageChangeListener {
    private static final int NUM_PAGES = 3;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;

    //pager indicator variable
    private LinearLayout pager_indicator;
    private int dotsCount;
    private ImageView[] dots;

    //Manage session
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_greeting);

        sessionManager = new SessionManager(this);

        if(sessionManager.checkLogin()){
            Intent intent = new Intent(this, ContainerActivity.class);
            startActivity(intent);
            finish();
        }

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
            Fragment fragment = null;
            if (position == 0) {
                fragment = new GreetingSlideFragment();
            } else if (position == 1) {
                fragment = new LoginFragment();
            } else if (position == 2) {
                fragment = new RegisterFragment();
            }
            return fragment;
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

    /*
    * This method use to redirect to the main page (temporary)
    * */

    public void goTo(View view) {
        //sessionManager.createLoginSession("Rezpa Aditya", "respa@gmail.com", "respa");

        /*
        * temporary this method call remoteDataTask, it should be in login page
        * */
        new RemoteDataTask().execute();
    }

    private class RemoteDataTask extends AsyncTask<String, Void, String> {
        ProgressDialog pDialog;
        SessionManager session;
        HashMap<String, String> user;
        String token;
        String email;
        String password;
        String name;
        Config cfg = new Config();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(GreetingActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

            session = new SessionManager(GreetingActivity.this);
            email = "rezpa.snk@gmail.com";
            password = "blackout";
            name = "respa";
            token = "ainu"; //dummy
        }

        @Override
        protected String doInBackground(String... urls) {
            String url = cfg.HOSTNAME + "/login";
            List<NameValuePair> nvp = new ArrayList<NameValuePair>();
            nvp.add(new BasicNameValuePair("email", email));
            nvp.add(new BasicNameValuePair("password", password));

            JSONParser jParser = new JSONParser();
            //JSONObject json = jParser.makeHttpRequestToObject(url, "GET", nvp);      //get data from server
            try {
                /*if (json.getString("access_token") != null) {
                    token   = json.getString("access_token");
                } else {
                    Log.e("error", "unable to get data 0");
                }*/
            } catch (Exception e) {
                Log.e("error", "unable to get data 1");
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String organization) {
            super.onPostExecute(organization);
            pDialog.dismiss();
            sessionManager.createLoginSession("rezpa", "Rezpa Aditya", "a@a.com", "volunteer", "respa");
            Intent intent = new Intent(GreetingActivity.this, ContainerActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
