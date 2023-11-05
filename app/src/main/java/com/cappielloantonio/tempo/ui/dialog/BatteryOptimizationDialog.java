package com.cappielloantonio.tempo.ui.dialog;

import android.app.AlertDialog;
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
    private static final String TAG = "BatteryOptimizationDialog";

    private DialogBatteryOptimizationBinding bind;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        DialogBatteryOptimizationBinding bind = DialogBatteryOptimizationBinding.inflate(getLayoutInflater());

        return new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.activity_battery_optimizations_title)
                .setView(bind.getRoot())
                .setPositiveButton(R.string.battery_optimization_positive_button, (dialog, which) -> openPowerSettings())
                .setNeutralButton(R.string.battery_optimization_neutral_button, (dialog, which) -> Preferences.dontAskForOptimization())
                .setNegativeButton(R.string.battery_optimization_negative_button, null)
                .create();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind = null;
    }

    private void openPowerSettings() {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
        startActivity(intent);
    }
}
