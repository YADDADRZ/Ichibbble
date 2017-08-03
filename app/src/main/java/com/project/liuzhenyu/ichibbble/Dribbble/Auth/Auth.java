package com.project.liuzhenyu.ichibbble.Dribbble.Auth;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/*-------------------------------------------------------------------------------------------------
  Created by liuzhenyu on 7/28/17.

  The purpose of this class is to provide build method for url to get code and url to get access
  token. It also provide method for start Authorize activity (webview of dribbble).

  Implementation strictly follow Dribbble API:
  http://developer.dribbble.com/v1/oauth/#web-application-flow
--------------------------------------------------------------------------------------------------*/

public class Auth {

    public static final int REQ_CODE = 100;

    // Constant parameter name from Dribbble API
    private static final String KEY_CODE = "code";
    private static final String KEY_CLIENT_ID = "client_id";
    private static final String KEY_CLIENT_SECRET = "client_secret";
    private static final String KEY_SCOPE = "scope";
    private static final String KEY_ACCESS_TOKEN = "access_token";
    private static final String KEY_REDIRECT_URI = "redirect_uri";

    // Hard code Values for this app
    private static final String CLIENT_ID = "d1dc86caf93684bc861e80c3be3cf5a780877414ebf8934d436cf2caafdef498";
    private static final String CLIENT_SECRET = "c55468835a1aff5403a14b49fe8c10c6f0a76d3d4f833d57f1932c7d05c4fde8";
    // For this app we only require scope for public and write
    private static final String SCOPE = "public+write";

    // url
    private static final String URI_AUTHORIZE = "https://dribbble.com/oauth/authorize"; // GET
    private static final String URI_TOKEN = "https://dribbble.com/oauth/token"; // POST

    // redirect url
    public static final String REDIRECT_URI = "http://www.google.com"; // Random website

    private static OkHttpClient client;

    private static String buildAuthorizeUrl() {
        String url = Uri.parse(URI_AUTHORIZE)
                .buildUpon()
                .appendQueryParameter(KEY_CLIENT_ID, CLIENT_ID)
                .build()
                .toString();

        // fix encode issue; redirect url is optional
        url += "&" + KEY_REDIRECT_URI + "=" + REDIRECT_URI;
        /*
        If not provided, scope defaults to the public scope for users that don’t have a valid token
        for the application. For users who do already have a valid token for the application,
        the user won’t be shown the authorization page with the list of scopes.
         */
        url += "&" + KEY_SCOPE + "=" + SCOPE;

        return url;
    }

    // open webview to start authorize acitvity
    // start activity from onClick of LoginAcivity btn
    public static void openAuthActivity(@NonNull Activity activity) {
        Intent intent = new Intent(activity, AuthActivity.class);
        intent.putExtra(AuthActivity.KEY_URL, buildAuthorizeUrl());
        activity.startActivityForResult(intent, REQ_CODE);
    }

    // Get Access token by using the token url
    // Use multi-task to handle POST request in here
    private static class FetchAccessToken extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            Log.i("String[0]", strings[0]);

            // Build access token url; follow Dribbble API
            RequestBody postBody = new FormBody.Builder()
                    .add(KEY_CLIENT_ID, CLIENT_ID)
                    .add(KEY_CLIENT_SECRET, CLIENT_SECRET)
                    .add(KEY_CODE, strings[0])
                    .add(KEY_REDIRECT_URI, REDIRECT_URI)
                    .build();

            Request request = new Request.Builder()
                    .url(URI_TOKEN)
                    .post(postBody)
                    .build();

            // Start request; Override method does not allowed to add IOException
            String responseString = null;
            try {
                responseString = client.newCall(request).execute().body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // The return String will be in Json
            try {
                JSONObject obj = new JSONObject(responseString);
                return obj.getString(KEY_ACCESS_TOKEN);
            } catch (JSONException e) {
                e.printStackTrace();
                return  "";
            }
        }
    }

    public static String fetchAccessToken(String authCode)
            throws ExecutionException, InterruptedException {
        client = new OkHttpClient();
        return new FetchAccessToken().execute(authCode).get();
    }
}
