package com.ngapainya.ngapainya.helper;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.ngapainya.ngapainya.R;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.HashMap;

public class RegistrationIntentService extends IntentService {

    private static final String TAG = "RegIntentService";
    private static final String[] TOPICS = {"global"};

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        try {
            // In the (unlikely) event that multiple refresh operations occur simultaneously,
            // ensure that they are processed sequentially.
            synchronized (TAG) {
                // [START register_for_gcm]
                // Initially this call goes out to the network to retrieve the token, subsequent calls
                // are local.
                // [START get_token]
                InstanceID instanceID = InstanceID.getInstance(this);
                String GCMtoken = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                        GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                // [END get_token]
                Log.i(TAG, "GCM Registration Token: " + GCMtoken);

                // TODO: Implement this method to send any registration to your app's servers.
                sendRegistrationToServer(GCMtoken);

                // [END register_for_gcm]
            }
        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
        }
    }

    /**
     * Persist registration to third-party servers.
     *
     * Modify this method to associate the user's GCM registration token with any server-side account
     * maintained by your application.
     *
     * @param GCMtoken The new token.
     */
    private void sendRegistrationToServer(String GCMtoken) throws Exception{
        OkHttpClient client = new OkHttpClient();
        SessionManager session = new SessionManager(this);
        HashMap<String, String> user = session.getUserDetails();
        String token = user.get(SessionManager.KEY_TOKEN);

        Request request = new Request.Builder()
                .url(Config.HOSTNAME + "/gcm/add?access_token="+token+"&registration_id=" + GCMtoken)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                if(!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                Log.e("success", response.body().string());
            }
        });
    }
}
