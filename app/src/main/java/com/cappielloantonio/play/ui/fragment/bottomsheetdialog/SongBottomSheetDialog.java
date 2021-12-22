package com.cappielloantonio.play.ui.fragment.bottomsheetdialog;

import android.os.Bundle;
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
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.cappielloantonio.play.App;
import com.cappielloantonio.play.R;
import com.cappielloantonio.play.glide.CustomGlideRequest;
import com.cappielloantonio.play.interfaces.MediaCallback;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.repository.QueueRepository;
import com.cappielloantonio.play.repository.SongRepository;
import com.cappielloantonio.play.service.MusicPlayerRemote;
import com.cappielloantonio.play.ui.activity.MainActivity;
import com.cappielloantonio.play.ui.dialog.PlaylistChooserDialog;
import com.cappielloantonio.play.ui.dialog.RatingDialog;
import com.cappielloantonio.play.util.DownloadUtil;
import com.cappielloantonio.play.util.MusicUtil;
import com.cappielloantonio.play.viewmodel.SongBottomSheetViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SongBottomSheetDialog extends BottomSheetDialogFragment implements View.OnClickListener {
    private static final String TAG = "SongBottomSheetDialog";

    private SongBottomSheetViewModel songBottomSheetViewModel;
    private Song song;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_song_dialog, container, false);

        song = requireArguments().getParcelable("song_object");

        songBottomSheetViewModel = new ViewModelProvider(requireActivity()).get(SongBottomSheetViewModel.class);
        songBottomSheetViewModel.setSong(song);

        init(view);

        return view;
    }

    private void init(View view) {
        ImageView coverSong = view.findViewById(R.id.song_cover_image_view);
        CustomGlideRequest.Builder
                .from(requireContext(), songBottomSheetViewModel.getSong().getPrimary(), CustomGlideRequest.SONG_PIC, null)
                .build()
                .transform(new RoundedCorners(CustomGlideRequest.CORNER_RADIUS))
                .into(coverSong);

        TextView titleSong = view.findViewById(R.id.song_title_text_view);
        titleSong.setText(MusicUtil.getReadableString(songBottomSheetViewModel.getSong().getTitle()));

        titleSong.setSelected(true);

        TextView artistSong = view.findViewById(R.id.song_artist_text_view);
        artistSong.setText(MusicUtil.getReadableString(songBottomSheetViewModel.getSong().getArtistName()));

        ToggleButton favoriteToggle = view.findViewById(R.id.button_favorite);
        favoriteToggle.setChecked(songBottomSheetViewModel.getSong().isFavorite());
        favoriteToggle.setOnClickListener(v -> {
            songBottomSheetViewModel.setFavorite(requireContext());
            dismissBottomSheet();
        });
        favoriteToggle.setOnLongClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putParcelable("song_object", song);

            RatingDialog dialog = new RatingDialog();
            dialog.setArguments(bundle);
            dialog.show(requireActivity().getSupportFragmentManager(), null);

            dismissBottomSheet();
            return true;
        });

        TextView playRadio = view.findViewById(R.id.play_radio_text_view);
        playRadio.setOnClickListener(v -> {
            List<Song> opener = new ArrayList<>();
            opener.add(song);
            MusicPlayerRemote.openQueue(opener, 0, true);

            QueueRepository queueRepository = new QueueRepository(App.getInstance());
            queueRepository.insertAllAndStartNew(opener);

            ((MainActivity) requireActivity()).setBottomSheetInPeek(true);
            ((MainActivity) requireActivity()).setBottomSheetMusicInfo(song);

            SongRepository songRepository = new SongRepository(App.getInstance());
            songRepository.getInstantMix(song, 20, new MediaCallback() {
                @Override
                public void onError(Exception exception) {
                    Log.e(TAG, "onError() " + exception.getMessage());
                }

                @Override
                public void onLoadMedia(List<?> media) {
                    MusicPlayerRemote.enqueue((List<Song>) media);
                }
            });

            dismissBottomSheet();
        });

        TextView playNext = view.findViewById(R.id.play_next_text_view);
        playNext.setOnClickListener(v -> {
            MusicPlayerRemote.playNext(song);
            ((MainActivity) requireActivity()).setBottomSheetInPeek(true);
            dismissBottomSheet();
        });

        TextView addToQueue = view.findViewById(R.id.add_to_queue_text_view);
        addToQueue.setOnClickListener(v -> {
            MusicPlayerRemote.enqueue(song);
            ((MainActivity) requireActivity()).setBottomSheetInPeek(true);
            dismissBottomSheet();
        });

        TextView rate = view.findViewById(R.id.rate_text_view);
        rate.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putParcelable("song_object", song);

            RatingDialog dialog = new RatingDialog();
            dialog.setArguments(bundle);
            dialog.show(requireActivity().getSupportFragmentManager(), null);

            dismissBottomSheet();
        });

        TextView download = view.findViewById(R.id.download_text_view);
        download.setOnClickListener(v -> {
            DownloadUtil.getDownloadTracker(requireContext()).download(Collections.singletonList(song), null, null);
            dismissBottomSheet();
        });

        TextView remove = view.findViewById(R.id.remove_text_view);
        remove.setOnClickListener(v -> {
            DownloadUtil.getDownloadTracker(requireContext()).remove(Collections.singletonList(song));
            dismissBottomSheet();
        });

        initDownloadUI(download, remove);

        TextView addToPlaylist = view.findViewById(R.id.add_to_playlist_text_view);
        addToPlaylist.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putParcelable("song_object", song);

            PlaylistChooserDialog dialog = new PlaylistChooserDialog();
            dialog.setArguments(bundle);
            dialog.show(requireActivity().getSupportFragmentManager(), null);

            dismissBottomSheet();
        });

        TextView goToAlbum = view.findViewById(R.id.go_to_album_text_view);
        goToAlbum.setOnClickListener(v -> songBottomSheetViewModel.getAlbum().observe(requireActivity(), album -> {
            if (album != null) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("album_object", album);
                NavHostFragment.findNavController(this).navigate(R.id.albumPageFragment, bundle);
            } else
                Toast.makeText(requireContext(), getString(R.string.song_bottom_sheet_error_retrieving_album), Toast.LENGTH_SHORT).show();

            dismissBottomSheet();
        }));

        TextView goToArtist = view.findViewById(R.id.go_to_artist_text_view);
        goToArtist.setOnClickListener(v -> songBottomSheetViewModel.getArtist().observe(requireActivity(), artist -> {
            if (artist != null) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("artist_object", artist);
                NavHostFragment.findNavController(this).navigate(R.id.artistPageFragment, bundle);
            } else
                Toast.makeText(requireContext(), getString(R.string.song_bottom_sheet_error_retrieving_artist), Toast.LENGTH_SHORT).show();

            dismissBottomSheet();
        }));
    }

    @Override
    public void onClick(View v) {
        dismissBottomSheet();
    }

    private void dismissBottomSheet() {
        dismiss();
    }

    private void initDownloadUI(TextView download, TextView remove) {
        if (DownloadUtil.getDownloadTracker(requireContext()).isDownloaded(song)) {
            download.setVisibility(View.GONE);
            remove.setVisibility(View.VISIBLE);
        } else {
            download.setVisibility(View.VISIBLE);
            remove.setVisibility(View.GONE);
        }
    }
}