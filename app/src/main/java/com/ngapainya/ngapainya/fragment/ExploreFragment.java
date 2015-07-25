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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ngapainya.ngapainya.R;
import com.ngapainya.ngapainya.activity.volunteer.ContainerActivity;
import com.ngapainya.ngapainya.adapter.ExploreAdapter;
import com.ngapainya.ngapainya.fragment.child.DetailExploreFragment;
import com.ngapainya.ngapainya.model.Explore;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExploreFragment extends Fragment implements AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    private FragmentActivity myContext;
    private View myFragmentView;
    private ArrayList<Explore> filelist;
    private ListView myList;
    private ExploreAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    /*
    * these variables used to restore the listview
    * when calling the fragment from backstack
    * */
    private boolean fromBackStack = false;
    private ExploreAdapter savedAdapter;
    private ArrayList<Explore> savedFilelist;

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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Auto-generated method stub
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_search, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragmentView = inflater.inflate(R.layout.fragment_explore, container, false);

        swipeRefreshLayout = (SwipeRefreshLayout) myFragmentView.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        setHasOptionsMenu(true);

        filelist = new ArrayList<Explore>();

        myList = (ListView) myFragmentView.findViewById(R.id.list_explore);
        myList.setOnItemClickListener(this);

        adapter = new ExploreAdapter(myContext, filelist);

        if(fromBackStack) {
            filelist = savedFilelist;
            adapter = savedAdapter;
        }

        new RemoteDataTask().execute();

        return myFragmentView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DetailExploreFragment detailExploreFragment = new DetailExploreFragment();
        ((ContainerActivity) getActivity()).changeFragment(detailExploreFragment, new ColorDrawable(getResources().getColor(R.color.ColorPrimary)));
    }

    @Override
    public void onRefresh() {
        ArrayList<Explore> new_filelist = new ArrayList<>();
        new_filelist.add(new Explore("img " + 99, "title " + 99, "text " + 99, "strDate " + 99, "endDate " + 99, "image"));

        filelist.addAll(0, new_filelist);
        adapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);

        Log.e("onRefresh", "onRefresh");
    }

    private class RemoteDataTask extends AsyncTask<String, Void, ArrayList<Explore>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<Explore> doInBackground(String... urls) {
            try {
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
            return filelist;
        }

        @Override
        protected void onPostExecute(ArrayList<Explore> organization) {
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

    public class LoadMoreDataTask extends AsyncTask<String, Void, ArrayList<Explore>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<Explore> doInBackground(String... urls) {
            try {
                // web service request
                ArrayList<Explore> new_filelist = new ArrayList<Explore>();
                new_filelist.add(new Explore("img " + 88, "title " + 88, "text " + 88, "strDate " + 88, "endDate " + 88, "image"));
                if(new_filelist  != null && new_filelist.size() > 0 ){
                    filelist.addAll(new_filelist);
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
            myList.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            myList.setSelectionFromTop(position, 0);
        }
    }

}
