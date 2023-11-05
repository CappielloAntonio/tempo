package com.cappielloantonio.tempo.ui.dialog;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.cappielloantonio.tempo.R;
import com.cappielloantonio.tempo.databinding.DialogConnectionAlertBinding;
import com.cappielloantonio.tempo.util.Preferences;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Objects;

public class ConnectionAlertDialog extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        DialogConnectionAlertBinding bind = DialogConnectionAlertBinding.inflate(getLayoutInflater());

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity())
                .setView(bind.getRoot())
                .setTitle(R.string.connection_alert_dialog_title)
                .setPositiveButton(R.string.connection_alert_dialog_positive_button, (dialog, id) -> dialog.cancel())
                .setNegativeButton(R.string.connection_alert_dialog_negative_button, (dialog, id) -> dialog.cancel());

        if (!Preferences.isDataSavingMode()) {
            builder.setNeutralButton(R.string.connection_alert_dialog_neutral_button, (dialog, id) -> {
            });
        }

        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();

        setButtonAction();
    }

    private void setButtonAction() {
        androidx.appcompat.app.AlertDialog alertDialog = (androidx.appcompat.app.AlertDialog) Objects.requireNonNull(getDialog());

        alertDialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEUTRAL).setOnClickListener(v -> {
            Preferences.setDataSavingMode(true);
            Objects.requireNonNull(getDialog()).dismiss();
        });
    }
}