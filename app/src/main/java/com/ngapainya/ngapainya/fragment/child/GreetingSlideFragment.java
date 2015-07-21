package com.ngapainya.ngapainya.fragment.child;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ngapainya.ngapainya.R;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class GreetingSlideFragment extends Fragment {
    private FragmentActivity myContext;
    private View myFragmentView;

    public GreetingSlideFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myFragmentView = inflater.inflate(R.layout.fragment_greeting_slide, container, false);
        // Inflate the layout for this fragment

        //Initialize ImageView
        ImageView imageView = (ImageView) myFragmentView.findViewById(R.id.imgGreeting);

        //Loading image from below url into imageView
        Picasso.with(myContext)
                .load("http://nyewamobil.com/customer/images/car/nissanmicra.png")
                .resize(500, 200)
                .into(imageView);

        return myFragmentView;
    }
}
