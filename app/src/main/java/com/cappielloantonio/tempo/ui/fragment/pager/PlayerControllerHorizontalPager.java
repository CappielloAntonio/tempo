package com.cappielloantonio.tempo.ui.fragment.pager;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.fragment.app.Fragment;
import androidx.media3.common.util.UnstableApi;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.cappielloantonio.tempo.ui.fragment.PlayerCoverFragment;
import com.cappielloantonio.tempo.ui.fragment.PlayerLyricsFragment;

@OptIn(markerClass = UnstableApi.class)
public class PlayerControllerHorizontalPager extends FragmentStateAdapter {
    private static final String TAG = "PlayerControllerHorizontalPager";

    public PlayerControllerHorizontalPager(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new PlayerCoverFragment();
            case 1:
                return new PlayerLyricsFragment();
        }

        return new PlayerCoverFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}