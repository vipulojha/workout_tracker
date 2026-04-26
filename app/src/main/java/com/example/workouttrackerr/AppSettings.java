package com.example.workouttrackerr;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatDelegate;

public class AppSettings {
    public static final String THEME_LIGHT = "Light";
    public static final String THEME_DARK = "Dark";

    private static final String PREFS_NAME = "workout_tracker_settings";
    private static final String KEY_THEME = "theme";

    private final SharedPreferences prefs;

    public AppSettings(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void applyTheme() {
        AppCompatDelegate.setDefaultNightMode(
                THEME_LIGHT.equals(getTheme())
                        ? AppCompatDelegate.MODE_NIGHT_NO
                        : AppCompatDelegate.MODE_NIGHT_YES
        );
    }

    public String getTheme() {
        return prefs.getString(KEY_THEME, THEME_DARK);
    }

    public void setTheme(String theme) {
        prefs.edit().putString(KEY_THEME, theme).apply();
        applyTheme();
    }

    public void clear() {
        prefs.edit().clear().apply();
    }
}
