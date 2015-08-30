package com.ngapainya.ngapainya.fragment.volunteer;


import android.app.Activity;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.ngapainya.ngapainya.R;
import com.ngapainya.ngapainya.activity.volunteer.ContainerActivity;
import com.ngapainya.ngapainya.adapter.HomeAdapter;
import com.ngapainya.ngapainya.adapter.SpeedScrollListener;
import com.ngapainya.ngapainya.fragment.volunteer.child.DetailPostFragment;
import com.ngapainya.ngapainya.fragment.volunteer.child.FindFriendFragment;
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
public class HomeFragment extends Fragment implements AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    private static final String LIST_VIEW_INSTANCE_STATE_KEY = "myList";
    private FragmentActivity myContext;
    private View myFragmentView;
    private ArrayList<Home> filelist;
    private ListView myList;
    private HomeAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    private SpeedScrollListener speedScrollListener;

    private LinearLayout layout_find_friend;
    private Button find_btn;

    /*
    * Variable for request data
    * */
    JSONArray json;
    final int threshold = 10;
    int iterator = 0;

    int start = 0;
    int end = 5;

    /*
    * these variables used to restore the listview
    * when calling the fragment from backstack
    * */
    private boolean fromBackStack = false;
    private HomeAdapter savedAdapter;
    private ArrayList<Home> savedFilelist;

    private final String PACKAGE_NAME = "com.ngapainya.ngapainya.activity.";

    /*@Override
    public void onDestroyView() {
        super.onDestroyView();
        final Bundle arguments = new Bundle();
        final Parcelable listViewInstanceState = myList.onSaveInstanceState();
        arguments.putParcelable(LIST_VIEW_INSTANCE_STATE_KEY, listViewInstanceState);
        Log.e("onDestroyView", "onDestroyView");
    }*/

    /*@Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        final Parcelable listViewInstanceState = myList.onSaveInstanceState();
        outState.putParcelable(LIST_VIEW_INSTANCE_STATE_KEY, listViewInstanceState);
    }*/

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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ArrayList<Home> temp;
        temp = filelist;
        outState.putParcelableArrayList("adapter_content", temp);
        Log.e("onSaveInstanceState", "works");
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

        if(myContext.getClass().getName().equals(PACKAGE_NAME+"volunteer.ContainerActivity")) {
        /*Customize actionbar*/
            ((ContainerActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            ((ContainerActivity) getActivity()).homeTitleBar("Ngapain");
            ((com.ngapainya.ngapainya.activity.volunteer.ContainerActivity) getActivity()).changeActionbarStyle(false);
        }else{
            ((com.ngapainya.ngapainya.activity.owner.ContainerActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            ((com.ngapainya.ngapainya.activity.owner.ContainerActivity) getActivity()).homeTitleBar("Ngapain");
            ((com.ngapainya.ngapainya.activity.owner.ContainerActivity) getActivity()).changeActionbarStyle(false);
        }

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

        layout_find_friend = (LinearLayout) myFragmentView.findViewById(R.id.layout_find_friend);
        find_btn = (Button) myFragmentView.findViewById(R.id.find_btn);
        find_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FindFriendFragment findFriendFragment = new FindFriendFragment();
                ((ContainerActivity) getActivity()).changeFragment(findFriendFragment);
            }
        });

        myList = (ListView) myFragmentView.findViewById(R.id.list_home);
        myList.setAdapter(adapter);
        myList.setOnScrollListener(speedScrollListener);


        //if(savedInstanceState == null){

        /*}else{
            myList.setAdapter(adapter);
            myList.onRestoreInstanceState(savedInstanceState.getParcelable(LIST_VIEW_INSTANCE_STATE_KEY));
        }*/

        myList.setOnItemClickListener(this);

        //adapter = new HomeAdapter(myContext, filelist);
/*
        if (fromBackStack) {
            filelist = savedFilelist;
            adapter = savedAdapter;
        }*/

        return myFragmentView;
    }

    /**
     * This method is called when swipe refresh is pulled down
     */
    @Override
    public void onRefresh() {
        filelist.clear();
        start = 0;
        end = 10;
        new RemoteDataTask().execute();
        swipeRefreshLayout.setRefreshing(false);
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
        args.putString("username", tmp.getUsername());
        switch (tmp.getAct_type()) {
            case "Text":
                args.putInt("postType", 0);
                args.putString("act_id", tmp.getAct_id());
                detailPostFragment.setArguments(args);
                if (myContext.getClass().getName().equals("com.ngapainya.ngapainya.activity.volunteer.ContainerActivity")) {
                    ((ContainerActivity) getActivity()).changeFragment(detailPostFragment);
                }else{
                    ((com.ngapainya.ngapainya.activity.owner.ContainerActivity) getActivity()).changeFragment(detailPostFragment);
                }
                break;
            case "Photo":
                args.putInt("postType", 1);
                args.putString("act_id", tmp.getAct_id());
                detailPostFragment.setArguments(args);
                if (myContext.getClass().getName().equals("com.ngapainya.ngapainya.activity.volunteer.ContainerActivity")) {
                    ((ContainerActivity) getActivity()).changeFragment(detailPostFragment);
                }else{
                    ((com.ngapainya.ngapainya.activity.owner.ContainerActivity) getActivity()).changeFragment(detailPostFragment);
                }
                break;
            case "Location":
                args.putInt("postType", 2);
                args.putString("act_id", tmp.getAct_id());
                detailPostFragment.setArguments(args);
                if (myContext.getClass().getName().equals("com.ngapainya.ngapainya.activity.volunteer.ContainerActivity")) {
                    ((ContainerActivity) getActivity()).changeFragment(detailPostFragment);
                }else{
                    ((com.ngapainya.ngapainya.activity.owner.ContainerActivity) getActivity()).changeFragment(detailPostFragment);
                }
                break;
            case "Url":
                args.putInt("postType", 3);
                args.putString("act_id", tmp.getAct_id());
                detailPostFragment.setArguments(args);
                if (myContext.getClass().getName().equals("com.ngapainya.ngapainya.activity.volunteer.ContainerActivity")) {
                    ((ContainerActivity) getActivity()).changeFragment(detailPostFragment);
                }else{
                    ((com.ngapainya.ngapainya.activity.owner.ContainerActivity) getActivity()).changeFragment(detailPostFragment);
                }
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
            String url = cfg.HOSTNAME + "/activity/followed/limit/" + start + "/" + end;
            List<NameValuePair> nvp = new ArrayList<NameValuePair>();
            nvp.add(new BasicNameValuePair("access_token", token));

            JSONParser jParser = new JSONParser();
            json = jParser.makeHttpRequest(url, "GET", nvp);      //get data from server
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
            iterator = threshold;
            if (isSuccess) {
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
            } else {
                swipeRefreshLayout.setVisibility(View.GONE);
                layout_find_friend.setVisibility(View.VISIBLE);
            }
        }
    }

    /*
    * This method is called when the listview pulled down
    * it will trigger this method to load more items from server
    * */

    public class LoadMoreDataTask extends AsyncTask<String, Void, ArrayList<Home>> {
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
            user    = session.getUserDetails();
            token   = user.get(SessionManager.KEY_TOKEN);
            start   = end - 1;
            end     = start + 5;

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
            String url = cfg.HOSTNAME + "/activity/followed/limit/" + start + "/" + end;
            List<NameValuePair> nvp = new ArrayList<NameValuePair>();
            nvp.add(new BasicNameValuePair("access_token", token));

            JSONParser jParser = new JSONParser();
            json = jParser.makeHttpRequest(url, "GET", nvp);      //get data from server
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
            int position = myList.getLastVisiblePosition();
            //adapter = new HomeAdapter(myContext, filelist);
            adapter.notifyDataSetChanged();
            myList.setSelectionFromTop(position, 0);
        }
    }
}
