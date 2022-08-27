package com.cappielloantonio.play.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cappielloantonio.play.R;
import com.cappielloantonio.play.adapter.ArtistHorizontalAdapter;
import com.cappielloantonio.play.databinding.FragmentArtistListPageBinding;
import com.cappielloantonio.play.model.Artist;
import com.cappielloantonio.play.ui.activity.MainActivity;
import com.cappielloantonio.play.viewmodel.ArtistListPageViewModel;

public class ArtistListPageFragment extends Fragment {
    private FragmentArtistListPageBinding bind;

    private MainActivity activity;
    private ArtistListPageViewModel artistListPageViewModel;

    private ArtistHorizontalAdapter artistHorizontalAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();

        bind = FragmentArtistListPageBinding.inflate(inflater, container, false);
        View view = bind.getRoot();
        artistListPageViewModel = new ViewModelProvider(requireActivity()).get(ArtistListPageViewModel.class);

        init();
        initAppBar();
        initArtistListView();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind = null;
    }

    private void init() {
        if (requireArguments().getString(Artist.STARRED) != null) {
            artistListPageViewModel.title = Artist.STARRED;
            bind.pageTitleLabel.setText(R.string.artist_list_page_starred);
        } else if (requireArguments().getString(Artist.DOWNLOADED) != null) {
            artistListPageViewModel.title = Artist.DOWNLOADED;
            bind.pageTitleLabel.setText(R.string.artist_list_page_downloaded);
        }
    }

    private void initAppBar() {
        activity.setSupportActionBar(bind.toolbar);

        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        bind.toolbar.setNavigationOnClickListener(v -> activity.navController.navigateUp());

        bind.appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if ((bind.artistInfoSector.getHeight() + verticalOffset) < (2 * ViewCompat.getMinimumHeight(bind.toolbar))) {
                bind.toolbar.setTitle(R.string.artist_list_page_title);
            } else {
                bind.toolbar.setTitle(R.string.empty_string);
            }
        });
    }

    private void initArtistListView() {
        bind.artistListRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        bind.artistListRecyclerView.setHasFixedSize(true);

        artistHorizontalAdapter = new ArtistHorizontalAdapter(requireContext(), artistListPageViewModel.title.equals(Artist.DOWNLOADED));
        bind.artistListRecyclerView.setAdapter(artistHorizontalAdapter);
        artistListPageViewModel.getArtistList(getViewLifecycleOwner()).observe(getViewLifecycleOwner(), artists -> artistHorizontalAdapter.setItems(artists));
    }
}