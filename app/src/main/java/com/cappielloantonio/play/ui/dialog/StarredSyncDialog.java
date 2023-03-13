package com.cappielloantonio.play.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.cappielloantonio.play.R;
import com.cappielloantonio.play.databinding.DialogStarredSyncBinding;
import com.cappielloantonio.play.model.Download;
import com.cappielloantonio.play.util.DownloadUtil;
import com.cappielloantonio.play.util.MappingUtil;
import com.cappielloantonio.play.util.Preferences;
import com.cappielloantonio.play.viewmodel.StarredSyncViewModel;

import java.util.stream.Collectors;

public class StarredSyncDialog extends DialogFragment {
    private static final String TAG = "ServerUnreachableDialog";

    private DialogStarredSyncBinding bind;
    private StarredSyncViewModel starredSyncViewModel;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        bind = DialogStarredSyncBinding.inflate(LayoutInflater.from(requireContext()));
        starredSyncViewModel = new ViewModelProvider(requireActivity()).get(StarredSyncViewModel.class);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setView(bind.getRoot())
                .setTitle(R.string.starred_sync_dialog_title)
                .setPositiveButton(R.string.starred_sync_dialog_positive_button, null)
                .setNegativeButton(R.string.starred_sync_dialog_negative_button, null);

        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        setButtonAction(requireContext());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind = null;
    }

    private void setButtonAction(Context context) {
        AlertDialog dialog = ((AlertDialog) getDialog());

        if (dialog != null) {
            Button positiveButton = dialog.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(v -> {
                starredSyncViewModel.getStarredTracks(requireActivity()).observe(requireActivity(), songs -> {
                    if (songs != null) {
                        DownloadUtil.getDownloadTracker(context).download(
                                MappingUtil.mapDownloads(songs),
                                songs.stream().map(Download::new).collect(Collectors.toList())
                        );
                    }

                    dialog.dismiss();
                });
            });

            Button negativeButton = dialog.getButton(Dialog.BUTTON_NEGATIVE);
            negativeButton.setOnClickListener(v -> {
                Preferences.setStarredSyncEnabled(false);
                dialog.dismiss();
            });
        }
    }
}
