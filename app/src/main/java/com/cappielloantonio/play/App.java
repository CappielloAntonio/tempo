package com.cappielloantonio.play;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

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

        SubsonicPreferences preferences = new SubsonicPreferences(server, username, password);

        return new Subsonic(preferences);
    }
}
