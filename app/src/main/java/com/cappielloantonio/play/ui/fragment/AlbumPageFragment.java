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

import com.cappielloantonio.play.R;
import com.cappielloantonio.play.adapter.SongResultSearchAdapter;
import com.cappielloantonio.play.databinding.FragmentAlbumPageBinding;
import com.cappielloantonio.play.glide.CustomGlideRequest;
import com.cappielloantonio.play.ui.activities.MainActivity;
import com.cappielloantonio.play.viewmodel.AlbumPageViewModel;

public class AlbumPageFragment extends Fragment {

    private FragmentAlbumPageBinding bind;
    private MainActivity activity;
    private AlbumPageViewModel albumPageViewModel;

    private SongResultSearchAdapter songResultSearchAdapter;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initAppBar();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();

        bind = FragmentAlbumPageBinding.inflate(inflater, container, false);
        View view = bind.getRoot();
        albumPageViewModel = new ViewModelProvider(requireActivity()).get(AlbumPageViewModel.class);

        init();
        initBackCover();
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
        albumPageViewModel.setAlbum(getArguments().getParcelable("album_object"));
    }

    private void initAppBar() {
        activity.setSupportActionBar(bind.animToolbar);
        if (activity.getSupportActionBar() != null)
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bind.collapsingToolbar.setTitle(albumPageViewModel.getAlbum().getTitle());
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

    private void initBackCover() {
        CustomGlideRequest.Builder
                .from(requireContext(), albumPageViewModel.getAlbum().getPrimary(), albumPageViewModel.getAlbum().getBlurHash(), CustomGlideRequest.PRIMARY, CustomGlideRequest.TOP_QUALITY)
                .build()
                .into(bind.albumBackCoverImageView);
    }

    private void initSongsView() {
        bind.songRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        bind.songRecyclerView.setHasFixedSize(true);

        songResultSearchAdapter = new SongResultSearchAdapter(activity, requireContext(), getChildFragmentManager());
        bind.songRecyclerView.setAdapter(songResultSearchAdapter);
        albumPageViewModel.getAlbumSongList().observe(requireActivity(), songs -> songResultSearchAdapter.setItems(songs));
    }
}