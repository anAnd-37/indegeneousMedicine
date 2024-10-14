package com.example.indegenousmedicine2;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;

import java.util.Locale;

public class LanguageManager {
    private static final String LANGUAGE_PREF_KEY = "languagePref";
    private static final String DEFAULT_LANGUAGE = "en"; // Default language is English

    public static void setLanguage(Context context, String languageCode) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(LANGUAGE_PREF_KEY, languageCode);
        editor.apply();
    }

    public static String getLanguage(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(LANGUAGE_PREF_KEY, DEFAULT_LANGUAGE);
    }

    public static void applyLanguage(Context context) {
        String languageCode = getLanguage(context);
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.locale = locale;

        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }
}
