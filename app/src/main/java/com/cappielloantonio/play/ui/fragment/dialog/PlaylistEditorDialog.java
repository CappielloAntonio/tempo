package com.cappielloantonio.play.ui.fragment.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.cappielloantonio.play.R;
import com.cappielloantonio.play.databinding.DialogPlaylistEditorBinding;
import com.cappielloantonio.play.ui.activity.MainActivity;
import com.cappielloantonio.play.viewmodel.PlaylistEditorViewModel;

import java.util.Objects;

public class PlaylistEditorDialog extends DialogFragment {
    private static final String TAG = "ServerSignupDialog";

    private DialogPlaylistEditorBinding bind;
    private MainActivity activity;
    private Context context;
    private PlaylistEditorViewModel playlistEditorViewModel;

    private String playlistName;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();
        context = requireContext();

        bind = DialogPlaylistEditorBinding.inflate(LayoutInflater.from(requireContext()));
        playlistEditorViewModel = new ViewModelProvider(requireActivity()).get(PlaylistEditorViewModel.class);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppTheme_AlertDialog);

        builder.setView(bind.getRoot())
                .setTitle("Create playlist")
                .setPositiveButton("Save", (dialog, id) -> {
                })
                .setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());

        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();

        setSongInfo();
        setButtonAction();
    }

    private void setSongInfo() {
        if (getArguments() != null) {
            playlistEditorViewModel.setSongToAdd(getArguments().getParcelable("song_object"));
        } else {
            playlistEditorViewModel.setSongToAdd(null);
        }
    }

    private void setButtonAction() {
        ((AlertDialog) Objects.requireNonNull(getDialog())).getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(getResources().getColor(R.color.colorAccent, null));
        ((AlertDialog) Objects.requireNonNull(getDialog())).getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorAccent, null));
        ((AlertDialog) Objects.requireNonNull(getDialog())).getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorAccent, null));

        ((AlertDialog) Objects.requireNonNull(getDialog())).getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            if (validateInput()) {
                playlistEditorViewModel.createPlaylist(playlistName);
                Objects.requireNonNull(getDialog()).dismiss();
            }
        });
    }

    private boolean validateInput() {
        playlistName = bind.playlistNameTextView.getText().toString().trim();

        if (TextUtils.isEmpty(playlistName)) {
            bind.playlistNameTextView.setError("Required");
            return false;
        }

        return true;
    }
}
