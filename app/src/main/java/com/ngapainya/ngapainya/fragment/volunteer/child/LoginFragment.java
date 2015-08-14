package com.ngapainya.ngapainya.fragment.volunteer.child;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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

import com.ngapainya.ngapainya.R;
import com.ngapainya.ngapainya.activity.volunteer.ContainerActivity;
import com.ngapainya.ngapainya.helper.Config;
import com.ngapainya.ngapainya.helper.JSONParser;
import com.ngapainya.ngapainya.helper.RegistrationIntentService;
import com.ngapainya.ngapainya.helper.SessionManager;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {
    private FragmentActivity myContext;
    private View myFragmentView;
    /*
    * Variable from the view
    * */
    private EditText email;
    private EditText password;
    private Button btn_login;

    /*Variable to store data after login*/
    private String username;
    private String user_fullname;
    private String current_status;
    private String user_email;
    private String token;

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragmentView = inflater.inflate(R.layout.fragment_login, container, false);
        /*
        * Initialize views variable
        * */

        new getTokenGCM().execute();

        email = (EditText) myFragmentView.findViewById(R.id.email);
        password = (EditText) myFragmentView.findViewById(R.id.password);
        btn_login = (Button) myFragmentView.findViewById(R.id.login_btn);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(email.getText().length()>0
                        && password.getText().length()>=8){
                    new doLogin().execute();
                }
            }
        });

        return myFragmentView;
    }

    public class doLogin extends AsyncTask<String, String, String> {
        ProgressDialog pDialog;
        String input_email;
        String input_password;
        boolean isSuccess;
        Config cfg = new Config();

        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

            isSuccess = false;

            input_email     = email.getText().toString();
            input_password  = password.getText().toString();
        }

        @Override
        protected String doInBackground(String... arg0) {
            String url = cfg.HOSTNAME +"/login";
            List<NameValuePair> nvp = new ArrayList<NameValuePair>();
            nvp.add(new BasicNameValuePair("email", input_email));
            nvp.add(new BasicNameValuePair("password", input_password));

            JSONParser jParser = new JSONParser();
            JSONObject json = jParser.makeHttpRequestToObject(url, "GET", nvp);      //get data from server

            try {
                if (json.getString("access_token") != null) {
                    token   = json.getString("access_token");
                    current_status = json.getString("current_status");
                    isSuccess = true;
                } else {
                    Log.e("error", "unable to get data 0");
                }
            } catch (Exception e) {
                Log.e("error", "unable to get data 1");
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pDialog.dismiss();

            if(isSuccess) {
               new getMyProfile().execute();
            }
        }
    }

    public class getMyProfile extends AsyncTask<String, String, String> {
        boolean isSuccess;
        ProgressDialog pDialog;
        SessionManager session;
        JSONArray json;
        Config cfg = new Config();

        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

            isSuccess = false;
            session = new SessionManager(myContext);
        }

        @Override
        protected String doInBackground(String... arg0) {
            String url = cfg.HOSTNAME + "/profile";
            List<NameValuePair> nvp = new ArrayList<NameValuePair>();
            nvp.add(new BasicNameValuePair("access_token", token));

            JSONParser jParser = new JSONParser();
            json = jParser.makeHttpRequest(url, "GET", nvp);      //get data from server

            try {
                if(json.length() > 0) {
                    for (int i = 0; i < json.length(); i++) {
                        JSONObject result = json.getJSONObject(i);
                        username = result.getString("username");
                        user_fullname = result.getString("user_fullname");
                        user_email = result.getString("email");
                        Log.e("ok", " ambil data");
                    }
                }
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
            //make login session here
            session.createLoginSession(username, user_fullname, user_email, current_status, token);
            Intent intent = new Intent(myContext, ContainerActivity.class);
            startActivity(intent);
            myContext.finish();
        }
    }

    private class getTokenGCM extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Intent intent = new Intent(myContext, RegistrationIntentService.class);
            myContext.startService(intent);
            return null;
        }
    }
}
