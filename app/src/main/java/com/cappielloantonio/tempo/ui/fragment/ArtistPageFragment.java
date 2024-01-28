package com.cappielloantonio.tempo.ui.fragment;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.session.MediaBrowser;
import androidx.media3.session.SessionToken;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cappielloantonio.tempo.R;
import com.cappielloantonio.tempo.databinding.FragmentArtistPageBinding;
import com.cappielloantonio.tempo.glide.CustomGlideRequest;
import com.cappielloantonio.tempo.helper.recyclerview.CustomLinearSnapHelper;
import com.cappielloantonio.tempo.helper.recyclerview.GridItemDecoration;
import com.cappielloantonio.tempo.interfaces.ClickCallback;
import com.cappielloantonio.tempo.service.MediaManager;
import com.cappielloantonio.tempo.service.MediaService;
import com.cappielloantonio.tempo.ui.activity.MainActivity;
import com.cappielloantonio.tempo.ui.adapter.AlbumArtistPageOrSimilarAdapter;
import com.cappielloantonio.tempo.ui.adapter.AlbumCatalogueAdapter;
import com.cappielloantonio.tempo.ui.adapter.ArtistSimilarAdapter;
import com.cappielloantonio.tempo.ui.adapter.SongHorizontalAdapter;
import com.cappielloantonio.tempo.util.Constants;
import com.cappielloantonio.tempo.util.MusicUtil;
import com.cappielloantonio.tempo.util.Preferences;
import com.cappielloantonio.tempo.viewmodel.ArtistPageViewModel;
import com.google.common.util.concurrent.ListenableFuture;

@UnstableApi
public class ArtistPageFragment extends Fragment implements ClickCallback {
    private FragmentArtistPageBinding bind;
    private MainActivity activity;
    private ArtistPageViewModel artistPageViewModel;

    private SongHorizontalAdapter songHorizontalAdapter;
    private AlbumArtistPageOrSimilarAdapter albumArtistPageOrSimilarAdapter;
    private AlbumCatalogueAdapter albumCatalogueAdapter;
    private ArtistSimilarAdapter artistSimilarAdapter;

    private ListenableFuture<MediaBrowser> mediaBrowserListenableFuture;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();

        bind = FragmentArtistPageBinding.inflate(inflater, container, false);
        View view = bind.getRoot();
        artistPageViewModel = new ViewModelProvider(requireActivity()).get(ArtistPageViewModel.class);

        init();
        initAppBar();
        initArtistInfo();
        initPlayButtons();
        initTopSongsView();
        initHorizontalAlbumsView();
        initVerticalAlbumsView();
        initSimilarArtistsView();

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

    private void init() {
        artistPageViewModel.setArtist(requireArguments().getParcelable(Constants.ARTIST_OBJECT));

        bind.mostStreamedSongTextViewClickable.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.MEDIA_BY_ARTIST, Constants.MEDIA_BY_ARTIST);
            bundle.putParcelable(Constants.ARTIST_OBJECT, artistPageViewModel.getArtist());
            activity.navController.navigate(R.id.action_artistPageFragment_to_songListPageFragment, bundle);
        });

        bind.artistPageAlbumsSwitchLayoutTextViewClickable.setOnClickListener(view -> {
            boolean isHorizontalRecyclerViewVisible = bind.albumsHorizontalRecyclerView.getVisibility() == View.VISIBLE;

            bind.albumsHorizontalRecyclerView.setVisibility(isHorizontalRecyclerViewVisible ? View.GONE : View.VISIBLE);
            bind.albumsVerticalRecyclerView.setVisibility(isHorizontalRecyclerViewVisible ? View.VISIBLE : View.GONE);

            Preferences.setArtistAlbumLayout(!isHorizontalRecyclerViewVisible);
        });

        bind.albumsHorizontalRecyclerView.setVisibility(Preferences.isArtistAlbumLayoutHorizontal() ? View.VISIBLE : View.GONE);
        bind.albumsVerticalRecyclerView.setVisibility(Preferences.isArtistAlbumLayoutHorizontal() ? View.GONE : View.VISIBLE);
    }

    private void initAppBar() {
        activity.setSupportActionBar(bind.animToolbar);
        if (activity.getSupportActionBar() != null)
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bind.collapsingToolbar.setTitle(MusicUtil.getReadableString(artistPageViewModel.getArtist().getName()));
        bind.animToolbar.setNavigationOnClickListener(v -> activity.navController.navigateUp());
        bind.collapsingToolbar.setExpandedTitleColor(getResources().getColor(R.color.white, null));
    }

    private void initArtistInfo() {
        artistPageViewModel.getArtistInfo(artistPageViewModel.getArtist().getId()).observe(getViewLifecycleOwner(), artistInfo -> {
            if (artistInfo == null) {
                if (bind != null)
                    bind.artistPageBioPlaceholder.placeholder.setVisibility(View.VISIBLE);
                if (bind != null) bind.artistPageBioSector.setVisibility(View.GONE);
            } else {
                String normalizedBio = MusicUtil.forceReadableString(artistInfo.getBiography());

                if (bind != null)
                    bind.artistPageBioSector.setVisibility(!normalizedBio.trim().isEmpty() ? View.VISIBLE : View.GONE);
                if (bind != null)
                    bind.bioMoreTextViewClickable.setVisibility(artistInfo.getLastFmUrl() != null ? View.VISIBLE : View.GONE);

                if (getContext() != null && bind != null) CustomGlideRequest.Builder
                        .from(requireContext(), artistPageViewModel.getArtist().getId(), CustomGlideRequest.ResourceType.Artist)
                        .build()
                        .into(bind.artistBackdropImageView);

                if (bind != null) bind.bioTextView.setText(normalizedBio);

                if (bind != null) bind.bioMoreTextViewClickable.setOnClickListener(v -> {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(artistInfo.getLastFmUrl()));
                    startActivity(intent);
                });

                if (bind != null)
                    bind.artistPageBioPlaceholder.placeholder.setVisibility(View.GONE);
                if (bind != null) bind.artistPageBioSector.setVisibility(View.VISIBLE);
            }
        });
    }

    private void initPlayButtons() {
        bind.artistPageShuffleButton.setOnClickListener(v -> {
            artistPageViewModel.getArtistShuffleList().observe(getViewLifecycleOwner(), songs -> {
                if (songs.size() > 0) {
                    MediaManager.startQueue(mediaBrowserListenableFuture, songs, 0);
                    activity.setBottomSheetInPeek(true);
                } else {
                    Toast.makeText(requireContext(), getString(R.string.artist_error_retrieving_tracks), Toast.LENGTH_SHORT).show();
                }
            });
        });

        bind.artistPageRadioButton.setOnClickListener(v -> {
            artistPageViewModel.getArtistInstantMix().observe(getViewLifecycleOwner(), songs -> {
                if (songs.size() > 0) {
                    MediaManager.startQueue(mediaBrowserListenableFuture, songs, 0);
                    activity.setBottomSheetInPeek(true);
                } else {
                    Toast.makeText(requireContext(), getString(R.string.artist_error_retrieving_radio), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void initTopSongsView() {
        bind.mostStreamedSongRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        songHorizontalAdapter = new SongHorizontalAdapter(this, true, true);
        bind.mostStreamedSongRecyclerView.setAdapter(songHorizontalAdapter);
        artistPageViewModel.getArtistTopSongList().observe(getViewLifecycleOwner(), songs -> {
            if (songs == null) {
                if (bind != null)
                    bind.artistPageTopTracksPlaceholder.placeholder.setVisibility(View.VISIBLE);
                if (bind != null) bind.artistPageTopSongsSector.setVisibility(View.GONE);
            } else {
                if (bind != null)
                    bind.artistPageTopTracksPlaceholder.placeholder.setVisibility(View.GONE);
                if (bind != null)
                    bind.artistPageTopSongsSector.setVisibility(!songs.isEmpty() ? View.VISIBLE : View.GONE);
                songHorizontalAdapter.setItems(songs);
            }
        });
    }

    private void initHorizontalAlbumsView() {
        bind.albumsHorizontalRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));

        albumArtistPageOrSimilarAdapter = new AlbumArtistPageOrSimilarAdapter(this);
        bind.albumsHorizontalRecyclerView.setAdapter(albumArtistPageOrSimilarAdapter);
        artistPageViewModel.getAlbumList().observe(getViewLifecycleOwner(), albums -> {
            if (albums == null) {
                if (bind != null)
                    bind.artistPageAlbumPlaceholder.placeholder.setVisibility(View.VISIBLE);
                if (bind != null) bind.artistPageAlbumsSector.setVisibility(View.GONE);
            } else {
                if (bind != null)
                    bind.artistPageAlbumPlaceholder.placeholder.setVisibility(View.GONE);
                if (bind != null)
                    bind.artistPageAlbumsSector.setVisibility(!albums.isEmpty() ? View.VISIBLE : View.GONE);
                albumArtistPageOrSimilarAdapter.setItems(albums);
            }
        });

        CustomLinearSnapHelper albumSnapHelper = new CustomLinearSnapHelper();
        albumSnapHelper.attachToRecyclerView(bind.albumsHorizontalRecyclerView);
    }

    private void initVerticalAlbumsView() {
        bind.albumsVerticalRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        bind.albumsVerticalRecyclerView.addItemDecoration(new GridItemDecoration(2, 20, false));
        bind.albumsVerticalRecyclerView.setHasFixedSize(true);

        albumCatalogueAdapter = new AlbumCatalogueAdapter(this);
        bind.albumsVerticalRecyclerView.setAdapter(albumCatalogueAdapter);

        artistPageViewModel.getAlbumList().observe(getViewLifecycleOwner(), albums -> {
            if (albums == null) {
                if (bind != null)
                    bind.artistPageAlbumPlaceholder.placeholder.setVisibility(View.VISIBLE);
                if (bind != null) bind.artistPageAlbumsSector.setVisibility(View.GONE);
            } else {
                if (bind != null)
                    bind.artistPageAlbumPlaceholder.placeholder.setVisibility(View.GONE);
                if (bind != null)
                    bind.artistPageAlbumsSector.setVisibility(!albums.isEmpty() ? View.VISIBLE : View.GONE);
                albumCatalogueAdapter.setItems(albums);
            }
        });
    }

    private void initSimilarArtistsView() {
        bind.similarArtistsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        bind.similarArtistsRecyclerView.setHasFixedSize(true);

        artistSimilarAdapter = new ArtistSimilarAdapter(this);
        bind.similarArtistsRecyclerView.setAdapter(artistSimilarAdapter);
        artistPageViewModel.getArtistInfo(artistPageViewModel.getArtist().getId()).observe(getViewLifecycleOwner(), artist -> {
            if (artist == null) {
                if (bind != null)
                    bind.artistPageSimilarArtistPlaceholder.placeholder.setVisibility(View.VISIBLE);
                if (bind != null) bind.similarArtistSector.setVisibility(View.GONE);
            } else {
                if (bind != null)
                    bind.artistPageSimilarArtistPlaceholder.placeholder.setVisibility(View.GONE);
                if (bind != null)
                    bind.similarArtistSector.setVisibility(!artist.getSimilarArtists().isEmpty() ? View.VISIBLE : View.GONE);
                artistSimilarAdapter.setItems(artist.getSimilarArtists());
            }
        });

        CustomLinearSnapHelper similarArtistSnapHelper = new CustomLinearSnapHelper();
        similarArtistSnapHelper.attachToRecyclerView(bind.similarArtistsRecyclerView);
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

    @Override
    public void onAlbumClick(Bundle bundle) {
        Navigation.findNavController(requireView()).navigate(R.id.albumPageFragment, bundle);
    }

    @Override
    public void onAlbumLongClick(Bundle bundle) {
        Navigation.findNavController(requireView()).navigate(R.id.albumBottomSheetDialog, bundle);
    }

    @Override
    public void onArtistClick(Bundle bundle) {
        Navigation.findNavController(requireView()).navigate(R.id.artistPageFragment, bundle);
    }

    @Override
    public void onArtistLongClick(Bundle bundle) {
        Navigation.findNavController(requireView()).navigate(R.id.artistBottomSheetDialog, bundle);
    }
}