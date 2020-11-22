package com.cappielloantonio.play.ui.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cappielloantonio.play.R;
import com.cappielloantonio.play.adapter.AlbumArtistPageAdapter;
import com.cappielloantonio.play.adapter.RecentMusicAdapter;
import com.cappielloantonio.play.adapter.SongResultSearchAdapter;
import com.cappielloantonio.play.databinding.FragmentArtistPageBinding;
import com.cappielloantonio.play.databinding.FragmentHomeBinding;
import com.cappielloantonio.play.ui.activities.MainActivity;
import com.cappielloantonio.play.viewmodel.ArtistPageViewModel;
import com.cappielloantonio.play.viewmodel.HomeViewModel;

import java.util.ArrayList;

public class ArtistPageFragment extends Fragment {

    private FragmentArtistPageBinding bind;
    private MainActivity activity;
    private ArtistPageViewModel artistPageViewModel;

    private SongResultSearchAdapter songResultSearchAdapter;
    private AlbumArtistPageAdapter albumArtistPageAdapter;

    private String artistID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();

        bind = FragmentArtistPageBinding.inflate(inflater, container, false);
        View view = bind.getRoot();
        artistPageViewModel = new ViewModelProvider(requireActivity()).get(ArtistPageViewModel.class);

        init();
        initTopSongsView();
        initAlbumsView();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind = null;
    }

    private void init() {
        artistID = getArguments().getString("artistID");
    }

    private void initTopSongsView() {
        bind.mostStreamedSongRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        bind.mostStreamedSongRecyclerView.setHasFixedSize(true);

        songResultSearchAdapter = new SongResultSearchAdapter(requireContext(), new ArrayList<>());
        bind.mostStreamedSongRecyclerView.setAdapter(songResultSearchAdapter);
        artistPageViewModel.getArtistTopSongList(artistID).observe(requireActivity(), songs -> songResultSearchAdapter.setItems(songs));
    }

    private void initAlbumsView() {
        bind.albumsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        bind.albumsRecyclerView.setHasFixedSize(true);

        albumArtistPageAdapter = new AlbumArtistPageAdapter(requireContext(), new ArrayList<>());
        bind.albumsRecyclerView.setAdapter(albumArtistPageAdapter);
        artistPageViewModel.getAlbumList(artistID).observe(requireActivity(), songs -> albumArtistPageAdapter.setItems(songs));
    }
}