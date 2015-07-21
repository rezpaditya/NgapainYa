package com.ngapainya.ngapainya.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ngapainya.ngapainya.R;
import com.ngapainya.ngapainya.model.Home;

import java.util.ArrayList;

/**
 * Created by Ari Anggraeni on 7/8/2015.
 */
public class HomeAdapter extends BaseAdapter {
    Context context;
    ArrayList<Home> list;

    public HomeAdapter(Context context, ArrayList<Home> items) {
        this.context = context;
        list = items;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        ViewHolder holder = null;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        switch (list.get(position).type) {
            case 0:
                row = inflater.inflate(R.layout.item_home_sts, parent, false);
                holder = new ViewHolder(row);
                row.setTag(holder);
                break;
            case 1:
                row = inflater.inflate(R.layout.item_home_img, parent, false);
                holder = new ViewHolder(row);
                row.setTag(holder);
                break;
            case 2:
                row = inflater.inflate(R.layout.item_home_location, parent, false);
                holder = new ViewHolder(row);
                row.setTag(holder);
                break;
            case 3:
                row = inflater.inflate(R.layout.item_home_url, parent, false);
                holder = new ViewHolder(row);
                row.setTag(holder);
                break;
            case 4:
                row = inflater.inflate(R.layout.item_home_bf, parent, false);
                holder = new ViewHolder(row);
                row.setTag(holder);
                break;
            default:
                row = inflater.inflate(R.layout.item_home_sts, parent, false);
                holder = new ViewHolder(row);
                row.setTag(holder);
        }
        return row;
    }
}
