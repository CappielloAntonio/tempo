package com.cappielloantonio.play.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import com.cappielloantonio.play.R;
import com.cappielloantonio.play.adapter.PlayerNowPlayingSongAdapter;
import com.cappielloantonio.play.adapter.PlayerSongQueueAdapter;
import com.cappielloantonio.play.databinding.FragmentPlayerBottomSheetBinding;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.ui.activities.MainActivity;
import com.cappielloantonio.play.viewmodel.PlayerBottomSheetViewModel;

public class PlayerBottomSheetFragment extends Fragment {
    private static final String TAG = "PlayerBottomSheetFragment";

    private FragmentPlayerBottomSheetBinding bind;
    private MainActivity activity;
    private PlayerBottomSheetViewModel playerBottomSheetViewModel;

    private PlayerNowPlayingSongAdapter playerNowPlayingSongAdapter;
    private PlayerSongQueueAdapter playerSongQueueAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();

        bind = FragmentPlayerBottomSheetBinding.inflate(inflater, container, false);
        View view = bind.getRoot();
        playerBottomSheetViewModel = new ViewModelProvider(requireActivity()).get(PlayerBottomSheetViewModel.class);

        initQueueSlideView();
        initQueueRecyclerView();

        return view;
    }

    private void initQueueSlideView() {
        bind.playerSongCoverViewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);

        playerNowPlayingSongAdapter = new PlayerNowPlayingSongAdapter(requireContext());
        bind.playerSongCoverViewPager.setAdapter(playerNowPlayingSongAdapter);
        playerBottomSheetViewModel.getQueueSong().observe(requireActivity(), songs -> playerNowPlayingSongAdapter.setItems(songs));

        bind.playerSongCoverViewPager.setOffscreenPageLimit(3);
        setDiscoverSongSlideViewOffset(40, 4);

        bind.playerSongCoverViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);

                playerBottomSheetViewModel.setNowPlayingSong(position);
            }
        });

        playerBottomSheetViewModel.getNowPlayingSong().observe(requireActivity(), song -> {
            if(song != null)
                setSongInfo(song);
        });
    }

    private void initQueueRecyclerView() {
        bind.playerQueueRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        bind.playerQueueRecyclerView.setHasFixedSize(true);

        playerSongQueueAdapter = new PlayerSongQueueAdapter(activity, requireContext(), getChildFragmentManager());
        bind.playerQueueRecyclerView.setAdapter(playerSongQueueAdapter);
        playerBottomSheetViewModel.getQueueSong().observe(requireActivity(), songs -> playerSongQueueAdapter.setItems(songs));
    }

    private void setDiscoverSongSlideViewOffset(float pageOffset, float pageMargin) {
        bind.playerSongCoverViewPager.setPageTransformer((page, position) -> {
            float myOffset = position * -(2 * pageOffset + pageMargin);
            if (bind.playerSongCoverViewPager.getOrientation() == ViewPager2.ORIENTATION_HORIZONTAL) {
                if (ViewCompat.getLayoutDirection(bind.playerSongCoverViewPager) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                    page.setTranslationX(-myOffset);
                } else {
                    page.setTranslationX(myOffset);
                }
            } else {
                page.setTranslationY(myOffset);
            }
        });
    }

    private void setSongInfo(Song song) {
        if(song != null) {
            bind.playerSongTitleLabel.setText(song.getTitle());
            bind.playerArtistNameLabel.setText(song.getArtistName());
        }
    }

    public View getPlayerHeader() {
        return getView().findViewById(R.id.player_header_layout);
    }

    public void scrollOnTop() {
        bind.playerNestedScrollView.fullScroll(ScrollView.FOCUS_UP);
    }
}
