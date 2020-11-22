package com.cappielloantonio.play.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cappielloantonio.play.adapter.DiscoverSongAdapter;
import com.cappielloantonio.play.adapter.RecentMusicAdapter;
import com.cappielloantonio.play.databinding.FragmentHomeBinding;
import com.cappielloantonio.play.ui.activities.MainActivity;
import com.cappielloantonio.play.util.PreferenceUtil;
import com.cappielloantonio.play.viewmodel.HomeViewModel;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private static final String TAG = "CategoriesFragment";

    private FragmentHomeBinding bind;
    private MainActivity activity;
    private HomeViewModel homeViewModel;

    private DiscoverSongAdapter discoverSongAdapter;
    private RecentMusicAdapter recentlyAddedMusicAdapter;
    private RecentMusicAdapter recentlyPlayedMusicAdapter;
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
        initRecentAddedSongView();
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
        discoverSongAdapter = new DiscoverSongAdapter(requireContext(), homeViewModel.getDiscoverSongList());
        bind.discoverSongViewPager.setAdapter(discoverSongAdapter);
        bind.discoverSongViewPager.setPageMargin(20);
    }

    private void initRecentAddedSongView() {
        bind.recentlyAddedTracksRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        bind.recentlyAddedTracksRecyclerView.setHasFixedSize(true);

        recentlyAddedMusicAdapter = new RecentMusicAdapter(requireContext(), new ArrayList<>());
        bind.recentlyAddedTracksRecyclerView.setAdapter(recentlyAddedMusicAdapter);
        homeViewModel.getRecentlyAddedSongList().observe(requireActivity(), songs -> recentlyAddedMusicAdapter.setItems(songs));
    }

    private void initRecentPlayedSongView() {
        bind.recentlyPlayedTracksRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        bind.recentlyPlayedTracksRecyclerView.setHasFixedSize(true);

        recentlyPlayedMusicAdapter = new RecentMusicAdapter(requireContext(), new ArrayList<>());
        bind.recentlyPlayedTracksRecyclerView.setAdapter(recentlyPlayedMusicAdapter);
        homeViewModel.getRecentlyPlayedSongList().observe(requireActivity(), songs -> recentlyPlayedMusicAdapter.setItems(songs));
    }

    private void initMostPlayedSongView() {
        bind.mostPlayedTracksRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        bind.mostPlayedTracksRecyclerView.setHasFixedSize(true);

        mostPlayedMusicAdapter = new RecentMusicAdapter(requireContext(), new ArrayList<>());
        bind.mostPlayedTracksRecyclerView.setAdapter(mostPlayedMusicAdapter);
        homeViewModel.getMostPlayedSongList().observe(requireActivity(), songs -> mostPlayedMusicAdapter.setItems(songs));
    }
}
