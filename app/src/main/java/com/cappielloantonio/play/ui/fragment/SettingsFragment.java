package com.cappielloantonio.play.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.cappielloantonio.play.R;
import com.cappielloantonio.play.helper.ThemeHelper;
import com.cappielloantonio.play.ui.activities.MainActivity;
import com.cappielloantonio.play.util.PreferenceUtil;
import com.cappielloantonio.play.util.SyncUtil;

public class SettingsFragment extends PreferenceFragmentCompat {
    private static final String TAG = "SettingsFragment";

    private MainActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();

        View view = super.onCreateView(inflater, container, savedInstanceState);

        if(view != null) {
            getListView().setPadding(0, 0, 0, (int) getResources().getDimension(R.dimen.global_padding_bottom));
        }

        return view;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.global_preferences, rootKey);

        ListPreference themePreference = findPreference("themePref");
        if (themePreference != null) {
            themePreference.setOnPreferenceChangeListener(
                    (preference, newValue) -> {
                        String themeOption = (String) newValue;
                        ThemeHelper.applyTheme(themeOption);
                        return true;
                    });
        }

        Preference music_sync_button = findPreference(getString(R.string.music_sync));
        music_sync_button.setOnPreferenceClickListener(preference -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setMessage("Force reload your entire music library")
                    .setTitle("Force sync")
                    .setNegativeButton(R.string.ignore, null)
                    .setPositiveButton("Sync", (dialog, id) -> {
                        PreferenceUtil.getInstance(requireContext()).setSync(false);
                        PreferenceUtil.getInstance(requireContext()).setSongGenreSync(false);

                        Bundle bundle = SyncUtil.getSyncBundle(true, true, true, true, true, false);
                        activity.goFromSettingsToSync(bundle);
                    })
                    .show();
            return true;
        });

        Preference cross_sync_button = findPreference(getString(R.string.genres_music_cross_sync));
        cross_sync_button.setOnPreferenceClickListener(preference -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setMessage("Sync song's genres otherwise nothing will be shown in each genre category")
                    .setTitle("Song's genres not synchronized")
                    .setNegativeButton(R.string.ignore, null)
                    .setPositiveButton("Sync", (dialog, id) -> {
                        Bundle bundle = SyncUtil.getSyncBundle(false, false, true, false, false, true);
                        activity.goFromSettingsToSync(bundle);
                    })
                    .show();
            return true;
        });
    }

}
