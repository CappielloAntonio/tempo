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

@OptIn(markerClass = UnstableApi.class)
public class BatteryOptimizationDialog extends DialogFragment {
    private static final String TAG = "BatteryOptimizationDialog";

    private DialogBatteryOptimizationBinding bind;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        bind = DialogBatteryOptimizationBinding.inflate(getLayoutInflater());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setView(bind.getRoot())
                .setTitle(R.string.activity_battery_optimizations_title)
                .setNeutralButton(R.string.battery_optimization_neutral_button, (dialog, id) -> Preferences.dontAskForOptimization())
                .setNegativeButton(R.string.battery_optimization_negative_button, null)
                .setPositiveButton(R.string.battery_optimization_positive_button, (dialog, id) -> openPowerSettings());

        AlertDialog popup = builder.create();

        popup.setCancelable(false);
        popup.setCanceledOnTouchOutside(false);

        return popup;
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
