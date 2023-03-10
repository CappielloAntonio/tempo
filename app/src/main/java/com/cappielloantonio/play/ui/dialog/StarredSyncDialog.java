package com.cappielloantonio.play.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.cappielloantonio.play.R;
import com.cappielloantonio.play.databinding.DialogConnectionAlertBinding;
import com.cappielloantonio.play.model.Download;
import com.cappielloantonio.play.util.DownloadUtil;
import com.cappielloantonio.play.util.MappingUtil;
import com.cappielloantonio.play.util.Preferences;
import com.cappielloantonio.play.viewmodel.StarredSyncViewModel;

import java.util.Objects;
import java.util.stream.Collectors;

public class StarredSyncDialog extends DialogFragment {
    private static final String TAG = "ServerUnreachableDialog";

    private DialogConnectionAlertBinding bind;
    private StarredSyncViewModel starredSyncViewModel;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        bind = DialogConnectionAlertBinding.inflate(LayoutInflater.from(requireContext()));
        starredSyncViewModel = new ViewModelProvider(requireActivity()).get(StarredSyncViewModel.class);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setView(bind.getRoot())
                .setTitle(R.string.starred_sync_dialog_title)
                .setPositiveButton(R.string.starred_sync_dialog_positive_button, (dialog, id) -> {
                })
                .setNegativeButton(R.string.starred_sync_dialog_negative_button, (dialog, id) -> {
                });

        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();

        setButtonAction(requireContext());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind = null;
    }

    private void setButtonAction(Context context) {
        ((AlertDialog) Objects.requireNonNull(getDialog())).getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            starredSyncViewModel.getStarredTracks(getViewLifecycleOwner()).observe(getViewLifecycleOwner(), songs -> {
                if (songs != null) {
                    DownloadUtil.getDownloadTracker(context).download(
                            MappingUtil.mapMediaItems(context, songs, false),
                            songs.stream().map(Download::new).collect(Collectors.toList())
                    );
                }
            });

            Objects.requireNonNull(getDialog()).dismiss();
        });

        ((AlertDialog) Objects.requireNonNull(getDialog())).getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(v -> {
            Preferences.setStarredSyncEnabled(false);
            Objects.requireNonNull(getDialog()).dismiss();
        });
    }
}
