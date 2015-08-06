package com.ngapainya.ngapainya.fragment.owner;


import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ngapainya.ngapainya.R;
import com.ngapainya.ngapainya.activity.owner.ContainerActivity;
import com.ngapainya.ngapainya.adapter.ExploreAdapter;
import com.ngapainya.ngapainya.adapter.SpeedScrollListener;
import com.ngapainya.ngapainya.fragment.volunteer.child.DetailExploreFragment;
import com.ngapainya.ngapainya.helper.Config;
import com.ngapainya.ngapainya.helper.JSONParser;
import com.ngapainya.ngapainya.helper.SessionManager;
import com.ngapainya.ngapainya.model.Explore;

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
public class ShowProgram extends Fragment implements AdapterView.OnItemClickListener {
    private FragmentActivity myContext;
    private View myFragmentView;

    private ArrayList<Explore> filelist;
    private ListView myList;
    private ExploreAdapter adapter;
    private SpeedScrollListener speedScrollListener;

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        filelist = new ArrayList<Explore>();
        speedScrollListener = new SpeedScrollListener();
        adapter = new ExploreAdapter(myContext, filelist);

        if (savedInstanceState != null) {
            filelist = savedInstanceState.getParcelableArrayList("adapter_content");
            adapter = new ExploreAdapter(myContext, filelist);
            Log.e("savedInstanceState", "works");
        } else {
            new RemoteDataTask().execute();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragmentView = inflater.inflate(R.layout.fragment_show_program, container, false);

        myList = (ListView) myFragmentView.findViewById(R.id.list_program);
        myList.setAdapter(adapter);
        myList.setOnScrollListener(speedScrollListener);
        myList.setOnItemClickListener(this);

        return myFragmentView;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
        DetailExploreFragment detailExploreFragment = new DetailExploreFragment();
        Explore tmp = (Explore) parent.getItemAtPosition(position);
        Bundle args = new Bundle();
        args.putString("owner", "owner");
        args.putString("program_id", tmp.getProgram_id());
        detailExploreFragment.setArguments(args);
        ((ContainerActivity) getActivity()).changeFragment(detailExploreFragment);
    }

    private class RemoteDataTask extends AsyncTask<String, Void, ArrayList<Explore>> {
        SessionManager session;
        HashMap<String, String> user;
        String token;
        Config cfg = new Config();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            session = new SessionManager(myContext);
            user = session.getUserDetails();
            token = user.get(SessionManager.KEY_TOKEN);
        }

        private void addList(JSONObject result) {
            try {
                Explore temp_home = new Explore();
                temp_home.setProgram_id(result.getString("program_id"));
                temp_home.setProgram_name(result.getString("program_name"));
                temp_home.setUser_id(result.getString("user_id"));
                temp_home.setProgram_desc(result.getString("program_desc"));
                temp_home.setProgram_date_start(result.getString("program_date_start"));
                temp_home.setProgram_date_end(result.getString("program_date_end"));
                temp_home.setUser_pic(result.getString("user_pic"));

                filelist.add(temp_home);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected ArrayList<Explore> doInBackground(String... urls) {
            String url = cfg.HOSTNAME + "/program/all";
            List<NameValuePair> nvp = new ArrayList<NameValuePair>();
            nvp.add(new BasicNameValuePair("access_token", token));

            JSONParser jParser = new JSONParser();
            JSONArray json = jParser.makeHttpRequest(url, "GET", nvp);      //get data from server
            try {
                /*if data less than threshold, load them all
                * or if not, load data as much as threshold, repeat it in loadmore class
                * */
                for (int i = 0; i < json.length(); i++) {
                    JSONObject result = json.getJSONObject(i);
                    addList(result);
                    Log.e("ok", " ambil data");
                }

            } catch (Exception e) {
                Log.e("error", "tidak bisa ambil data 1");
                e.printStackTrace();
            }
            return filelist;
            /*try {
                ArrayList<Explore> new_filelist = new ArrayList<Explore>();
                //dummy data
                for (int i = 0; i < 10; i++) {
                    filelist.add(new Explore("img " + i, "title " + i, "text " + i, "strDate " + i, "endDate " + i, "image"));
                }
                filelist.addAll(new_filelist);
            } catch (ParseException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return filelist;*/
        }

        @Override
        protected void onPostExecute(ArrayList<Explore> organization) {
            // Create an OnScrollListener
            adapter.notifyDataSetChanged();
        }
    }
}
