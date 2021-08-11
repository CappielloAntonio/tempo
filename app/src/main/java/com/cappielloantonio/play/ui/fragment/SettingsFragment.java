package com.cappielloantonio.play.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.cappielloantonio.play.App;
import com.cappielloantonio.play.R;
import com.cappielloantonio.play.helper.ThemeHelper;
import com.cappielloantonio.play.interfaces.ScanCallback;
import com.cappielloantonio.play.repository.QueueRepository;
import com.cappielloantonio.play.ui.activity.MainActivity;
import com.cappielloantonio.play.util.PreferenceUtil;
import com.cappielloantonio.play.viewmodel.LibraryViewModel;
import com.cappielloantonio.play.viewmodel.SettingViewModel;

public class SettingsFragment extends PreferenceFragmentCompat {
    private static final String TAG = "SettingsFragment";

    private MainActivity activity;
    private SettingViewModel settingViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
                    Toast.makeText(requireContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(boolean isScanning, long count) {
                    Toast.makeText(requireContext(), "Scanning: counting " + count + " elements", Toast.LENGTH_SHORT).show();
                }
            });
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
}
