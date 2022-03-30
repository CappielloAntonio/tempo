package com.cappielloantonio.play.ui.fragment;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.media3.session.MediaBrowser;
import androidx.media3.session.SessionToken;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cappielloantonio.play.App;
import com.cappielloantonio.play.R;
import com.cappielloantonio.play.adapter.AlbumArtistPageOrSimilarAdapter;
import com.cappielloantonio.play.adapter.ArtistSimilarAdapter;
import com.cappielloantonio.play.adapter.SongHorizontalAdapter;
import com.cappielloantonio.play.databinding.FragmentArtistPageBinding;
import com.cappielloantonio.play.glide.CustomGlideRequest;
import com.cappielloantonio.play.helper.recyclerview.CustomLinearSnapHelper;
import com.cappielloantonio.play.interfaces.MediaCallback;
import com.cappielloantonio.play.model.Media;
import com.cappielloantonio.play.repository.ArtistRepository;
import com.cappielloantonio.play.service.MediaManager;
import com.cappielloantonio.play.service.MediaService;
import com.cappielloantonio.play.ui.activity.MainActivity;
import com.cappielloantonio.play.util.MusicUtil;
import com.cappielloantonio.play.viewmodel.ArtistPageViewModel;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.ArrayList;
import java.util.List;

public class ArtistPageFragment extends Fragment {
    private static final String TAG = "ArtistPageFragment";

    private FragmentArtistPageBinding bind;
    private MainActivity activity;
    private ArtistPageViewModel artistPageViewModel;

    private SongHorizontalAdapter songHorizontalAdapter;
    private AlbumArtistPageOrSimilarAdapter albumArtistPageOrSimilarAdapter;
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
        initAlbumsView();
        initSimilarArtistsView();

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();

        initializeMediaBrowser();
        setMediaBrowserListenableFuture();
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
        artistPageViewModel.setArtist(requireArguments().getParcelable("artist_object"));

        bind.mostStreamedSongTextViewClickable.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString(Media.BY_ARTIST, Media.BY_ARTIST);
            bundle.putParcelable("artist_object", artistPageViewModel.getArtist());
            activity.navController.navigate(R.id.action_artistPageFragment_to_songListPageFragment, bundle);
        });
    }

    @SuppressLint("NewApi")
    private void initAppBar() {
        activity.setSupportActionBar(bind.animToolbar);
        if (activity.getSupportActionBar() != null) activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bind.collapsingToolbar.setTitle(MusicUtil.getReadableString(artistPageViewModel.getArtist().getName()));
        bind.animToolbar.setNavigationOnClickListener(v -> activity.navController.navigateUp());
        bind.collapsingToolbar.setExpandedTitleColor(getResources().getColor(R.color.white, null));
    }

    private void initArtistInfo() {
        artistPageViewModel.getArtistInfo(artistPageViewModel.getArtist().getId()).observe(requireActivity(), artist -> {
            String normalizedBio = MusicUtil.forceReadableString(artist.getBio());

            if (bind != null) bind.artistPageBioSector.setVisibility(!normalizedBio.trim().isEmpty() ? View.VISIBLE : View.GONE);
            if (bind != null) bind.bioMoreTextViewClickable.setVisibility(artist.getLastfm() != null ? View.VISIBLE : View.GONE);

            if (getContext() != null && bind != null) CustomGlideRequest.Builder
                    .from(
                            requireContext(),
                            artistPageViewModel.getArtist().getId(),
                            CustomGlideRequest.ARTIST_PIC,
                            // artist.getImageUrl()
                            null
                    )
                    .build()
                    .into(bind.artistBackdropImageView);

            if (bind != null) bind.bioTextView.setText(normalizedBio);

            if (bind != null) bind.bioMoreTextViewClickable.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(artist.getLastfm()));
                startActivity(intent);
            });
        });
    }

    private void initPlayButtons() {
        bind.artistPageShuffleButton.setOnClickListener(v -> {
            ArtistRepository artistRepository = new ArtistRepository(App.getInstance());
            artistRepository.getArtistRandomSong(requireActivity(), artistPageViewModel.getArtist(), 20).observe(requireActivity(), songs -> {
                if (songs.size() > 0) {
                    MediaManager.startQueue(mediaBrowserListenableFuture, requireContext(), songs, 0);
                    activity.setBottomSheetInPeek(true);
                } else {
                    Toast.makeText(requireContext(), getString(R.string.artist_error_retrieving_tracks), Toast.LENGTH_SHORT).show();
                }
            });
        });

        bind.artistPageRadioButton.setOnClickListener(v -> {
            ArtistRepository artistRepository = new ArtistRepository(App.getInstance());
            artistRepository.getInstantMix(artistPageViewModel.getArtist(), 20, new MediaCallback() {
                @Override
                public void onError(Exception exception) {
                    Log.e(TAG, "onError() " + exception.getMessage());
                }

                @Override
                public void onLoadMedia(List<?> media) {
                    if (media.size() > 0) {
                        MediaManager.startQueue(mediaBrowserListenableFuture, requireContext(), (ArrayList<Media>) media, 0);
                        activity.setBottomSheetInPeek(true);
                    } else {
                        Toast.makeText(requireContext(), getString(R.string.artist_error_retrieving_radio), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });
    }

    private void initTopSongsView() {
        bind.mostStreamedSongRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        songHorizontalAdapter = new SongHorizontalAdapter(activity, requireContext(), true);
        bind.mostStreamedSongRecyclerView.setAdapter(songHorizontalAdapter);
        artistPageViewModel.getArtistTopSongList(10).observe(requireActivity(), songs -> {
            if (bind != null) bind.artistPageTopSongsSector.setVisibility(!songs.isEmpty() ? View.VISIBLE : View.GONE);
            songHorizontalAdapter.setItems(songs);
        });
    }

    private void initAlbumsView() {
        bind.albumsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));

        albumArtistPageOrSimilarAdapter = new AlbumArtistPageOrSimilarAdapter(requireContext());
        bind.albumsRecyclerView.setAdapter(albumArtistPageOrSimilarAdapter);
        artistPageViewModel.getAlbumList().observe(requireActivity(), albums -> {
            if (bind != null) bind.artistPageAlbumsSector.setVisibility(!albums.isEmpty() ? View.VISIBLE : View.GONE);
            albumArtistPageOrSimilarAdapter.setItems(albums);
        });

        CustomLinearSnapHelper albumSnapHelper = new CustomLinearSnapHelper();
        albumSnapHelper.attachToRecyclerView(bind.albumsRecyclerView);
    }

    private void initSimilarArtistsView() {
        bind.similarArtistsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        bind.similarArtistsRecyclerView.setHasFixedSize(true);

        artistSimilarAdapter = new ArtistSimilarAdapter(requireContext());
        bind.similarArtistsRecyclerView.setAdapter(artistSimilarAdapter);
        artistPageViewModel.getArtistInfo(artistPageViewModel.getArtist().getId()).observe(requireActivity(), artist -> {
            if (bind != null) bind.similarArtistSector.setVisibility(!artist.getSimilarArtists().isEmpty() ? View.VISIBLE : View.GONE);
            artistSimilarAdapter.setItems(artist.getSimilarArtists());
        });

        CustomLinearSnapHelper similarArtistSnapHelper = new CustomLinearSnapHelper();
        similarArtistSnapHelper.attachToRecyclerView(bind.similarArtistsRecyclerView);
    }

    @SuppressLint("UnsafeOptInUsageError")
    private void initializeMediaBrowser() {
        mediaBrowserListenableFuture = new MediaBrowser.Builder(requireContext(), new SessionToken(requireContext(), new ComponentName(requireContext(), MediaService.class))).buildAsync();
    }

    private void releaseMediaBrowser() {
        MediaBrowser.releaseFuture(mediaBrowserListenableFuture);
    }

    private void setMediaBrowserListenableFuture() {
        songHorizontalAdapter.setMediaBrowserListenableFuture(mediaBrowserListenableFuture);
    }
}