package com.example.miguelsoler.mipedidos.Configs;

/****************************************************************************************
 * CREDITOS:__________________________________________________________
 * |  * | * | 01 |  29/7/2017          |  Shadowns                  | @Miguelslr
 * |__________________________________________________________________
 *******************************************************************************************/
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


import com.example.miguelsoler.mipedidos.Activity.LoginActivity;

import java.util.HashMap;

public class SessionManager {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "EnergesApp";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "name";
    public static final String KEY_SURNAME = "surname";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_CELPHONE = "celphone";
    public static final String KEY_IMEI = "imei";
    public static final String KEY_DEVICE = "devicename";
    public static final String KEY_CODE = "code";


    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String code,String name, String surname, String email, String cel, String imei, String device){
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_CODE, code);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_SURNAME, surname);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_CELPHONE, cel);
        editor.putString(KEY_IMEI, imei);
        editor.putString(KEY_DEVICE, device);
        editor.commit();
    }


    public void checkLogin(){
        if(!this.isLoggedIn()){
            Intent i = new Intent(_context, LoginActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }

    }


    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(KEY_CODE, pref.getString(KEY_CODE, null));
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));
        user.put(KEY_SURNAME, pref.getString(KEY_SURNAME, null));
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        user.put(KEY_CELPHONE, pref.getString(KEY_CELPHONE, null));
        user.put(KEY_IMEI, pref.getString(KEY_IMEI, null));
        user.put(KEY_DEVICE, pref.getString(KEY_DEVICE, null));

        // return user
        return user;
    }

    public void logoutUser(){
        editor.clear();
        editor.commit();
        Intent i = new Intent(_context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);
    }
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
}
