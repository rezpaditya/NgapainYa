package com.ngapainya.ngapainya.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ngapainya.ngapainya.R;
import com.ngapainya.ngapainya.model.Home;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Ari Anggraeni on 7/8/2015.
 */
public class HomeAdapter extends GNowListAdapter {
    Context context;
    ArrayList<Home> list;

    public HomeAdapter(Context context, SpeedScrollListener scrollListener, ArrayList<Home> items) {
        super(context, scrollListener, items);
        this.context = context;
        list = items;
    }

   /* public HomeAdapter(Context context, ArrayList<Home> items) {
        this.context = context;
        list = items;
    }*/

    @Override
    protected View getRowView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        ViewHolder holder = null;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        switch (list.get(position).getAct_type()) {
            case "Text":
                row = inflater.inflate(R.layout.item_home_sts, parent, false);

                //define the view
                ImageView propic_0 = (ImageView) row.findViewById(R.id.propic);
                TextView content_0 = (TextView) row.findViewById(R.id.content);
                TextView time_0 = (TextView) row.findViewById(R.id.time);

                //set the value to the view
                time_0.setText(list.get(position).getCreated_at());
                content_0.setText(list.get(position).getAct_content());
                Picasso.with(context)
                        .load(list.get(position).getUser_pic())
                        .placeholder(R.drawable.propic_default)
                        .into(propic_0);
                break;
            case "status":
                row = inflater.inflate(R.layout.item_home_sts, parent, false);

                //define the view
                ImageView propic_01 = (ImageView) row.findViewById(R.id.propic);
                TextView content_01 = (TextView) row.findViewById(R.id.content);
                TextView time_1 = (TextView) row.findViewById(R.id.time);

                //set the value to the view
                time_1.setText(list.get(position).getCreated_at());
                content_01.setText(list.get(position).getAct_content());
                Picasso.with(context)
                        .load(list.get(position).getUser_pic())
                        .placeholder(R.drawable.propic_default)
                        .into(propic_01);
                break;
            case "Photo":
                row = inflater.inflate(R.layout.item_home_img, parent, false);
                ImageView propic_02 = (ImageView) row.findViewById(R.id.propic);
                ImageView img = (ImageView) row.findViewById(R.id.content_img);
                TextView content_02 = (TextView) row.findViewById(R.id.content);
                TextView time_2 = (TextView) row.findViewById(R.id.time);

                //set the value to the view
                time_2.setText(list.get(position).getCreated_at());
                content_02.setText(list.get(position).getAct_content());
                Picasso.with(context)
                        .load(list.get(position).getUser_pic())
                        .placeholder(R.drawable.propic_default)
                        .into(propic_02);
                Picasso.with(context)
                        .load(list.get(position).getAct_url())
                                //.placeholder(R.drawable.bg_front)
                        .into(img);
                break;
            case "Location":
                row = inflater.inflate(R.layout.item_home_location, parent, false);
                ImageView propic_03 = (ImageView) row.findViewById(R.id.propic);
                TextView address = (TextView) row.findViewById(R.id.address);
                TextView time_3 = (TextView) row.findViewById(R.id.time);

                //set the value to the view
                time_3.setText(list.get(position).getCreated_at());
                address.setText(list.get(position).getUsername() + " is at " + list.get(position).getAct_address());
                Picasso.with(context)
                        .load(list.get(position).getUser_pic())
                        .placeholder(R.drawable.propic_default)
                        .into(propic_03);
                break;
            case "Url":
                row = inflater.inflate(R.layout.item_home_url, parent, false);
                ImageView propic_04 = (ImageView) row.findViewById(R.id.propic);
                TextView url_content = (TextView) row.findViewById(R.id.content);
                TextView time_4 = (TextView) row.findViewById(R.id.time);

                //set the value to the view
                time_4.setText(list.get(position).getCreated_at());
                url_content.setText(list.get(position).getAct_url());
                Picasso.with(context)
                        .load(list.get(position).getUser_pic())
                        .placeholder(R.drawable.propic_default)
                        .into(propic_04);
                break;
            case "become_friend":
                row = inflater.inflate(R.layout.item_home_bf, parent, false);
                holder = new ViewHolder(row);
                row.setTag(holder);
                break;
            default:
                row = inflater.inflate(R.layout.item_home_sts, parent, false);
                holder = new ViewHolder(row);
                row.setTag(holder);
        }
        /*if(position > lastPosition){
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.up_from_bottom);
            row.startAnimation(animation);
            lastPosition = position;
        }*/
        return row;
    }

    private class ViewHolder {
        TextView tv_1;

        ViewHolder(View v) {
            tv_1 = (TextView) v.findViewById(R.id.textView7);
        }
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
