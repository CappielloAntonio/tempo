package com.cappielloantonio.play.ui.fragment;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cappielloantonio.play.App;
import com.cappielloantonio.play.R;
import com.cappielloantonio.play.adapter.SongResultSearchAdapter;
import com.cappielloantonio.play.databinding.FragmentPlaylistPageBinding;
import com.cappielloantonio.play.glide.CustomGlideRequest;
import com.cappielloantonio.play.repository.QueueRepository;
import com.cappielloantonio.play.service.MusicPlayerRemote;
import com.cappielloantonio.play.ui.activity.MainActivity;
import com.cappielloantonio.play.util.PreferenceUtil;
import com.cappielloantonio.play.viewmodel.PlaylistPageViewModel;

import java.util.Collections;

public class PlaylistPageFragment extends Fragment {

    private FragmentPlaylistPageBinding bind;
    private MainActivity activity;
    private PlaylistPageViewModel playlistPageViewModel;

    private SongResultSearchAdapter songResultSearchAdapter;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initAppBar();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();

        bind = FragmentPlaylistPageBinding.inflate(inflater, container, false);
        View view = bind.getRoot();
        playlistPageViewModel = new ViewModelProvider(requireActivity()).get(PlaylistPageViewModel.class);

        init();
        initBackCover();
        initMusicButton();
        initSongsView();

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
        playlistPageViewModel.setPlaylist(getArguments().getParcelable("playlist_object"));
    }

    private void initAppBar() {
        activity.setSupportActionBar(bind.animToolbar);
        if (activity.getSupportActionBar() != null)
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bind.collapsingToolbar.setTitle(playlistPageViewModel.getPlaylist().getName());
        bind.animToolbar.setNavigationOnClickListener(v -> activity.navController.navigateUp());
        bind.collapsingToolbar.setCollapsedTitleTextColor(getResources().getColor(R.color.titleTextColor, null));

        bind.appbar.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if ((bind.collapsingToolbar.getHeight() + verticalOffset) < (2 * ViewCompat.getMinimumHeight(bind.collapsingToolbar))) {
                bind.animToolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.titleTextColor, null), PorterDuff.Mode.SRC_ATOP);
            } else {
                bind.animToolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white, null), PorterDuff.Mode.SRC_ATOP);
            }
        });
    }

    private void initMusicButton() {
        playlistPageViewModel.getPlaylistSongList().observe(requireActivity(), songs -> {
            if(bind != null) {
                bind.playlistPagePlayButton.setOnClickListener(v -> {
                    QueueRepository queueRepository = new QueueRepository(App.getInstance());
                    queueRepository.insertAllAndStartNew(songs);

                    activity.isBottomSheetInPeek(true);
                    activity.setBottomSheetMusicInfo(songs.get(0));

                    MusicPlayerRemote.openQueue(songs, 0, true);
                });

                bind.playlistPageShuffleButton.setOnClickListener(v -> {
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

    private void initBackCover() {
        CustomGlideRequest.Builder
                .from(requireContext(), playlistPageViewModel.getPlaylist().getPrimary(), playlistPageViewModel.getPlaylist().getBlurHash(), CustomGlideRequest.PRIMARY, PreferenceUtil.getInstance(requireContext()).getImageQuality(), CustomGlideRequest.ALBUM_PIC)
                .build()
                .into(bind.albumBackCoverImageView);
    }

    private void initSongsView() {
        bind.playlistRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        bind.playlistRecyclerView.setHasFixedSize(true);

        songResultSearchAdapter = new SongResultSearchAdapter(activity, requireContext(), getChildFragmentManager());
        bind.playlistRecyclerView.setAdapter(songResultSearchAdapter);
        playlistPageViewModel.getPlaylistSongList().observe(requireActivity(), songs -> {
            songResultSearchAdapter.setItems(songs);
        });
    }
}