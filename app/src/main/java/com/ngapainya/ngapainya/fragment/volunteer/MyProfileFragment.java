package com.ngapainya.ngapainya.fragment.volunteer;


import android.app.Activity;
import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.ngapainya.ngapainya.R;
import com.ngapainya.ngapainya.activity.SettingsActivity;
import com.ngapainya.ngapainya.activity.owner.ContainerActivity;
import com.ngapainya.ngapainya.fragment.volunteer.child.EditProfileFragment;
import com.ngapainya.ngapainya.fragment.volunteer.child.ShowFeedFragment;
import com.ngapainya.ngapainya.fragment.volunteer.child.ShowProgram;
import com.ngapainya.ngapainya.helper.Config;
import com.ngapainya.ngapainya.helper.JSONParser;
import com.ngapainya.ngapainya.helper.SessionManager;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
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

    private Bitmap photo = null;
    private String image_url;

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
                    image_url = getRealPathFromURI(selectedImage);
                    propic.setImageBitmap(photo);

                    try {
                        Log.e("Path", getRealPathFromURI(selectedImage));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //Log.e("URL", selectedImage.toString());
                }
        }
        if (photo == null) {
            Toast.makeText(myContext,
                    "Please select image", Toast.LENGTH_SHORT).show();
        } else {
            new updateProfilePicture().execute();
            Log.e("Path", "uploading...");
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

    /*public void decodeFile(String filePath) {
        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 1024;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp &lt; REQUIRED_SIZE &amp;&amp; height_tmp &lt; REQUIRED_SIZE)
            break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        bitmap = BitmapFactory.decodeFile(filePath, o2);

        imgView.setImageBitmap(bitmap);

    }*/

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
        ProgressDialog pDialog;
        SessionManager session;
        HashMap<String, String> user;
        String token;
        Config cfg = new Config();

        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Uploading image...");
            pDialog.setIndeterminate(false);
            pDialog.show();

            session = new SessionManager(myContext);
            user = session.getUserDetails();
            token = user.get(SessionManager.KEY_TOKEN);
        }

        @Override
        protected String doInBackground(String... arg0) {
            try {
                //Image path /storage/emulated/0/DCIM/photo.png
                File image = new File(image_url);

                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(cfg.HOSTNAME+"/profile/update/pp");

                MultipartEntityBuilder multipartEntity = MultipartEntityBuilder.create();
                multipartEntity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
                multipartEntity.addTextBody("access_token", token);
                multipartEntity.addPart("avatar", new FileBody(image));
                post.setEntity(multipartEntity.build());

                HttpResponse response = client.execute(post);
                String responseBody = EntityUtils.toString(response.getEntity());

                Log.e("multiPartPost", responseBody);
                return responseBody;

            } catch (Exception e) {
                Log.e(e.getClass().getName(), e.getMessage(), e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pDialog.dismiss();
        }
    }
}
