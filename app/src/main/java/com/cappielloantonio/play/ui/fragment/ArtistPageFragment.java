package com.cappielloantonio.play.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
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
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.repository.ArtistRepository;
import com.cappielloantonio.play.repository.QueueRepository;
import com.cappielloantonio.play.ui.activity.MainActivity;
import com.cappielloantonio.play.util.MusicUtil;
import com.cappielloantonio.play.viewmodel.ArtistPageViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ArtistPageFragment extends Fragment {
    private static final String TAG = "ArtistPageFragment";

    private FragmentArtistPageBinding bind;
    private MainActivity activity;
    private ArtistPageViewModel artistPageViewModel;

    private SongHorizontalAdapter songHorizontalAdapter;
    private AlbumArtistPageOrSimilarAdapter albumArtistPageOrSimilarAdapter;
    private ArtistSimilarAdapter artistSimilarAdapter;

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
        activity.setBottomNavigationBarVisibility(false);
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
            bundle.putString(Song.BY_ARTIST, Song.BY_ARTIST);
            bundle.putParcelable("artist_object", artistPageViewModel.getArtist());
            activity.navController.navigate(R.id.action_artistPageFragment_to_songListPageFragment, bundle);
        });
    }

    @SuppressLint("NewApi")
    private void initAppBar() {
        activity.setSupportActionBar(bind.animToolbar);
        if (activity.getSupportActionBar() != null)
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bind.collapsingToolbar.setTitle(MusicUtil.getReadableString(artistPageViewModel.getArtist().getName()));
        bind.animToolbar.setNavigationOnClickListener(v -> activity.navController.navigateUp());
        bind.collapsingToolbar.setCollapsedTitleTextColor(getResources().getColor(R.color.titleTextColor, null));

        bind.appbar.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if ((bind.collapsingToolbar.getHeight() + verticalOffset) < (2 * ViewCompat.getMinimumHeight(bind.collapsingToolbar))) {
                Objects.requireNonNull(bind.animToolbar.getNavigationIcon()).setColorFilter(new BlendModeColorFilter(getResources().getColor(R.color.titleTextColor, null), BlendMode.SRC_ATOP));
            } else {
                Objects.requireNonNull(bind.animToolbar.getNavigationIcon()).setColorFilter(new BlendModeColorFilter(getResources().getColor(R.color.white, null), BlendMode.SRC_ATOP));
            }
        });
    }

    private void initArtistInfo() {
        artistPageViewModel.getArtistInfo(artistPageViewModel.getArtist().getId()).observe(requireActivity(), artist -> {
            String normalizedBio = MusicUtil.forceReadableString(artist.getBio());

            if (bind != null) bind.artistPageBioSector.setVisibility(!normalizedBio.trim().isEmpty() ? View.VISIBLE : View.GONE);
            if (bind != null) bind.bioMoreTextViewClickable.setVisibility(artist.getLastfm() != null ? View.VISIBLE : View.GONE);

            CustomGlideRequest.Builder
                    .from(
                            requireContext(),
                            artistPageViewModel.getArtist().getId(),
                            CustomGlideRequest.ARTIST_PIC,
                            // artist.getImageUrl()
                            null
                    )
                    .build()
                    .into(bind.artistBackdropImageView);

            bind.bioTextView.setText(normalizedBio);

            bind.bioMoreTextViewClickable.setOnClickListener(v -> {
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
                    QueueRepository queueRepository = new QueueRepository(App.getInstance());
                    queueRepository.insertAllAndStartNew(songs);

                    // MusicPlayerRemote.openQueue(songs, 0, true);
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
                        QueueRepository queueRepository = new QueueRepository(App.getInstance());
                        queueRepository.insertAllAndStartNew((ArrayList<Song>) media);

                        activity.setBottomSheetInPeek(true);
                        // activity.setBottomSheetMusicInfo((Song) media.get(0));

                        // MusicPlayerRemote.openQueue((List<Song>) media, 0, true);
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
}