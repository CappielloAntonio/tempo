package com.cappielloantonio.play.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.cappielloantonio.play.R;
import com.cappielloantonio.play.databinding.DialogConnectionAlertBinding;
import com.cappielloantonio.play.util.PreferenceUtil;

import java.util.Objects;

public class ConnectionAlertDialog extends DialogFragment {
    private static final String TAG = "ServerUnreachableDialog";

    private DialogConnectionAlertBinding bind;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        bind = DialogConnectionAlertBinding.inflate(LayoutInflater.from(requireContext()));

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppTheme_AlertDialog);

        builder.setView(bind.getRoot())
                .setTitle(R.string.connection_alert_dialog_title)
                .setPositiveButton(R.string.connection_alert_dialog_positive_button, (dialog, id) -> dialog.cancel())
                .setNegativeButton(R.string.connection_alert_dialog_negative_button, (dialog, id) -> dialog.cancel());

        if(!PreferenceUtil.getInstance(requireContext()).isDataSavingMode()) {
            builder.setNeutralButton(R.string.connection_alert_dialog_neutral_button, (dialog, id) -> { });
        }

        return builder.create();
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
        ((AlertDialog) Objects.requireNonNull(getDialog())).getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(getResources().getColor(R.color.colorAccent, null));
        ((AlertDialog) Objects.requireNonNull(getDialog())).getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorAccent, null));
        ((AlertDialog) Objects.requireNonNull(getDialog())).getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorAccent, null));

        ((AlertDialog) Objects.requireNonNull(getDialog())).getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(v -> {
            PreferenceUtil.getInstance(requireContext()).setDataSavingMode(true);
            Objects.requireNonNull(getDialog()).dismiss();
        });
    }
}
