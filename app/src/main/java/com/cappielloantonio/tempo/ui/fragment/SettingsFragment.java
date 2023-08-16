package com.cappielloantonio.tempo.ui.fragment;

import android.content.Intent;
import android.media.audiofx.AudioEffect;
import android.os.Bundle;
import android.os.LocaleList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;
import androidx.media3.common.util.UnstableApi;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.cappielloantonio.tempo.BuildConfig;
import com.cappielloantonio.tempo.R;
import com.cappielloantonio.tempo.helper.ThemeHelper;
import com.cappielloantonio.tempo.interfaces.DialogClickCallback;
import com.cappielloantonio.tempo.interfaces.ScanCallback;
import com.cappielloantonio.tempo.ui.activity.MainActivity;
import com.cappielloantonio.tempo.ui.dialog.DeleteDownloadStorageDialog;
import com.cappielloantonio.tempo.ui.dialog.DownloadStorageDialog;
import com.cappielloantonio.tempo.ui.dialog.StarredSyncDialog;
import com.cappielloantonio.tempo.util.Preferences;
import com.cappielloantonio.tempo.viewmodel.SettingViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@OptIn(markerClass = UnstableApi.class)
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
        activity.setBottomSheetVisibility(false);
    }

    @Override
    public void onResume() {
        super.onResume();

        checkEqualizer();

        checkStorage();

        findPreference("version").setSummary(BuildConfig.VERSION_NAME);

        findPreference("logout").setOnPreferenceClickListener(preference -> {
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
                    getScanStatus();
                }
            });

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

        findPreference("download_storage").setOnPreferenceClickListener(preference -> {
            DownloadStorageDialog dialog = new DownloadStorageDialog(new DialogClickCallback() {
                @Override
                public void onPositiveClick() {
                    findPreference("download_storage").setSummary(R.string.download_storage_external_dialog_positive_button);
                }

                @Override
                public void onNegativeClick() {
                    findPreference("download_storage").setSummary(R.string.download_storage_internal_dialog_negative_button);
                }
            });
            dialog.show(activity.getSupportFragmentManager(), null);
            return true;
        });

        findPreference("delete_download_storage").setOnPreferenceClickListener(preference -> {
            DeleteDownloadStorageDialog dialog = new DeleteDownloadStorageDialog();
            dialog.show(activity.getSupportFragmentManager(), null);
            return true;
        });

        ListPreference localePref = (ListPreference) findPreference("locale");
        assert localePref != null;
        LocaleList localeList = getResources().getConfiguration().getLocales();
        List<String> languageTags = new ArrayList<>(Arrays.asList(localeList.toLanguageTags().split(",")));
        localePref.setEntries(languageTags.stream().map(Locale::forLanguageTag).map(Locale::getDisplayLanguage).toArray(CharSequence[]::new));
        localePref.setEntryValues(languageTags.toArray(new CharSequence[0]));
        localePref.setDefaultValue(getResources().getConfiguration().getLocales().get(0).toLanguageTag());
        localePref.setValue(getResources().getConfiguration().getLocales().get(0).toLanguageTag());
        localePref.setSummary(Locale.forLanguageTag(localePref.getValue()).getDisplayLanguage());
        localePref.setOnPreferenceChangeListener((preference, newValue) -> {
            Locale newLocale = Locale.forLanguageTag(newValue.toString());
            preference.setSummary(getResources().getString(R.string.active_locale_setting, newLocale.getDisplayLanguage()));
            return true;
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        activity.setBottomSheetVisibility(true);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.global_preferences, rootKey);
        ListPreference themePreference = findPreference(Preferences.THEME);
        if (themePreference != null) {
            themePreference.setOnPreferenceChangeListener(
                    (preference, newValue) -> {
                        String themeOption = (String) newValue;
                        ThemeHelper.applyTheme(themeOption);
                        return true;
                    });
        }
    }

    private void checkEqualizer() {
        Preference equalizer = findPreference("equalizer");

        if (equalizer == null) return;

        Intent intent = new Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL);

        if ((intent.resolveActivity(requireActivity().getPackageManager()) != null)) {
            equalizer.setOnPreferenceClickListener(preference -> {
                someActivityResultLauncher.launch(intent);
                return true;
            });
        } else {
            equalizer.setVisible(false);
        }
    }

    private void checkStorage() {
        Preference storage = findPreference("download_storage");

        if (storage == null) return;

        try {
            if (requireContext().getExternalFilesDirs(null)[1] == null) {
                storage.setVisible(false);
            } else {
                storage.setSummary(Preferences.getDownloadStoragePreference() == 0 ? R.string.download_storage_internal_dialog_negative_button : R.string.download_storage_external_dialog_positive_button);
            }
        } catch (Exception exception) {
            storage.setVisible(false);
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
