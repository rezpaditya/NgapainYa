package com.ngapainya.ngapainya.fragment;


import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ngapainya.ngapainya.R;
import com.ngapainya.ngapainya.activity.volunteer.ContainerActivity;
import com.ngapainya.ngapainya.adapter.HomeAdapter;
import com.ngapainya.ngapainya.fragment.child.DetailPostFragment;
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
public class HomeFragment extends Fragment implements AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    private FragmentActivity myContext;
    private View myFragmentView;
    private ArrayList<Home> filelist;
    private ListView myList;
    private HomeAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    /*
    * Variable for request data
    * */
    JSONArray json;
    final int threshold = 10;
    int iterator = 0;

    /*
    * these variables used to restore the listview
    * when calling the fragment from backstack
    * */
    private boolean fromBackStack = false;
    private HomeAdapter savedAdapter;
    private ArrayList<Home> savedFilelist;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        savedAdapter = adapter;
        savedFilelist = filelist;
        fromBackStack = true;
        Log.e("onDestroyView", "onDestroyView");
    }

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragmentView = inflater.inflate(R.layout.fragment_home, container, false);

        Log.e("onCreateView", "onCreateView");

        swipeRefreshLayout = (SwipeRefreshLayout) myFragmentView.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        /*swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                        //do something
                                        swipeRefreshLayout.setRefreshing(false);
                                    }
                                }
        );*/

        myList = (ListView) myFragmentView.findViewById(R.id.list_home);
        //myList.setAdapter(new HomeAdapter(myContext, filelist));

        myList.setOnItemClickListener(this);

        filelist = new ArrayList<Home>();
        adapter = new HomeAdapter(myContext, filelist);

        if (fromBackStack) {
            filelist = savedFilelist;
            adapter = savedAdapter;
        }

        new RemoteDataTask().execute();
        return myFragmentView;
    }

    /**
     * This method is called when swipe refresh is pulled down
     */
    @Override
    public void onRefresh() {
        ArrayList<Home> new_filelist = new ArrayList<>();
        /*new_filelist.add(new Home("Post", 4));

        filelist.addAll(0, new_filelist);
        adapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);*/

        Log.e("onRefresh", "onRefresh");
    }

    /*
    * This method is called whenever the listview item is clicked
    * */

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Home tmp = (Home) parent.getItemAtPosition(position);
        DetailPostFragment detailPostFragment = new DetailPostFragment();
        Bundle args = new Bundle();
        switch (tmp.getAct_type()) {
            case "status":
                args.putInt("postType", 0);
                detailPostFragment.setArguments(args);
                ((ContainerActivity) getActivity()).changeFragment(detailPostFragment, new ColorDrawable(getResources().getColor(R.color.ColorPrimary)));
                break;
            case "photo":
                args.putInt("postType", 1);
                detailPostFragment.setArguments(args);
                ((ContainerActivity) getActivity()).changeFragment(detailPostFragment, new ColorDrawable(getResources().getColor(R.color.ColorPrimary)));
                break;
            case "location":
                args.putInt("postType", 2);
                detailPostFragment.setArguments(args);
                ((ContainerActivity) getActivity()).changeFragment(detailPostFragment, new ColorDrawable(getResources().getColor(R.color.ColorPrimary)));
                break;
            case "url":
                args.putInt("postType", 3);
                detailPostFragment.setArguments(args);
                ((ContainerActivity) getActivity()).changeFragment(detailPostFragment, new ColorDrawable(getResources().getColor(R.color.ColorPrimary)));
                break;
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

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*
            * Get user information
            * */
            session = new SessionManager(myContext);
            user = session.getUserDetails();
            token = user.get(SessionManager.KEY_TOKEN);
        }

        @Override
        protected ArrayList<Home> doInBackground(String... urls) {

            String url = "http://ainufaisal.com/activity/followed";
            List<NameValuePair> nvp = new ArrayList<NameValuePair>();
            nvp.add(new BasicNameValuePair("access_token", token));

            JSONParser jParser = new JSONParser();
            json = jParser.makeHttpRequest(url, "GET", nvp);      //get data from server
            try {
                /*if data less than threshold, load them all
                * or if not, load data as much as threshold, repeat it in loadmore class
                * */
                if (json.length() < threshold) {
                    for (int i = 0; i < json.length(); i++) {
                        JSONObject result = json.getJSONObject(i);

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
                        Log.e("ok", " ambil data");
                    }
                } else {
                    for (int i = 0; i < threshold; i++) {
                        JSONObject result = json.getJSONObject(i);
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
                        Log.e("ok", " ambil data");
                    }
                }
            } catch (Exception e) {
                Log.e("error", "tidak bisa ambil data 1");
                e.printStackTrace();
            }
            return filelist;
        }

        @Override
        protected void onPostExecute(ArrayList<Home> organization) {
            myList.setAdapter(adapter);
            // Create an OnScrollListener
            iterator = threshold;
            myList.setOnScrollListener(new AbsListView.OnScrollListener() {

                @Override
                public void onScrollStateChanged(AbsListView view,
                                                 int scrollState) { // TODO Auto-generated method stub
                    int threshold = 1;
                    int count = myList.getCount();

                    if (scrollState == SCROLL_STATE_IDLE) {
                        if (myList.getLastVisiblePosition() >= count
                                - threshold) {
                            // Execute LoadMoreDataTask AsyncTask
                            new LoadMoreDataTask().execute();
                        }
                    }
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem,
                                     int visibleItemCount, int totalItemCount) {
                    // TODO Auto-generated method stub

                }

            });
        }
    }

    /*
    * This method is called when the listview pulled down
    * it will trigger this method to load more items from server
    * */

    public class LoadMoreDataTask extends AsyncTask<String, Void, ArrayList<Home>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<Home> doInBackground(String... urls) {
            try {
                // web service request
                if (iterator != json.length()) {
                    JSONObject result = json.getJSONObject(iterator);

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
                    iterator++;
                    Log.e("ok", " ambil data");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return filelist;
        }

        @Override
        protected void onPostExecute(ArrayList<Home> organization) {
            int position = myList.getLastVisiblePosition();
            //adapter = new HomeAdapter(myContext, filelist);
            myList.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            myList.setSelectionFromTop(position, 0);
        }
    }
}
