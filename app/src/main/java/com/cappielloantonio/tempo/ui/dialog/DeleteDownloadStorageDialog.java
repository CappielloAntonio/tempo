package com.cappielloantonio.tempo.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.fragment.app.DialogFragment;
import androidx.media3.common.util.UnstableApi;

import com.cappielloantonio.tempo.R;
import com.cappielloantonio.tempo.databinding.DialogDeleteDownloadStorageBinding;
import com.cappielloantonio.tempo.interfaces.DialogClickCallback;
import com.cappielloantonio.tempo.util.DownloadUtil;

@OptIn(markerClass = UnstableApi.class)
public class DeleteDownloadStorageDialog extends DialogFragment {
    private DialogDeleteDownloadStorageBinding bind;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        bind = DialogDeleteDownloadStorageBinding.inflate(getLayoutInflater());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setView(bind.getRoot())
                .setTitle(R.string.delete_download_storage_dialog_title)
                .setPositiveButton(R.string.delete_download_storage_dialog_positive_button, null)
                .setNegativeButton(R.string.delete_download_storage_dialog_negative_button, null);

        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        setButtonAction();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind = null;
    }

    private void setButtonAction() {
        AlertDialog dialog = ((AlertDialog) getDialog());

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
