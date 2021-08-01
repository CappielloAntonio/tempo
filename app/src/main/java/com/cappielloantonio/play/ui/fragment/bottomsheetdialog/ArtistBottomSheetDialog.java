package com.cappielloantonio.play.ui.fragment.bottomsheetdialog;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.cappielloantonio.play.App;
import com.cappielloantonio.play.R;
import com.cappielloantonio.play.glide.CustomGlideRequest;
import com.cappielloantonio.play.interfaces.MediaCallback;
import com.cappielloantonio.play.model.Artist;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.repository.ArtistRepository;
import com.cappielloantonio.play.repository.QueueRepository;
import com.cappielloantonio.play.service.MusicPlayerRemote;
import com.cappielloantonio.play.ui.activity.MainActivity;
import com.cappielloantonio.play.viewmodel.ArtistBottomSheetViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class ArtistBottomSheetDialog extends BottomSheetDialogFragment implements View.OnClickListener {
    private static final String TAG = "AlbumBottomSheetDialog";

    private MainActivity activity;

    private ArtistBottomSheetViewModel artistBottomSheetViewModel;
    private Artist artist;

    private ImageView coverArtist;
    private TextView nameArtist;
    private ToggleButton favoriteToggle;

    private TextView playRadio;
    private TextView playRandom;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_artist_dialog, container, false);

        artist = this.getArguments().getParcelable("artist_object");

        artistBottomSheetViewModel = new ViewModelProvider(requireActivity()).get(ArtistBottomSheetViewModel.class);
        artistBottomSheetViewModel.setArtist(artist);

        init(view);

        return view;
    }

    private void init(View view) {
        activity = (MainActivity) requireActivity();

        coverArtist = view.findViewById(R.id.artist_cover_image_view);
        CustomGlideRequest.Builder
                .from(requireContext(), artistBottomSheetViewModel.getArtist().getPrimary(), artistBottomSheetViewModel.getArtist().getPrimaryBlurHash(), CustomGlideRequest.ARTIST_PIC)
                .build()
                .into(coverArtist);

        nameArtist = view.findViewById(R.id.song_title_text_view);
        nameArtist.setText(Html.fromHtml(artistBottomSheetViewModel.getArtist().getName(), Html.FROM_HTML_MODE_COMPACT));
        nameArtist.setSelected(true);

        favoriteToggle = view.findViewById(R.id.button_favorite);
        favoriteToggle.setChecked(artistBottomSheetViewModel.getArtist().isFavorite());
        favoriteToggle.setOnClickListener(v -> {
            artistBottomSheetViewModel.setFavorite();
            dismissBottomSheet();
        });

        playRadio = view.findViewById(R.id.play_radio_text_view);
        playRadio.setOnClickListener(v -> {
            ArtistRepository artistRepository = new ArtistRepository(App.getInstance());
            artistRepository.getInstantMix(artist, 20, new MediaCallback() {
                @Override
                public void onError(Exception exception) {
                    Log.e(TAG, "onError: " + exception.getMessage());

                    dismissBottomSheet();
                }

                @Override
                public void onLoadMedia(List<?> media) {
                    if (media.size() > 0) {
                        QueueRepository queueRepository = new QueueRepository(App.getInstance());
                        queueRepository.insertAllAndStartNew((ArrayList<Song>) media);

                        activity.isBottomSheetInPeek(true);
                        activity.setBottomSheetMusicInfo((Song) media.get(0));

                        MusicPlayerRemote.openQueue((List<Song>) media, 0, true);
                    } else {
                        Toast.makeText(requireContext(), "Error retrieving artist's radio", Toast.LENGTH_SHORT).show();
                    }

                    dismissBottomSheet();
                }
            });
        });

        playRandom = view.findViewById(R.id.play_random_text_view);
        playRandom.setOnClickListener(v -> {
            ArtistRepository artistRepository = new ArtistRepository(App.getInstance());
            artistRepository.getArtistRandomSong(requireActivity(), artist, 20).observe(requireActivity(), songs -> {
                if (songs.size() > 0) {
                    QueueRepository queueRepository = new QueueRepository(App.getInstance());
                    queueRepository.insertAllAndStartNew(songs);

                    MusicPlayerRemote.openQueue(songs, 0, true);
                    activity.isBottomSheetInPeek(true);
                } else {
                    Toast.makeText(requireContext(), "Error retrieving artist's songs", Toast.LENGTH_SHORT).show();
                }

                dismissBottomSheet();
            });
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