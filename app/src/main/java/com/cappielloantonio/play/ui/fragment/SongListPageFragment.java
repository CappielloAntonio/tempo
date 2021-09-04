package com.cappielloantonio.play.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cappielloantonio.play.App;
import com.cappielloantonio.play.adapter.SongHorizontalAdapter;
import com.cappielloantonio.play.databinding.FragmentSongListPageBinding;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.repository.QueueRepository;
import com.cappielloantonio.play.service.MusicPlayerRemote;
import com.cappielloantonio.play.ui.activity.MainActivity;
import com.cappielloantonio.play.util.MusicUtil;
import com.cappielloantonio.play.viewmodel.SongListPageViewModel;

import java.util.Collections;

public class SongListPageFragment extends Fragment {

    private FragmentSongListPageBinding bind;
    private MainActivity activity;
    private SongListPageViewModel songListPageViewModel;

    private SongHorizontalAdapter songHorizontalAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();

        bind = FragmentSongListPageBinding.inflate(inflater, container, false);
        View view = bind.getRoot();
        songListPageViewModel = new ViewModelProvider(requireActivity()).get(SongListPageViewModel.class);

        init();
        initAppBar();
        initButtons();
        initSongListView();

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
        if (requireArguments().getString(Song.RECENTLY_PLAYED) != null) {
            songListPageViewModel.title = Song.RECENTLY_PLAYED;
            bind.pageTitleLabel.setText("Recently played tracks");
        } else if (requireArguments().getString(Song.MOST_PLAYED) != null) {
            songListPageViewModel.title = Song.MOST_PLAYED;
            bind.pageTitleLabel.setText("Most played tracks");
        } else if (requireArguments().getString(Song.RECENTLY_ADDED) != null) {
            songListPageViewModel.title = Song.RECENTLY_ADDED;
            bind.pageTitleLabel.setText("Recently added tracks");
        } else if (requireArguments().getString(Song.BY_GENRE) != null) {
            songListPageViewModel.title = Song.BY_GENRE;
            songListPageViewModel.genre = requireArguments().getParcelable("genre_object");
            bind.pageTitleLabel.setText(MusicUtil.getReadableString(songListPageViewModel.genre.getName()) + ": all tracks");
        } else if (requireArguments().getString(Song.BY_ARTIST) != null) {
            songListPageViewModel.title = Song.BY_ARTIST;
            songListPageViewModel.artist = requireArguments().getParcelable("artist_object");
            bind.pageTitleLabel.setText(MusicUtil.getReadableString(songListPageViewModel.artist.getName()) + "'s top tracks");
        } else if (requireArguments().getString(Song.BY_GENRES) != null) {
            songListPageViewModel.title = Song.BY_GENRES;
            songListPageViewModel.filters = requireArguments().getStringArrayList("filters_list");
            songListPageViewModel.filterNames = requireArguments().getStringArrayList("filter_name_list");
            bind.pageTitleLabel.setText(songListPageViewModel.getFiltersTitle());
        } else if (requireArguments().getString(Song.BY_YEAR) != null) {
            songListPageViewModel.title = Song.BY_YEAR;
            songListPageViewModel.year = requireArguments().getInt("year_object");
            bind.pageTitleLabel.setText("Year " + songListPageViewModel.year);
        } else if (requireArguments().getString(Song.STARRED) != null) {
            songListPageViewModel.title = Song.STARRED;
            bind.pageTitleLabel.setText("Starred tracks");
        } else if (requireArguments().getString(Song.DOWNLOADED) != null) {
            songListPageViewModel.title = Song.DOWNLOADED;
            bind.pageTitleLabel.setText("Downloaded");
        } else if (requireArguments().getParcelable("album_object") != null) {
            songListPageViewModel.album = requireArguments().getParcelable("album_object");
            songListPageViewModel.title = Song.FROM_ALBUM;
            bind.pageTitleLabel.setText(MusicUtil.getReadableString(songListPageViewModel.album.getTitle()));
        }
    }

    private void initAppBar() {
        activity.setSupportActionBar(bind.toolbar);

        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        bind.toolbar.setNavigationOnClickListener(v -> activity.navController.navigateUp());

        bind.appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if ((bind.albumInfoSector.getHeight() + verticalOffset) < (2 * ViewCompat.getMinimumHeight(bind.toolbar))) {
                bind.toolbar.setTitle("Songs");
            } else {
                bind.toolbar.setTitle("");
            }
        });
    }

    private void initButtons() {
        songListPageViewModel.getSongList(requireActivity()).observe(requireActivity(), songs -> {
            if (bind != null) {
                bind.songListShuffleImageView.setOnClickListener(v -> {
                    Collections.shuffle(songs);

                    QueueRepository queueRepository = new QueueRepository(App.getInstance());
                    queueRepository.insertAllAndStartNew(songs);

                    activity.isBottomSheetInPeek(true);
                    activity.setBottomSheetMusicInfo(songs.get(0));

                    MusicPlayerRemote.openQueue(songs, 0, true);
                });
            }
        });
    }

    private void initSongListView() {
        bind.songListRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        bind.songListRecyclerView.setHasFixedSize(true);

        songHorizontalAdapter = new SongHorizontalAdapter(activity, requireContext(), true);
        bind.songListRecyclerView.setAdapter(songHorizontalAdapter);
        songListPageViewModel.getSongList(requireActivity()).observe(requireActivity(), songs -> songHorizontalAdapter.setItems(songs));
    }
}