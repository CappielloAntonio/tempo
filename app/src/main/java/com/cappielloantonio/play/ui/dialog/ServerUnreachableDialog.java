package com.cappielloantonio.play.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.cappielloantonio.play.R;
import com.cappielloantonio.play.databinding.DialogServerUnreachableBinding;
import com.cappielloantonio.play.ui.activity.MainActivity;
import com.cappielloantonio.play.util.Preferences;

import java.util.Objects;

public class ServerUnreachableDialog extends DialogFragment {
    private static final String TAG = "ServerUnreachableDialog";

    private DialogServerUnreachableBinding bind;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        bind = DialogServerUnreachableBinding.inflate(LayoutInflater.from(requireContext()));

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setView(bind.getRoot())
                .setTitle(R.string.server_unreachable_dialog_title)
                .setPositiveButton(R.string.server_unreachable_dialog_positive_button, null)
                .setNeutralButton(R.string.server_unreachable_dialog_neutral_button, null)
                .setNegativeButton(R.string.server_unreachable_dialog_negative_button, (dialog, id) -> dialog.cancel());

        AlertDialog popup = builder.create();

        popup.setCancelable(false);
        popup.setCanceledOnTouchOutside(false);

        return popup;
    }

    @Override
    public void onStart() {
        super.onStart();

        setButtonAction();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind = null;
    }

    private void setButtonAction() {
        AlertDialog dialog = (AlertDialog) getDialog();

        if(dialog != null) {
            (dialog).getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(v -> {
                MainActivity activity = (MainActivity) getActivity();
                if (activity != null) activity.quit();
                dialog.dismiss();
            });

            (dialog).getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
                Preferences.setServerUnreachableDatetime(System.currentTimeMillis());
                dialog.dismiss();
            });
        }
    }
}
