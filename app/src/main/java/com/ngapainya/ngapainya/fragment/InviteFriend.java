package com.ngapainya.ngapainya.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ngapainya.ngapainya.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class InviteFriend extends Fragment {
    private FragmentActivity myContext;
    private View myFragmentView;
    private ListView myList;

    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragmentView  = inflater.inflate(R.layout.fragment_invite_friend, container, false);

        myList = (ListView) myFragmentView.findViewById(R.id.list_friends);

        return myFragmentView;
    }


}
