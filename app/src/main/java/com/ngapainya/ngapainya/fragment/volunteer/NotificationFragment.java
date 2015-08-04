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
