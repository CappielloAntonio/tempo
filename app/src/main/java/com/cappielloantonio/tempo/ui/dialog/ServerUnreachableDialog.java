package com.cappielloantonio.tempo.ui.dialog;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.media3.common.util.UnstableApi;

import com.cappielloantonio.tempo.R;
import com.cappielloantonio.tempo.databinding.DialogServerUnreachableBinding;
import com.cappielloantonio.tempo.ui.activity.MainActivity;
import com.cappielloantonio.tempo.util.Preferences;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Objects;

@OptIn(markerClass = UnstableApi.class)
public class ServerUnreachableDialog extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        DialogServerUnreachableBinding bind = DialogServerUnreachableBinding.inflate(getLayoutInflater());

        AlertDialog popup = new MaterialAlertDialogBuilder(getActivity()).setView(bind.getRoot())
                .setTitle(R.string.server_unreachable_dialog_title)
                .setPositiveButton(R.string.server_unreachable_dialog_positive_button, null)
                .setNeutralButton(R.string.server_unreachable_dialog_neutral_button, null)
                .setNegativeButton(R.string.server_unreachable_dialog_negative_button, (dialog, id) -> dialog.cancel())
                .create();

        popup.setCanceledOnTouchOutside(false);
        popup.setCancelable(false);

        return popup;
    }


    @Override
    public void onStart() {
        super.onStart();

        setButtonAction();
    }

    private void setButtonAction() {
        androidx.appcompat.app.AlertDialog alertDialog = (androidx.appcompat.app.AlertDialog) Objects.requireNonNull(getDialog());

        alertDialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEUTRAL).setOnClickListener(v -> {
            MainActivity activity = (MainActivity) getActivity();
            if (activity != null) activity.quit();
            alertDialog.dismiss();
        });

        alertDialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            Preferences.setServerUnreachableDatetime(System.currentTimeMillis());
            alertDialog.dismiss();
        });
    }
}
