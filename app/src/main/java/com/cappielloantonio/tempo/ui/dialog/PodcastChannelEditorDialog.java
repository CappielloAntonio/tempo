package com.cappielloantonio.tempo.ui.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.cappielloantonio.tempo.R;
import com.cappielloantonio.tempo.databinding.DialogPodcastChannelEditorBinding;
import com.cappielloantonio.tempo.interfaces.PodcastCallback;
import com.cappielloantonio.tempo.viewmodel.PodcastChannelEditorViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Objects;

public class PodcastChannelEditorDialog extends DialogFragment {
    private DialogPodcastChannelEditorBinding bind;
    private PodcastChannelEditorViewModel podcastChannelEditorViewModel;
    private PodcastCallback podcastCallback;

    private String channelUrl;

    public PodcastChannelEditorDialog(PodcastCallback podcastCallback) {
        this.podcastCallback = podcastCallback;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        bind = DialogPodcastChannelEditorBinding.inflate(getLayoutInflater());

        podcastChannelEditorViewModel = new ViewModelProvider(requireActivity()).get(PodcastChannelEditorViewModel.class);

        return new MaterialAlertDialogBuilder(getActivity())
                .setView(bind.getRoot())
                .setTitle(R.string.podcast_channel_editor_dialog_title)
                .setPositiveButton(R.string.radio_editor_dialog_positive_button, (dialog, id) -> { })
                .setNegativeButton(R.string.radio_editor_dialog_negative_button, (dialog, id) -> dialog.cancel())
                .create();
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
        androidx.appcompat.app.AlertDialog dialog = (androidx.appcompat.app.AlertDialog) getDialog();
        if (dialog != null) {
            Button positiveButton = dialog.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(v -> {
                if (validateInput()) {
                    podcastChannelEditorViewModel.createChannel(channelUrl);
                    dismissDialog();
                }
            });
        }
    }


    private boolean validateInput() {
        channelUrl = Objects.requireNonNull(bind.podcastChannelRssUrlNameTextView.getText()).toString().trim();

        if (TextUtils.isEmpty(channelUrl)) {
            bind.podcastChannelRssUrlNameTextView.setError(getString(R.string.error_required));
            return false;
        }

        return true;
    }

    private void dismissDialog() {
        podcastCallback.onDismiss();
        Objects.requireNonNull(getDialog()).dismiss();
    }
}
