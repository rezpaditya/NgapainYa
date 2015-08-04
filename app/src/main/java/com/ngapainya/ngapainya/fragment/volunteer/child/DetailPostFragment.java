package com.ngapainya.ngapainya.fragment.volunteer.child;


import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ngapainya.ngapainya.R;
import com.ngapainya.ngapainya.helper.Config;
import com.ngapainya.ngapainya.helper.JSONParser;
import com.ngapainya.ngapainya.helper.SessionManager;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailPostFragment extends Fragment {
    private FragmentActivity myContext;
    private View myFragmentView;
    private int postType;
    private String act_id;

    /*
    * general variable view
    * */
    private ImageView avatar;
    private TextView content_status;
    private TextView time_post;
    private EditText comment;
    private Button post_comment_btn;

    /*variable post Photo view*/
    private ImageView photo;

    /*
    * Variable to get from server
    * */
    boolean isSuccess;
    private String avatar_url;
    private String content;
    private String time;
    private String photo_url;

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    public void initializeVariable() {
        /*Initialize variable view*/
        avatar = (ImageView) myFragmentView.findViewById(R.id.avatar);
        content_status = (TextView) myFragmentView.findViewById(R.id.content);
        time_post = (TextView) myFragmentView.findViewById(R.id.time);

        comment = (EditText) myFragmentView.findViewById(R.id.content_comment);
        post_comment_btn = (Button) myFragmentView.findViewById(R.id.post_comment_btn);

        post_comment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new postComment().execute();
            }
        });
    }

    public void initializePostStatus() {
        Picasso.with(myContext)
                .load("http://ainufaisal.com/" + avatar_url)
                .placeholder(R.drawable.propic_default)
                .into(avatar);
        content_status.setText(content);
        time_post.setText(time);
    }

    public void initializePostPhoto() {
        Picasso.with(myContext)
                .load("http://ainufaisal.com/" + avatar_url)
                .placeholder(R.drawable.propic_default)
                .into(avatar);
        content_status.setText(content);
        time_post.setText(time);
        Picasso.with(myContext)
                .load("http://ainufaisal.com/" + photo_url)
                .placeholder(R.drawable.propic_default)
                .into(photo);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((com.ngapainya.ngapainya.activity.volunteer.ContainerActivity)
                getActivity()).getSupportActionBar()
                .setBackgroundDrawable
                        (new ColorDrawable(getResources().getColor(R.color.ColorPrimary)));

        isSuccess = false;
        postType = getArguments().getInt("postType"); //get the post type
        act_id = getArguments().getString("act_id");
        switch (postType) {
            case 0:
                myFragmentView = inflater.inflate(R.layout.fragment_detail_post_status, container, false);
                initializeVariable(); //must be called
                new getDetailPost().execute();
                break;
            case 1:
                myFragmentView = inflater.inflate(R.layout.fragment_detail_image_post, container, false);
                initializeVariable(); //must be called
                    photo = (ImageView) myFragmentView.findViewById(R.id.photo);
                new getDetailPost().execute();
                break;
            case 2:
                Toast.makeText(myContext, "here is post type " + postType, Toast.LENGTH_SHORT).show();
                //do something here
                break;
            case 3:
                Toast.makeText(myContext, "here is post type " + postType, Toast.LENGTH_SHORT).show();
                //do something here
                break;
            case 4:
                Toast.makeText(myContext, "here is post type " + postType, Toast.LENGTH_SHORT).show();
                //do something here
                break;
        }

        return myFragmentView;
    }

    public class getDetailPost extends AsyncTask<String, String, String> {
        ProgressDialog pDialog;
        SessionManager session;
        HashMap<String, String> user;
        String token;
        Config cfg = new Config();

        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.show();

            session = new SessionManager(myContext);
            user = session.getUserDetails();
            token = user.get(SessionManager.KEY_TOKEN);
        }

        @Override
        protected String doInBackground(String... arg0) {
            String url = cfg.HOSTNAME + "/activity/" + act_id;
            List<NameValuePair> nvp = new ArrayList<NameValuePair>();
            nvp.add(new BasicNameValuePair("access_token", token));

            Log.e("url", url);
            Log.e("token", token);

            JSONParser jParser = new JSONParser();
            JSONObject json = jParser.makeHttpRequestToObject(url, "GET", nvp);      //get data from server
            if (json != null) {
                isSuccess = true;
                try {
                    avatar_url = json.getString("user_pic");
                    content = json.getString("act_content");
                    time = json.getString("created_at");
                    switch (postType){
                        case 0:
                            Log.e("ok", " ambil data");
                            break;
                        case 1:
                            photo_url = json.getString("act_url");
                            Log.e("ok", " ambil data");
                            break;
                    }
                } catch (Exception e) {
                    Log.e("error", "tidak bisa ambil data 1");
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            switch (postType){
                case 0:
                    initializePostStatus();
                    break;
                case 1:
                    initializePostPhoto();
                    break;
            }
        }
    }

    public class postComment extends AsyncTask<String, String, String> {
        ProgressDialog pDialog;
        SessionManager session;
        HashMap<String, String> user;
        String token;
        String input_comment;
        Config cfg = new Config();

        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.show();

            session = new SessionManager(myContext);
            user = session.getUserDetails();
            token = user.get(SessionManager.KEY_TOKEN);
            input_comment = comment.getText().toString();
        }

        @Override
        protected String doInBackground(String... arg0) {
            String url = cfg.HOSTNAME +"/comment/add/activity/"+act_id;
            List<NameValuePair> nvp = new ArrayList<NameValuePair>();
            nvp.add(new BasicNameValuePair("access_token", token));
            nvp.add(new BasicNameValuePair("comment", input_comment));

            JSONParser jParser = new JSONParser();
            JSONObject json = jParser.makeHttpRequestToObject(url, "GET", nvp);      //get data from server

            try {
                if(json != null){
                    Log.e("comment", "sent");
                }
            } catch (Exception e) {

                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            comment.setText("");
            new getDetailPost().execute();
            Toast.makeText(myContext, "comment sent",
                    Toast.LENGTH_SHORT).show();
        }
    }

}
