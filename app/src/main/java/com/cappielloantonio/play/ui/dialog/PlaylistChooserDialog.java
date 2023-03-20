package com.cappielloantonio.play.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cappielloantonio.play.R;
import com.cappielloantonio.play.databinding.DialogPlaylistChooserBinding;
import com.cappielloantonio.play.interfaces.ClickCallback;
import com.cappielloantonio.play.subsonic.models.Playlist;
import com.cappielloantonio.play.ui.adapter.PlaylistDialogHorizontalAdapter;
import com.cappielloantonio.play.util.Constants;
import com.cappielloantonio.play.viewmodel.PlaylistChooserViewModel;

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

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

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
        ((AlertDialog) Objects.requireNonNull(getDialog())).getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putParcelable(Constants.TRACK_OBJECT, playlistChooserViewModel.getSongToAdd());

            PlaylistEditorDialog dialog = new PlaylistEditorDialog();
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
