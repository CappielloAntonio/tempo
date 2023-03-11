package com.cappielloantonio.play.ui.fragment;

import android.content.ComponentName;
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

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.cappielloantonio.play.R;
import com.cappielloantonio.play.databinding.FragmentAlbumPageBinding;
import com.cappielloantonio.play.glide.CustomGlideRequest;
import com.cappielloantonio.play.interfaces.ClickCallback;
import com.cappielloantonio.play.model.Download;
import com.cappielloantonio.play.service.MediaManager;
import com.cappielloantonio.play.service.MediaService;
import com.cappielloantonio.play.ui.activity.MainActivity;
import com.cappielloantonio.play.ui.adapter.SongHorizontalAdapter;
import com.cappielloantonio.play.util.Constants;
import com.cappielloantonio.play.util.DownloadUtil;
import com.cappielloantonio.play.util.MappingUtil;
import com.cappielloantonio.play.util.MusicUtil;
import com.cappielloantonio.play.viewmodel.AlbumPageViewModel;
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
                DownloadUtil.getDownloadTracker(requireContext()).download(
                        MappingUtil.mapMediaItems(songs, false),
                        songs.stream().map(Download::new).collect(Collectors.toList())
                );
            });
            return true;
        }

        return false;
    }

    private void init() {
        albumPageViewModel.setAlbum(requireArguments().getParcelable(Constants.ALBUM_OBJECT));
    }

    private void initAppBar() {
        activity.setSupportActionBar(bind.animToolbar);

        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        bind.animToolbar.setTitle(MusicUtil.getReadableString(albumPageViewModel.getAlbum().getName()));

        bind.albumNameLabel.setText(MusicUtil.getReadableString(albumPageViewModel.getAlbum().getName()));
        bind.albumArtistLabel.setText(MusicUtil.getReadableString(albumPageViewModel.getAlbum().getArtist()));
        bind.albumReleaseYearLabel.setText(albumPageViewModel.getAlbum().getYear() != 0 ? String.valueOf(albumPageViewModel.getAlbum().getYear()) : "");

        bind.animToolbar.setNavigationOnClickListener(v -> activity.navController.navigateUp());

        Objects.requireNonNull(bind.animToolbar.getOverflowIcon()).setTint(requireContext().getResources().getColor(R.color.titleTextColor, null));
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
        CustomGlideRequest.Builder
                .from(requireContext(), albumPageViewModel.getAlbum().getCoverArtId(), CustomGlideRequest.ALBUM_PIC)
                .build()
                .transition(DrawableTransitionOptions.withCrossFade())
                .transform(new CenterCrop(), new RoundedCorners(CustomGlideRequest.CORNER_RADIUS))
                .into(bind.albumCoverImageView);
    }

    private void initSongsView() {
        bind.songRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        bind.songRecyclerView.setHasFixedSize(true);

        songHorizontalAdapter = new SongHorizontalAdapter(this, false);
        bind.songRecyclerView.setAdapter(songHorizontalAdapter);

        albumPageViewModel.getAlbumSongLiveList().observe(getViewLifecycleOwner(), songs -> songHorizontalAdapter.setItems(songs));
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