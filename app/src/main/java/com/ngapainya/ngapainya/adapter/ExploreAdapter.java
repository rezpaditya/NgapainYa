package com.ngapainya.ngapainya.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ngapainya.ngapainya.R;
import com.ngapainya.ngapainya.model.Explore;
import com.squareup.picasso.Picasso;

import java.text.DateFormatSymbols;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Ari Anggraeni on 7/7/2015.
 */
public class ExploreAdapter extends BaseAdapter {
    Context context;
    ArrayList<Explore> list;
    String image_url;
    boolean splitted = false;
    String str_month, str_day, en_month, en_day;

    public ExploreAdapter(Context context, ArrayList<Explore> items){
        this.context = context;
        list = items;
    }

    public void splitDate(Explore tmp){
        String pu_tmp = tmp.getProgram_date_start();
        String date1 [] = pu_tmp.split("-");
        String date2 [] = date1[2].split(" ");
        //str_year  = date1[0];
        str_month  = new DateFormatSymbols().getMonths()[Integer.parseInt(date1[1])-1];
        str_day  = date2[0];

        String do_tmp = tmp.getProgram_date_end();
        String date3 [] = do_tmp.split("-");
        String date4 [] = date3[2].split(" ");
        //en_year = date3[0];
        en_month = new DateFormatSymbols().getMonths()[Integer.parseInt(date3[1])-1];
        en_day = date4[0];
        splitted = true;
    }

    static class ViewHolder{
        @Bind(R.id.program_title) TextView program_title;
        @Bind(R.id.program_dec) TextView program_dec;
        @Bind(R.id.start_date) TextView start_date;
        @Bind(R.id.end_date)  TextView end_date;
        @Bind(R.id.propic) ImageView propic;

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
            row = inflater.inflate(R.layout.item_explore, parent, false);
            holder = new ViewHolder(row);
            row.setTag(holder);
        }else{
            holder = (ViewHolder) row.getTag();
        }

        Explore temp = list.get(position);
        if(temp.getProgram_date_start() != null
                && temp.getProgram_date_end() != null) {
            splitDate(temp);
        }

        holder.program_title.setText(temp.getProgram_name());
        holder.program_title.setTag(temp);

        holder.program_dec.setText(temp.getProgram_desc());
        holder.program_dec.setTag(temp);

        if(splitted) {
            holder.start_date.setText(str_day + " " + str_month);
            holder.start_date.setTag(temp);

            holder.end_date.setText(en_day + " " + en_month);
            holder.end_date.setTag(temp);
        }

        Picasso.with(context)
                .load(list.get(position).getUser_pic())
                .placeholder(R.drawable.propic_default)
                .into(holder.propic);
        holder.propic.setTag(temp);

        return row;
    }
}
