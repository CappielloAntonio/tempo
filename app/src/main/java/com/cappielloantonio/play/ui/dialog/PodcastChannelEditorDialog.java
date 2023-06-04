package com.cappielloantonio.play.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.cappielloantonio.play.R;
import com.cappielloantonio.play.databinding.DialogPodcastChannelEditorBinding;
import com.cappielloantonio.play.interfaces.PodcastCallback;
import com.cappielloantonio.play.viewmodel.PodcastChannelEditorViewModel;

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

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setView(bind.getRoot())
                .setTitle(R.string.podcast_channel_editor_dialog_title)
                .setPositiveButton(R.string.radio_editor_dialog_positive_button, (dialog, id) -> {
                })
                .setNegativeButton(R.string.radio_editor_dialog_negative_button, (dialog, id) -> dialog.cancel());

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
        ((AlertDialog) Objects.requireNonNull(getDialog())).getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            if (validateInput()) {
                podcastChannelEditorViewModel.createChannel(channelUrl);
                dismissDialog();
            }
        });
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
