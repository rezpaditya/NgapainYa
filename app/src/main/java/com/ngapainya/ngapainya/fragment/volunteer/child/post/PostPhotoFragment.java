package com.ngapainya.ngapainya.fragment.volunteer.child.post;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.ngapainya.ngapainya.R;
import com.ngapainya.ngapainya.activity.volunteer.ContainerActivity;
import com.ngapainya.ngapainya.fragment.volunteer.HomeFragment;
import com.ngapainya.ngapainya.helper.Config;
import com.ngapainya.ngapainya.helper.JSONParser;
import com.ngapainya.ngapainya.helper.SessionManager;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostPhotoFragment extends Fragment {
    private FragmentActivity myContext;
    private View myFragmentView;
    Bitmap photo;

    /*
    * Variable from the view
    * */
    @Bind(R.id.preview) ImageView preview;
    @Bind(R.id.caption) EditText caption;

    private static final int SELECT_PHOTO = 100;

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startPickerIntent();
    }

    public void startPickerIntent() {
        /*
        * Choose photo frm gallery
        * */
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        myFragmentView = inflater.inflate(R.layout.fragment_post_photo, container, false);

        ButterKnife.bind(this, myFragmentView);

        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPickerIntent();
            }
        });

        if(myContext.getClass().getName().equals("com.ngapainya.ngapainya.activity.volunteer.ContainerActivity")) {
            ((com.ngapainya.ngapainya.activity.volunteer.ContainerActivity)
                    getActivity()).getSupportActionBar()
                    .setDisplayHomeAsUpEnabled(true);
            ((com.ngapainya.ngapainya.activity.volunteer.ContainerActivity)
                    getActivity()).standardTitleBar("Post Photo");
        }else{
            ((com.ngapainya.ngapainya.activity.owner.ContainerActivity)
                    getActivity()).getSupportActionBar()
                    .setDisplayHomeAsUpEnabled(true);
            ((com.ngapainya.ngapainya.activity.owner.ContainerActivity)
                    getActivity()).standardTitleBar("Post Photo");
        }

        setHasOptionsMenu(true);

        return myFragmentView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        photo = null;
        switch (requestCode) {
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
                    preview.setImageBitmap(photo);
                }
        }
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
                doPost(v);
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

    public void doPost(View view) {
        if (caption.getText().length() > 0) {
            new postPhoto().execute();
        }
    }

    /*
    * This method used to encode image to String
    * */
    public String encodeImage(Bitmap photo) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        byte[] byteArrayImage = baos.toByteArray();
        return Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
    }

    public class postPhoto extends AsyncTask<String, String, String> {
        ProgressDialog pDialog;
        SessionManager session;
        HashMap<String, String> user;
        String token;
        String input_caption;
        String input_image;
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
            input_caption = caption.getText().toString();
            input_image = encodeImage(photo);
        }

        @Override
        protected String doInBackground(String... arg0) {
            String url = cfg.HOSTNAME +"/activity/add/photo";
            List<NameValuePair> nvp = new ArrayList<NameValuePair>();
            nvp.add(new BasicNameValuePair("access_token", token));
            nvp.add(new BasicNameValuePair("text", input_caption));
            nvp.add(new BasicNameValuePair("photo", input_image));

            JSONParser jParser = new JSONParser();
            JSONArray json = jParser.makeHttpRequest(url, "GET", nvp);      //get data from server
//            Log.e("text", input_caption);
//            Log.e("photo", input_image);
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
