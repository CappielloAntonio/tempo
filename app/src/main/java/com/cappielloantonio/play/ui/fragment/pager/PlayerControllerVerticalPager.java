package com.cappielloantonio.play.ui.fragment.pager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.cappielloantonio.play.ui.fragment.PlayerControllerFragment;
import com.cappielloantonio.play.ui.fragment.PlayerQueueFragment;

public class PlayerControllerVerticalPager extends FragmentStateAdapter {
    private static final String TAG = "PlayerControllerVerticalPager";

    public PlayerControllerVerticalPager(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new PlayerControllerFragment();
            case 1:
                return new PlayerQueueFragment();
        }

        return new PlayerControllerFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}