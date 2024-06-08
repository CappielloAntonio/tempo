package com.cappielloantonio.tempo.ui.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.fragment.app.DialogFragment;
import androidx.media3.common.util.UnstableApi;

import com.cappielloantonio.tempo.App;
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

    @SuppressLint("BatteryLife")
    private void openPowerSettings() {
        try {
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:"+App.getContext().getPackageName()));
            startActivity(intent);
        }catch (ActivityNotFoundException e){
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
    }
}
