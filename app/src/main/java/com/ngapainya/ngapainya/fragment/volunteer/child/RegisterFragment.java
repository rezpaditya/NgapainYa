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
import com.ngapainya.ngapainya.helper.SessionManager;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {
    private FragmentActivity myContext;
    private View myFragmentView;
    /*
    * Varibale from view
    * */
    @Bind(R.id.email) EditText email;
    @Bind(R.id.username) EditText username;
    @Bind(R.id.password) EditText password;
    @Bind(R.id.regist_btn) Button btn_regist;


    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragmentView =  inflater.inflate(R.layout.fragment_register, container, false);

        /*Initialize the views variable
        * */
        ButterKnife.bind(this, myFragmentView);
        btn_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email.getText().length() > 0
                        && username.getText().length() > 0
                        && password.getText().length() >= 8) {
                    new doRegister().execute();
                }
            }
        });


        return myFragmentView;
    }

    public class doRegister extends AsyncTask<String, String, String> {
        ProgressDialog pDialog;
        SessionManager session;
        HashMap<String, String> user;
        String token;
        String input_email;
        String input_username;
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
            session = new SessionManager(myContext);
            user    = session.getUserDetails();
            token   = user.get(SessionManager.KEY_TOKEN);

            input_email     = email.getText().toString();
            input_username  = username.getText().toString();
            input_password  = password.getText().toString();
        }

        @Override
        protected String doInBackground(String... arg0) {
            String url = cfg.HOSTNAME +"/register";
            List<NameValuePair> nvp = new ArrayList<NameValuePair>();
            nvp.add(new BasicNameValuePair("access_token", token));
            nvp.add(new BasicNameValuePair("email", input_email));
            nvp.add(new BasicNameValuePair("username", input_username));
            nvp.add(new BasicNameValuePair("password", input_password));

            JSONParser jParser = new JSONParser();
            JSONObject json = jParser.makeHttpRequestToObject(url, "GET", nvp);      //get data from server
            //Log.e("error", "jalan");
            try {
                if (json.getString("access_token") != null) {
                    token   = json.getString("access_token");
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
                session.createLoginSession(input_username, "",input_email, "volunteer", token);
                Intent intent = new Intent(myContext, ContainerActivity.class);
                startActivity(intent);
                myContext.finish();
            }
        }
    }
}
