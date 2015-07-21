package com.ngapainya.ngapainya.fragment.child;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ngapainya.ngapainya.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class VolunteerProfileFragment extends Fragment {
    private FragmentActivity myContext;
    private View myFragmentView;

    private static final String FOLLOWING_SPEC = "following";
    private static final String YOU_SPEC = "you";
    private FragmentTabHost tabHost;

    private TextView txtShowFeed, txtShowFriend;

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

        txtShowFeed = (TextView) myFragmentView.findViewById(R.id.txtShwFeed);
        txtShowFriend = (TextView) myFragmentView.findViewById(R.id.txtShwFriend);

        tabHost = (FragmentTabHost) myFragmentView.findViewById(android.R.id.tabhost);  // The activity TabHost
        tabHost.setup(getActivity(), getChildFragmentManager(), android.R.id.tabcontent);

        tabHost.addTab(tabHost.newTabSpec(FOLLOWING_SPEC).setIndicator("Following"), ShowFeedFragment.class, null);
        tabHost.addTab(tabHost.newTabSpec(YOU_SPEC).setIndicator("You"), ShowFriendFragment.class, null);
        tabHost.setCurrentTab(0);

        return myFragmentView;
    }

    /*public void onClick(View v){
        switch (v.getId()) {
            case R.id.showFeed:
                tabHost.setCurrentTab(0);
                txtShowFeed.setTextColor(getResources().getColor(R.color.Red));
                txtShowFriend.setTextColor(Color.BLACK);
                break;
            case R.id.showFriend:
                tabHost.setCurrentTab(1);
                txtShowFriend.setTextColor(getResources().getColor(R.color.Red));
                txtShowFeed.setTextColor(Color.BLACK);
                break;
        }
    }*/
}
