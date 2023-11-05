package com.cappielloantonio.tempo.ui.dialog;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.cappielloantonio.tempo.R;
import com.cappielloantonio.tempo.databinding.DialogRatingBinding;
import com.cappielloantonio.tempo.util.Constants;
import com.cappielloantonio.tempo.viewmodel.RatingViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class RatingDialog extends DialogFragment {
    private static final String TAG = "ServerSignupDialog";

    private DialogRatingBinding bind;
    private RatingViewModel ratingViewModel;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        bind = DialogRatingBinding.inflate(getLayoutInflater());
        ratingViewModel = new ViewModelProvider(requireActivity()).get(RatingViewModel.class);

        return new MaterialAlertDialogBuilder(getActivity())
                .setView(bind.getRoot())
                .setTitle(R.string.rating_dialog_title)
                .setNegativeButton(R.string.rating_dialog_negative_button, (dialog, id) -> dialog.cancel())
                .setPositiveButton(R.string.rating_dialog_positive_button, (dialog, id) -> ratingViewModel.rate((int) bind.ratingBar.getRating()))
                .create();
    }

    @Override
    public void onStart() {
        super.onStart();

        setElementInfo();
        setRating();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind = null;
    }

    private void setElementInfo() {
        if (requireArguments().getParcelable(Constants.TRACK_OBJECT) != null) {
            ratingViewModel.setSong(requireArguments().getParcelable(Constants.TRACK_OBJECT));
        } else if (requireArguments().getParcelable(Constants.ALBUM_OBJECT) != null) {
            ratingViewModel.setAlbum(requireArguments().getParcelable(Constants.ALBUM_OBJECT));
        } else if (requireArguments().getParcelable(Constants.ARTIST_OBJECT) != null) {
            ratingViewModel.setArtist(requireArguments().getParcelable(Constants.ARTIST_OBJECT));
        }
    }

    private void setRating() {
        if (ratingViewModel.getSong() != null) {
            ratingViewModel.getLiveSong().observe(this, song -> {
                bind.ratingBar.setRating(song.getUserRating() != null ? song.getUserRating() : 0);
            });
        } else if (ratingViewModel.getAlbum() != null) {
            ratingViewModel.getLiveAlbum().observe(this, album -> bind.ratingBar.setRating(/*album.getRating()*/ 0));
        } else if (ratingViewModel.getArtist() != null) {
            ratingViewModel.getLiveArtist().observe(this, artist -> bind.ratingBar.setRating(/*artist.getRating()*/ 0));
        }
    }
}
