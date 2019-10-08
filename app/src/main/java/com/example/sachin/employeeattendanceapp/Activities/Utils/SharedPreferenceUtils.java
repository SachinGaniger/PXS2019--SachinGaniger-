package com.example.sachin.employeeattendanceapp.Activities.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceUtils {

    Context context;

    public SharedPreferenceUtils(Context _context){
        this.context = _context;
    }

    public void putString(String key, String value){
        SharedPreferences sharedPreferences = context.getSharedPreferences("myPref", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getString(String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences("myPref", 0);
        return sharedPreferences.getString(key, null);
    }

}
