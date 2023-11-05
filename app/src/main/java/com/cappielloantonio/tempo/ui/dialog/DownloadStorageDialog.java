package com.cappielloantonio.tempo.ui.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.fragment.app.DialogFragment;
import androidx.media3.common.util.UnstableApi;

import com.cappielloantonio.tempo.R;
import com.cappielloantonio.tempo.databinding.DialogDownloadStorageBinding;
import com.cappielloantonio.tempo.interfaces.DialogClickCallback;
import com.cappielloantonio.tempo.util.DownloadUtil;
import com.cappielloantonio.tempo.util.Preferences;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

@OptIn(markerClass = UnstableApi.class)
public class DownloadStorageDialog extends DialogFragment {
    private final DialogClickCallback dialogClickCallback;

    public DownloadStorageDialog(DialogClickCallback dialogClickCallback) {
        this.dialogClickCallback = dialogClickCallback;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        DialogDownloadStorageBinding bind = DialogDownloadStorageBinding.inflate(getLayoutInflater());

        return new MaterialAlertDialogBuilder(getActivity())
                .setView(bind.getRoot())
                .setTitle(R.string.download_storage_dialog_title)
                .setPositiveButton(R.string.download_storage_external_dialog_positive_button, null)
                .setNegativeButton(R.string.download_storage_internal_dialog_negative_button, null)
                .create();
    }

    @Override
    public void onResume() {
        super.onResume();
        setButtonAction();
    }

    private void setButtonAction() {
        androidx.appcompat.app.AlertDialog dialog = (androidx.appcompat.app.AlertDialog) getDialog();

        if (dialog != null) {
            Button positiveButton = dialog.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(v -> {
                int currentPreference = Preferences.getDownloadStoragePreference();
                int newPreference = 1;

                if (currentPreference != newPreference) {
                    Preferences.setDownloadStoragePreference(newPreference);
                    DownloadUtil.getDownloadTracker(requireContext()).removeAll();
                    dialogClickCallback.onPositiveClick();
                }

                dialog.dismiss();
            });

            Button negativeButton = dialog.getButton(Dialog.BUTTON_NEGATIVE);
            negativeButton.setOnClickListener(v -> {
                int currentPreference = Preferences.getDownloadStoragePreference();
                int newPreference = 0;

                if (currentPreference != newPreference) {
                    Preferences.setDownloadStoragePreference(newPreference);
                    DownloadUtil.getDownloadTracker(requireContext()).removeAll();
                    dialogClickCallback.onNegativeClick();
                }

                dialog.dismiss();
            });
        }
    }
}
