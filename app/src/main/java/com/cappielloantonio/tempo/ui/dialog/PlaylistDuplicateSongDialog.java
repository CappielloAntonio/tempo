package com.cappielloantonio.tempo.ui.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.cappielloantonio.tempo.R;
import com.cappielloantonio.tempo.databinding.DialogPlaylistDuplicateSongBinding;
import com.cappielloantonio.tempo.interfaces.DialogClickCallback;
import com.cappielloantonio.tempo.subsonic.models.Child;
import com.cappielloantonio.tempo.subsonic.models.Playlist;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class PlaylistDuplicateSongDialog extends DialogFragment {

    private final Playlist playlist;

    private final Child song;

    private final DialogClickCallback dialogClickCallback;

    public PlaylistDuplicateSongDialog(Playlist playlist, Child song, DialogClickCallback dialogClickCallback) {
        this.playlist = playlist;
        this.song = song;
        this.dialogClickCallback = dialogClickCallback;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        DialogPlaylistDuplicateSongBinding bind = DialogPlaylistDuplicateSongBinding.inflate(getLayoutInflater());
        bind.playlistDuplicateSongDialogSummary
                .setText(getResources().getString(R.string.playlist_duplicate_song_dialog_summary, song.getTitle(), playlist.getName()));

        return new MaterialAlertDialogBuilder(requireContext())
                .setView(bind.getRoot())
                .setTitle(R.string.playlist_duplicate_song_dialog_title)
                .setPositiveButton(R.string.playlist_duplicate_song_dialog_positive_button, null)
                .setNegativeButton(R.string.playlist_duplicate_song_dialog_negative_button, null)
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
                dialogClickCallback.onPositiveClick();
                dialog.dismiss();
            });

            Button negativeButton = dialog.getButton(Dialog.BUTTON_NEGATIVE);
            negativeButton.setOnClickListener(v -> {
                dialogClickCallback.onNegativeClick();
                dialog.dismiss();
            });
        }
    }
}
