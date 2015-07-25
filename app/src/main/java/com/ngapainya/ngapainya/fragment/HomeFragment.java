package com.ngapainya.ngapainya.fragment;


import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.net.ParseException;
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
import com.ngapainya.ngapainya.model.Home;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener{
    private FragmentActivity myContext;
    private View myFragmentView;
    private ArrayList<Home> filelist;
    private ListView myList;
    private HomeAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

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

        if(fromBackStack) {
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
        new_filelist.add(new Home("Post", 4));

        filelist.addAll(0, new_filelist);
        adapter.notifyDataSetChanged();
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
        switch (tmp.type) {
            case 0:
                args.putInt("postType", tmp.type);
                detailPostFragment.setArguments(args);
                ((ContainerActivity) getActivity()).changeFragment(detailPostFragment, new ColorDrawable(getResources().getColor(R.color.ColorPrimary)));
                break;
            case 1:
                args.putInt("postType", tmp.type);
                detailPostFragment.setArguments(args);
                ((ContainerActivity) getActivity()).changeFragment(detailPostFragment, new ColorDrawable(getResources().getColor(R.color.ColorPrimary)));
                break;
            case 2:
                args.putInt("postType", tmp.type);
                detailPostFragment.setArguments(args);
                ((ContainerActivity) getActivity()).changeFragment(detailPostFragment, new ColorDrawable(getResources().getColor(R.color.ColorPrimary)));
                break;
            case 3:
                args.putInt("postType", tmp.type);
                detailPostFragment.setArguments(args);
                ((ContainerActivity) getActivity()).changeFragment(detailPostFragment, new ColorDrawable(getResources().getColor(R.color.ColorPrimary)));
                break;
            case 4:
                args.putInt("postType", tmp.type);
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

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<Home> doInBackground(String... urls) {
            try {
                ArrayList<Home> new_filelist = new ArrayList<Home>();
                //dummy data
                new_filelist.add(new Home("post", 1)); //post image
                new_filelist.add(new Home("post", 2)); //post location
                new_filelist.add(new Home("post", 3)); //post url
                new_filelist.add(new Home("post", 4)); //become friend with
                new_filelist.add(new Home("post", 0)); //post status
                new_filelist.add(new Home("post", 3)); //post url
                new_filelist.add(new Home("post", 4)); //become friend with
                new_filelist.add(new Home("post", 0)); //post status
                filelist.addAll(new_filelist);
            } catch (ParseException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return filelist;
        }

        @Override
        protected void onPostExecute(ArrayList<Home> organization) {
            myList.setAdapter(adapter);
            // Create an OnScrollListener
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
                ArrayList<Home> new_filelist = new ArrayList<Home>();
                new_filelist.add(new Home("post", 0)); //post status
                new_filelist.add(new Home("post", 1));
                if(new_filelist  != null && new_filelist.size() > 0 ){
                    filelist.addAll(new_filelist);
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
