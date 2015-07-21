package com.ngapainya.ngapainya.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ngapainya.ngapainya.R;
import com.ngapainya.ngapainya.model.Explore;

import java.util.ArrayList;

/**
 * Created by Ari Anggraeni on 7/7/2015.
 */
public class ExploreAdapter extends BaseAdapter {
    Context context;
    ArrayList<Explore> list;
    String image_url;

    public ExploreAdapter(Context context, ArrayList<Explore> items){
        this.context = context;
        list = items;
    }

    private class ViewHolder{
        TextView tv_1;
        ViewHolder(View v){
            tv_1 = (TextView) v.findViewById(R.id.exp_title);
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
        if(row==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.item_explore, parent, false);
            holder = new ViewHolder(row);
            row.setTag(holder);
        }else{
            holder = (ViewHolder) row.getTag();
        }

        Explore temp = list.get(position);
        holder.tv_1.setText(temp.title);
        holder.tv_1.setTag(temp);

        return row;
    }
}
