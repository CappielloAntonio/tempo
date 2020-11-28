package com.cappielloantonio.play.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cappielloantonio.play.adapter.SongResultSearchAdapter;
import com.cappielloantonio.play.databinding.FragmentSongListPageBinding;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.ui.activities.MainActivity;
import com.cappielloantonio.play.viewmodel.SongListPageViewModel;

import java.util.ArrayList;

public class SongListPageFragment extends Fragment {

    private FragmentSongListPageBinding bind;
    private MainActivity activity;
    private SongListPageViewModel songListPageViewModel;

    private SongResultSearchAdapter songResultSearchAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();

        bind = FragmentSongListPageBinding.inflate(inflater, container, false);
        View view = bind.getRoot();
        songListPageViewModel = new ViewModelProvider(requireActivity()).get(SongListPageViewModel.class);

        init();
        initSongListView();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind = null;
    }

    private void init() {
        if(getArguments().getString(Song.RECENTLY_PLAYED) != null) {
            songListPageViewModel.title = Song.RECENTLY_PLAYED;
            bind.pageTitleLabel.setText("Recently played songs");
        }
        else if(getArguments().getString(Song.MOST_PLAYED) != null) {
            songListPageViewModel.title = Song.MOST_PLAYED;
            bind.pageTitleLabel.setText("Most played songs");
        }
        else if(getArguments().getString(Song.RECENTLY_ADDED) != null) {
            songListPageViewModel.title = Song.RECENTLY_ADDED;
            bind.pageTitleLabel.setText("Recently added song");
        }
        else if(getArguments().getString(Song.BY_GENRE) != null) {
            songListPageViewModel.title = Song.BY_GENRE;
            songListPageViewModel.genre = getArguments().getParcelable("genre_object");
            bind.pageTitleLabel.setText(songListPageViewModel.genre.getName() + ": all songs");
        }
        else if(getArguments().getString(Song.BY_ARTIST) != null) {
            songListPageViewModel.title = Song.BY_ARTIST;
            songListPageViewModel.artist = getArguments().getParcelable("artist_object");
            bind.pageTitleLabel.setText(songListPageViewModel.artist.getName() + "'s top songs");
        }
        else if(getArguments().getString(Song.BY_GENRES) != null) {
            songListPageViewModel.title = Song.BY_GENRES;
            songListPageViewModel.filters = getArguments().getStringArrayList("filters_list");
            songListPageViewModel.filterNames = getArguments().getStringArrayList("filter_name_list");
            bind.pageTitleLabel.setText(songListPageViewModel.getFiltersTitle());
        }
        else if(getArguments().getString(Song.BY_YEAR) != null) {
            songListPageViewModel.title = Song.BY_YEAR;
            songListPageViewModel.year = getArguments().getInt("year_object");
            bind.pageTitleLabel.setText("Year " + songListPageViewModel.year);
        }
    }

    private void initSongListView() {
        bind.songListRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        bind.songListRecyclerView.setHasFixedSize(true);

        songResultSearchAdapter = new SongResultSearchAdapter(requireContext(), new ArrayList<>());
        bind.songListRecyclerView.setAdapter(songResultSearchAdapter);
        songListPageViewModel.getSongList().observe(requireActivity(), songs -> songResultSearchAdapter.setItems(songs));
    }
}