package com.project.liuzhenyu.ichibbble.Dribbble;

/*--------------------------------------------------------------------------------------------------
 * Created by liuzhenyu on 7/28/17.
 * This class provide method for all data retrieval method, and contition check method, login/logout
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
import com.project.liuzhenyu.ichibbble.Model.User;
import com.project.liuzhenyu.ichibbble.Utils.ModelUtils;
import com.project.liuzhenyu.ichibbble.View.LoginActivity;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Dribbble {

    /*---------------------------------------------------------------------------------------------
      Constant Variable
      API Page: http://developer.dribbble.com/v1/users/
     --------------------------------------------------------------------------------------------*/

    private static final String API_URL = "https://api.dribbble.com/v1/";
    private static final String USER_END_POINT = API_URL + "user";
    private static final String SP_AUTH = "auth";
    private static final String SP_USER = "user";
    private static final String KEY_ACCESS_TOKEN = "access_token";
    private static final String KEY_USER = "user";
    private static String accessToken;
    private static OkHttpClient client = new OkHttpClient();
    private static final TypeToken<User> USER_TYPE = new TypeToken<User>(){}; // Get User Type
    private static User user;
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
            throws IOException {
        // store access token
        Dribbble.accessToken = accessToken;
        Log.i("TOKEN", accessToken);
        storeAccessToken(context, accessToken);

        // get and store user info
        try {
            Dribbble.user = new FetchUserInfo().execute().get();
            storeUser(context, user);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
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
      AsyncTask GET request user info and store in SP
     ---------------------------------------------------------------------------------------------*/

    private static class FetchUserInfo extends AsyncTask<Void, Void, User> {

        @Override
        protected User doInBackground(Void... voids) {
            Request resquest = authRequestBuilder();

            try {
                Response response = client.newCall(resquest).execute();
                Log.i("response", response.toString());
                return parseResponse(response, USER_TYPE);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return new User();
        }
    }

    private static Request authRequestBuilder() {
        return new Request.Builder().addHeader("Authorization", "Bearer " + accessToken)
                .url(USER_END_POINT).build();
    }

    private static <T> T parseResponse(Response response, TypeToken<T> typeToken)
            throws IOException, JsonSyntaxException{
        String responseString = response.body().string();
        Log.i("user_str", responseString);
        return ModelUtils.toObject(responseString, typeToken);
    }
}
