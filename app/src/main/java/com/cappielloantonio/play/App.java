package com.cappielloantonio.play;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.balsikandar.crashreporter.CrashReporter;
import com.cappielloantonio.play.helper.ThemeHelper;
import com.cappielloantonio.play.subsonic.Subsonic;
import com.cappielloantonio.play.subsonic.SubsonicPreferences;
import com.cappielloantonio.play.util.PreferenceUtil;

public class App extends Application {
    private static final String TAG = "App";
    private static App instance;
    private static Subsonic subsonic;

    @Override
    public void onCreate() {
        super.onCreate();
        // RedScreenOfDeath.init(this);
        CrashReporter.initialize(this);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String themePref = sharedPreferences.getString("themePref", ThemeHelper.DEFAULT_MODE);
        ThemeHelper.applyTheme(themePref);
    }

    public static App getInstance() {
        if (instance == null) {
            instance = new App();
        }
        return instance;
    }

    public static Subsonic getSubsonicClientInstance(Context context, boolean override) {
        if (subsonic == null || override) {
            subsonic = getSubsonicClient(context);
        }
        return subsonic;
    }

    private static Subsonic getSubsonicClient(Context context) {
        String server = PreferenceUtil.getInstance(context).getServer();
        String username = PreferenceUtil.getInstance(context).getUser();
        String password = PreferenceUtil.getInstance(context).getPassword();
        String token = PreferenceUtil.getInstance(context).getToken();
        String salt = PreferenceUtil.getInstance(context).getSalt();

        SubsonicPreferences preferences = new SubsonicPreferences();
        preferences.setServerUrl(server);
        preferences.setUsername(username);
        preferences.setAuthentication(password, token, salt);

        return new Subsonic(context, preferences);
    }
}
