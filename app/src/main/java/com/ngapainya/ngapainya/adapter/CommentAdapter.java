package com.ngapainya.ngapainya.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ngapainya.ngapainya.R;
import com.ngapainya.ngapainya.model.Comment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Ari Anggraeni on 7/7/2015.
 */
public class CommentAdapter extends BaseAdapter {
    Context context;
    ArrayList<Comment> list;
    String image_url;
    String str_month, str_day, en_month, en_day;

    public CommentAdapter(Context context, ArrayList<Comment> items){
        this.context = context;
        list = items;
    }

    static class ViewHolder{
        @Bind(R.id.comment) TextView comment;
        @Bind(R.id.time) TextView time;
        @Bind(R.id.propic) ImageView propic;
        @Bind(R.id.username) TextView username;

        ViewHolder(View v){
            ButterKnife.bind(this, v);
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
            row = inflater.inflate(R.layout.item_comment, parent, false);
            holder = new ViewHolder(row);
            row.setTag(holder);
        }else{
            holder = (ViewHolder) row.getTag();
        }

        Comment temp = list.get(position);

        holder.comment.setText(temp.getUser_comment());
        holder.comment.setTag(temp);

        Picasso.with(context)
                .load("http://ainufaisal.com/"+temp.getUser_ava())
                .placeholder(R.drawable.propic_default)
                .into(holder.propic);
        holder.propic.setTag(temp);

        holder.time.setText(temp.getTime());
        holder.time.setTag(temp);

        holder.username.setText(temp.getUser_name());
        holder.username.setTag(temp);

        return row;
    }
}
