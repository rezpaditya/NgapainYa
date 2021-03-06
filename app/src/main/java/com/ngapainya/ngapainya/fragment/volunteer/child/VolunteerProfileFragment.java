package com.ngapainya.ngapainya.fragment.volunteer.child;


import android.annotation.TargetApi;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ngapainya.ngapainya.R;
import com.ngapainya.ngapainya.activity.volunteer.ContainerActivity;
import com.ngapainya.ngapainya.helper.Config;
import com.ngapainya.ngapainya.helper.JSONParser;
import com.ngapainya.ngapainya.helper.SessionManager;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class VolunteerProfileFragment extends Fragment {
    private FragmentActivity myContext;
    private View myFragmentView;

    private static final String FOLLOWING_SPEC = "following";
    private static final String YOU_SPEC = "you";
    private FragmentTabHost tabHost;

    /*Variable from get server*/
    private String avatar_url;
    private String total_post;
    private String total_following;
    private String total_follower;
    private String follow_status;
    private String user_type;

    /*
    * Variabel from view
    * */
    @Bind(R.id.following) TextView following;
    @Bind(R.id.follower) TextView follower;
    @Bind(R.id.profile_image) ImageView avatar;
    @Bind(R.id.ttl_post) TextView ttl_post;
    @Bind(R.id.follow_btn) Button follow_btn;

    /*
    * variable from argument
    * */
    private String user_id;

    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragmentView = inflater.inflate(R.layout.fragment_volunteer_profile, container, false);

        /*get variable from argument*/
        user_id = getArguments().getString("user_id");
        ((ContainerActivity)getActivity()).standardTitleBar(user_id);
        ((ContainerActivity)getActivity()).changeActionbarStyle(true);

        ButterKnife.bind(this, myFragmentView);

        tabHost = (FragmentTabHost) myFragmentView.findViewById(android.R.id.tabhost);  // The activity TabHost
        tabHost.setup(getActivity(), getChildFragmentManager(), android.R.id.tabcontent);

        tabHost.addTab(tabHost.newTabSpec(YOU_SPEC).setIndicator("You"), LockedFragment.class, null);
        tabHost.setCurrentTab(0);

        Log.e("user_id", user_id);
        new getProfile().execute();

        return myFragmentView;
    }

    public class getProfile extends AsyncTask<String, String, String> {
        SessionManager session;
        HashMap<String, String> user;
        String token;
        Config cfg = new Config();

        protected void onPreExecute() {
            super.onPreExecute();
            session = new SessionManager(myContext);
            user = session.getUserDetails();
            token = user.get(SessionManager.KEY_TOKEN);
        }

        @Override
        protected String doInBackground(String... arg0) {
            String url = cfg.HOSTNAME +"/profile/"+user_id;
            List<NameValuePair> nvp = new ArrayList<NameValuePair>();
            nvp.add(new BasicNameValuePair("access_token", token));

            JSONParser jParser = new JSONParser();
            JSONArray json = jParser.makeHttpRequest(url, "GET", nvp);      //get data from server

            try {
                for(int i=0;i<json.length();i++) {
                    JSONObject result = json.getJSONObject(i);
                    avatar_url      = result.getString("avatar");
                    total_post      = result.getString("count_activity");
                    total_follower  = result.getString("follower");
                    total_following = result.getString("following");
                    follow_status   = result.getString("follow_status");
                    user_type       = result.getString("user_current_status");
                }
                Log.e("ok", " ambil data");
            } catch (Exception e) {
                Log.e("error", "tidak bisa ambil data 1");
                e.printStackTrace();
            }
            return null;
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Picasso.with(myContext)
                    .load("http://ainufaisal.com/" + avatar_url)
                    .placeholder(R.drawable.propic_default)
                    .into(avatar);
            ttl_post.setText(total_post);
            following.setText(total_following);
            follower.setText(total_follower);
            if(follow_status.equals("follow")){
                follow_btn.setBackground(myContext.getResources().getDrawable(R.drawable.my_button_green)); //work on API 16 or above
                follow_btn.setText("Followed");
                follow_btn.setTextColor(myContext.getResources().getColor(R.color.white));
            }else{
                follow_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new doFollow().execute();
                    }
                });
            }
        }
    }

    public class doFollow extends AsyncTask<String, String, String> {
        SessionManager session;
        HashMap<String, String> user;
        String token;
        String input_user_id;
        String input_user_type;
        Config cfg = new Config();

        protected void onPreExecute() {
            super.onPreExecute();

            session = new SessionManager(myContext);
            user = session.getUserDetails();
            token = user.get(SessionManager.KEY_TOKEN);

            input_user_id   = user_id;
            input_user_type = user_type;
        }

        @Override
        protected String doInBackground(String... arg0) {
            String url = cfg.HOSTNAME +"/follow/add/"+input_user_id+"/"+input_user_type;
            List<NameValuePair> nvp = new ArrayList<NameValuePair>();
            nvp.add(new BasicNameValuePair("access_token", token));

            JSONParser jParser = new JSONParser();
            JSONObject json = jParser.makeHttpRequestToObject(url, "GET", nvp);      //get data from server
            //Log.e("error", "jalan");
            try {
                Log.e("error", "tidak bisa ambil data 0");
            } catch (Exception e) {
                Log.e("error", "tidak bisa ambil data 1");
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            new getProfile().execute();
        }
    }
}
