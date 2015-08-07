package com.ngapainya.ngapainya.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ngapainya.ngapainya.R;
import com.ngapainya.ngapainya.helper.Config;
import com.ngapainya.ngapainya.helper.JSONParser;
import com.ngapainya.ngapainya.helper.SessionManager;
import com.ngapainya.ngapainya.model.Friend;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Ari Anggraeni on 7/2/2015.
 */
public class FriendAdapter extends BaseAdapter {
    Context context;
    ArrayList<Friend> list;
    String image_url;

    public FriendAdapter(Context context, ArrayList<Friend> items){
        this.context = context;
        list = items;
    }

    private class ViewHolder{
        TextView name;
        ImageView avatar;
        Button invite;

        ViewHolder(View v){
            name = (TextView) v.findViewById(R.id.name);
            avatar= (ImageView) v.findViewById(R.id.avatar);
            invite = (Button) v.findViewById(R.id.inviteBtn);
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        View row = convertView;
        ViewHolder holder = null;
        if(row==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.item_friends, parent, false);
            holder = new ViewHolder(row);
            row.setTag(holder);
        }else{
            holder = (ViewHolder) row.getTag();
        }

        Friend temp = list.get(position);
        final ViewHolder v = holder;

        holder.name.setText(temp.getFriend_name());
        holder.name.setTag(temp);

        Picasso.with(context)
                .load("http://ainufaisal.com/" + temp.getFriend_ava())
                .placeholder(R.drawable.propic_default)
                .into(holder.avatar);
        holder.invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new doInvite().execute();
                v.invite.setBackgroundResource(R.drawable.my_button_grey);
                v.invite.setText("Invited");
                v.invite.setTextColor(Color.rgb(6, 6, 6));
            }
        });


        return row;
    }

    public class doInvite extends AsyncTask<String, String, String> {
        ProgressDialog pDialog;
        SessionManager session;
        HashMap<String, String> user;
        String token;
        Config cfg = new Config();

        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.show();

            session = new SessionManager(context);
            user = session.getUserDetails();
            token = user.get(SessionManager.KEY_TOKEN);
        }

        @Override
        protected String doInBackground(String... arg0) {
            String url = cfg.HOSTNAME +"/activity/add/text";
            List<NameValuePair> nvp = new ArrayList<NameValuePair>();
            nvp.add(new BasicNameValuePair("access_token", token));

            JSONParser jParser = new JSONParser();
            //JSONArray json = jParser.makeHttpRequest(url, "GET", nvp);      //get data from server
            Log.e("Asynctask", "works");
            try {
                Log.e("error", "tidak bisa ambil data 0");
            } catch (Exception e) {
                Log.e("error", "tidak bisa ambil data 1");
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pDialog.dismiss();

        }
    }
}
