package com.ngapainya.ngapainya.fragment;


import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class ExploreFragment extends Fragment implements AdapterView.OnItemClickListener {
    private FragmentActivity myContext;
    private View myFragmentView;
    private ArrayList<Explore> filelist;
    private ListView myList;

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

        setHasOptionsMenu(true);

        filelist = new ArrayList<Explore>();

        //dummy data
        for (int i = 0; i < 10; i++) {
            filelist.add(new Explore("img " + i, "title " + i, "text " + i, "strDate " + i, "endDate " + i, "image"));
        }

        myList = (ListView) myFragmentView.findViewById(R.id.list_explore);
        myList.setAdapter(new ExploreAdapter(myContext, filelist));
        myList.setOnItemClickListener(this);

        return myFragmentView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DetailExploreFragment detailExploreFragment = new DetailExploreFragment();
        ((ContainerActivity) getActivity()).changeFragment(detailExploreFragment, new ColorDrawable(getResources().getColor(R.color.ColorPrimary)));
    }
}
