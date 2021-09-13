package com.cappielloantonio.play.ui.fragment;

import android.content.Intent;
import android.media.audiofx.AudioEffect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;

import com.cappielloantonio.play.R;
import com.cappielloantonio.play.helper.ThemeHelper;
import com.cappielloantonio.play.interfaces.ScanCallback;
import com.cappielloantonio.play.ui.activity.MainActivity;
import com.cappielloantonio.play.ui.fragment.dialog.StarredSyncDialog;
import com.cappielloantonio.play.util.PreferenceUtil;
import com.cappielloantonio.play.viewmodel.SettingViewModel;

public class SettingsFragment extends PreferenceFragmentCompat {
    private static final String TAG = "SettingsFragment";

    private MainActivity activity;
    private SettingViewModel settingViewModel;

    private ActivityResultLauncher<Intent> someActivityResultLauncher;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();

        View view = super.onCreateView(inflater, container, savedInstanceState);
        settingViewModel = new ViewModelProvider(requireActivity()).get(SettingViewModel.class);

        if (view != null) {
            getListView().setPadding(0, 0, 0, (int) getResources().getDimension(R.dimen.global_padding_bottom));
        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        activity.setBottomNavigationBarVisibility(false);
    }

    @Override
    public void onResume() {
        super.onResume();

        findPreference("logout").setOnPreferenceClickListener(preference -> {
            PreferenceUtil.getInstance(requireContext()).setUser(null);
            PreferenceUtil.getInstance(requireContext()).setServer(null);
            PreferenceUtil.getInstance(requireContext()).setPassword(null);
            PreferenceUtil.getInstance(requireContext()).setToken(null);
            PreferenceUtil.getInstance(requireContext()).setSalt(null);
            PreferenceUtil.getInstance(requireContext()).setServerId(null);

            activity.quit();

            return true;
        });

        findPreference("scan_library").setOnPreferenceClickListener(preference -> {
            settingViewModel.launchScan(new ScanCallback() {
                @Override
                public void onError(Exception exception) {
                    findPreference("scan_library").setSummary(exception.getMessage());
                }

                @Override
                public void onSuccess(boolean isScanning, long count) {
                    if (isScanning) getScanStatus();
                    else findPreference("scan_library").setSummary("Scanning: counting " + count + " tracks");
                }
            });

            return true;
        });

        findPreference("equalizer").setOnPreferenceClickListener(preference -> {
            Intent intent = new Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL);
            someActivityResultLauncher.launch(intent);
            return true;
        });

        findPreference("sync_starred_tracks_for_offline_use").setOnPreferenceChangeListener((preference, newValue) -> {
            if (newValue instanceof Boolean) {
                if ((Boolean) newValue) {
                    StarredSyncDialog dialog = new StarredSyncDialog();
                    dialog.show(activity.getSupportFragmentManager(), null);
                }
            }
            return true;
        });
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
    }

    private void getScanStatus() {
        settingViewModel.getScanStatus(new ScanCallback() {
            @Override
            public void onError(Exception exception) {
                findPreference("scan_library").setSummary(exception.getMessage());
            }

            @Override
            public void onSuccess(boolean isScanning, long count) {
                findPreference("scan_library").setSummary("Scanning: counting " + count + " tracks");
                if (isScanning) getScanStatus();
            }
        });
    }
}
