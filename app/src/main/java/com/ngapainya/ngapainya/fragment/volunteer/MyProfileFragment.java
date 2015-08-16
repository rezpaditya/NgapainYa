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
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

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
    @Bind(R.id.txtShwFeed) TextView txtShowFeed;
    @Bind(R.id.txtShwProgram) TextView txtShwProgram;
    @Bind(R.id.follower) TextView follower;
    @Bind(R.id.following) TextView following;
    @Bind(R.id.ttl_post) TextView ttl_post;
    @Bind(R.id.sum_post) TextView sum_post;
    @Bind(R.id.sum_acc_program) TextView sum_acc_program;
    @Bind(R.id.profile_image) ImageView propic;
    @Bind(R.id.location) TextView location;

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
    private String uploaded_img;

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

        if (myContext.getClass().getName().equals(PACKAGE_NAME + "volunteer.ContainerActivity")) {
        /*Customize actionbar*/
            ((com.ngapainya.ngapainya.activity.volunteer.ContainerActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            ((com.ngapainya.ngapainya.activity.volunteer.ContainerActivity) getActivity()).changeActionbarStyle(true);
        } else {
            ((com.ngapainya.ngapainya.activity.owner.ContainerActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            ((com.ngapainya.ngapainya.activity.owner.ContainerActivity) getActivity()).changeActionbarStyle(true);
        }

        setHasOptionsMenu(true);

        new getMyProfile().execute();

        /*
        * Initialize the variables
        * */
        ButterKnife.bind(this, myFragmentView);
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
                            /*Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(cameraIntent, CAMERA_REQUEST);*/
                        } else if (menuItem.getItemId() == R.id.gallery) {
                            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                            photoPickerIntent.setType("image/*");
                            startActivityForResult(photoPickerIntent, SELECT_PHOTO);
                        } else if(menuItem.getItemId() == R.id.remove){
                            Picasso.with(myContext)
                                    .load(R.drawable.propic_default)
                                    .placeholder(R.drawable.propic_default)
                                    .into(propic);
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
            //new updateProfilePicture().execute();
            try {
                run();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.e("Path", "uploading...");
        }
        /*if (photo != null) {
            //base64 encoding
            encodeImage(photo);
            new updateProfilePicture().execute();
        }*/
    }

    public void run() throws Exception {
        OkHttpClient client = new OkHttpClient();
        final boolean[] isSuccess = {false};
        File file = new File(image_url);

        RequestBody requestBody = new MultipartBuilder()
                .type(MultipartBuilder.FORM)
                .addFormDataPart("title", file.getName())
                .addFormDataPart("image", file.getName(),
                        RequestBody.create(MediaType.parse("image/png"), file))
                .build();

        Request request = new Request.Builder()
                .header("Authorization", "Client-ID 55bdd6074a641b3") //should be make a variable for client id
                .url("https://api.imgur.com/3/image")
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Toast.makeText(myContext,
                        "Upload image failed 0", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                /*Headers responseHeaders = response.headers();
                for (int i = 0; i < responseHeaders.size(); i++) {
                    System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                }*/
                try {
                    JSONObject obj = new JSONObject(response.body().string());
                    JSONObject result = obj.getJSONObject("data");
                    String img = result.getString("link");
                    uploaded_img = img;
                    isSuccess[0] = true;
                    Log.e("link", img);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    sendPhoto();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void sendPhoto() throws Exception{
        OkHttpClient client = new OkHttpClient();
        SessionManager session = new SessionManager(myContext);
        HashMap<String, String> user = session.getUserDetails();
        String token = user.get(SessionManager.KEY_TOKEN);

        RequestBody formBody = new FormEncodingBuilder()
                .add("access_token", token)
                .add("photo", uploaded_img)
                .build();

        Request request = new Request.Builder()
                .url(Config.HOSTNAME + "/profile/update/pp")
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Toast.makeText(myContext,
                        "Upload image failed 0", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            }
        });
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
                    .load(pic_url)
                    .placeholder(R.drawable.propic_default)
                    .into(propic);
            follower.setText(total_friend);
            following.setText(total_following);
            ttl_post.setText(total_post);
            sum_post.setText(total_post);
            sum_acc_program.setText(apply_accepted);
            if (user_location != null) {
                location.setText(user_location + " city");
            } else {
                location.setText("Nowhere");
            }
        }
    }

}
