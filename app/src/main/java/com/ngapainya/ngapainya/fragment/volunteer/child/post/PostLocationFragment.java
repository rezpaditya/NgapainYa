package com.ngapainya.ngapainya.fragment.volunteer.child.post;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.ngapainya.ngapainya.R;
import com.ngapainya.ngapainya.activity.volunteer.ContainerActivity;
import com.ngapainya.ngapainya.fragment.volunteer.HomeFragment;
import com.ngapainya.ngapainya.helper.Config;
import com.ngapainya.ngapainya.helper.JSONParser;
import com.ngapainya.ngapainya.helper.SessionManager;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostLocationFragment extends Fragment implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleApiClient mGoogleApiClient;
    private LocationManager locationManager;
    private static AlertDialog alert;
    private FragmentActivity myContext;
    private View myFragmentView;

    @Bind(R.id.text_input) TextView text;
    private String latitude;
    private String longitude;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragmentView = inflater.inflate(R.layout.fragment_post_location, container, false);

        ButterKnife.bind(this, myFragmentView);

        if (myContext.getClass().getName().equals("com.ngapainya.ngapainya.activity.volunteer.ContainerActivity")) {
            ((com.ngapainya.ngapainya.activity.volunteer.ContainerActivity)
                    getActivity()).getSupportActionBar()
                    .setDisplayHomeAsUpEnabled(true);
            ((com.ngapainya.ngapainya.activity.volunteer.ContainerActivity)
                    getActivity()).standardTitleBar("Post Location");
        } else {
            ((com.ngapainya.ngapainya.activity.owner.ContainerActivity)
                    getActivity()).getSupportActionBar()
                    .setDisplayHomeAsUpEnabled(true);
            ((com.ngapainya.ngapainya.activity.owner.ContainerActivity)
                    getActivity()).standardTitleBar("Post Location");
        }

        locationManager = (LocationManager) myContext.getSystemService(myContext.LOCATION_SERVICE);
        mGoogleApiClient = new GoogleApiClient.Builder(myContext.getApplicationContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        //call this to get current location
        shareLocation(location);

        setHasOptionsMenu(true);

        return myFragmentView;
    }

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Auto-generated method stub
        super.onCreateOptionsMenu(menu, inflater);
        if (myContext.getClass().getName().equals("com.ngapainya.ngapainya.activity.volunteer.ContainerActivity")) {
            myContext.getMenuInflater().inflate(R.menu.menu_post_text, menu);
        }else{
            myContext.getMenuInflater().inflate(R.menu.menu_post_program, menu);
        }
        final MenuItem item = menu.findItem(R.id.action_done);
        item.getActionView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (longitude != null && latitude != null) {
                    new postLocation().execute();
                } else {
                    Toast.makeText(myContext, "Couldn't get current location, try again!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                //do something
                break;
            case android.R.id.home:
                myContext.onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }



    public void shareLocation(Location location) {
        if (location != null) {
            latitude = Double.toString(location.getLatitude());
            longitude = Double.toString(location.getLongitude());
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        boolean gpsIsEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (gpsIsEnabled) {
            Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            latitude = Double.toString(location.getLatitude());
            longitude = Double.toString(location.getLongitude());
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(myContext);
            builder.setMessage(
                    "You need to activate location service to use this feature. Please turn on network or GPS mode in location settings")
                    .setTitle("Enable Location")
                    .setCancelable(false)
                    .setPositiveButton("Settings",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent intent = new Intent(
                                            Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    startActivity(intent);
                                    alert.dismiss();
                                }
                            })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    alert.dismiss();
                                }
                            });
            alert = builder.create();
            alert.show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        shareLocation(location);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public class postLocation extends AsyncTask<String, String, String> {
        ProgressDialog pDialog;
        SessionManager session;
        HashMap<String, String> user;
        String token;
        String input_text;
        String input_long;
        String input_lat;
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
            input_lat = latitude;
            input_long = longitude;
            input_text = text.getText().toString();
        }

        @Override
        protected String doInBackground(String... arg0) {
            String url = cfg.HOSTNAME + "/activity/add/location";
            List<NameValuePair> nvp = new ArrayList<NameValuePair>();
            nvp.add(new BasicNameValuePair("access_token", token));
            nvp.add(new BasicNameValuePair("text", input_text));
            nvp.add(new BasicNameValuePair("lat", input_lat));
            nvp.add(new BasicNameValuePair("lng", input_long));

            JSONParser jParser = new JSONParser();
            JSONArray json = jParser.makeHttpRequest(url, "GET", nvp);      //get data from server
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
            HomeFragment homeFragment = new HomeFragment();
            if (myContext.getClass().getName().equals("com.ngapainya.ngapainya.activity.volunteer.ContainerActivity")) {
                ((ContainerActivity) getActivity()).changeFragment(homeFragment);
            }else{
                ((com.ngapainya.ngapainya.activity.owner.ContainerActivity) getActivity()).changeFragment(homeFragment);
            }
        }
    }
}
