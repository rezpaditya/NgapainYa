package com.ngapainya.ngapainya.fragment;


import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ngapainya.ngapainya.R;
import com.ngapainya.ngapainya.activity.volunteer.ContainerActivity;
import com.ngapainya.ngapainya.adapter.HomeAdapter;
import com.ngapainya.ngapainya.controller.EndlessScrollListener;
import com.ngapainya.ngapainya.fragment.child.DetailPostFragment;
import com.ngapainya.ngapainya.model.Home;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements AdapterView.OnItemClickListener {
    private FragmentActivity myContext;
    private View myFragmentView;
    private ArrayList<Home> filelist;
    private ListView myList;

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

        filelist = new ArrayList<Home>();

        //dummy data
        filelist.add(new Home("post", 1)); //post image
        filelist.add(new Home("post", 2)); //post location
        filelist.add(new Home("post", 3)); //post url
        filelist.add(new Home("post", 4)); //become friend with
        filelist.add(new Home("post", 0)); //post status


        myList = (ListView) myFragmentView.findViewById(R.id.list_home);
        myList.setAdapter(new HomeAdapter(myContext, filelist));
        myList.setOnItemClickListener(this);
        myList.setOnScrollListener(new EndlessScrollListener() {

            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // TODO Auto-generated method stub
                Log.e("loadmore", "load broo!!");
            }
        });

        return myFragmentView;
    }

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

}
