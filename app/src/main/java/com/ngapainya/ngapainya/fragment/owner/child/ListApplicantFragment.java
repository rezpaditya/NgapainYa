package com.ngapainya.ngapainya.fragment.owner.child;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ngapainya.ngapainya.R;
import com.ngapainya.ngapainya.adapter.ListApplicantAdapter;
import com.ngapainya.ngapainya.helper.Config;
import com.ngapainya.ngapainya.helper.JSONParser;
import com.ngapainya.ngapainya.helper.SessionManager;
import com.ngapainya.ngapainya.model.Explore;
import com.ngapainya.ngapainya.model.Friend;
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
public class ListApplicantFragment extends Fragment {
    private FragmentActivity myContext;
    private View myFragmentView;

    private Explore explore;
    private String program_id;

    @Bind(R.id.avatar) ImageView avatar;
    @Bind(R.id.name) TextView name;
    @Bind(R.id.title) TextView title;

    @Bind(R.id.list_applicant) ListView list_applicant;
    private ArrayList<Friend> filelist;
    private ListApplicantAdapter adapter;
    private Friend friend;

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragmentView  = inflater.inflate(R.layout.fragment_list_applicant, container, false);
        ButterKnife.bind(this, myFragmentView);

        if (getArguments() != null) {
            program_id = getArguments().getString("program_id");
            new getDetailExplore().execute();
            new getApplicant().execute();
        }

        explore = new Explore();
        filelist = new ArrayList<>();
        adapter = new ListApplicantAdapter(myContext, filelist, program_id);
        list_applicant.setAdapter(adapter);

        return myFragmentView;
    }


    public class getApplicant extends AsyncTask<String, String, String> {
        ProgressDialog pDialog;
        SessionManager session;
        HashMap<String, String> user;
        String token;
        boolean isSuccess;

        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.show();

            isSuccess = false;
            session = new SessionManager(myContext);
            user = session.getUserDetails();
            token = user.get(SessionManager.KEY_TOKEN);
        }

        private void addList(JSONObject result) {
            try {
                friend = new Friend();
                friend.setFriend_id(result.getString("user_id"));
                friend.setFriend_name(result.getString("user_fullname"));
                friend.setFriend_ava(result.getString("user_pic"));
                friend.setApply_status(result.getString("apply_status"));
                Log.e("user_id", result.getString("user_id"));
                filelist.add(friend);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... arg0) {
            String url = Config.HOSTNAME + "/program/"+program_id+"/apply/list";
            List<NameValuePair> nvp = new ArrayList<NameValuePair>();
            nvp.add(new BasicNameValuePair("access_token", token));

            Log.e("url", url);
            Log.e("token", token);

            JSONParser jParser = new JSONParser();
            JSONArray json = jParser.makeHttpRequest(url, "GET", nvp);      //get data from server
            if (json != null) {
                isSuccess = true;
                try {
                    for(int i=0;i<json.length();i++){
                        addList(json.getJSONObject(i));
                        Log.e("get", "get list applicant");
                    }
                } catch (Exception e) {
                    Log.e("error", "tidak bisa ambil data 1");
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            adapter.notifyDataSetChanged();
        }
    }

    public class getDetailExplore extends AsyncTask<String, String, String> {
        ProgressDialog pDialog;
        SessionManager session;
        HashMap<String, String> user;
        String token;
        boolean isSuccess;

        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.show();

            isSuccess = false;
            session = new SessionManager(myContext);
            user = session.getUserDetails();
            token = user.get(SessionManager.KEY_TOKEN);
        }

        private void addList(JSONObject result) {
            try {
                explore.setProgram_id(program_id);
                explore.setProgram_name(result.getString("program_name"));
                explore.setUser_id(result.getString("user_id"));
                explore.setProgram_desc(result.getString("program_desc"));
                explore.setProgram_date_start(result.getString("program_date_start"));
                explore.setProgram_date_end(result.getString("program_date_end"));
                explore.setUser_pic(result.getString("user_pic"));
                explore.setUser_fullname(result.getString("user_fullname"));
                explore.setProgram_age_min(result.getString("program_age_min"));
                explore.setProgram_age_max(result.getString("program_age_max"));
                explore.setProgram_fee(result.getString("program_fee"));
                explore.setProgram_url(result.getString("program_url"));
                explore.setProgram_quota(result.getString("program_quota"));

                Log.e("name", explore.getProgram_name());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... arg0) {
            String url = Config.HOSTNAME + "/program/" + program_id;
            List<NameValuePair> nvp = new ArrayList<NameValuePair>();
            nvp.add(new BasicNameValuePair("access_token", token));

            Log.e("url", url);
            Log.e("token", token);

            JSONParser jParser = new JSONParser();
            JSONObject json = jParser.makeHttpRequestToObject(url, "GET", nvp);      //get data from server
            if (json != null) {
                isSuccess = true;
                try {
                    addList(json);
                    Log.e("get", "get data");
                } catch (Exception e) {
                    Log.e("error", "tidak bisa ambil data 1");
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pDialog.dismiss();

            name.setText(explore.getUser_fullname());
            title.setText(explore.getProgram_name());
            Picasso.with(myContext)
                    .load("http://ainufaisal.com/" + explore.getUser_pic())
                    .placeholder(R.drawable.propic_default)
                    .into(avatar);

            Log.e("get", explore.getUser_pic());
        }
    }
}
