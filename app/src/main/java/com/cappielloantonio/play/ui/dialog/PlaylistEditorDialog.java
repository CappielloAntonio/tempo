package com.cappielloantonio.play.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cappielloantonio.play.R;
import com.cappielloantonio.play.databinding.DialogPlaylistEditorBinding;
import com.cappielloantonio.play.ui.adapter.PlaylistDialogSongHorizontalAdapter;
import com.cappielloantonio.play.util.Constants;
import com.cappielloantonio.play.util.MusicUtil;
import com.cappielloantonio.play.viewmodel.PlaylistEditorViewModel;

import java.util.Collections;
import java.util.Objects;

public class PlaylistEditorDialog extends DialogFragment {
    private DialogPlaylistEditorBinding bind;
    private PlaylistEditorViewModel playlistEditorViewModel;

    private String playlistName;
    private PlaylistDialogSongHorizontalAdapter playlistDialogSongHorizontalAdapter;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        bind = DialogPlaylistEditorBinding.inflate(getLayoutInflater());
        playlistEditorViewModel = new ViewModelProvider(requireActivity()).get(PlaylistEditorViewModel.class);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setView(bind.getRoot())
                .setTitle(R.string.playlist_editor_dialog_title)
                .setPositiveButton(R.string.playlist_editor_dialog_positive_button, (dialog, id) -> {
                })
                .setNeutralButton(R.string.playlist_editor_dialog_neutral_button, (dialog, id) -> dialog.cancel())
                .setNegativeButton(R.string.playlist_editor_dialog_negative_button, (dialog, id) -> dialog.cancel());

        return builder.create();
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
        ((AlertDialog) Objects.requireNonNull(getDialog())).getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            if (validateInput()) {
                if (playlistEditorViewModel.getSongToAdd() != null) {
                    playlistEditorViewModel.createPlaylist(playlistName);
                } else if (playlistEditorViewModel.getPlaylistToEdit() != null) {
                    playlistEditorViewModel.updatePlaylist(playlistName);
                }

                Objects.requireNonNull(getDialog()).dismiss();
            }
        });

        ((AlertDialog) Objects.requireNonNull(getDialog())).getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(v -> {
            playlistEditorViewModel.deletePlaylist();
            Objects.requireNonNull(getDialog()).dismiss();
        });
    }

    private void initSongsView() {
        bind.playlistSongRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        bind.playlistSongRecyclerView.setHasFixedSize(true);

        playlistDialogSongHorizontalAdapter = new PlaylistDialogSongHorizontalAdapter();
        bind.playlistSongRecyclerView.setAdapter(playlistDialogSongHorizontalAdapter);

        playlistEditorViewModel.getPlaylistSongLiveList().observe(requireActivity(), songs -> playlistDialogSongHorizontalAdapter.setItems(songs));

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
}
