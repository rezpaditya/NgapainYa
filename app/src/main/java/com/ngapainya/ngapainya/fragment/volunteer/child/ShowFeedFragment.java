package com.ngapainya.ngapainya.fragment.volunteer.child;


import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ngapainya.ngapainya.R;
import com.ngapainya.ngapainya.activity.volunteer.ContainerActivity;
import com.ngapainya.ngapainya.adapter.HomeAdapter;
import com.ngapainya.ngapainya.adapter.SpeedScrollListener;
import com.ngapainya.ngapainya.helper.Config;
import com.ngapainya.ngapainya.helper.JSONParser;
import com.ngapainya.ngapainya.helper.SessionManager;
import com.ngapainya.ngapainya.model.Home;

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
public class ShowFeedFragment extends Fragment implements AdapterView.OnItemClickListener {
    private FragmentActivity myContext;
    private View myFragmentView;

    private ArrayList<Home> filelist;
    private ListView myList;
    private HomeAdapter adapter;
    private SpeedScrollListener speedScrollListener;

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        filelist = new ArrayList<Home>();
        speedScrollListener = new SpeedScrollListener();
        adapter = new HomeAdapter(myContext, speedScrollListener, filelist);

        if (savedInstanceState != null) {
            filelist = savedInstanceState.getParcelableArrayList("adapter_content");
            adapter = new HomeAdapter(myContext, speedScrollListener, filelist);
            Log.e("savedInstanceState", "works");
        } else {
            new RemoteDataTask().execute();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragmentView = inflater.inflate(R.layout.fragment_show_feed, container, false);

        myList = (ListView) myFragmentView.findViewById(R.id.list_home);
        myList.setAdapter(adapter);
        myList.setOnScrollListener(speedScrollListener);
        myList.setOnItemClickListener(this);

        return myFragmentView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Home tmp = (Home) parent.getItemAtPosition(position);
        DetailPostFragment detailPostFragment = new DetailPostFragment();
        Bundle args = new Bundle();
        args.putString("username", tmp.getUsername());
        switch (tmp.getAct_type()) {
            case "Text":
                args.putInt("postType", 0);
                args.putString("act_id", tmp.getAct_id());
                detailPostFragment.setArguments(args);
                ((ContainerActivity) getActivity()).changeFragment(detailPostFragment);
                break;
            /*case "status":
                args.putInt("postType", 0);
                detailPostFragment.setArguments(args);
                ((ContainerActivity) getActivity()).changeFragment(detailPostFragment);
                break;*/
            case "Photo":
                args.putInt("postType", 1);
                args.putString("act_id", tmp.getAct_id());
                detailPostFragment.setArguments(args);
                ((ContainerActivity) getActivity()).changeFragment(detailPostFragment);
                break;
            /*case "Location":
                args.putInt("postType", 2);
                detailPostFragment.setArguments(args);
                ((ContainerActivity) getActivity()).changeFragment(detailPostFragment);
                break;
            case "Url":
                args.putInt("postType", 3);
                detailPostFragment.setArguments(args);
                ((ContainerActivity) getActivity()).changeFragment(detailPostFragment);
                break;*/
        }
    }

    /*
    * This method is called in onCreateView method
    * Used to load data from server and populate them into
    * the listview
    * */

    private class RemoteDataTask extends AsyncTask<String, Void, ArrayList<Home>> {
        SessionManager session;
        HashMap<String, String> user;
        String token;
        boolean isSuccess;
        Config cfg = new Config();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*
            * Get user information
            * */
            isSuccess = false;
            session = new SessionManager(myContext);
            user = session.getUserDetails();
            token = user.get(SessionManager.KEY_TOKEN);
        }

        private void addList(JSONObject result) {
            try {
                Home temp_home = new Home();
                temp_home.setAct_id(result.getString("act_id"));
                temp_home.setAct_content(result.getString("act_content"));
                temp_home.setAct_type(result.getString("act_type"));
                temp_home.setAct_url(result.getString("act_url"));
                temp_home.setAct_lat(result.getString("act_lat"));
                temp_home.setAct_lng(result.getString("act_lng"));
                temp_home.setAct_address(result.getString("act_address"));
                temp_home.setUsername(result.getString("username"));
                temp_home.setUser_pic(result.getString("user_pic"));
                temp_home.setCreated_at(result.getString("created_at"));

                filelist.add(temp_home);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected ArrayList<Home> doInBackground(String... urls) {
            String url = cfg.HOSTNAME + "/activity/created";
            List<NameValuePair> nvp = new ArrayList<NameValuePair>();
            nvp.add(new BasicNameValuePair("access_token", token));

            JSONParser jParser = new JSONParser();
            JSONArray json = jParser.makeHttpRequest(url, "GET", nvp);      //get data from server
            if (json.length() > 0) {  //check the result
                isSuccess = true;
                try {
                    for (int i = 0; i < json.length(); i++) {
                        JSONObject result = json.getJSONObject(i);
                        addList(result);
                        Log.e("ok", " ambil data");
                    }
                } catch (Exception e) {
                    Log.e("error", "tidak bisa ambil data 1");
                    e.printStackTrace();
                }
            }
            return filelist;
        }

        @Override
        protected void onPostExecute(ArrayList<Home> organization) {
            // Create an OnScrollListener
            adapter.notifyDataSetChanged();
        }
    }
}
