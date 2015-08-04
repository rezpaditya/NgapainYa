package com.ngapainya.ngapainya.fragment.volunteer.child;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class FindFriendFragment extends Fragment {
    private FragmentActivity myContext;
    private View myFragmentView;

    /*
    * Variable from the view
    * */
    private EditText friend_id;
    private LinearLayout friend_result;
    private ImageView propic_result;
    private TextView name_result;

    /*
    * Variabel to get data from server
    * */
    private boolean isFounded;
    private String avatar_url;
    private String username;
    private String user_id;

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragmentView = inflater.inflate(R.layout.fragment_find_friend, container, false);

        isFounded = false;

        friend_result = (LinearLayout) myFragmentView.findViewById(R.id.friend_result);
        propic_result = (ImageView) myFragmentView.findViewById(R.id.profile_image);
        name_result = (TextView) myFragmentView.findViewById(R.id.name_result);

        friend_id = (EditText) myFragmentView.findViewById(R.id.input_search);
        friend_id.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    new findFriend().execute();
                    return true;
                }
                return false;
            }
        });

        friend_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args = new Bundle();
                args.putString("user_id", user_id);
                //set Fragmentclass Arguments

                VolunteerProfileFragment volunteerProfileFragment = new VolunteerProfileFragment();
                volunteerProfileFragment.setArguments(args);
                ((ContainerActivity) getActivity()).changeFragment(volunteerProfileFragment);
                ((ContainerActivity) getActivity()).changeActionbarStyle(true);
                ((ContainerActivity) getActivity()).standardTitleBar(username);
            }
        });


        return myFragmentView;
    }

    public class findFriend extends AsyncTask<String, String, String> {
        ProgressDialog pDialog;
        SessionManager session;
        HashMap<String, String> user;
        String token;
        String input;
        Config cfg = new Config();

        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

            session = new SessionManager(myContext);
            user = session.getUserDetails();
            token = user.get(SessionManager.KEY_TOKEN);
            input = friend_id.getText().toString();
        }

        @Override
        protected String doInBackground(String... arg0) {
            String url = cfg.HOSTNAME + "/profile/search";
            List<NameValuePair> nvp = new ArrayList<NameValuePair>();
            nvp.add(new BasicNameValuePair("access_token", token));
            nvp.add(new BasicNameValuePair("keyword", input));

            JSONParser jParser = new JSONParser();
            JSONArray json = jParser.makeHttpRequest(url, "GET", nvp);      //get data from server
            try {
                if (json.length() > 0) {
                    isFounded = true;
                    for (int i = 0; i < json.length(); i++) {
                        JSONObject result = json.getJSONObject(i);
                        username = result.getString("username");
                        avatar_url = result.getString("avatar");
                        user_id = result.getString("user_id");
                    }
                }
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
            pDialog.dismiss();
            if (isFounded) {
                isFounded = false;
                friend_result.setVisibility(View.VISIBLE);
                Picasso.with(myContext)
                        .load("http://ainufaisal.com/" + avatar_url)
                        .placeholder(R.drawable.propic_default)
                        .into(propic_result);
                name_result.setText(username);
            } else {
                friend_result.setVisibility(View.GONE);
                Log.e("not found", "works");
            }
        }
    }
}
