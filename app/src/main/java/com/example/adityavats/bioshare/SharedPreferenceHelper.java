package com.example.adityavats.bioshare;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

/**
 * Created by Aditya Vats on 3/2/2017.
 */

public class SharedPreferenceHelper {

    private final static String PREF_FILE = "PREF";
    private final static String PREF_FILE2 = "PREF";

    public static void setSharedPreferenceString(Context context, String key, String value){
        SharedPreferences settings = context.getSharedPreferences(PREF_FILE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
//        editor.clear();
        editor.apply();

    }

   public static String getSharedPreferenceString(Context context, String key, String defValue){
        SharedPreferences settings = context.getSharedPreferences(PREF_FILE, 0);
        return settings.getString(key, defValue);
    }
}
