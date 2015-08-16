package com.ngapainya.ngapainya.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ngapainya.ngapainya.R;
import com.ngapainya.ngapainya.model.Notification;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Ari Anggraeni on 7/2/2015.
 */
public class NotifAdapter extends BaseAdapter {
    Context context;
    ArrayList<Notification> list;
    String image_url;

    public NotifAdapter(Context context, ArrayList<Notification> items){
        this.context = context;
        list = items;
    }

    private class ViewHolder{
        TextView tv_1;
        ImageView notif_img;

        ViewHolder(View v){
            tv_1 = (TextView) v.findViewById(R.id.text_notif);
            notif_img = (ImageView) v.findViewById(R.id.notif_img);
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
            row = inflater.inflate(R.layout.item_notification, parent, false);
            holder = new ViewHolder(row);
            row.setTag(holder);
        }else{
            holder = (ViewHolder) row.getTag();
        }
        Notification temp = list.get(position);

        holder.tv_1.setText(temp.getText_notif());
        holder.tv_1.setTag(temp);

        Picasso.with(context)
                .load(list.get(position).getPropic())
                .placeholder(R.drawable.propic_default)
                .into(holder.notif_img);
        holder.notif_img.setTag(temp);

        return row;
    }
}
