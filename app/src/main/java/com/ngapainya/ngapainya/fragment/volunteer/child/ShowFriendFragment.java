package com.ngapainya.ngapainya.fragment.volunteer.child;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.ngapainya.ngapainya.R;
import com.ngapainya.ngapainya.activity.volunteer.ContainerActivity;
import com.ngapainya.ngapainya.model.MyFriend;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShowFriendFragment extends Fragment implements AdapterView.OnItemClickListener {
    private FragmentActivity myContext;
    private View myFragmentView;
    private GridView myGrid;
    private ArrayList<MyFriend> myFriendArrayList;
    private showFriendAdapter showFriendAdapter;

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragmentView = inflater.inflate(R.layout.fragment_show_friend, container, false);

        myFriendArrayList = new ArrayList<>();
        showFriendAdapter = new showFriendAdapter(myContext, myFriendArrayList);

        myFriendArrayList.add(new MyFriend(0, "img00.jpg"));
        myFriendArrayList.add(new MyFriend(1, "img01.jpg"));
        myFriendArrayList.add(new MyFriend(2, "img02.jpg"));
        myFriendArrayList.add(new MyFriend(3, "img03.jpg"));

        myGrid = (GridView) myFragmentView.findViewById(R.id.grid_friend);
        myGrid.setOnItemClickListener(this);
        myGrid.setAdapter(showFriendAdapter);
        return myFragmentView;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        MyFriend myFriend = (MyFriend) adapterView.getItemAtPosition(i);
        Toast.makeText(myContext, "hi! here I am number " + myFriend.id, Toast.LENGTH_LONG).show();
        //call container method to change the fragment
        VolunteerProfileFragment volunteerProfileFragment = new VolunteerProfileFragment();
        ((ContainerActivity) getActivity()).changeFragment(volunteerProfileFragment);
    }

    private class showFriendAdapter extends BaseAdapter {
        Context context;
        public ArrayList<MyFriend> filelist;

        showFriendAdapter(Context context, ArrayList<MyFriend> filelist) {
            this.context = context;
            this.filelist = filelist;
        }

        private class ViewHolder {
            ImageView propic;

            ViewHolder(View v) {
                propic = (ImageView) v.findViewById(R.id.propic);
            }
        }

        @Override
        public int getCount() {
            return filelist.size();
        }

        @Override
        public Object getItem(int i) {
            return filelist.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View row = view;
            ViewHolder holder = null;
            if (row == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.item_my_friend, viewGroup, false);
                holder = new ViewHolder(row);
                row.setTag(holder);
            } else {
                holder = (ViewHolder) row.getTag();
            }

            MyFriend temp = filelist.get(i);
            Picasso.with(myContext)
                    .load("http://s10.postimg.org/m9i1d59sp/justin_profile_face.jpg")
                    .into(holder.propic);
            holder.propic.setTag(temp);

            return row;
        }
    }

}
