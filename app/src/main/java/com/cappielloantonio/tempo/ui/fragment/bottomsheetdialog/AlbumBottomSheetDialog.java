package com.cappielloantonio.tempo.ui.fragment.bottomsheetdialog;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
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

import com.cappielloantonio.tempo.R;
import com.cappielloantonio.tempo.glide.CustomGlideRequest;
import com.cappielloantonio.tempo.interfaces.MediaCallback;
import com.cappielloantonio.tempo.model.Download;
import com.cappielloantonio.tempo.repository.AlbumRepository;
import com.cappielloantonio.tempo.service.MediaManager;
import com.cappielloantonio.tempo.service.MediaService;
import com.cappielloantonio.tempo.subsonic.models.AlbumID3;
import com.cappielloantonio.tempo.subsonic.models.Child;
import com.cappielloantonio.tempo.ui.activity.MainActivity;
import com.cappielloantonio.tempo.util.Constants;
import com.cappielloantonio.tempo.util.DownloadUtil;
import com.cappielloantonio.tempo.util.MappingUtil;
import com.cappielloantonio.tempo.util.MusicUtil;
import com.cappielloantonio.tempo.util.Preferences;
import com.cappielloantonio.tempo.viewmodel.AlbumBottomSheetViewModel;
import com.cappielloantonio.tempo.viewmodel.HomeViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@UnstableApi
public class AlbumBottomSheetDialog extends BottomSheetDialogFragment implements View.OnClickListener {
    private HomeViewModel homeViewModel;
    private AlbumBottomSheetViewModel albumBottomSheetViewModel;
    private AlbumID3 album;

    private ListenableFuture<MediaBrowser> mediaBrowserListenableFuture;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_album_dialog, container, false);

        album = this.requireArguments().getParcelable(Constants.ALBUM_OBJECT);

        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
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
                .from(requireContext(), albumBottomSheetViewModel.getAlbum().getCoverArtId(), CustomGlideRequest.ResourceType.Album)
                .build()
                .into(coverAlbum);

        TextView titleAlbum = view.findViewById(R.id.album_title_text_view);
        titleAlbum.setText(MusicUtil.getReadableString(albumBottomSheetViewModel.getAlbum().getName()));
        titleAlbum.setSelected(true);

        TextView artistAlbum = view.findViewById(R.id.album_artist_text_view);
        artistAlbum.setText(MusicUtil.getReadableString(albumBottomSheetViewModel.getAlbum().getArtist()));

        ToggleButton favoriteToggle = view.findViewById(R.id.button_favorite);
        favoriteToggle.setChecked(albumBottomSheetViewModel.getAlbum().getStarred() != null);
        favoriteToggle.setOnClickListener(v -> {
            albumBottomSheetViewModel.setFavorite();
        });

        TextView playRadio = view.findViewById(R.id.play_radio_text_view);
        playRadio.setOnClickListener(v -> {
            AlbumRepository albumRepository = new AlbumRepository();
            albumRepository.getInstantMix(album, 20, new MediaCallback() {
                @Override
                public void onError(Exception exception) {
                    exception.printStackTrace();
                }

                @Override
                public void onLoadMedia(List<?> media) {
                    if (media.size() > 0) {
                        MediaManager.startQueue(mediaBrowserListenableFuture, (ArrayList<Child>) media, 0);
                        ((MainActivity) requireActivity()).setBottomSheetInPeek(true);
                    }

                    dismissBottomSheet();
                }
            });
        });

        TextView playRandom = view.findViewById(R.id.play_random_text_view);
        playRandom.setOnClickListener(v -> {
            AlbumRepository albumRepository = new AlbumRepository();
            albumRepository.getAlbumTracks(album.getId()).observe(getViewLifecycleOwner(), songs -> {
                Collections.shuffle(songs);

                MediaManager.startQueue(mediaBrowserListenableFuture, songs, 0);
                ((MainActivity) requireActivity()).setBottomSheetInPeek(true);

                dismissBottomSheet();
            });
        });

        TextView playNext = view.findViewById(R.id.play_next_text_view);
        playNext.setOnClickListener(v -> albumBottomSheetViewModel.getAlbumTracks().observe(getViewLifecycleOwner(), songs -> {
            MediaManager.enqueue(mediaBrowserListenableFuture, songs, true);
            ((MainActivity) requireActivity()).setBottomSheetInPeek(true);

            dismissBottomSheet();
        }));

        TextView addToQueue = view.findViewById(R.id.add_to_queue_text_view);
        addToQueue.setOnClickListener(v -> albumBottomSheetViewModel.getAlbumTracks().observe(getViewLifecycleOwner(), songs -> {
            MediaManager.enqueue(mediaBrowserListenableFuture, songs, false);
            ((MainActivity) requireActivity()).setBottomSheetInPeek(true);

            dismissBottomSheet();
        }));

        TextView downloadAll = view.findViewById(R.id.download_all_text_view);
        albumBottomSheetViewModel.getAlbumTracks().observe(getViewLifecycleOwner(), songs -> {
            List<MediaItem> mediaItems = MappingUtil.mapDownloads(songs);
            List<Download> downloads = songs.stream().map(Download::new).collect(Collectors.toList());

            downloadAll.setOnClickListener(v -> {
                DownloadUtil.getDownloadTracker(requireContext()).download(mediaItems, downloads);
                dismissBottomSheet();
            });
        });

        TextView removeAll = view.findViewById(R.id.remove_all_text_view);
        albumBottomSheetViewModel.getAlbumTracks().observe(getViewLifecycleOwner(), songs -> {
            List<MediaItem> mediaItems = MappingUtil.mapDownloads(songs);
            List<Download> downloads = songs.stream().map(Download::new).collect(Collectors.toList());

            removeAll.setOnClickListener(v -> {
                DownloadUtil.getDownloadTracker(requireContext()).remove(mediaItems, downloads);
                dismissBottomSheet();
            });
        });

        initDownloadUI(removeAll);

        TextView goToArtist = view.findViewById(R.id.go_to_artist_text_view);
        goToArtist.setOnClickListener(v -> albumBottomSheetViewModel.getArtist().observe(getViewLifecycleOwner(), artist -> {
            if (artist != null) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(Constants.ARTIST_OBJECT, artist);
                NavHostFragment.findNavController(this).navigate(R.id.artistPageFragment, bundle);
            } else {
                Toast.makeText(requireContext(), getString(R.string.album_error_retrieving_artist), Toast.LENGTH_SHORT).show();
            }

            dismissBottomSheet();
        }));

        TextView share = view.findViewById(R.id.share_text_view);
        share.setOnClickListener(v -> albumBottomSheetViewModel.shareAlbum().observe(getViewLifecycleOwner(), sharedAlbum -> {
            if (sharedAlbum != null) {
                ClipboardManager clipboardManager = (ClipboardManager) requireActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText(getString(R.string.app_name), sharedAlbum.getUrl());
                clipboardManager.setPrimaryClip(clipData);
                refreshShares();
                dismissBottomSheet();
            } else {
                Toast.makeText(requireContext(), getString(R.string.share_unsupported_error), Toast.LENGTH_SHORT).show();
                dismissBottomSheet();
            }
        }));

        share.setVisibility(Preferences.isSharingEnabled() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onClick(View v) {
        dismissBottomSheet();
    }

    private void dismissBottomSheet() {
        dismiss();
    }

    private void initDownloadUI(TextView removeAll) {
        albumBottomSheetViewModel.getAlbumTracks().observe(getViewLifecycleOwner(), songs -> {
            List<MediaItem> mediaItems = MappingUtil.mapDownloads(songs);

            if (DownloadUtil.getDownloadTracker(requireContext()).areDownloaded(mediaItems)) {
                removeAll.setVisibility(View.VISIBLE);
            }
        });
    }

    private void initializeMediaBrowser() {
        mediaBrowserListenableFuture = new MediaBrowser.Builder(requireContext(), new SessionToken(requireContext(), new ComponentName(requireContext(), MediaService.class))).buildAsync();
    }

    private void releaseMediaBrowser() {
        MediaBrowser.releaseFuture(mediaBrowserListenableFuture);
    }

    private void refreshShares() {
        homeViewModel.refreshShares(requireActivity());
    }
}