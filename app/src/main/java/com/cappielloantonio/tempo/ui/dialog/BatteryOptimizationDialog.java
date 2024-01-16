package com.cappielloantonio.tempo.ui.dialog;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.fragment.app.DialogFragment;
import androidx.media3.common.util.UnstableApi;

import com.cappielloantonio.tempo.R;
import com.cappielloantonio.tempo.databinding.DialogBatteryOptimizationBinding;
import com.cappielloantonio.tempo.util.Preferences;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

@OptIn(markerClass = UnstableApi.class)
public class BatteryOptimizationDialog extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        DialogBatteryOptimizationBinding bind = DialogBatteryOptimizationBinding.inflate(getLayoutInflater());

        return new MaterialAlertDialogBuilder(requireContext())
                .setView(bind.getRoot())
                .setTitle(R.string.activity_battery_optimizations_title)
                .setPositiveButton(R.string.battery_optimization_positive_button, (dialog, listener) -> openPowerSettings())
                .setNeutralButton(R.string.battery_optimization_neutral_button, (dialog, listener) -> Preferences.dontAskForOptimization())
                .setNegativeButton(R.string.battery_optimization_negative_button, null)
                .create();
    }

    private void openPowerSettings() {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
        startActivity(intent);
    }
}
