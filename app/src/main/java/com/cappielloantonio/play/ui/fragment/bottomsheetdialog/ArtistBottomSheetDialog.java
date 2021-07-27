package com.cappielloantonio.play.ui.fragment.bottomsheetdialog;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.cappielloantonio.play.App;
import com.cappielloantonio.play.R;
import com.cappielloantonio.play.glide.CustomGlideRequest;
import com.cappielloantonio.play.service.MusicPlayerRemote;
import com.cappielloantonio.play.interfaces.MediaCallback;
import com.cappielloantonio.play.model.Artist;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.repository.QueueRepository;
import com.cappielloantonio.play.repository.SongRepository;
import com.cappielloantonio.play.ui.activity.MainActivity;
import com.cappielloantonio.play.util.PreferenceUtil;
import com.cappielloantonio.play.util.SyncUtil;
import com.cappielloantonio.play.viewmodel.ArtistBottomSheetViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class ArtistBottomSheetDialog extends BottomSheetDialogFragment implements View.OnClickListener {
    private static final String TAG = "AlbumBottomSheetDialog";

    private ArtistBottomSheetViewModel artistBottomSheetViewModel;
    private SongRepository songRepository;
    private Artist artist;

    private ImageView coverArtist;
    private TextView nameArtist;

    private TextView playRadio;
    private TextView playRandom;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_artist_dialog, container, false);

        artist = this.getArguments().getParcelable("artist_object");

        artistBottomSheetViewModel = new ViewModelProvider(requireActivity()).get(ArtistBottomSheetViewModel.class);
        artistBottomSheetViewModel.setArtist(artist);

        songRepository = new SongRepository(App.getInstance());

        init(view);

        return view;
    }

    private void init(View view) {
        coverArtist = view.findViewById(R.id.artist_cover_image_view);
        CustomGlideRequest.Builder
                .from(requireContext(), artistBottomSheetViewModel.getArtist().getPrimary(), artistBottomSheetViewModel.getArtist().getPrimaryBlurHash(), CustomGlideRequest.ARTIST_PIC)
                .build()
                .into(coverArtist);

        nameArtist = view.findViewById(R.id.song_title_text_view);
        nameArtist.setText(artistBottomSheetViewModel.getArtist().getName());
        nameArtist.setSelected(true);

        playRadio = view.findViewById(R.id.play_radio_text_view);
        playRadio.setOnClickListener(v -> {
            SyncUtil.getInstantMix(requireContext(), new MediaCallback() {
                MainActivity activity = (MainActivity) requireActivity();

                @Override
                public void onError(Exception exception) {
                    Log.e(TAG, "onError: " + exception.getMessage());
                }

                @Override
                public void onLoadMedia(List<?> media) {
                    QueueRepository queueRepository = new QueueRepository(App.getInstance());
                    List<Song> mix = queueRepository.insertMix((ArrayList<Song>) media);

                    activity.isBottomSheetInPeek(true);
                    activity.setBottomSheetMusicInfo(mix.get(0));

                    MusicPlayerRemote.openQueue(mix, 0, true);
                }
            }, SyncUtil.SONG, artist.getId(), PreferenceUtil.getInstance(requireContext()).getInstantMixSongNumber());

            dismissBottomSheet();
        });

        playRandom = view.findViewById(R.id.play_random_text_view);
        playRandom.setOnClickListener(v -> {
            List<Song> songs = songRepository.getArtistListLiveRandomSong(artist.getId());

            if(songs.size() > 0) {
                QueueRepository queueRepository = new QueueRepository(App.getInstance());
                queueRepository.insertAllAndStartNew(songs);

                MusicPlayerRemote.openQueue(songs, 0, true);
                ((MainActivity) requireActivity()).isBottomSheetInPeek(true);
                dismissBottomSheet();
            }
            else Toast.makeText(requireContext(), "Error retrieving artist's songs", Toast.LENGTH_SHORT).show();
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