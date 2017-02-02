package com.digital.ayaz.storage;

import android.content.Context;
import android.content.SharedPreferences;

public class Preference {


    private static final String DRAWER_HINT = "drawer_hint";
    private static Preference instance = null;
    private final String FILENAME = "guide_help_preference";
    private SharedPreferences sharedPrefrence;

    private Preference(Context context) {
        sharedPrefrence = context.getApplicationContext().getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
    }

    public static Preference getInstance(Context context) {
        if (instance == null) {
            instance = new Preference(context);
        }
        return instance;
    }

    public boolean isDrawableHintShown() {
        return sharedPrefrence.getBoolean(DRAWER_HINT, false);
    }

    public void setDrawableHintShown(boolean isCompleted) {
        sharedPrefrence.edit().putBoolean(DRAWER_HINT, isCompleted).apply();
    }

}