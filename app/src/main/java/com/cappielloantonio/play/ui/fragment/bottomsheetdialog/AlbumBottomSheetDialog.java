package com.cappielloantonio.play.ui.fragment.bottomsheetdialog;

import android.content.ComponentName;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.media3.common.MediaItem;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.session.MediaBrowser;
import androidx.media3.session.SessionToken;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.cappielloantonio.play.App;
import com.cappielloantonio.play.R;
import com.cappielloantonio.play.glide.CustomGlideRequest;
import com.cappielloantonio.play.interfaces.MediaCallback;
import com.cappielloantonio.play.model.Album;
import com.cappielloantonio.play.model.Download;
import com.cappielloantonio.play.model.Media;
import com.cappielloantonio.play.repository.AlbumRepository;
import com.cappielloantonio.play.service.MediaManager;
import com.cappielloantonio.play.service.MediaService;
import com.cappielloantonio.play.ui.activity.MainActivity;
import com.cappielloantonio.play.util.DownloadUtil;
import com.cappielloantonio.play.util.MappingUtil;
import com.cappielloantonio.play.util.MusicUtil;
import com.cappielloantonio.play.viewmodel.AlbumBottomSheetViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@UnstableApi
public class AlbumBottomSheetDialog extends BottomSheetDialogFragment implements View.OnClickListener {
    private AlbumBottomSheetViewModel albumBottomSheetViewModel;
    private Album album;

    private ListenableFuture<MediaBrowser> mediaBrowserListenableFuture;

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

    @Override
    public void onStart() {
        super.onStart();

        initializeMediaBrowser();
    }

    @Override
    public void onStop() {
        releaseMediaBrowser();
        super.onStop();
    }

    private void init(View view) {
        ImageView coverAlbum = view.findViewById(R.id.album_cover_image_view);
        CustomGlideRequest.Builder
                .from(requireContext(), albumBottomSheetViewModel.getAlbum().getPrimary(), CustomGlideRequest.ALBUM_PIC, null)
                .build()
                .transform(new CenterCrop(), new RoundedCorners(CustomGlideRequest.CORNER_RADIUS))
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
                    exception.printStackTrace();
                }

                @Override
                public void onLoadMedia(List<?> media) {
                    if (media.size() > 0) {
                        MediaManager.startQueue(mediaBrowserListenableFuture, requireContext(), (ArrayList<Media>) media, 0);
                        ((MainActivity) requireActivity()).setBottomSheetInPeek(true);
                    }

                    dismissBottomSheet();
                }
            });
        });

        TextView playRandom = view.findViewById(R.id.play_random_text_view);
        playRandom.setOnClickListener(v -> {
            AlbumRepository albumRepository = new AlbumRepository(App.getInstance());
            albumRepository.getAlbumTracks(album.getId()).observe(getViewLifecycleOwner(), songs -> {
                Collections.shuffle(songs);

                MediaManager.startQueue(mediaBrowserListenableFuture, requireContext(), songs, 0);
                ((MainActivity) requireActivity()).setBottomSheetInPeek(true);

                dismissBottomSheet();
            });
        });

        TextView playNext = view.findViewById(R.id.play_next_text_view);
        playNext.setOnClickListener(v -> albumBottomSheetViewModel.getAlbumTracks().observe(getViewLifecycleOwner(), songs -> {
            MediaManager.enqueue(mediaBrowserListenableFuture, requireContext(), songs, true);
            ((MainActivity) requireActivity()).setBottomSheetInPeek(true);

            dismissBottomSheet();
        }));

        TextView addToQueue = view.findViewById(R.id.add_to_queue_text_view);
        addToQueue.setOnClickListener(v -> albumBottomSheetViewModel.getAlbumTracks().observe(getViewLifecycleOwner(), songs -> {
            MediaManager.enqueue(mediaBrowserListenableFuture, requireContext(), songs, false);
            ((MainActivity) requireActivity()).setBottomSheetInPeek(true);

            dismissBottomSheet();
        }));

        TextView downloadAll = view.findViewById(R.id.download_all_text_view);
        TextView removeAll = view.findViewById(R.id.remove_all_text_view);

        albumBottomSheetViewModel.getAlbumTracks().observe(getViewLifecycleOwner(), songs -> {
            List<MediaItem> mediaItems = MappingUtil.mapMediaItems(requireContext(), songs, false);
            List<Download> downloads = MappingUtil.mapDownload(songs, null, null);

            downloadAll.setOnClickListener(v -> {
                DownloadUtil.getDownloadTracker(requireContext()).download(mediaItems, downloads);
                dismissBottomSheet();
            });

            if (DownloadUtil.getDownloadTracker(requireContext()).areDownloaded(mediaItems)) {
                removeAll.setOnClickListener(v -> {
                    DownloadUtil.getDownloadTracker(requireContext()).remove(mediaItems, downloads);
                    dismissBottomSheet();
                });
            } else {
                removeAll.setVisibility(View.GONE);
            }
        });

        TextView goToArtist = view.findViewById(R.id.go_to_artist_text_view);
        goToArtist.setOnClickListener(v -> albumBottomSheetViewModel.getArtist().observe(getViewLifecycleOwner(), artist -> {
            if (artist != null) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("artist_object", artist);
                NavHostFragment.findNavController(this).navigate(R.id.artistPageFragment, bundle);
            } else {
                Toast.makeText(requireContext(), getString(R.string.album_error_retrieving_artist), Toast.LENGTH_SHORT).show();
            }

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

    private void initializeMediaBrowser() {
        mediaBrowserListenableFuture = new MediaBrowser.Builder(requireContext(), new SessionToken(requireContext(), new ComponentName(requireContext(), MediaService.class))).buildAsync();
    }

    private void releaseMediaBrowser() {
        MediaBrowser.releaseFuture(mediaBrowserListenableFuture);
    }
}