package com.cappielloantonio.play.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.cappielloantonio.play.ui.fragment.PlayerCoverFragment;
import com.cappielloantonio.play.ui.fragment.PlayerLyricsFragment;

public class PlayerNowPlayingSongAdapter extends FragmentStateAdapter {
    private static final String TAG = "PlayerNowPlayingSongInfoAdapter";

    public PlayerNowPlayingSongAdapter(@NonNull Fragment fragment) {
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