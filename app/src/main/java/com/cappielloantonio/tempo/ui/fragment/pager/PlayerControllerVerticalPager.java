package com.cappielloantonio.tempo.ui.fragment.pager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.cappielloantonio.tempo.ui.fragment.PlayerControllerFragment;
import com.cappielloantonio.tempo.ui.fragment.PlayerQueueFragment;

import java.util.HashMap;

public class PlayerControllerVerticalPager extends FragmentStateAdapter {
    private final HashMap<Integer, Fragment> maps;

    public PlayerControllerVerticalPager(@NonNull Fragment fragment) {
        super(fragment);

        this.maps = new HashMap<>();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                Fragment playerControllerFragment = new PlayerControllerFragment();
                maps.put(position, playerControllerFragment);
                return playerControllerFragment;
            case 1:
                Fragment playerQueueFragment = new PlayerQueueFragment();
                maps.put(position, playerQueueFragment);
                return playerQueueFragment;
        }

        Fragment playerControllerFragment = new PlayerControllerFragment();
        maps.put(position, playerControllerFragment);
        return playerControllerFragment;
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public Fragment getRegisteredFragment(int position) {
        return maps.get(position);
    }
}