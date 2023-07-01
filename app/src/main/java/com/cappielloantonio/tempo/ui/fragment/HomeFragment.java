package com.cappielloantonio.tempo.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.media3.common.util.UnstableApi;

import com.cappielloantonio.tempo.R;
import com.cappielloantonio.tempo.databinding.FragmentHomeBinding;
import com.cappielloantonio.tempo.ui.activity.MainActivity;
import com.cappielloantonio.tempo.ui.fragment.pager.HomePager;
import com.cappielloantonio.tempo.util.Preferences;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Objects;

@UnstableApi
public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";

    private FragmentHomeBinding bind;
    private MainActivity activity;

    private MaterialToolbar materialToolbar;
    private AppBarLayout appBarLayout;
    private TabLayout tabLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();
        bind = FragmentHomeBinding.inflate(inflater, container, false);
        return bind.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initAppBar();
        initHomePager();
    }

    @Override
    public void onStart() {
        super.onStart();

        activity.setBottomNavigationBarVisibility(true);
        activity.setBottomSheetVisibility(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind = null;
    }

    private void initAppBar() {
        appBarLayout = bind.getRoot().findViewById(R.id.toolbar_fragment);
        materialToolbar = bind.getRoot().findViewById(R.id.toolbar);

        activity.setSupportActionBar(materialToolbar);
        Objects.requireNonNull(materialToolbar.getOverflowIcon()).setTint(requireContext().getResources().getColor(R.color.titleTextColor, null));

        tabLayout = new TabLayout(requireContext());
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        appBarLayout.addView(tabLayout);
    }

    private void initHomePager() {
        HomePager pager = new HomePager(this);

        pager.addFragment(new HomeTabMusicFragment(), "Music", R.drawable.ic_home);

        if (Preferences.isPodcastSectionVisible())
            pager.addFragment(new HomeTabPodcastFragment(), "Podcast", R.drawable.ic_graphic_eq);

        if (Preferences.isRadioSectionVisible())
            pager.addFragment(new HomeTabRadioFragment(), "Radio", R.drawable.ic_play_for_work);

        bind.homeViewPager.setAdapter(pager);
        bind.homeViewPager.setOffscreenPageLimit(3);
        bind.homeViewPager.setUserInputEnabled(false);

        new TabLayoutMediator(tabLayout, bind.homeViewPager,
                (tab, position) -> {
                    tab.setText(pager.getPageTitle(position));
                    // tab.setIcon(pager.getPageIcon(position));
                }
        ).attach();

        tabLayout.setVisibility(Preferences.isPodcastSectionVisible() || Preferences.isRadioSectionVisible() ? View.VISIBLE : View.GONE);
    }
}
