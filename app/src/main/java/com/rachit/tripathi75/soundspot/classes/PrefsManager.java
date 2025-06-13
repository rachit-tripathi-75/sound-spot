package com.rachit.tripathi75.soundspot.classes;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.rachit.tripathi75.soundspot.model.UserDetails;

public class PrefsManager {
    private static final String PREF_NAME = "MyPrefs";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public PrefsManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static void setSession(Context context, boolean flag) {
        SharedPreferences.Editor editor = context.getSharedPreferences("userSessionPrefs", MODE_PRIVATE).edit();
        editor.putBoolean("isUserLoggedIn", flag);
        editor.apply();
    }

    public static boolean getSession(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("userSessionPrefs", MODE_PRIVATE);
        return sharedPreferences.getBoolean("isUserLoggedIn", false);
    }

    public static void setUserDetails(Context context, UserDetails userDetails) {
        SharedPreferences.Editor editor = context.getSharedPreferences("userInformationPrefs", MODE_PRIVATE).edit();
        Gson gson = new Gson();
        String json = gson.toJson(userDetails);
        editor.putString("userInformation", json);
        editor.apply();
    }

    public static UserDetails getUserDetails(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("userInformationPrefs", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("userInformation", null);
        if (json != null) {
            return gson.fromJson(json, UserDetails.class);
        }
        return null;
    }

    /*
     *       0 --> for guest user
     *       1 --> if signed in with email and password
     *       2 --> if signed in with google
     *       3 --> if signed in with facebook
     *       4 --> if signed in with apple
     *      -1 --> other case.......
     * */
    public static void setLoginInType(Context context, int type) {
        SharedPreferences.Editor editor = context.getSharedPreferences("userLoginTypePrefs", MODE_PRIVATE).edit();
        editor.putInt("loginType", type);
        editor.apply();
    }

    public static int getLogInType(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("userLoginTypePrefs", MODE_PRIVATE);
        return sharedPreferences.getInt("isUserLoggedIn", -1);
    }


}
