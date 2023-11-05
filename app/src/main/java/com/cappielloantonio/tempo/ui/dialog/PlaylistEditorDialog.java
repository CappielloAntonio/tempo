package com.cappielloantonio.tempo.ui.dialog;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cappielloantonio.tempo.R;
import com.cappielloantonio.tempo.databinding.DialogPlaylistEditorBinding;
import com.cappielloantonio.tempo.interfaces.PlaylistCallback;
import com.cappielloantonio.tempo.ui.adapter.PlaylistDialogSongHorizontalAdapter;
import com.cappielloantonio.tempo.util.Constants;
import com.cappielloantonio.tempo.util.MusicUtil;
import com.cappielloantonio.tempo.util.Preferences;
import com.cappielloantonio.tempo.viewmodel.PlaylistEditorViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Collections;
import java.util.Objects;

public class PlaylistEditorDialog extends DialogFragment {
    private DialogPlaylistEditorBinding bind;
    private PlaylistEditorViewModel playlistEditorViewModel;
    private PlaylistCallback playlistCallback;

    private String playlistName;
    private PlaylistDialogSongHorizontalAdapter playlistDialogSongHorizontalAdapter;

    public PlaylistEditorDialog(PlaylistCallback playlistCallback) {
        this.playlistCallback = playlistCallback;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        bind = DialogPlaylistEditorBinding.inflate(getLayoutInflater());

        playlistEditorViewModel = new ViewModelProvider(requireActivity()).get(PlaylistEditorViewModel.class);

        return new MaterialAlertDialogBuilder(getActivity())
                .setView(bind.getRoot())
                .setTitle(R.string.playlist_editor_dialog_title)
                .setPositiveButton(R.string.playlist_editor_dialog_positive_button, (dialog, id) -> { })
                .setNeutralButton(R.string.playlist_editor_dialog_neutral_button, (dialog, id) -> dialog.cancel())
                .setNegativeButton(R.string.playlist_editor_dialog_negative_button, (dialog, id) -> dialog.cancel())
                .create();
    }

    @Override
    public void onStart() {
        super.onStart();

        setParameterInfo();
        setButtonAction();
        initSongsView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind = null;
    }

    private void setParameterInfo() {
        if (requireArguments().getParcelable(Constants.TRACK_OBJECT) != null) {
            playlistEditorViewModel.setSongToAdd(requireArguments().getParcelable(Constants.TRACK_OBJECT));
            playlistEditorViewModel.setPlaylistToEdit(null);
        } else if (requireArguments().getParcelable(Constants.PLAYLIST_OBJECT) != null) {
            playlistEditorViewModel.setSongToAdd(null);
            playlistEditorViewModel.setPlaylistToEdit(requireArguments().getParcelable(Constants.PLAYLIST_OBJECT));

            if (playlistEditorViewModel.getPlaylistToEdit() != null) {
                bind.playlistNameTextView.setText(MusicUtil.getReadableString(playlistEditorViewModel.getPlaylistToEdit().getName()));
            }
        }
    }

    private void setButtonAction() {
        androidx.appcompat.app.AlertDialog alertDialog = (androidx.appcompat.app.AlertDialog) Objects.requireNonNull(getDialog());

        alertDialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            if (validateInput()) {
                if (playlistEditorViewModel.getSongToAdd() != null) {
                    playlistEditorViewModel.createPlaylist(playlistName);
                } else if (playlistEditorViewModel.getPlaylistToEdit() != null) {
                    playlistEditorViewModel.updatePlaylist(playlistName);
                }

                dialogDismiss();
            }
        });

        alertDialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEUTRAL).setOnClickListener(v -> {
            playlistEditorViewModel.deletePlaylist();
            dialogDismiss();
        });

        bind.playlistShareButton.setOnClickListener(view -> {
            playlistEditorViewModel.sharePlaylist().observe(requireActivity(), sharedPlaylist -> {
                ClipboardManager clipboardManager = (ClipboardManager) requireActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText(getString(R.string.app_name), sharedPlaylist.getUrl());
                clipboardManager.setPrimaryClip(clipData);
            });
        });

        bind.playlistShareButton.setVisibility(Preferences.isSharingEnabled() ? View.VISIBLE : View.GONE);
    }

    private void initSongsView() {
        bind.playlistSongRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        bind.playlistSongRecyclerView.setHasFixedSize(true);

        playlistDialogSongHorizontalAdapter = new PlaylistDialogSongHorizontalAdapter();
        bind.playlistSongRecyclerView.setAdapter(playlistDialogSongHorizontalAdapter);

        playlistEditorViewModel.getPlaylistSongLiveList().observe(requireActivity(), songs -> {
            if (songs != null) playlistDialogSongHorizontalAdapter.setItems(songs);
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT) {
            int originalPosition = -1;
            int fromPosition = -1;
            int toPosition = -1;

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                if (originalPosition == -1)
                    originalPosition = viewHolder.getBindingAdapterPosition();

                fromPosition = viewHolder.getBindingAdapterPosition();
                toPosition = target.getBindingAdapterPosition();

                Collections.swap(playlistDialogSongHorizontalAdapter.getItems(), fromPosition, toPosition);
                Objects.requireNonNull(recyclerView.getAdapter()).notifyItemMoved(fromPosition, toPosition);

                return false;
            }

            @Override
            public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);

                /*
                 * Qui vado a riscivere tutta la table Queue, quando teoricamente potrei solo swappare l'ordine degli elementi interessati
                 * Nel caso la coda contenesse parecchi brani, potrebbero verificarsi rallentamenti pesanti
                 */
                playlistEditorViewModel.orderPlaylistSongLiveListAfterSwap(playlistDialogSongHorizontalAdapter.getItems());

                originalPosition = -1;
                fromPosition = -1;
                toPosition = -1;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                playlistEditorViewModel.removeFromPlaylistSongLiveList(viewHolder.getBindingAdapterPosition());
                Objects.requireNonNull(bind.playlistSongRecyclerView.getAdapter()).notifyItemRemoved(viewHolder.getBindingAdapterPosition());
            }
        }
        ).attachToRecyclerView(bind.playlistSongRecyclerView);
    }

    private boolean validateInput() {
        playlistName = Objects.requireNonNull(bind.playlistNameTextView.getText()).toString().trim();

        if (TextUtils.isEmpty(playlistName)) {
            bind.playlistNameTextView.setError(getString(R.string.error_required));
            return false;
        }

        return true;
    }

    private void dialogDismiss() {
        Objects.requireNonNull(getDialog()).dismiss();
        if (playlistCallback != null) {
            playlistCallback.onDismiss();
        }
    }
}
