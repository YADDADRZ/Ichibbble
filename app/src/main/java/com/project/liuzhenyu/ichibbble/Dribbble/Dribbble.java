package com.project.liuzhenyu.ichibbble.Dribbble;

/*--------------------------------------------------------------------------------------------------
 * Created by liuzhenyu on 7/28/17.
 * This class provide method for all API data retrieval method, and contition check method, login/logout
 --------------------------------------------------------------------------------------------------*/

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.project.liuzhenyu.ichibbble.Dribbble.Auth.DribbbleException;
import com.project.liuzhenyu.ichibbble.Model.Shot;
import com.project.liuzhenyu.ichibbble.Model.User;
import com.project.liuzhenyu.ichibbble.Utils.ModelUtils;
import com.project.liuzhenyu.ichibbble.View.LoginActivity;
import com.project.liuzhenyu.ichibbble.View.base.DribbbleTask;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
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

    // Http client
    private static OkHttpClient client = new OkHttpClient();

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

        // Clear local memory
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
      GET request user info and store in SP
     ---------------------------------------------------------------------------------------------*/

    private static User fetchUserInfo() throws DribbbleException {
        Request resquest = authRequestBuilder(USER_END_POINT);
        try {
            Response response = client.newCall(resquest).execute();
            return parseResponse(response, USER_TYPE);
        } catch (IOException e) {
            e.printStackTrace();
            throw new DribbbleException(e.getMessage());
        }
    }

    private static Request authRequestBuilder(String url) {
        return new Request.Builder().addHeader("Authorization", "Bearer " + accessToken)
                .url(url).build();
    }

    private static <T> T parseResponse(Response response, TypeToken<T> typeToken)
            throws IOException, JsonSyntaxException{
        String responseString = response.body().string();
        Log.i("USER_INFO_RESPONSE", responseString);
        return ModelUtils.toObject(responseString, typeToken);
    }

    /*----------------------------------------------------------------------------------------------
      Shots info Retrieval Method (List<Shot>); 12 shots(one page) per API request.
     ---------------------------------------------------------------------------------------------*/
    // GET SHOTS
    // This method must run on the non-main thread
    public static List<Shot> getShots(int page) throws DribbbleException{
        Request request = authRequestBuilder(SHOT_END_POINT + "?page=" + page);
        Log.i("SHOTS_REQUEST", request.toString());
        try {
            Response response = client.newCall(request).execute();
            Log.i("SHOTS_RESPONSE", response.toString());
            return parseResponse(response, SHOTS_TYPE);
        } catch (IOException e) {
            e.printStackTrace();
            throw new DribbbleException(e.getMessage());
        }
    }

    public static Boolean isLikeShot(String id) {
        return false;
    }
}
