package com.ngapainya.ngapainya.fragment.owner.child;


import android.app.Activity;
import android.app.ProgressDialog;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.ngapainya.ngapainya.R;
import com.ngapainya.ngapainya.activity.volunteer.ContainerActivity;
import com.ngapainya.ngapainya.fragment.volunteer.ExploreFragment;
import com.ngapainya.ngapainya.helper.Config;
import com.ngapainya.ngapainya.helper.JSONParser;
import com.ngapainya.ngapainya.helper.SessionManager;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostProgramFragment extends Fragment implements AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener{
    private FragmentActivity myContext;
    private View myFragmentView;
    private Calendar calendar;
    private DateFormat dateFormat;

    /*Variable input*/
    @Bind(R.id.title) EditText title;
    @Bind(R.id.address) EditText address;
    @Bind(R.id.fee) EditText fee;
    @Bind(R.id.ageMin) EditText age_min;
    @Bind(R.id.ageMax) EditText age_max;
    @Bind(R.id.quota) EditText quota;
    @Bind(R.id.description) EditText desc;
    @Bind(R.id.pjtStart) EditText pjtStart;
    @Bind(R.id.pjtEnd) EditText pjtEnd;

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Auto-generated method stub
        super.onCreateOptionsMenu(menu, inflater);
        myContext.getMenuInflater().inflate(R.menu.menu_post_program, menu);
        final MenuItem item = menu.findItem(R.id.action_done);
        item.getActionView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new postProgram().execute();
                Log.e("Done", "works");
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        calendar = Calendar.getInstance();
        dateFormat = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragmentView = inflater.inflate(R.layout.fragment_post_program, container, false);
        ButterKnife.bind(this, myFragmentView);

        setHasOptionsMenu(true);

        //language = (Spinner) myFragmentView.findViewById(R.id.language);
        ArrayAdapter adapterSpinner = ArrayAdapter.createFromResource(myContext,
                R.array.language_array, android.R.layout.simple_spinner_item);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //language.setAdapter(adapterSpinner);
        //language.setOnItemSelectedListener(this);

        pjtStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDateClicked();
            }
        });

        pjtEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endDateClicked();
            }
        });


        return myFragmentView;
    }

    public void startDateClicked(){
        DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show(myContext.getSupportFragmentManager(), "pjtStart");
    }

    public void endDateClicked(){
        DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show(myContext.getSupportFragmentManager(), "pjtEnd");
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        calendar.set(year, month, day);
        //Toast.makeText(myContext, "date set "+ day, Toast.LENGTH_LONG).show();
        update();
    }

    private void update() {
        if (myContext.getSupportFragmentManager().findFragmentByTag("pjtStart") != null) {
            pjtStart.setText(dateFormat.format(calendar.getTime()));
            calendar.add(Calendar.DATE, 1);
            pjtEnd.setText(dateFormat.format(calendar.getTime()));
            //Toast.makeText(myContext, "date set ", Toast.LENGTH_LONG).show();
        }
        if (myContext.getSupportFragmentManager().findFragmentByTag("pjtEnd") != null) {
            pjtEnd.setText(dateFormat.format(calendar.getTime()));
            calendar.add(Calendar.DATE, 1);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public class postProgram extends AsyncTask<String, String, String> {
        ProgressDialog pDialog;
        SessionManager session;
        HashMap<String, String> user;
        Config cfg = new Config();
        String token;
        /*variable input*/
        String input_title;
        String input_address;
        String input_fee;
        String input_str_date;
        String input_end_date;
        String input_age_max;
        String input_age_min;
        String input_quota;
        String input_desc;

        public String formatDate(String s){
            Date myDate = null;
            try {
                myDate = dateFormat.parse(s);

            } catch (ParseException e) {
                e.printStackTrace();
            }

            SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy/MM/dd");
            return timeFormat.format(myDate);
        }

        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.show();

            session = new SessionManager(myContext);
            user = session.getUserDetails();
            token = user.get(SessionManager.KEY_TOKEN);

            input_title = title.getText().toString();
            input_address = address.getText().toString();
            input_fee = fee.getText().toString();
            input_age_max = age_max.getText().toString();
            input_age_min = age_min.getText().toString();
            input_quota = quota.getText().toString();
            input_desc = desc.getText().toString();
            input_str_date = formatDate(pjtStart.getText().toString());
            input_end_date = formatDate(pjtEnd.getText().toString());
        }

        @Override
        protected String doInBackground(String... arg0) {
            String url = cfg.HOSTNAME +"/program/add";
            List<NameValuePair> nvp = new ArrayList<NameValuePair>();
            nvp.add(new BasicNameValuePair("access_token", token));
            nvp.add(new BasicNameValuePair("title", input_title));
            nvp.add(new BasicNameValuePair("address", input_address));
            nvp.add(new BasicNameValuePair("fee", input_fee));
            nvp.add(new BasicNameValuePair("date_start", input_str_date));
            nvp.add(new BasicNameValuePair("date_end", input_end_date));
            nvp.add(new BasicNameValuePair("age_max", input_age_max));
            nvp.add(new BasicNameValuePair("age_min", input_age_min));
            nvp.add(new BasicNameValuePair("quota", input_quota));
            nvp.add(new BasicNameValuePair("description", input_desc));

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
            ExploreFragment exploreFragment = new ExploreFragment();
            if (myContext.getClass().getName().equals("com.ngapainya.ngapainya.activity.volunteer.ContainerActivity")) {
                ((ContainerActivity) getActivity()).changeFragment(exploreFragment);
            }else{
                ((com.ngapainya.ngapainya.activity.owner.ContainerActivity) getActivity()).changeFragment(exploreFragment);
            }
        }
    }

}
