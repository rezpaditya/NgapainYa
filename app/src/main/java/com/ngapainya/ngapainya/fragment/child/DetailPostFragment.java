package com.ngapainya.ngapainya.fragment.child;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ngapainya.ngapainya.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailPostFragment extends Fragment {
    private FragmentActivity myContext;
    private View myFragmentView;
    private int postType;

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragmentView = inflater.inflate(R.layout.fragment_image_post, container, false);

        postType = getArguments().getInt("postType"); //get the post type

        switch (postType) {
            case 0:
                Toast.makeText(myContext, "here is post type " + postType, Toast.LENGTH_SHORT).show();
                //do something here
                break;
            case 1:
                Toast.makeText(myContext, "here is post type " + postType, Toast.LENGTH_SHORT).show();
                //do something here
                break;
            case 2:
                Toast.makeText(myContext, "here is post type " + postType, Toast.LENGTH_SHORT).show();
                //do something here
                break;
            case 3:
                Toast.makeText(myContext, "here is post type " + postType, Toast.LENGTH_SHORT).show();
                //do something here
                break;
            case 4:
                Toast.makeText(myContext, "here is post type " + postType, Toast.LENGTH_SHORT).show();
                //do something here
                break;
        }
        return myFragmentView;
    }


}
