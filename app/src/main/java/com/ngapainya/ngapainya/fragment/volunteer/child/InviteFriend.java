package com.ngapainya.ngapainya.fragment.volunteer.child;


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
import com.ngapainya.ngapainya.adapter.FriendAdapter;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class InviteFriend extends Fragment  {
    private FragmentActivity myContext;
    private View myFragmentView;

    private ListView myList;
    private ImageView avatar;
    private TextView user_fullname;
    private TextView program_name;

    private String program_id;

    private Friend friend;
    private ArrayList<Friend> filelist;
    private FriendAdapter adapter;


    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        filelist = new ArrayList<>();
        if(getArguments() != null) {
            program_id = getArguments().getString("program_id");
            adapter = new FriendAdapter(myContext, filelist, program_id);
        }
        new getFriends().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragmentView  = inflater.inflate(R.layout.fragment_invite_friend, container, false);

        myList = (ListView) myFragmentView.findViewById(R.id.list_friends);
        avatar = (ImageView) myFragmentView.findViewById(R.id.avatar);
        user_fullname = (TextView) myFragmentView.findViewById(R.id.user_fullname);
        program_name = (TextView) myFragmentView.findViewById(R.id.program_name);

        myList.setAdapter(adapter);

        if(getArguments() != null) {
            program_id = getArguments().getString("program_id");
            Explore explore = getArguments().getParcelable("program_detail");

            Picasso.with(myContext)
                    .load("http://ainufaisal.com/" + explore.getUser_pic())
                    .placeholder(R.drawable.propic_default)
                    .into(avatar);
            user_fullname.setText(explore.getUser_fullname());
            program_name.setText(explore.getProgram_name());

            /*//dummy data
            for(int i=0;i<5;i++) {
                friend = new Friend();
                friend.setFriend_id(String.valueOf(i));
                friend.setFriend_name("dummy name"+i);
                friend.setFriend_ava("dummy_ava"+i);
                filelist.add(friend);
            }*/
        }

        return myFragmentView;
    }

    public class getFriends extends AsyncTask<String, String, String> {
        ProgressDialog pDialog;
        SessionManager session;
        HashMap<String, String> user;
        String token;
        boolean isSuccess;
        Config cfg = new Config();

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
                friend.setFriend_name(result.getString("username"));
                friend.setFriend_ava(result.getString("user_pic"));
                filelist.add(friend);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... arg0) {
            String url = cfg.HOSTNAME + "/follower/volunteer";
            List<NameValuePair> nvp = new ArrayList<NameValuePair>();
            nvp.add(new BasicNameValuePair("access_token", token));

            Log.e("url", url);
            Log.e("token", token);

            JSONParser jParser = new JSONParser();
            JSONArray json = jParser.makeHttpRequest(url, "GET", nvp);      //get data from server
            if (json != null) {
                isSuccess = true;
                try {
                    for(int i=0;i<json.length();i++) {
                        addList(json.getJSONObject(i));
                    }
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
            adapter.notifyDataSetChanged();
        }
    }

}
