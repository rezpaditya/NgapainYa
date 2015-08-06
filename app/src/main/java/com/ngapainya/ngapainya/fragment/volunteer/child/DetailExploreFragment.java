package com.ngapainya.ngapainya.fragment.volunteer.child;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ngapainya.ngapainya.R;
import com.ngapainya.ngapainya.activity.owner.ContainerActivity;
import com.ngapainya.ngapainya.fragment.owner.ListApplicantFragment;
import com.ngapainya.ngapainya.helper.Config;
import com.ngapainya.ngapainya.helper.JSONParser;
import com.ngapainya.ngapainya.helper.SessionManager;
import com.ngapainya.ngapainya.model.Explore;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailExploreFragment extends Fragment {
    private FragmentActivity myContext;
    private View myFragmentView;
    private String program_id;

    /*
    * Variabel for retrieve data from server
    * */
    private Explore explore;

    /*
    * Variable view
    * */
    private ImageView avatar;
    private ImageView program_image;
    private TextView user_fullname;
    private TextView program_name;
    private TextView start_date;
    private TextView end_date;
    private TextView min_age;
    private TextView max_age;
    private TextView volume;
    private TextView program_desc;
    private TextView program_accomodation;
    private TextView program_fee;

    private String startDate;
    private String endDate;

    private Menu menu;

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    public void splitDate(Explore tmp) {
        String pu_tmp = tmp.getProgram_date_start();
        String date1[] = pu_tmp.split("-");
        String date2[] = date1[2].split(" ");
        //str_year  = date1[0];
        String str_month, str_day;
        str_month = new DateFormatSymbols().getMonths()[Integer.parseInt(date1[1]) - 1];
        str_day = date2[0];
        startDate = str_day + " " + str_month;

        String do_tmp = tmp.getProgram_date_end();
        String date3[] = do_tmp.split("-");
        String date4[] = date3[2].split(" ");
        //en_year = date3[0];
        String en_month, en_day;
        en_month = new DateFormatSymbols().getMonths()[Integer.parseInt(date3[1]) - 1];
        en_day = date4[0];
        endDate = en_day + " " + en_month;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragmentView = inflater.inflate(R.layout.fragment_detail_explore, container, false);
        setHasOptionsMenu(true);

        explore = new Explore();

        /*Initialize variable*/
        avatar = (ImageView) myFragmentView.findViewById(R.id.avatar);
        program_image = (ImageView) myFragmentView.findViewById(R.id.prg_img);
        user_fullname = (TextView) myFragmentView.findViewById(R.id.org_name);
        program_name = (TextView) myFragmentView.findViewById(R.id.prg_name);
        start_date = (TextView) myFragmentView.findViewById(R.id.strDate);
        end_date = (TextView) myFragmentView.findViewById(R.id.endDate);
        min_age = (TextView) myFragmentView.findViewById(R.id.minAge);
        max_age = (TextView) myFragmentView.findViewById(R.id.maxAge);
        volume = (TextView) myFragmentView.findViewById(R.id.vlm);
        program_desc = (TextView) myFragmentView.findViewById(R.id.desc);
        program_accomodation = (TextView) myFragmentView.findViewById(R.id.accmd);
        program_fee = (TextView) myFragmentView.findViewById(R.id.prg_fee);

        if (getArguments() != null) {
            program_id = getArguments().getString("program_id");
            new getDetailExplore().execute();
        }
        return myFragmentView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Auto-generated method stub
        super.onCreateOptionsMenu(menu, inflater);
        this.menu = menu;

        String param = getArguments().getString("owner");
        if (param != null) {
            myContext.getMenuInflater().inflate(R.menu.menu_detail_program, menu);
        } else {
            myContext.getMenuInflater().inflate(R.menu.menu_detail_explore, menu);
            final MenuItem item = menu.findItem(R.id.action_done);
            item.getActionView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doApply();
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                Log.e("delete", "works");
                break;
            case R.id.action_view_applicant:
                ListApplicantFragment listApplicantFragment = new ListApplicantFragment();
                Bundle args = new Bundle();
                args.putString("program_id", program_id);
                listApplicantFragment.setArguments(args);
                ((ContainerActivity) getActivity()).changeFragment(listApplicantFragment);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void hideMenu() {
        MenuItem item = menu.findItem(R.id.action_done);
        item.setVisible(false);
    }

    public void doApply() {
        final AlertDialog.Builder dlgAlert = new AlertDialog.Builder(myContext);
        dlgAlert.setMessage("Are you sure want to apply this program?");
        //dlgAlert.setTitle("Developer Nyewamobil");
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);
        LayoutInflater inflater = myContext.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_title_bar, null);
        dlgAlert.setCustomTitle(view);
        dlgAlert.setPositiveButton("Confirm",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        new applyProgram().execute();
                    }
                });
        dlgAlert.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        dlgAlert.create().show();
    }

    public class getDetailExplore extends AsyncTask<String, String, String> {
        ProgressDialog pDialog;
        SessionManager session;
        HashMap<String, String> user;
        String token;
        boolean isSuccess;
        Config cfg = new Config();

        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.show();

            isSuccess = false;
            session = new SessionManager(myContext);
            user = session.getUserDetails();
            token = user.get(SessionManager.KEY_TOKEN);
        }

        private void addList(JSONObject result) {
            try {
                explore.setProgram_id(program_id);
                explore.setProgram_name(result.getString("program_name"));
                explore.setUser_id(result.getString("user_id"));
                explore.setProgram_desc(result.getString("program_desc"));
                explore.setProgram_date_start(result.getString("program_date_start"));
                explore.setProgram_date_end(result.getString("program_date_end"));
                explore.setUser_pic(result.getString("user_pic"));
                explore.setUser_fullname(result.getString("user_fullname"));
                explore.setProgram_age_min(result.getString("program_age_min"));
                explore.setProgram_age_max(result.getString("program_age_max"));
                explore.setProgram_fee(result.getString("program_fee"));
                explore.setProgram_url(result.getString("program_url"));
                explore.setProgram_quota(result.getString("program_quota"));

                Log.e("name", explore.getProgram_name());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... arg0) {
            String url = cfg.HOSTNAME + "/program/" + program_id;
            List<NameValuePair> nvp = new ArrayList<NameValuePair>();
            nvp.add(new BasicNameValuePair("access_token", token));

            Log.e("url", url);
            Log.e("token", token);

            JSONParser jParser = new JSONParser();
            JSONObject json = jParser.makeHttpRequestToObject(url, "GET", nvp);      //get data from server
            if (json != null) {
                isSuccess = true;
                try {
                    addList(json);
                    Log.e("get", "get data");
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

            /*Split the date*/
            splitDate(explore);

            Picasso.with(myContext)
                    .load("http://ainufaisal.com/" + explore.getUser_pic())
                    .placeholder(R.drawable.propic_default)
                    .into(avatar);
            Picasso.with(myContext)
                    .load("http://ainufaisal.com/" + explore.getProgram_url())
                    .placeholder(R.drawable.bg_front)
                    .into(program_image);
            user_fullname.setText(explore.getUser_fullname());
            program_name.setText(explore.getProgram_name());
            start_date.setText(startDate);                      //Splited start date
            end_date.setText(endDate);                          //Splited end date
            min_age.setText(explore.getProgram_age_min());
            max_age.setText(explore.getProgram_age_max());
            volume.setText(explore.getProgram_quota());
            program_desc.setText(explore.getProgram_desc());
            program_fee.setText(explore.getProgram_fee());

            Log.e("get", explore.getUser_pic());
        }
    }

    public class applyProgram extends AsyncTask<String, String, String> {
        ProgressDialog pDialog;
        SessionManager session;
        HashMap<String, String> user;
        String token;
        String input_program_id;
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
            input_program_id = program_id;
        }

        @Override
        protected String doInBackground(String... arg0) {
            String url = cfg.HOSTNAME + "/program/" + input_program_id + "/apply";
            List<NameValuePair> nvp = new ArrayList<NameValuePair>();
            nvp.add(new BasicNameValuePair("access_token", token));

            JSONParser jParser = new JSONParser();
            JSONObject json = jParser.makeHttpRequestToObject(url, "GET", nvp);      //get data from server
            //Log.e("error", "jalan");
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
            hideMenu();
            new getDetailExplore().execute();
        }
    }
}
