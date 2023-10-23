package com.cappielloantonio.tempo.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cappielloantonio.tempo.R;
import com.cappielloantonio.tempo.databinding.DialogPlaylistChooserBinding;
import com.cappielloantonio.tempo.interfaces.ClickCallback;
import com.cappielloantonio.tempo.subsonic.models.Playlist;
import com.cappielloantonio.tempo.ui.adapter.PlaylistDialogHorizontalAdapter;
import com.cappielloantonio.tempo.util.Constants;
import com.cappielloantonio.tempo.viewmodel.PlaylistChooserViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Objects;

public class PlaylistChooserDialog extends DialogFragment implements ClickCallback {
    private DialogPlaylistChooserBinding bind;
    private PlaylistChooserViewModel playlistChooserViewModel;

    private PlaylistDialogHorizontalAdapter playlistDialogHorizontalAdapter;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        bind = DialogPlaylistChooserBinding.inflate(getLayoutInflater());
        playlistChooserViewModel = new ViewModelProvider(requireActivity()).get(PlaylistChooserViewModel.class);

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());

        builder.setView(bind.getRoot())
                .setTitle(R.string.playlist_chooser_dialog_title)
                .setNeutralButton(R.string.playlist_chooser_dialog_neutral_button, (dialog, id) -> {
                })
                .setNegativeButton(R.string.playlist_chooser_dialog_negative_button, (dialog, id) -> dialog.cancel());

        return builder.create();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind = null;
    }

    @Override
    public void onStart() {
        super.onStart();

        initPlaylistView();
        setSongInfo();
        setButtonAction();
    }

    private void setSongInfo() {
        playlistChooserViewModel.setSongToAdd(requireArguments().getParcelable(Constants.TRACK_OBJECT));
    }

    private void setButtonAction() {
        androidx.appcompat.app.AlertDialog alertDialog = (androidx.appcompat.app.AlertDialog) Objects.requireNonNull(getDialog());
        alertDialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEUTRAL).setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putParcelable(Constants.TRACK_OBJECT, playlistChooserViewModel.getSongToAdd());

            PlaylistEditorDialog dialog = new PlaylistEditorDialog(null);
            dialog.setArguments(bundle);
            dialog.show(requireActivity().getSupportFragmentManager(), null);

            Objects.requireNonNull(getDialog()).dismiss();
        });
    }

    private void initPlaylistView() {
        bind.playlistDialogRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        bind.playlistDialogRecyclerView.setHasFixedSize(true);

        playlistDialogHorizontalAdapter = new PlaylistDialogHorizontalAdapter(this);
        bind.playlistDialogRecyclerView.setAdapter(playlistDialogHorizontalAdapter);

        playlistChooserViewModel.getPlaylistList(requireActivity()).observe(requireActivity(), playlists -> {
            if (playlists != null) {
                if (playlists.size() > 0) {
                    if (bind != null) bind.noPlaylistsCreatedTextView.setVisibility(View.GONE);
                    if (bind != null) bind.playlistDialogRecyclerView.setVisibility(View.VISIBLE);
                    playlistDialogHorizontalAdapter.setItems(playlists);
                } else {
                    if (bind != null) bind.noPlaylistsCreatedTextView.setVisibility(View.VISIBLE);
                    if (bind != null) bind.playlistDialogRecyclerView.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onPlaylistClick(Bundle bundle) {
        Playlist playlist = bundle.getParcelable(Constants.PLAYLIST_OBJECT);
        playlistChooserViewModel.addSongToPlaylist(playlist.getId());
        dismiss();
    }
}
