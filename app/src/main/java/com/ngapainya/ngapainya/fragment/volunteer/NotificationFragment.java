package com.ngapainya.ngapainya.fragment.volunteer;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ngapainya.ngapainya.R;
import com.ngapainya.ngapainya.activity.volunteer.ContainerActivity;
import com.ngapainya.ngapainya.adapter.NotifAdapter;
import com.ngapainya.ngapainya.model.Notification;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment {
    private FragmentActivity myContext;
    private View myFragmentView;
    private ArrayList<Notification> filelist;
    private ListView myList;

    private final String PACKAGE_NAME = "com.ngapainya.ngapainya.activity.";

    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragmentView = inflater.inflate(R.layout.fragment_notification, container, false);

        if(myContext.getClass().getName().equals(PACKAGE_NAME+"volunteer.ContainerActivity")) {
        /*Customize actionbar*/
            ((ContainerActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            ((ContainerActivity) getActivity()).homeTitleBar("Notification");
            ((com.ngapainya.ngapainya.activity.volunteer.ContainerActivity) getActivity()).changeActionbarStyle(false);
        }
        filelist = new ArrayList<Notification>();

        //dummy data
        for(int i=0;i<10;i++) {
            filelist.add(new Notification("data "+i));
        }

        myList = (ListView) myFragmentView.findViewById(R.id.list_notif);
        myList.setAdapter(new NotifAdapter(myContext, filelist));

        return myFragmentView;
    }


}
