package com.cappielloantonio.play.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cappielloantonio.play.adapter.DiscoverSongAdapter;
import com.cappielloantonio.play.adapter.RecentMusicAdapter;
import com.cappielloantonio.play.databinding.FragmentHomeBinding;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.ui.activities.MainActivity;
import com.cappielloantonio.play.util.PreferenceUtil;
import com.cappielloantonio.play.viewmodel.HomeViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements RecentMusicAdapter.ItemClickListener {
    private static final String TAG = "CategoriesFragment";

    private FragmentHomeBinding bind;
    private MainActivity activity;
    private HomeViewModel homeViewModel;

    private DiscoverSongAdapter discoverSongAdapter;
    private RecentMusicAdapter recentlyAddedMusicAdapter;
    private RecentMusicAdapter mostPlayedMusicAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();

        bind = FragmentHomeBinding.inflate(inflater, container, false);
        View view = bind.getRoot();
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        init();
        initDiscoverSongSlideView();
        initRecentPlayedSongView();
        initMostPlayedSongView();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind = null;
    }

    private void init() {
        bind.resyncButton.setOnClickListener(v -> {
            PreferenceUtil.getInstance(requireContext()).setSync(false);
            activity.goToSync();
        });
    }

    private void initDiscoverSongSlideView() {
        discoverSongAdapter = new DiscoverSongAdapter(requireContext(), new ArrayList<>());
        bind.discoverSongViewPager.setAdapter(discoverSongAdapter);
        bind.discoverSongViewPager.setPageMargin(20);
        homeViewModel.getDiscoverSongList().observe(requireActivity(), songs -> discoverSongAdapter.setItems(songs));
    }

    private void initRecentPlayedSongView() {
        bind.recentlyPlayedTracksRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        bind.recentlyPlayedTracksRecyclerView.setHasFixedSize(true);

        recentlyAddedMusicAdapter = new RecentMusicAdapter(requireContext(), new ArrayList<>());
        recentlyAddedMusicAdapter.setClickListener(this);
        bind.recentlyPlayedTracksRecyclerView.setAdapter(recentlyAddedMusicAdapter);
        homeViewModel.getRecentlyAddedSongList().observe(requireActivity(), songs -> recentlyAddedMusicAdapter.setItems(songs));
    }

    private void initMostPlayedSongView() {
        bind.mostPlayedTracksRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        bind.mostPlayedTracksRecyclerView.setHasFixedSize(true);

        mostPlayedMusicAdapter = new RecentMusicAdapter(requireContext(), new ArrayList<>());
        mostPlayedMusicAdapter.setClickListener(this);
        bind.mostPlayedTracksRecyclerView.setAdapter(mostPlayedMusicAdapter);
        homeViewModel.getMostPlayedSongList().observe(requireActivity(), songs -> mostPlayedMusicAdapter.setItems(songs));
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(requireContext(), "Click: " + position, Toast.LENGTH_SHORT).show();
    }
}
