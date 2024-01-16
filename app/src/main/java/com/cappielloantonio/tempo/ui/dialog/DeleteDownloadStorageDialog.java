package com.cappielloantonio.tempo.ui.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.fragment.app.DialogFragment;
import androidx.media3.common.util.UnstableApi;

import com.cappielloantonio.tempo.R;
import com.cappielloantonio.tempo.databinding.DialogDeleteDownloadStorageBinding;
import com.cappielloantonio.tempo.util.DownloadUtil;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

@OptIn(markerClass = UnstableApi.class)
public class DeleteDownloadStorageDialog extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        DialogDeleteDownloadStorageBinding bind = DialogDeleteDownloadStorageBinding.inflate(getLayoutInflater());

        return new MaterialAlertDialogBuilder(requireContext())
                .setView(bind.getRoot())
                .setTitle(R.string.delete_download_storage_dialog_title)
                .setPositiveButton(R.string.delete_download_storage_dialog_positive_button, null)
                .setNegativeButton(R.string.delete_download_storage_dialog_negative_button, null)
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
                DownloadUtil.getDownloadTracker(requireContext()).removeAll();
                dialog.dismiss();
            });

            Button negativeButton = dialog.getButton(Dialog.BUTTON_NEGATIVE);
            negativeButton.setOnClickListener(v -> {
                dialog.dismiss();
            });
        }
    }
}
