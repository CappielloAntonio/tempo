package com.cappielloantonio.play.ui.fragment.bottomsheetdialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.cappielloantonio.play.R;
import com.cappielloantonio.play.glide.CustomGlideRequest;
import com.cappielloantonio.play.model.Album;
import com.cappielloantonio.play.model.Artist;
import com.cappielloantonio.play.viewmodel.AlbumBottomSheetViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AlbumBottomSheetDialog extends BottomSheetDialogFragment implements View.OnClickListener {
    private static final String TAG = "AlbumBottomSheetDialog";

    private AlbumBottomSheetViewModel albumBottomSheetViewModel;
    private Album album;

    private ImageView coverAlbum;
    private TextView titleAlbum;
    private TextView artistAlbum;

    private TextView playRadio;
    private TextView playRandom;
    private TextView playNext;
    private TextView addToQueue;
    private TextView Download;
    private TextView addToPlaylist;
    private TextView goToArtist;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_album_dialog, container, false);

        album = this.getArguments().getParcelable("album_object");

        albumBottomSheetViewModel = new ViewModelProvider(requireActivity()).get(AlbumBottomSheetViewModel.class);
        albumBottomSheetViewModel.setAlbum(album);

        init(view);

        return view;
    }

    private void init(View view) {
        coverAlbum = view.findViewById(R.id.album_cover_image_view);
        CustomGlideRequest.Builder
                .from(requireContext(), albumBottomSheetViewModel.getAlbum().getPrimary(), albumBottomSheetViewModel.getAlbum().getBlurHash(), CustomGlideRequest.PRIMARY, CustomGlideRequest.TOP_QUALITY, CustomGlideRequest.ALBUM_PIC)
                .build()
                .into(coverAlbum);

        titleAlbum = view.findViewById(R.id.album_title_text_view);
        titleAlbum.setText(albumBottomSheetViewModel.getAlbum().getTitle());

        artistAlbum = view.findViewById(R.id.album_artist_text_view);
        artistAlbum.setText(albumBottomSheetViewModel.getAlbum().getArtistName());

        playRadio = view.findViewById(R.id.play_radio_text_view);
        playRadio.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Play radio", Toast.LENGTH_SHORT).show();
            dismissBottomSheet();
        });

        playRandom = view.findViewById(R.id.play_random_text_view);
        playRandom.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Play next", Toast.LENGTH_SHORT).show();
            dismissBottomSheet();
        });

        playNext = view.findViewById(R.id.play_next_text_view);
        playNext.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Play next", Toast.LENGTH_SHORT).show();
            dismissBottomSheet();
        });

        addToQueue = view.findViewById(R.id.add_to_queue_text_view);
        addToQueue.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Add to queue", Toast.LENGTH_SHORT).show();
            dismissBottomSheet();
        });

        Download = view.findViewById(R.id.download_text_view);
        Download.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Download", Toast.LENGTH_SHORT).show();
            dismissBottomSheet();
        });

        addToPlaylist = view.findViewById(R.id.add_to_playlist_text_view);
        addToPlaylist.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Add to playlist", Toast.LENGTH_SHORT).show();
            dismissBottomSheet();
        });

        goToArtist = view.findViewById(R.id.go_to_artist_text_view);
        goToArtist.setOnClickListener(v -> {
            Artist artist = albumBottomSheetViewModel.getArtist();
            if(artist != null) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("artist_object", artist);
                NavHostFragment.findNavController(this).navigate(R.id.artistPageFragment, bundle);
            }
            else Toast.makeText(requireContext(), "Error retrieving artist", Toast.LENGTH_SHORT).show();

            dismissBottomSheet();
        });
    }

    @Override
    public void onClick(View v) {
        dismissBottomSheet();
    }

    private void dismissBottomSheet() {
        dismiss();
    }
}