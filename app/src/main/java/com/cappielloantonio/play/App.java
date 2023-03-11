package com.cappielloantonio.play;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.cappielloantonio.play.helper.ThemeHelper;
import com.cappielloantonio.play.subsonic.Subsonic;
import com.cappielloantonio.play.subsonic.SubsonicPreferences;
import com.cappielloantonio.play.util.Preferences;
import com.google.android.material.color.DynamicColors;

public class App extends Application {
    private static App instance;
    private static Context context;
    private static Subsonic subsonic;
    private static SharedPreferences preferences;

    @Override
    public void onCreate() {
        super.onCreate();

        DynamicColors.applyToActivitiesIfAvailable(this);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String themePref = sharedPreferences.getString(Preferences.THEME, ThemeHelper.DEFAULT_MODE);
        ThemeHelper.applyTheme(themePref);

        instance = new App();
        context = getApplicationContext();
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static App getInstance() {
        if (instance == null) {
            instance = new App();
        }

        return instance;
    }

    public static Context getContext() {
        if (context == null) {
            context = getInstance();
        }

        return context;
    }

    public static Subsonic getSubsonicClientInstance(boolean override) {
        if (subsonic == null || override) {
            subsonic = getSubsonicClient();
        }
        return subsonic;
    }

    public SharedPreferences getPreferences() {
        if (preferences == null) {
            preferences = PreferenceManager.getDefaultSharedPreferences(context);
        }

        return preferences;
    }

    private static Subsonic getSubsonicClient() {
        String server = Preferences.getServer();
        String username = Preferences.getUser();
        String password = Preferences.getPassword();
        String token = Preferences.getToken();
        String salt = Preferences.getSalt();
        boolean isLowSecurity = Preferences.isLowScurity();

        SubsonicPreferences preferences = new SubsonicPreferences();
        preferences.setServerUrl(server);
        preferences.setUsername(username);
        preferences.setAuthentication(password, token, salt, isLowSecurity);

        if (preferences.getAuthentication() != null) {
            if (preferences.getAuthentication().getPassword() != null)
                Preferences.setPassword(preferences.getAuthentication().getPassword());
            if (preferences.getAuthentication().getToken() != null)
                Preferences.setToken(preferences.getAuthentication().getToken());
            if (preferences.getAuthentication().getSalt() != null)
                Preferences.setSalt(preferences.getAuthentication().getSalt());
        }

        return new Subsonic(preferences);
    }
}
