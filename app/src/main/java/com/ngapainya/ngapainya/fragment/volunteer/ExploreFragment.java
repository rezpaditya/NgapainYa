package com.ngapainya.ngapainya.fragment.volunteer;


import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ngapainya.ngapainya.R;
import com.ngapainya.ngapainya.activity.volunteer.ContainerActivity;
import com.ngapainya.ngapainya.adapter.ExploreAdapter;
import com.ngapainya.ngapainya.fragment.volunteer.child.DetailExploreFragment;
import com.ngapainya.ngapainya.helper.Config;
import com.ngapainya.ngapainya.helper.JSONParser;
import com.ngapainya.ngapainya.helper.SessionManager;
import com.ngapainya.ngapainya.model.Explore;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * All program can be seen here
 * A simple {@link Fragment} subclass.
 */
public class ExploreFragment extends Fragment implements AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener, SearchView.OnQueryTextListener {
    private FragmentActivity myContext;
    private View myFragmentView;
    private ArrayList<Explore> filelist;
    private ListView myList;
    private ExploreAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    private final String PACKAGE_NAME = "com.ngapainya.ngapainya.activity.";

    private String keyword;

    /**
     * Variable for request data
     */
    JSONArray json;
    final int threshold = 10;
    int iterator = 0;

    /**
     * these variables used to restore the listview
     * when calling the fragment from backstack
     */
    private boolean fromBackStack = false;
    private ExploreAdapter savedAdapter;
    private ArrayList<Explore> savedFilelist;

    private SearchView mSearchView;

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    /**
     * Infalate action bar menu
     * @param menu
     * @param inflater
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Auto-generated method stub
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) searchItem.getActionView();
        setupSearchView(searchItem);
    }

    protected boolean isAlwaysExpanded() {
        return false;
    }

    /**
     * setup the menu
     * @param searchItem
     */
    private void setupSearchView(MenuItem searchItem) {

        if (isAlwaysExpanded()) {
            mSearchView.setIconifiedByDefault(false);
        } else {
            searchItem.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM
                    | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
        }
        mSearchView.setOnQueryTextListener(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("onCreate", "works");
        filelist = new ArrayList<Explore>();
        adapter = new ExploreAdapter(myContext, filelist);
        if (savedInstanceState != null) {
            filelist= savedInstanceState.getParcelableArrayList("adapter_content");
            adapter = new ExploreAdapter(myContext, filelist);
            Log.e("savedInstanceState", "works");
        }else{
            new RemoteDataTask().execute();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ArrayList<Explore> temp = new ArrayList<Explore>();
        temp = filelist;
        outState.putParcelableArrayList("adapter_content",temp);
        Log.e("onSaveInstanceState", "works");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragmentView = inflater.inflate(R.layout.fragment_explore, container, false);

        if(myContext.getClass().getName().equals(PACKAGE_NAME+"volunteer.ContainerActivity")) {
        /*Customize actionbar*/
            ((ContainerActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            ((ContainerActivity) getActivity()).homeTitleBar("Explore");
            ((com.ngapainya.ngapainya.activity.volunteer.ContainerActivity) getActivity()).changeActionbarStyle(false);
        }else{
            ((com.ngapainya.ngapainya.activity.owner.ContainerActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            ((com.ngapainya.ngapainya.activity.owner.ContainerActivity) getActivity()).homeTitleBar("Explore");
            ((com.ngapainya.ngapainya.activity.owner.ContainerActivity) getActivity()).changeActionbarStyle(false);
        }

        swipeRefreshLayout = (SwipeRefreshLayout) myFragmentView.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        setHasOptionsMenu(true);

        myList = (ListView) myFragmentView.findViewById(R.id.list_explore);
        myList.setAdapter(adapter);
        myList.setOnItemClickListener(this);

        return myFragmentView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DetailExploreFragment detailExploreFragment = new DetailExploreFragment();
        Explore tmp = (Explore) parent.getItemAtPosition(position);
        Bundle args = new Bundle();
        args.putString("program_id", tmp.getProgram_id());
        detailExploreFragment.setArguments(args);
        if (myContext.getClass().getName().equals("com.ngapainya.ngapainya.activity.volunteer.ContainerActivity")) {
            ((ContainerActivity) getActivity()).changeFragment(detailExploreFragment);
        }else{
            ((com.ngapainya.ngapainya.activity.owner.ContainerActivity) getActivity()).changeFragment(detailExploreFragment);
        }

    }

    /**
     * Recreate th list whenever list refreshed
     */
    @Override
    public void onRefresh() {
        filelist.clear();
        new RemoteDataTask().execute();
        swipeRefreshLayout.setRefreshing(false);

        Log.e("onRefresh", "onRefresh");
    }

    /**
     * Search the program then text submitted
     * @param s
     * @return
     */
    @Override
    public boolean onQueryTextSubmit(String s) {
        keyword = s;
        if(keyword != null) {
            new doSearch().execute();
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        Log.e("onQueryTextChange", "works "+s);
        return true;
    }

    /**
     * Send keyword to API
     */
    private class doSearch extends AsyncTask<String, Void, ArrayList<Explore>> {
        SessionManager session;
        HashMap<String, String> user;
        String token;
        Config cfg = new Config();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            session = new SessionManager(myContext);
            user = session.getUserDetails();
            token = user.get(SessionManager.KEY_TOKEN);

            filelist.clear();
        }

        private void addList(JSONObject result){
            try {
                Explore temp_home = new Explore();
                temp_home.setProgram_id(result.getString("program_id"));
                temp_home.setProgram_name(result.getString("program_name"));
                temp_home.setUser_id(result.getString("user_id"));
                temp_home.setProgram_desc(result.getString("program_desc"));
                temp_home.setProgram_date_start(result.getString("program_date_start"));
                temp_home.setProgram_date_end(result.getString("program_date_end"));
                //temp_home.setUser_pic(result.getString("user_pic"));

                filelist.add(temp_home);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        protected ArrayList<Explore> doInBackground(String... urls) {
            String url = cfg.HOSTNAME +"/program/search";
            List<NameValuePair> nvp = new ArrayList<NameValuePair>();
            nvp.add(new BasicNameValuePair("access_token", token));
            nvp.add(new BasicNameValuePair("search", keyword));

            JSONParser jParser = new JSONParser();
            JSONArray json = jParser.makeHttpRequest(url, "GET", nvp);      //get data from server
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
            return filelist;
        }

        @Override
        protected void onPostExecute(ArrayList<Explore> organization) {
            // Create an OnScrollListener
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * Get data program and populate to listview
     */
    private class RemoteDataTask extends AsyncTask<String, Void, ArrayList<Explore>> {
        SessionManager session;
        HashMap<String, String> user;
        String token;
        Config cfg = new Config();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            session = new SessionManager(myContext);
            user = session.getUserDetails();
            token = user.get(SessionManager.KEY_TOKEN);
        }

        private void addList(JSONObject result){
            try {
                Explore temp_home = new Explore();
                temp_home.setProgram_id(result.getString("program_id"));
                temp_home.setProgram_name(result.getString("program_name"));
                temp_home.setUser_id(result.getString("user_id"));
                temp_home.setProgram_desc(result.getString("program_desc"));
                temp_home.setProgram_date_start(result.getString("program_date_start"));
                temp_home.setProgram_date_end(result.getString("program_date_end"));
                temp_home.setUser_pic(result.getString("user_pic"));

                filelist.add(temp_home);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        protected ArrayList<Explore> doInBackground(String... urls) {
            String url = cfg.HOSTNAME +"/program/all";
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
                        addList(result);
                        Log.e("ok", " ambil data");
                    }
                } else {
                    for (int i = 0; i < threshold; i++) {
                        JSONObject result = json.getJSONObject(i);
                        addList(result);
                        Log.e("ok", " ambil data");
                    }
                }
            } catch (Exception e) {
                Log.e("error", "tidak bisa ambil data 1");
                e.printStackTrace();
            }
            return filelist;
            /*try {
                ArrayList<Explore> new_filelist = new ArrayList<Explore>();
                //dummy data
                for (int i = 0; i < 10; i++) {
                    filelist.add(new Explore("img " + i, "title " + i, "text " + i, "strDate " + i, "endDate " + i, "image"));
                }
                filelist.addAll(new_filelist);
            } catch (ParseException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return filelist;*/
        }

        @Override
        protected void onPostExecute(ArrayList<Explore> organization) {
            // Create an OnScrollListener
            adapter.notifyDataSetChanged();
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

    /**
     * This method is called when the listview pulled down
     * it will trigger this method to load more items from server
     */
    public class LoadMoreDataTask extends AsyncTask<String, Void, ArrayList<Explore>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<Explore> doInBackground(String... urls) {
            try {
                // web service request
                if (iterator != json.length()) {
                    JSONObject result = json.getJSONObject(iterator);

                    Explore temp_home = new Explore();
                    temp_home.setProgram_id(result.getString("program_id"));
                    temp_home.setProgram_name(result.getString("program_name"));
                    temp_home.setUser_id(result.getString("user_id"));
                    temp_home.setProgram_desc(result.getString("program_desc"));
                    temp_home.setProgram_date_start(result.getString("program_date_start"));
                    temp_home.setProgram_date_end(result.getString("program_date_end"));
                    temp_home.setUser_pic(result.getString("user_pic"));

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
        protected void onPostExecute(ArrayList<Explore> organization) {
            int position = myList.getLastVisiblePosition();
            //adapter = new HomeAdapter(myContext, filelist);
            adapter.notifyDataSetChanged();
            myList.setSelectionFromTop(position, 0);
        }
    }

}
