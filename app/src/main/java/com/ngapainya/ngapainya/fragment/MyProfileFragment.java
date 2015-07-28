package com.ngapainya.ngapainya.fragment;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.ngapainya.ngapainya.R;
import com.ngapainya.ngapainya.activity.owner.ContainerActivity;
import com.ngapainya.ngapainya.fragment.child.ShowFeedFragment;
import com.ngapainya.ngapainya.fragment.child.ShowFriendFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyProfileFragment extends Fragment {
    private FragmentActivity myContext;
    private View myFragmentView;

    private static final String FOLLOWING_SPEC = "following";
    private static final String YOU_SPEC = "you";
    private FragmentTabHost tabHost;

    private TextView txtShowFeed, txtShowFriend;
    private ImageView propic;

    public void switchMode(){
        Intent intent = new Intent(myContext, ContainerActivity.class);
        startActivity(intent);
        myContext.finish();
    }

    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragmentView = inflater.inflate(R.layout.fragment_profile, container, false);

        setHasOptionsMenu(true);

        propic = (ImageView) myFragmentView.findViewById(R.id.profile_image);

        txtShowFeed = (TextView) myFragmentView.findViewById(R.id.txtShwFeed);
        txtShowFriend = (TextView) myFragmentView.findViewById(R.id.txtShwFriend);

        tabHost = (FragmentTabHost) myFragmentView.findViewById(android.R.id.tabhost);  // The activity TabHost
        tabHost.setup(getActivity(), getChildFragmentManager(), android.R.id.tabcontent);

        tabHost.addTab(tabHost.newTabSpec(FOLLOWING_SPEC).setIndicator("Following"), ShowFeedFragment.class, null);
        tabHost.addTab(tabHost.newTabSpec(YOU_SPEC).setIndicator("You"), ShowFriendFragment.class, null);
        tabHost.setCurrentTab(0);

        return myFragmentView;
    }

    public void onClick(View v){
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
            case R.id.editProfileBtn:
                //do something here
                break;
            case R.id.profile_image:
                PopupMenu popup = new PopupMenu(myContext, propic);
                popup.getMenuInflater().inflate(R.menu.menu_pop_up_propic, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.capture) {
                            //do something
                        } else if (menuItem.getItemId() == R.id.gallery) {
                            //do something
                        }
                        return true;
                    }
                });
                popup.show();
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Auto-generated method stub
        super.onCreateOptionsMenu(menu, inflater);
        myContext.getMenuInflater().inflate(R.menu.menu_profile, menu);
        final MenuItem item = menu.findItem(R.id.action_done);
        /*item.getActionView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchMode();
            }
        });*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done : {
                switchMode();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
