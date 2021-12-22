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
import com.cappielloantonio.play.model.Album;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.repository.AlbumRepository;
import com.cappielloantonio.play.repository.QueueRepository;
import com.cappielloantonio.play.service.MusicPlayerRemote;
import com.cappielloantonio.play.ui.activity.MainActivity;
import com.cappielloantonio.play.util.DownloadUtil;
import com.cappielloantonio.play.util.MusicUtil;
import com.cappielloantonio.play.viewmodel.AlbumBottomSheetViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AlbumBottomSheetDialog extends BottomSheetDialogFragment implements View.OnClickListener {
    private static final String TAG = "AlbumBottomSheetDialog";

    private AlbumBottomSheetViewModel albumBottomSheetViewModel;
    private Album album;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_album_dialog, container, false);

        album = this.requireArguments().getParcelable("album_object");

        albumBottomSheetViewModel = new ViewModelProvider(requireActivity()).get(AlbumBottomSheetViewModel.class);
        albumBottomSheetViewModel.setAlbum(album);

        init(view);

        return view;
    }

    private void init(View view) {
        ImageView coverAlbum = view.findViewById(R.id.album_cover_image_view);
        CustomGlideRequest.Builder
                .from(requireContext(), albumBottomSheetViewModel.getAlbum().getPrimary(), CustomGlideRequest.ALBUM_PIC, null)
                .build()
                .transform(new RoundedCorners(CustomGlideRequest.CORNER_RADIUS))
                .into(coverAlbum);

        TextView titleAlbum = view.findViewById(R.id.album_title_text_view);
        titleAlbum.setText(MusicUtil.getReadableString(albumBottomSheetViewModel.getAlbum().getTitle()));
        titleAlbum.setSelected(true);

        TextView artistAlbum = view.findViewById(R.id.album_artist_text_view);
        artistAlbum.setText(MusicUtil.getReadableString(albumBottomSheetViewModel.getAlbum().getArtistName()));

        ToggleButton favoriteToggle = view.findViewById(R.id.button_favorite);
        favoriteToggle.setChecked(albumBottomSheetViewModel.getAlbum().isFavorite());
        favoriteToggle.setOnClickListener(v -> {
            albumBottomSheetViewModel.setFavorite();
            dismissBottomSheet();
        });

        TextView playRadio = view.findViewById(R.id.play_radio_text_view);
        playRadio.setOnClickListener(v -> {
            AlbumRepository albumRepository = new AlbumRepository(App.getInstance());
            albumRepository.getInstantMix(album, 20, new MediaCallback() {
                @Override
                public void onError(Exception exception) {
                    Log.e(TAG, "onError() " + exception.getMessage());

                    dismissBottomSheet();
                }

                @Override
                public void onLoadMedia(List<?> media) {
                    if (media.size() > 0) {
                        QueueRepository queueRepository = new QueueRepository(App.getInstance());
                        queueRepository.insertAllAndStartNew((ArrayList<Song>) media);

                        ((MainActivity) requireActivity()).setBottomSheetInPeek(true);
                        ((MainActivity) requireActivity()).setBottomSheetMusicInfo((Song) media.get(0));

                        MusicPlayerRemote.openQueue((List<Song>) media, 0, true);
                    } else {
                        Toast.makeText(requireContext(), getString(R.string.album_error_retrieving_radio), Toast.LENGTH_SHORT).show();
                    }

                    dismissBottomSheet();
                }
            });
        });

        TextView playRandom = view.findViewById(R.id.play_random_text_view);
        playRandom.setOnClickListener(v -> {
            AlbumRepository albumRepository = new AlbumRepository(App.getInstance());
            albumRepository.getAlbumTracks(album.getId()).observe(requireActivity(), songs -> {
                Collections.shuffle(songs);

                QueueRepository queueRepository = new QueueRepository(App.getInstance());
                queueRepository.insertAllAndStartNew(songs);

                MusicPlayerRemote.openQueue(songs, 0, true);
                ((MainActivity) requireActivity()).setBottomSheetInPeek(true);

                dismissBottomSheet();
            });
        });

        TextView playNext = view.findViewById(R.id.play_next_text_view);
        playNext.setOnClickListener(v -> albumBottomSheetViewModel.getAlbumTracks().observe(requireActivity(), songs -> {
            MusicPlayerRemote.playNext(songs);
            ((MainActivity) requireActivity()).setBottomSheetInPeek(true);
            dismissBottomSheet();
        }));

        TextView addToQueue = view.findViewById(R.id.add_to_queue_text_view);
        addToQueue.setOnClickListener(v -> albumBottomSheetViewModel.getAlbumTracks().observe(requireActivity(), songs -> {
            MusicPlayerRemote.enqueue(songs);
            dismissBottomSheet();
        }));

        TextView downloadAll = view.findViewById(R.id.download_all_text_view);
        TextView removeAll = view.findViewById(R.id.remove_all_text_view);

        albumBottomSheetViewModel.getAlbumTracks().observe(requireActivity(), songs -> {
            downloadAll.setOnClickListener(v -> {
                DownloadUtil.getDownloadTracker(requireContext()).download(songs, null, null);
                dismissBottomSheet();
            });

            if (DownloadUtil.getDownloadTracker(requireContext()).isDownloaded(songs)) {
                removeAll.setOnClickListener(v -> {
                    DownloadUtil.getDownloadTracker(requireContext()).remove(songs);
                    dismissBottomSheet();
                });
            } else {
                removeAll.setVisibility(View.GONE);
            }
        });

        TextView goToArtist = view.findViewById(R.id.go_to_artist_text_view);
        goToArtist.setOnClickListener(v -> albumBottomSheetViewModel.getArtist().observe(requireActivity(), artist -> {
            if (artist != null) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("artist_object", artist);
                NavHostFragment.findNavController(this).navigate(R.id.artistPageFragment, bundle);
            } else
                Toast.makeText(requireContext(), getString(R.string.album_error_retrieving_artist), Toast.LENGTH_SHORT).show();

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
}