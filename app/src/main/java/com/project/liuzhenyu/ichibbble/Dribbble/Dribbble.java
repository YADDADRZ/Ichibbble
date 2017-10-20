package com.project.liuzhenyu.ichibbble.Dribbble;

/*--------------------------------------------------------------------------------------------------
 * Created by liuzhenyu on 7/28/17.
 * This class provide method for all API data retrieval method, and contition check method, login/logout
 --------------------------------------------------------------------------------------------------*/

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.project.liuzhenyu.ichibbble.Model.Bucket;
import com.project.liuzhenyu.ichibbble.Model.Like;
import com.project.liuzhenyu.ichibbble.Model.Shot;
import com.project.liuzhenyu.ichibbble.Model.User;
import com.project.liuzhenyu.ichibbble.Utils.ModelUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Dribbble {

    /*---------------------------------------------------------------------------------------------
      Constant Variable
      API Page: http://developer.dribbble.com/v1/users/
     --------------------------------------------------------------------------------------------*/
    public static final int COUNT_PER_LOAD = 12;
    // Request url
    private static final String API_URL = "https://api.dribbble.com/v1/";
    private static final String USER_END_POINT = API_URL + "user";
    private static final String USERS_END_POINT = API_URL + "users";
    private static final String SHOT_END_POINT = API_URL + "shots";
    private static final String BUCKETS_END_POINT = API_URL + "buckets";

    // SharePreference name; special create for access token
    private static final String SP_AUTH = "auth";

    // sharePreference key name
    private static final String KEY_ACCESS_TOKEN = "access_token";
    private static final String KEY_USER = "user";

    // Memory variable
    private static String accessToken;
    private static User user;

    // Typetoken
    private static final TypeToken<User> USER_TYPE = new TypeToken<User>(){}; // Get User Type
    private static final TypeToken<Shot> SHOT_TYPE = new TypeToken<Shot>(){};
    private static final TypeToken<List<Shot>> SHOTS_TYPE = new TypeToken<List<Shot>>(){};
    private static final TypeToken<Like> LIKE_TYPE = new TypeToken<Like>(){};
    private static final TypeToken<List<Like>> LIST_LIKE_TYPE = new TypeToken<List<Like>>(){};
    private static final TypeToken<Bucket> BUCKET_TYPE = new TypeToken<Bucket>(){};


    // Http client
    private static OkHttpClient client = new OkHttpClient();

    /*--------------------------------------------------------------------------------------------------
    * HTTP request
    --------------------------------------------------------------------------------------------------*/

    // Form a builder with target url
    private static Request.Builder authRequestBuilder(String url) {
        return new Request.Builder().addHeader("Authorization", "Bearer " + accessToken)
                .url(url);
    }

    // Build GET request & make request
    private static Response makeGetRequest(@NonNull String url) throws DribbbleException {
        Request request = authRequestBuilder(url).build();
        return makeRequest(request);
    }

    // POST
    private static Response makePostRequest(@NonNull String url, @NonNull RequestBody requestBody)
            throws DribbbleException {
        Request request = authRequestBuilder(url)
                .post(requestBody).build();
        return makeRequest(request);
    }

    // Delete
    private static Response makeDeleteRequest(@NonNull String url)  throws DribbbleException {
        Request request = authRequestBuilder(url)
                .delete()
                .build();
        return makeRequest(request);
    }

    // Execute the request; must run on the non-main thread
    private static Response makeRequest(Request request) throws DribbbleException {
        try {
            Response response = client.newCall(request).execute();
            return response;
        } catch (IOException e) {
            throw new DribbbleException(e.getMessage());
        }
    }

    // Check the response see if the Dribbble server get the Request
    private static void checkStatusCode(Response response, int statusCode)
            throws DribbbleException {
        if (response.code() != statusCode) {
            throw new DribbbleException(response.message());
        }
    }

    /*----------------------------------------------------------------------------------------------
     * Parse response to target Type; User, Shot, List<Shot> ....
    ----------------------------------------------------------------------------------------------*/

    private static <T> T parseResponse(Response response, TypeToken<T> typeToken)
            throws DribbbleException {
        String responseString = null;
        try {
            responseString = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        Log.i("USER_INFO_RESPONSE", responseString);
        return ModelUtils.toObject(responseString, typeToken);
    }

    /*---------------------------------------------------------------------------------------------
      Login & Logout
     --------------------------------------------------------------------------------------------*/

    public static void init(@NonNull Context context){
        accessToken = loadAccessToken(context);
        if (accessToken != null) {
            user = loadUser(context);
        }
    }

    private static User loadUser(Context context) {
        return ModelUtils.read(context, KEY_USER, USER_TYPE);
    }

    private static String loadAccessToken(Context context) {
        SharedPreferences sp = context.getApplicationContext()
                .getSharedPreferences(SP_AUTH, Context.MODE_PRIVATE);
        return sp.getString(KEY_ACCESS_TOKEN, null);
    }

    public static void login(@NonNull Context context, @NonNull String accessToken)
            throws DribbbleException {
        // store access token
        Dribbble.accessToken = accessToken;
        Log.i("TOKEN", accessToken);
        storeAccessToken(context, accessToken);

        Dribbble.user = fetchUserInfo();
        storeUser(context, user);
    }

    public static void logout(@NonNull Context context) {
        // clear sharePreference
        storeAccessToken(context, null);
        storeUser(context, null);

        // Clear local memory TODO clear sharePreference
        accessToken = null;
        user = null;
    }

    public static boolean isLogin() {
        return accessToken != null;
    }

    public static User getCurrentUser() {
        return user;
    }

    private static void storeUser(Context context, User user) {
        ModelUtils.save(context, KEY_USER, user);
    }

    private static void storeAccessToken(@NonNull Context context, @Nullable String accessToken) {
        SharedPreferences sp = context.getApplicationContext()
                .getSharedPreferences(SP_AUTH, Context.MODE_PRIVATE);

        sp.edit().putString(KEY_ACCESS_TOKEN, accessToken).apply();
    }

    /*---------------------------------------------------------------------------------------------
      Get User info
     ---------------------------------------------------------------------------------------------*/
    private static User fetchUserInfo() throws DribbbleException {
        return parseResponse(makeGetRequest(USER_END_POINT), USER_TYPE);
    }

    /*----------------------------------------------------------------------------------------------
      Shots info Retrieval Method (List<Shot>);
      12 shots(one page) per API request.
     ---------------------------------------------------------------------------------------------*/
    // GET SHOTS
    // This method must run on the non-main thread
    public static List<Shot> getShots(int page) throws DribbbleException{
        return parseResponse(makeGetRequest(SHOT_END_POINT + "?page=" + page), SHOTS_TYPE);
    }

    /*--------------------------------------------------------------------------------------------------
      Like
      API: if shot is not like then return not_found;
    --------------------------------------------------------------------------------------------------*/
    // Checked the shot is likeed
    public static Boolean isLikeShot(String id) throws DribbbleException {
        String url = SHOT_END_POINT + "/" + id + "/like";
        Response response = makeGetRequest(url);
        switch (response.code()) {
            case HttpURLConnection.HTTP_OK:
                return true;
            case HttpURLConnection.HTTP_NOT_FOUND:
                return false;
            default:
                throw new DribbbleException(response.message());
        }
    }

    public static List<Shot> getLikeShots(int page) throws  DribbbleException {
        List<Like> likes = getLikes(page);
        List<Shot> likedShots = new ArrayList<>();
        for (Like like : likes) {
            likedShots.add(like.shot);
        }
        return likedShots;
    }

    private static List<Like> getLikes(int page) throws DribbbleException {
        String url = USER_END_POINT + "/likes?page=" + page;
        return parseResponse(makeGetRequest(url), LIST_LIKE_TYPE);
    }

    // Like & Unlike a shot
    /*--------------------------------------------------------------------------------------------------
     * https://github.com/square/okhttp/wiki/Recipes
     * Make a Post request
    --------------------------------------------------------------------------------------------------*/
    public static Like like (String id) throws DribbbleException {
        String url = SHOT_END_POINT + "/" + id + "/like";
        Response response = makePostRequest(url, new FormBody.Builder().build());
        checkStatusCode(response, HttpURLConnection.HTTP_CREATED); // According to Dribbble API
        return parseResponse(response, LIKE_TYPE);
    }

    // unlike a shot
    public static void unlike (String id) throws DribbbleException {
        String url = SHOT_END_POINT + "/" + id + "/like";
        Response response = makeDeleteRequest(url);
        checkStatusCode(response, HttpURLConnection.HTTP_NO_CONTENT); // according to Dribbble API
    }
}
