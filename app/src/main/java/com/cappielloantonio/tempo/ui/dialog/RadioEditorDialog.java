package com.cappielloantonio.tempo.ui.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.cappielloantonio.tempo.R;
import com.cappielloantonio.tempo.databinding.DialogRadioEditorBinding;
import com.cappielloantonio.tempo.interfaces.RadioCallback;
import com.cappielloantonio.tempo.subsonic.models.InternetRadioStation;
import com.cappielloantonio.tempo.util.Constants;
import com.cappielloantonio.tempo.viewmodel.RadioEditorViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Objects;

public class RadioEditorDialog extends DialogFragment {
    private DialogRadioEditorBinding bind;
    private RadioEditorViewModel radioEditorViewModel;
    private RadioCallback radioCallback;

    private String radioName;
    private String radioStreamURL;
    private String radioHomepageURL;

    public RadioEditorDialog(RadioCallback radioCallback) {
        this.radioCallback = radioCallback;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        bind = DialogRadioEditorBinding.inflate(getLayoutInflater());

        radioEditorViewModel = new ViewModelProvider(requireActivity()).get(RadioEditorViewModel.class);

        return new MaterialAlertDialogBuilder(requireContext())
                .setView(bind.getRoot())
                .setTitle(R.string.radio_editor_dialog_title)
                .setPositiveButton(R.string.radio_editor_dialog_positive_button, (dialog, id) -> {
                    if (validateInput()) {
                        if (radioEditorViewModel.getRadioToEdit() == null) {
                            radioEditorViewModel.createRadio(radioName, radioStreamURL, radioHomepageURL.isEmpty() ? null : radioHomepageURL);
                        } else {
                            radioEditorViewModel.updateRadio(radioName, radioStreamURL, radioHomepageURL.isEmpty() ? null : radioHomepageURL);
                        }
                        dismissDialog();
                    }
                })
                .setNeutralButton(R.string.radio_editor_dialog_neutral_button, (dialog, id) -> {
                    radioEditorViewModel.deleteRadio();
                    dismissDialog();
                })
                .setNegativeButton(R.string.radio_editor_dialog_negative_button, (dialog, id) -> {
                    dialog.cancel();
                })
                .create();
    }

    @Override
    public void onStart() {
        super.onStart();
        setParameterInfo();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind = null;
    }

    private void setParameterInfo() {
        if (getArguments() != null && getArguments().getParcelable(Constants.INTERNET_RADIO_STATION_OBJECT) != null) {
            InternetRadioStation toEdit = requireArguments().getParcelable(Constants.INTERNET_RADIO_STATION_OBJECT);

            radioEditorViewModel.setRadioToEdit(toEdit);

            bind.internetRadioStationNameTextView.setText(toEdit.getName());
            bind.internetRadioStationStreamUrlTextView.setText(toEdit.getStreamUrl());
            bind.internetRadioStationHomepageUrlTextView.setText(toEdit.getHomePageUrl());
        }
    }

    private boolean validateInput() {
        radioName = Objects.requireNonNull(bind.internetRadioStationNameTextView.getText()).toString().trim();
        radioStreamURL = Objects.requireNonNull(bind.internetRadioStationStreamUrlTextView.getText()).toString().trim();
        radioHomepageURL = Objects.requireNonNull(bind.internetRadioStationHomepageUrlTextView.getText()).toString().trim();

        if (TextUtils.isEmpty(radioName)) {
            bind.internetRadioStationNameTextView.setError(getString(R.string.error_required));
            return false;
        }

        if (TextUtils.isEmpty(radioStreamURL)) {
            bind.internetRadioStationStreamUrlTextView.setError(getString(R.string.error_required));
            return false;
        }

        return true;
    }

    private void dismissDialog() {
        radioCallback.onDismiss();
        Objects.requireNonNull(getDialog()).dismiss();
    }
}
