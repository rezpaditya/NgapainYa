package com.ngapainya.ngapainya.fragment.volunteer;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.ngapainya.ngapainya.R;
import com.ngapainya.ngapainya.activity.SettingsActivity;
import com.ngapainya.ngapainya.activity.owner.ContainerActivity;
import com.ngapainya.ngapainya.fragment.volunteer.child.EditProfileFragment;
import com.ngapainya.ngapainya.fragment.volunteer.child.ShowFeedFragment;
import com.ngapainya.ngapainya.fragment.volunteer.child.ShowProgram;
import com.ngapainya.ngapainya.helper.Config;
import com.ngapainya.ngapainya.helper.JSONParser;
import com.ngapainya.ngapainya.helper.SessionManager;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyProfileFragment extends Fragment {
    private FragmentActivity myContext;
    private View myFragmentView;

    private final String PACKAGE_NAME = "com.ngapainya.ngapainya.activity.";

    // Camera activity request codes
    private static final int CAMERA_REQUEST = 1888;
    private static final int SELECT_PHOTO = 100;
    String encodedImage = "";

    private static final String FOLLOWING_SPEC = "following";
    private static final String YOU_SPEC = "you";
    private FragmentTabHost tabHost;
    /*
    * Variable to retrieve from view
    * */
    private TextView txtShowFeed;
    private TextView txtShwProgram;
    private TextView follower;
    private TextView following;
    private TextView ttl_post;
    private TextView sum_post;
    private TextView sum_acc_program;
    private ImageView propic;
    private TextView location;

    /*
    * variable to retrieve data from server
    * */
    private String pic_url;
    private String total_post;
    private String total_friend;
    private String total_following;
    private String user_location;
    private String apply_accepted;

    public void switchMode() {
        Intent intent = new Intent(myContext, ContainerActivity.class);
        startActivity(intent);
        myContext.finish();
    }

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    /*@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        new getMyProfile().execute();

    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragmentView = inflater.inflate(R.layout.fragment_profile, container, false);
        Log.e("onCreateMyProfile", "works");

        if(myContext.getClass().getName().equals(PACKAGE_NAME+"volunteer.ContainerActivity")) {
        /*Customize actionbar*/
            ((com.ngapainya.ngapainya.activity.volunteer.ContainerActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            ((com.ngapainya.ngapainya.activity.volunteer.ContainerActivity) getActivity()).changeActionbarStyle(true);
        }
        setHasOptionsMenu(true);

        new getMyProfile().execute();

        /*
        * Initialize the variables
        * */
        propic = (ImageView) myFragmentView.findViewById(R.id.profile_image);
        txtShowFeed = (TextView) myFragmentView.findViewById(R.id.txtShwFeed);
        txtShwProgram = (TextView) myFragmentView.findViewById(R.id.txtShwProgram);
        follower = (TextView) myFragmentView.findViewById(R.id.follower);
        following = (TextView) myFragmentView.findViewById(R.id.following);
        ttl_post = (TextView) myFragmentView.findViewById(R.id.ttl_post);
        sum_post = (TextView) myFragmentView.findViewById(R.id.sum_post);
        sum_acc_program = (TextView) myFragmentView.findViewById(R.id.sum_acc_program);
        location = (TextView) myFragmentView.findViewById(R.id.location);

        tabHost = (FragmentTabHost) myFragmentView.findViewById(android.R.id.tabhost);  // The activity TabHost
        tabHost.setup(getActivity(), getChildFragmentManager(), android.R.id.tabcontent);

        tabHost.addTab(tabHost.newTabSpec(FOLLOWING_SPEC).setIndicator("Following"), ShowFeedFragment.class, null);
        tabHost.addTab(tabHost.newTabSpec(YOU_SPEC).setIndicator("You"), ShowProgram.class, null);
        tabHost.setCurrentTab(0);

        return myFragmentView;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.showFeed:
                tabHost.setCurrentTab(0);
                txtShowFeed.setTextColor(getResources().getColor(R.color.Red));
                txtShwProgram.setTextColor(Color.BLACK);
                break;
            case R.id.showProgram:
                tabHost.setCurrentTab(1);
                txtShwProgram.setTextColor(getResources().getColor(R.color.Red));
                txtShowFeed.setTextColor(Color.BLACK);
                break;
            case R.id.editProfileBtn:
                EditProfileFragment editProfile = new EditProfileFragment();
                ((com.ngapainya.ngapainya.activity.volunteer.ContainerActivity)
                        getActivity()).changeFragment(editProfile);
                ((com.ngapainya.ngapainya.activity.volunteer.ContainerActivity)
                        getActivity()).standardTitleBar("Edi_t Profile");
                break;
            case R.id.profile_image:
                PopupMenu popup = new PopupMenu(myContext, propic);
                popup.getMenuInflater().inflate(R.menu.menu_pop_up_propic, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.capture) {
                            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(cameraIntent, CAMERA_REQUEST);
                        } else if (menuItem.getItemId() == R.id.gallery) {
                            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                            photoPickerIntent.setType("image/*");
                            startActivityForResult(photoPickerIntent, SELECT_PHOTO);
                        }
                        return true;
                    }
                });
                popup.show();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap photo = null;
        switch (requestCode) {
            case CAMERA_REQUEST:
                if (requestCode == CAMERA_REQUEST && resultCode == myContext.RESULT_OK) {
                    photo = (Bitmap) data.getExtras().get("data");
                    //image.setImageBitmap(photo);
                }
                break;
            case SELECT_PHOTO:
                if (resultCode == myContext.RESULT_OK) {
                    Uri selectedImage = data.getData();
                    InputStream imageStream = null;
                    try {
                        imageStream = myContext.getContentResolver().openInputStream(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    photo = BitmapFactory.decodeStream(imageStream);
                    propic.setImageBitmap(photo);

                    try {
                        run(getRealPathFromURI(selectedImage));
                        Log.e("Path", getRealPathFromURI(selectedImage));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //Log.e("URL", selectedImage.toString());
                }
        }
        /*if (photo != null) {
            //base64 encoding
            encodeImage(photo);
            new updateProfilePicture().execute();
        }*/
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = myContext.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    /*
    * This method used to encode image to String
    * */
    public void encodeImage(Bitmap photo) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        byte[] byteArrayImage = baos.toByteArray();
        encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
    }

    /*
    * OKHTTP library to upload image
    * */

    public void run(String image_path) throws Exception {
        Config cfg = new Config();
        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new MultipartBuilder()
                .type(MultipartBuilder.FORM)
                .addFormDataPart("text", "ini Ok Http")
                .addFormDataPart("access_token", "respa")
                /*.addFormDataPart("avatar", "file.png",
                        RequestBody.create(MediaType.parse("image/png"), new File(image_path)))*/
                .build();

        Request request = new Request.Builder()
                .url(cfg.HOSTNAME + "/activity/add/text")
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        System.out.println(response.body().string());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Auto-generated method stub
        super.onCreateOptionsMenu(menu, inflater);
        myContext.getMenuInflater().inflate(R.menu.menu_profile, menu);
        final MenuItem item = menu.findItem(R.id.action_done);
        /*item.getActionView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchMode();
            }
        });*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done: {
                Intent intent = new Intent(myContext, SettingsActivity.class);
                startActivity(intent);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public class getMyProfile extends AsyncTask<String, String, String> {
        SessionManager session;
        HashMap<String, String> user;
        String token;
        Config cfg = new Config();

        protected void onPreExecute() {
            super.onPreExecute();
            session = new SessionManager(myContext);
            user = session.getUserDetails();
            token = user.get(SessionManager.KEY_TOKEN);
        }

        @Override
        protected String doInBackground(String... arg0) {
            String url = cfg.HOSTNAME + "/profile";
            List<NameValuePair> nvp = new ArrayList<NameValuePair>();
            nvp.add(new BasicNameValuePair("access_token", token));

            JSONParser jParser = new JSONParser();
            JSONArray json = jParser.makeHttpRequest(url, "GET", nvp);      //get data from server

            try {
                for (int i = 0; i < json.length(); i++) {
                    JSONObject result = json.getJSONObject(i);
                    pic_url = result.getString("user_pic");
                    total_post = result.getString("count_activity");
                    total_friend = result.getString("follower");
                    total_following = result.getString("following");
                    user_location = result.getString("user_location");
                    apply_accepted = result.getString("apply_accepted");
                }

                Log.e("ok", " ambil data");
            } catch (Exception e) {
                Log.e("error", "tidak bisa ambil data 1");
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Picasso.with(myContext)
                    .load("http://ainufaisal.com/" + pic_url)
                    .placeholder(R.drawable.propic_default)
                    .into(propic);
            follower.setText(total_friend);
            following.setText(total_following);
            ttl_post.setText(total_post);
            sum_post.setText(total_post);
            sum_acc_program.setText(apply_accepted);
            if(user_location != null) {
                location.setText(user_location + " city");
            }else{
                location.setText("Nowhere");
            }
        }
    }

    public class updateProfilePicture extends AsyncTask<String, String, String> {
        SessionManager session;
        HashMap<String, String> user;
        String token;
        Config cfg = new Config();

        protected void onPreExecute() {
            super.onPreExecute();

            session = new SessionManager(myContext);
            user = session.getUserDetails();
            token = user.get(SessionManager.KEY_TOKEN);
        }

        @Override
        protected String doInBackground(String... arg0) {
            String url = cfg.HOSTNAME + "/profile/update/pp";
            List<NameValuePair> nvp = new ArrayList<NameValuePair>();
            nvp.add(new BasicNameValuePair("access_token", token));
            nvp.add(new BasicNameValuePair("avatar", encodedImage));

            JSONParser jParser = new JSONParser();
            jParser.makeHttpRequest(url, "GET", nvp);      //get data from server
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
        }
    }
}
