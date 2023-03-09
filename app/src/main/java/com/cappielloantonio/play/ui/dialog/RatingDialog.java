package com.cappielloantonio.play.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.cappielloantonio.play.R;
import com.cappielloantonio.play.databinding.DialogRatingBinding;
import com.cappielloantonio.play.viewmodel.RatingViewModel;

public class RatingDialog extends DialogFragment {
    private static final String TAG = "ServerSignupDialog";

    private DialogRatingBinding bind;
    private RatingViewModel ratingViewModel;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        bind = DialogRatingBinding.inflate(LayoutInflater.from(requireContext()));
        ratingViewModel = new ViewModelProvider(requireActivity()).get(RatingViewModel.class);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setView(bind.getRoot())
                .setTitle(R.string.rating_dialog_title)
                .setNegativeButton(R.string.rating_dialog_negative_button, (dialog, id) -> dialog.cancel())
                .setPositiveButton(R.string.rating_dialog_positive_button, (dialog, id) -> ratingViewModel.rate((int) bind.ratingBar.getRating()));

        return builder.create();
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
        if (requireArguments().getParcelable("song_object") != null) {
            ratingViewModel.setSong(requireArguments().getParcelable("song_object"));
        } else if (requireArguments().getParcelable("album_object") != null) {
            ratingViewModel.setAlbum(requireArguments().getParcelable("album_object"));
        } else if (requireArguments().getParcelable("artist_object") != null) {
            ratingViewModel.setArtist(requireArguments().getParcelable("artist_object"));
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
