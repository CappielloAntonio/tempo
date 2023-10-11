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
import androidx.media3.common.util.UnstableApi;
import androidx.media3.session.MediaBrowser;
import androidx.media3.session.SessionToken;
import androidx.navigation.fragment.NavHostFragment;

import com.cappielloantonio.tempo.R;
import com.cappielloantonio.tempo.glide.CustomGlideRequest;
import com.cappielloantonio.tempo.model.Download;
import com.cappielloantonio.tempo.service.MediaManager;
import com.cappielloantonio.tempo.service.MediaService;
import com.cappielloantonio.tempo.subsonic.models.Child;
import com.cappielloantonio.tempo.ui.activity.MainActivity;
import com.cappielloantonio.tempo.ui.dialog.PlaylistChooserDialog;
import com.cappielloantonio.tempo.ui.dialog.RatingDialog;
import com.cappielloantonio.tempo.util.Constants;
import com.cappielloantonio.tempo.util.DownloadUtil;
import com.cappielloantonio.tempo.util.MappingUtil;
import com.cappielloantonio.tempo.util.MusicUtil;
import com.cappielloantonio.tempo.util.Preferences;
import com.cappielloantonio.tempo.viewmodel.HomeViewModel;
import com.cappielloantonio.tempo.viewmodel.SongBottomSheetViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.common.util.concurrent.ListenableFuture;

@UnstableApi
public class SongBottomSheetDialog extends BottomSheetDialogFragment implements View.OnClickListener {
    private HomeViewModel homeViewModel;
    private SongBottomSheetViewModel songBottomSheetViewModel;
    private Child song;

    private ListenableFuture<MediaBrowser> mediaBrowserListenableFuture;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_song_dialog, container, false);

        song = requireArguments().getParcelable(Constants.TRACK_OBJECT);

        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        songBottomSheetViewModel = new ViewModelProvider(requireActivity()).get(SongBottomSheetViewModel.class);
        songBottomSheetViewModel.setSong(song);

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
        ImageView coverSong = view.findViewById(R.id.song_cover_image_view);
        CustomGlideRequest.Builder
                .from(requireContext(), songBottomSheetViewModel.getSong().getCoverArtId(), CustomGlideRequest.ResourceType.Song)
                .build()
                .into(coverSong);

        TextView titleSong = view.findViewById(R.id.song_title_text_view);
        titleSong.setText(MusicUtil.getReadableString(songBottomSheetViewModel.getSong().getTitle()));

        titleSong.setSelected(true);

        TextView artistSong = view.findViewById(R.id.song_artist_text_view);
        artistSong.setText(MusicUtil.getReadableString(songBottomSheetViewModel.getSong().getArtist()));

        ToggleButton favoriteToggle = view.findViewById(R.id.button_favorite);
        favoriteToggle.setChecked(songBottomSheetViewModel.getSong().getStarred() != null);
        favoriteToggle.setOnClickListener(v -> {
            songBottomSheetViewModel.setFavorite(requireContext());
        });
        favoriteToggle.setOnLongClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putParcelable(Constants.TRACK_OBJECT, song);

            RatingDialog dialog = new RatingDialog();
            dialog.setArguments(bundle);
            dialog.show(requireActivity().getSupportFragmentManager(), null);

            dismissBottomSheet();
            return true;
        });

        TextView playRadio = view.findViewById(R.id.play_radio_text_view);
        playRadio.setOnClickListener(v -> {
            MediaManager.startQueue(mediaBrowserListenableFuture, song);
            ((MainActivity) requireActivity()).setBottomSheetInPeek(true);

            songBottomSheetViewModel.getInstantMix(getViewLifecycleOwner(), song).observe(getViewLifecycleOwner(), songs -> {
                if (songs == null) {
                    dismissBottomSheet();
                    return;
                }

                if (songs.size() > 0) {
                    MediaManager.enqueue(mediaBrowserListenableFuture, songs, true);
                    dismissBottomSheet();
                }
            });
        });

        TextView playNext = view.findViewById(R.id.play_next_text_view);
        playNext.setOnClickListener(v -> {
            MediaManager.enqueue(mediaBrowserListenableFuture, song, true);
            ((MainActivity) requireActivity()).setBottomSheetInPeek(true);
            dismissBottomSheet();
        });

        TextView addToQueue = view.findViewById(R.id.add_to_queue_text_view);
        addToQueue.setOnClickListener(v -> {
            MediaManager.enqueue(mediaBrowserListenableFuture, song, false);
            ((MainActivity) requireActivity()).setBottomSheetInPeek(true);
            dismissBottomSheet();
        });

        TextView rate = view.findViewById(R.id.rate_text_view);
        rate.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putParcelable(Constants.TRACK_OBJECT, song);

            RatingDialog dialog = new RatingDialog();
            dialog.setArguments(bundle);
            dialog.show(requireActivity().getSupportFragmentManager(), null);

            dismissBottomSheet();
        });

        TextView download = view.findViewById(R.id.download_text_view);
        download.setOnClickListener(v -> {
            DownloadUtil.getDownloadTracker(requireContext()).download(
                    MappingUtil.mapDownload(song),
                    new Download(song)
            );
            dismissBottomSheet();
        });

        TextView remove = view.findViewById(R.id.remove_text_view);
        remove.setOnClickListener(v -> {
            DownloadUtil.getDownloadTracker(requireContext()).remove(
                    MappingUtil.mapDownload(song),
                    new Download(song)
            );
            dismissBottomSheet();
        });

        initDownloadUI(download, remove);

        TextView addToPlaylist = view.findViewById(R.id.add_to_playlist_text_view);
        addToPlaylist.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putParcelable(Constants.TRACK_OBJECT, song);

            PlaylistChooserDialog dialog = new PlaylistChooserDialog();
            dialog.setArguments(bundle);
            dialog.show(requireActivity().getSupportFragmentManager(), null);

            dismissBottomSheet();
        });

        TextView goToAlbum = view.findViewById(R.id.go_to_album_text_view);
        goToAlbum.setOnClickListener(v -> songBottomSheetViewModel.getAlbum().observe(getViewLifecycleOwner(), album -> {
            if (album != null) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(Constants.ALBUM_OBJECT, album);
                NavHostFragment.findNavController(this).navigate(R.id.albumPageFragment, bundle);
            } else
                Toast.makeText(requireContext(), getString(R.string.song_bottom_sheet_error_retrieving_album), Toast.LENGTH_SHORT).show();

            dismissBottomSheet();
        }));

        goToAlbum.setVisibility(songBottomSheetViewModel.getSong().getAlbumId() != null ? View.VISIBLE : View.GONE);

        TextView goToArtist = view.findViewById(R.id.go_to_artist_text_view);
        goToArtist.setOnClickListener(v -> songBottomSheetViewModel.getArtist().observe(getViewLifecycleOwner(), artist -> {
            if (artist != null) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(Constants.ARTIST_OBJECT, artist);
                NavHostFragment.findNavController(this).navigate(R.id.artistPageFragment, bundle);
            } else
                Toast.makeText(requireContext(), getString(R.string.song_bottom_sheet_error_retrieving_artist), Toast.LENGTH_SHORT).show();

            dismissBottomSheet();
        }));

        goToArtist.setVisibility(songBottomSheetViewModel.getSong().getArtistId() != null ? View.VISIBLE : View.GONE);

        TextView share = view.findViewById(R.id.share_text_view);
        share.setOnClickListener(v -> songBottomSheetViewModel.shareTrack().observe(getViewLifecycleOwner(), sharedTrack -> {
            if (sharedTrack != null) {
                ClipboardManager clipboardManager = (ClipboardManager) requireActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText(getString(R.string.app_name), sharedTrack.getUrl());
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

    private void initDownloadUI(TextView download, TextView remove) {
        if (DownloadUtil.getDownloadTracker(requireContext()).isDownloaded(song.getId())) {
            remove.setVisibility(View.VISIBLE);
        } else {
            download.setVisibility(View.VISIBLE);
            remove.setVisibility(View.GONE);
        }
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