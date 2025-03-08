package com.cappielloantonio.tempo.ui.fragment;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.session.MediaBrowser;
import androidx.media3.session.SessionToken;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cappielloantonio.tempo.R;
import com.cappielloantonio.tempo.databinding.FragmentAlbumPageBinding;
import com.cappielloantonio.tempo.glide.CustomGlideRequest;
import com.cappielloantonio.tempo.interfaces.ClickCallback;
import com.cappielloantonio.tempo.model.Download;
import com.cappielloantonio.tempo.service.MediaManager;
import com.cappielloantonio.tempo.service.MediaService;
import com.cappielloantonio.tempo.ui.activity.MainActivity;
import com.cappielloantonio.tempo.ui.adapter.SongHorizontalAdapter;
import com.cappielloantonio.tempo.util.Constants;
import com.cappielloantonio.tempo.util.DownloadUtil;
import com.cappielloantonio.tempo.util.MappingUtil;
import com.cappielloantonio.tempo.util.MusicUtil;
import com.cappielloantonio.tempo.viewmodel.AlbumPageViewModel;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;

@UnstableApi
public class AlbumPageFragment extends Fragment implements ClickCallback {
    private FragmentAlbumPageBinding bind;
    private MainActivity activity;
    private AlbumPageViewModel albumPageViewModel;
    private SongHorizontalAdapter songHorizontalAdapter;
    private ListenableFuture<MediaBrowser> mediaBrowserListenableFuture;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.album_page_menu, menu);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();

        bind = FragmentAlbumPageBinding.inflate(inflater, container, false);
        View view = bind.getRoot();
        albumPageViewModel = new ViewModelProvider(requireActivity()).get(AlbumPageViewModel.class);

        init();
        initAppBar();
        initAlbumInfoTextButton();
        initAlbumNotes();
        initMusicButton();
        initBackCover();
        initSongsView();

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind = null;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_download_album) {
            albumPageViewModel.getAlbumSongLiveList().observe(getViewLifecycleOwner(), songs -> {
                DownloadUtil.getDownloadTracker(requireContext()).download(MappingUtil.mapDownloads(songs), songs.stream().map(Download::new).collect(Collectors.toList()));
            });
            return true;
        }
        if (item.getItemId() == R.id.action_add_to_queue) {
            albumPageViewModel.getAlbumSongLiveList().observe(getViewLifecycleOwner(), songs -> {
                MediaManager.enqueue(mediaBrowserListenableFuture, songs, false);
            });

            return true;
        }

        return false;
    }

    private void init() {
        albumPageViewModel.setAlbum(getViewLifecycleOwner(), requireArguments().getParcelable(Constants.ALBUM_OBJECT));
    }

    private void initAppBar() {
        activity.setSupportActionBar(bind.animToolbar);

        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            activity.getSupportActionBar().setDisplayShowHomeEnabled(true);

        }

        albumPageViewModel.getAlbum().observe(getViewLifecycleOwner(), album -> {
            if (bind != null && album != null) {
                bind.animToolbar.setTitle(album.getName());

                bind.albumNameLabel.setText(album.getName());
                bind.albumArtistLabel.setText(album.getArtist());
                bind.albumReleaseYearLabel.setText(album.getYear() != 0 ? String.valueOf(album.getYear()) : "");
                bind.albumSongCountDurationTextview.setText(getString(R.string.album_page_tracks_count_and_duration, album.getSongCount(), album.getDuration() != null ? album.getDuration() / 60 : 0));
                bind.albumGenresTextview.setText(album.getGenre());

                if (album.getReleaseDate() != null && album.getOriginalReleaseDate() != null) {
                    bind.albumReleaseYearsTextview.setVisibility(View.VISIBLE);

                    if (album.getReleaseDate() == null || album.getOriginalReleaseDate() == null) {
                        bind.albumReleaseYearsTextview.setText(getString(R.string.album_page_release_date_label, album.getReleaseDate() != null ? album.getReleaseDate().getFormattedDate() : album.getOriginalReleaseDate().getFormattedDate()));
                    }

                    if (album.getReleaseDate() != null && album.getOriginalReleaseDate() != null) {
                        if (Objects.equals(album.getReleaseDate().getYear(), album.getOriginalReleaseDate().getYear()) && Objects.equals(album.getReleaseDate().getMonth(), album.getOriginalReleaseDate().getMonth()) && Objects.equals(album.getReleaseDate().getDay(), album.getOriginalReleaseDate().getDay())) {
                            bind.albumReleaseYearsTextview.setText(getString(R.string.album_page_release_date_label, album.getReleaseDate().getFormattedDate()));
                        } else {
                            bind.albumReleaseYearsTextview.setText(getString(R.string.album_page_release_dates_label, album.getReleaseDate().getFormattedDate(), album.getOriginalReleaseDate().getFormattedDate()));
                        }
                    }
                }
            }
        });

        bind.animToolbar.setNavigationOnClickListener(v -> activity.navController.navigateUp());

        Objects.requireNonNull(bind.animToolbar.getOverflowIcon()).setTint(requireContext().getResources().getColor(R.color.titleTextColor, null));

        bind.albumOtherInfoButton.setOnClickListener(v -> {
            if (bind.albumDetailView.getVisibility() == View.GONE) {
                bind.albumDetailView.setVisibility(View.VISIBLE);
            } else if (bind.albumDetailView.getVisibility() == View.VISIBLE) {
                bind.albumDetailView.setVisibility(View.GONE);
            }
        });
    }

    private void initAlbumInfoTextButton() {
        bind.albumArtistLabel.setOnClickListener(v -> albumPageViewModel.getArtist().observe(getViewLifecycleOwner(), artist -> {
            if (artist != null) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(Constants.ARTIST_OBJECT, artist);
                activity.navController.navigate(R.id.action_albumPageFragment_to_artistPageFragment, bundle);
            } else
                Toast.makeText(requireContext(), getString(R.string.album_error_retrieving_artist), Toast.LENGTH_SHORT).show();
        }));
    }

    private void initAlbumNotes() {
        albumPageViewModel.getAlbumInfo().observe(getViewLifecycleOwner(), albumInfo -> {
            if (albumInfo != null) {
                if (bind != null) bind.albumNotesTextview.setVisibility(View.VISIBLE);
                if (bind != null)
                    bind.albumNotesTextview.setText(MusicUtil.forceReadableString(albumInfo.getNotes()));

                if (bind != null && albumInfo.getLastFmUrl() != null && !albumInfo.getLastFmUrl().isEmpty()) {
                    bind.albumNotesTextview.setOnClickListener(v -> {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(albumInfo.getLastFmUrl()));
                        startActivity(intent);
                    });
                }
            } else {
                if (bind != null) bind.albumNotesTextview.setVisibility(View.GONE);
            }
        });
    }

    private void initMusicButton() {
        albumPageViewModel.getAlbumSongLiveList().observe(getViewLifecycleOwner(), songs -> {
            if (bind != null && !songs.isEmpty()) {
                bind.albumPagePlayButton.setOnClickListener(v -> {
                    MediaManager.startQueue(mediaBrowserListenableFuture, songs, 0);
                    activity.setBottomSheetInPeek(true);
                });

                bind.albumPageShuffleButton.setOnClickListener(v -> {
                    Collections.shuffle(songs);
                    MediaManager.startQueue(mediaBrowserListenableFuture, songs, 0);
                    activity.setBottomSheetInPeek(true);
                });
            }

            if (bind != null && songs.isEmpty()) {
                bind.albumPagePlayButton.setEnabled(false);
                bind.albumPageShuffleButton.setEnabled(false);
            }
        });
    }

    private void initBackCover() {
        albumPageViewModel.getAlbum().observe(getViewLifecycleOwner(), album -> {
            if (bind != null && album != null) {
                CustomGlideRequest.Builder.from(requireContext(), album.getCoverArtId(), CustomGlideRequest.ResourceType.Album).build().into(bind.albumCoverImageView);
            }
        });
    }

    private void initSongsView() {
        albumPageViewModel.getAlbum().observe(getViewLifecycleOwner(), album -> {
            if (bind != null && album != null) {
                bind.songRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                bind.songRecyclerView.setHasFixedSize(true);

                songHorizontalAdapter = new SongHorizontalAdapter(this, false, false, album);
                bind.songRecyclerView.setAdapter(songHorizontalAdapter);

                albumPageViewModel.getAlbumSongLiveList().observe(getViewLifecycleOwner(), songs -> songHorizontalAdapter.setItems(songs));
            }
        });
    }

    private void initializeMediaBrowser() {
        mediaBrowserListenableFuture = new MediaBrowser.Builder(requireContext(), new SessionToken(requireContext(), new ComponentName(requireContext(), MediaService.class))).buildAsync();
    }

    private void releaseMediaBrowser() {
        MediaBrowser.releaseFuture(mediaBrowserListenableFuture);
    }

    @Override
    public void onMediaClick(Bundle bundle) {
        MediaManager.startQueue(mediaBrowserListenableFuture, bundle.getParcelableArrayList(Constants.TRACKS_OBJECT), bundle.getInt(Constants.ITEM_POSITION));
        activity.setBottomSheetInPeek(true);
    }

    @Override
    public void onMediaLongClick(Bundle bundle) {
        Navigation.findNavController(requireView()).navigate(R.id.songBottomSheetDialog, bundle);
    }
}