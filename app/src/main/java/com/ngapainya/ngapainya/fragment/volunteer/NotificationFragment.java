package com.ngapainya.ngapainya.fragment.volunteer;


import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ngapainya.ngapainya.R;
import com.ngapainya.ngapainya.activity.volunteer.ContainerActivity;
import com.ngapainya.ngapainya.adapter.NotifAdapter;
import com.ngapainya.ngapainya.helper.Config;
import com.ngapainya.ngapainya.helper.JSONParser;
import com.ngapainya.ngapainya.helper.SessionManager;
import com.ngapainya.ngapainya.model.Notification;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment {
    private FragmentActivity myContext;
    private View myFragmentView;
    private ArrayList<Notification> filelist;
    private ListView myList;
    private NotifAdapter adapter;

    int start = 0;
    int end = 10;

    private final String PACKAGE_NAME = "com.ngapainya.ngapainya.activity.";

    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        filelist = new ArrayList<Notification>();
        adapter = new NotifAdapter(myContext, filelist);

        if (savedInstanceState != null) {
            filelist = savedInstanceState.getParcelableArrayList("adapter_content");
            adapter = new NotifAdapter(myContext, filelist);
            Log.e("savedInstanceState", "works");
        } else {
            new getNotif().execute();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ArrayList<Notification> temp;
        temp = filelist;
        outState.putParcelableArrayList("adapter_content", temp);
        Log.e("onSaveInstanceState", "works");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragmentView = inflater.inflate(R.layout.fragment_notification, container, false);

        if(myContext.getClass().getName().equals(PACKAGE_NAME+"volunteer.ContainerActivity")) {
        /*Customize actionbar*/
            ((ContainerActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            ((ContainerActivity) getActivity()).homeTitleBar("Notification");
            ((com.ngapainya.ngapainya.activity.volunteer.ContainerActivity) getActivity()).changeActionbarStyle(false);
        }else{
            ((com.ngapainya.ngapainya.activity.owner.ContainerActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            ((com.ngapainya.ngapainya.activity.owner.ContainerActivity) getActivity()).homeTitleBar("Notification");
            ((com.ngapainya.ngapainya.activity.owner.ContainerActivity) getActivity()).changeActionbarStyle(false);
        }

        myList = (ListView) myFragmentView.findViewById(R.id.list_notif);
        myList.setAdapter(adapter);

        return myFragmentView;
    }

    private class getNotif extends AsyncTask<Void, Void, Void>{
        SessionManager sessionManager;
        HashMap<String, String> user;
        String token;
        String url;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            sessionManager = new SessionManager(myContext);
            user = sessionManager.getUserDetails();
            token = user.get(SessionManager.KEY_TOKEN);
        }

        private void addList(JSONObject result) {
            try {
                Notification notif = new Notification();
                notif.setText_notif(result.getString("notif_desc"));
                notif.setPropic(result.getString("notif_pic"));
                filelist.add(notif);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String url = Config.HOSTNAME + "/notification/view";
            List<NameValuePair> nvp = new ArrayList<NameValuePair>();
            nvp.add(new BasicNameValuePair("access_token", token));

            JSONParser jParser = new JSONParser();
            JSONArray json = jParser.makeHttpRequest(url, "GET", nvp);      //get data from server
            if (json.length() > 0) {  //check the result
                try {
                    for (int i = 0; i < json.length(); i++) {
                        JSONObject result = json.getJSONObject(i);
                        addList(result);
                        Log.e("ok", " ambil data");
                    }
                } catch (Exception e) {
                    Log.e("error", "tidak bisa ambil data 1");
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter.notifyDataSetChanged();
        }
    }
}
