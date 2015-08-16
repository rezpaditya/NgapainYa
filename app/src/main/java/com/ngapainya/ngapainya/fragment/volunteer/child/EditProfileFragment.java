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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.ngapainya.ngapainya.R;
import com.ngapainya.ngapainya.activity.volunteer.ContainerActivity;
import com.ngapainya.ngapainya.fragment.volunteer.MyProfileFragment;
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
public class EditProfileFragment extends Fragment implements DatePickerDialog.OnDateSetListener {
    private FragmentActivity myContext;
    private View myFragmentView;
    private Calendar calendar;
    private DateFormat dateFormat;

    /*
    * Variable of the view
    * */
    private EditText birthdate;
    @Bind(R.id.name) EditText name;
    @Bind(R.id.email) EditText email;
    @Bind(R.id.location) EditText location;
    @Bind(R.id.old_pwd) EditText oldPassword;
    @Bind(R.id.new_pwd) EditText newPassword;

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        calendar = Calendar.getInstance();
        calendar.set(1990, 01, 01);
        dateFormat = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragmentView = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        ((com.ngapainya.ngapainya.activity.volunteer.ContainerActivity)
                getActivity()).getSupportActionBar()
                .setBackgroundDrawable
                        (new ColorDrawable(getResources().getColor(R.color.ColorPrimary)));

        ((com.ngapainya.ngapainya.activity.volunteer.ContainerActivity)
                getActivity()).getSupportActionBar()
                .setDisplayHomeAsUpEnabled(true);

        setHasOptionsMenu(true);

        /*Initialize the variable*/
        ButterKnife.bind(this, myFragmentView);
        birthdate   = (EditText) myFragmentView.findViewById(R.id.birthdate);
        birthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("onclick", "works");
                showDatePickerDialog();
            }
        });

        return myFragmentView;
    }

    public void showDatePickerDialog(){
        DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH))
                .show(myContext.getSupportFragmentManager(), "birthdate");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Auto-generated method stub
        super.onCreateOptionsMenu(menu, inflater);
        myContext.getMenuInflater().inflate(R.menu.menu_post_text, menu);
        final MenuItem item = menu.findItem(R.id.action_done);
        item.getActionView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doPost(v);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                myContext.onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        calendar.set(year, month, day);
        update();
    }

    public void doPost(View view) {
        //Change the fragment to home fragment here
        if(name.getText().length() > 0
                && email.getText().length() > 0
                && birthdate.getText().length() >0){
            new updateProfile().execute();
        }

        Log.e("edit", "works");
    }

    private void update() {
        if (myContext.getSupportFragmentManager().findFragmentByTag("birthdate") != null) {
            birthdate.setText(dateFormat.format(calendar.getTime()));
        }
    }

    public class updateProfile extends AsyncTask<String, String, String> {
        ProgressDialog pDialog;
        SessionManager session;
        HashMap<String, String> user;
        String token;
        Config cfg = new Config();
        //input
        String input_name;
        String input_email;
        String input_birthdate;
        String input_location;
        String input_oldPassword;
        String input_newPassword;

        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

            session = new SessionManager(myContext);
            user = session.getUserDetails();
            token = user.get(SessionManager.KEY_TOKEN);
            //input
            input_location = location.getText().toString();
            input_oldPassword = oldPassword.getText().toString();
            input_newPassword = newPassword.getText().toString();
            input_name = name.getText().toString();
            input_email = email.getText().toString();
            Date myDate = null;
            try {
                myDate = dateFormat.parse(birthdate.getText().toString());

            } catch (ParseException e) {
                e.printStackTrace();
            }

            SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy/MM/dd");
            input_birthdate = timeFormat.format(myDate);
        }

        @Override
        protected String doInBackground(String... arg0) {
            String url = cfg.HOSTNAME +"/profile/update";
            List<NameValuePair> nvp = new ArrayList<NameValuePair>();
            nvp.add(new BasicNameValuePair("access_token", token));
            nvp.add(new BasicNameValuePair("fullname", input_name));
            nvp.add(new BasicNameValuePair("email", input_email));
            nvp.add(new BasicNameValuePair("birthdate", input_birthdate));
            nvp.add(new BasicNameValuePair("location", input_location));
            nvp.add(new BasicNameValuePair("password_old", input_oldPassword));
            nvp.add(new BasicNameValuePair("password_new", input_newPassword));

            JSONParser jParser = new JSONParser();
            JSONObject json = jParser.makeHttpRequestToObject(url, "GET", nvp);      //get data from server
            Log.e("fullname", input_birthdate);
            Log.e("email", input_email);
            Log.e("birthdate", input_birthdate);
            Log.e("location", input_birthdate);
            Log.e("password_old", input_birthdate);
            Log.e("password_new", input_birthdate);
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
            MyProfileFragment myProfileFragment = new MyProfileFragment();
            ((ContainerActivity) getActivity()).changeFragment(myProfileFragment);
            ((ContainerActivity) getActivity()).changeActionbarStyle(true);
        }
    }
}
