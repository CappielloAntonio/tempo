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
import com.cappielloantonio.play.adapter.AlbumArtistPageAdapter;
import com.cappielloantonio.play.adapter.SongResultSearchAdapter;
import com.cappielloantonio.play.databinding.FragmentArtistPageBinding;
import com.cappielloantonio.play.glide.CustomGlideRequest;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.ui.activities.MainActivity;
import com.cappielloantonio.play.viewmodel.ArtistPageViewModel;

public class ArtistPageFragment extends Fragment {

    private FragmentArtistPageBinding bind;
    private MainActivity activity;
    private ArtistPageViewModel artistPageViewModel;

    private SongResultSearchAdapter songResultSearchAdapter;
    private AlbumArtistPageAdapter albumArtistPageAdapter;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initAppBar();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();

        bind = FragmentArtistPageBinding.inflate(inflater, container, false);
        View view = bind.getRoot();
        artistPageViewModel = new ViewModelProvider(requireActivity()).get(ArtistPageViewModel.class);

        init();
        initBackdrop();
        initTopSongsView();
        initAlbumsView();

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
        artistPageViewModel.setArtist(getArguments().getParcelable("artist_object"));

        bind.mostStreamedSongTextViewClickable.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString(Song.BY_ARTIST, Song.BY_ARTIST);
            bundle.putParcelable("artist_object", artistPageViewModel.getArtist());
            activity.navController.navigate(R.id.action_artistPageFragment_to_songListPageFragment, bundle);
        });
    }

    private void initAppBar() {
        activity.setSupportActionBar(bind.animToolbar);
        if (activity.getSupportActionBar() != null)
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bind.collapsingToolbar.setTitle(artistPageViewModel.getArtist().getName());
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

    private void initBackdrop() {
        CustomGlideRequest.Builder
                .from(requireContext(), artistPageViewModel.getArtist().getBackdrop(), artistPageViewModel.getArtist().getBackdropBlurHash(), CustomGlideRequest.BACKDROP, CustomGlideRequest.TOP_QUALITY, CustomGlideRequest.ARTIST_PIC)
                .build()
                .into(bind.artistBackdropImageView);
    }

    private void initTopSongsView() {
        bind.mostStreamedSongRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        songResultSearchAdapter = new SongResultSearchAdapter(activity, requireContext(), getChildFragmentManager());
        bind.mostStreamedSongRecyclerView.setAdapter(songResultSearchAdapter);
        artistPageViewModel.getArtistTopSongList().observe(requireActivity(), songs -> songResultSearchAdapter.setItems(songs));
    }

    private void initAlbumsView() {
        bind.albumsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));

        albumArtistPageAdapter = new AlbumArtistPageAdapter(requireContext());
        bind.albumsRecyclerView.setAdapter(albumArtistPageAdapter);
        artistPageViewModel.getAlbumList().observe(requireActivity(), songs -> albumArtistPageAdapter.setItems(songs));
    }
}