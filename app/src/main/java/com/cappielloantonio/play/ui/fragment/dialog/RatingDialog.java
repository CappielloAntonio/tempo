package com.cappielloantonio.play.ui.fragment.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.cappielloantonio.play.R;
import com.cappielloantonio.play.databinding.DialogRatingBinding;
import com.cappielloantonio.play.ui.activity.MainActivity;
import com.cappielloantonio.play.viewmodel.RatingViewModel;

import java.util.Objects;

public class RatingDialog extends DialogFragment {
    private static final String TAG = "ServerSignupDialog";

    private DialogRatingBinding bind;
    private RatingViewModel ratingViewModel;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        bind = DialogRatingBinding.inflate(LayoutInflater.from(requireContext()));
        ratingViewModel = new ViewModelProvider(requireActivity()).get(RatingViewModel.class);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppTheme_AlertDialog);

        builder.setView(bind.getRoot())
                .setTitle("Rate")
                .setNegativeButton("Cancel", (dialog, id) -> dialog.cancel())
                .setPositiveButton("Save", (dialog, id) -> {
                    ratingViewModel.rate((int) bind.ratingBar.getRating());
                });

        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();

        setElementInfo();
        setButtonColor();
        setRating();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind = null;
    }

    private void setElementInfo() {
        if (getArguments() != null) {
            if (getArguments().getParcelable("song_object") != null) {
                ratingViewModel.setSong(getArguments().getParcelable("song_object"));
            } else if (getArguments().getParcelable("album_object") != null) {
                ratingViewModel.setAlbum(getArguments().getParcelable("album_object"));
            } else if (getArguments().getParcelable("artist_object") != null) {
                ratingViewModel.setArtist(getArguments().getParcelable("artist_object"));
            }
        }
    }

    private void setButtonColor() {
        ((AlertDialog) Objects.requireNonNull(getDialog())).getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorAccent, null));
        ((AlertDialog) Objects.requireNonNull(getDialog())).getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorAccent, null));
    }

    private void setRating() {
        if (ratingViewModel.getSong() != null) {
            ratingViewModel.getLiveSong().observe(requireActivity(), song -> bind.ratingBar.setRating(song.getRating()));
        } else if (ratingViewModel.getAlbum() != null) {
            ratingViewModel.getLiveAlbum().observe(requireActivity(), album -> bind.ratingBar.setRating(/*album.getRating()*/ 0));
        } else if (ratingViewModel.getArtist() != null) {
            ratingViewModel.getLiveArtist().observe(requireActivity(), artist -> bind.ratingBar.setRating(/*artist.getRating()*/ 0));
        }
    }
}
