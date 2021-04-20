package com.cappielloantonio.play.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cappielloantonio.play.R;
import com.cappielloantonio.play.adapter.GenreCatalogueAdapter;
import com.cappielloantonio.play.adapter.PlaylistAdapter;
import com.cappielloantonio.play.adapter.PlaylistCatalogueAdapter;
import com.cappielloantonio.play.databinding.FragmentGenreCatalogueBinding;
import com.cappielloantonio.play.databinding.FragmentPlaylistCatalogueBinding;
import com.cappielloantonio.play.helper.recyclerview.GridItemDecoration;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.ui.activities.MainActivity;
import com.cappielloantonio.play.viewmodel.GenreCatalogueViewModel;
import com.cappielloantonio.play.viewmodel.PlaylistCatalogueViewModel;

import java.util.ArrayList;

public class PlaylistCatalogueFragment extends Fragment {
    private static final String TAG = "GenreCatalogueFragment";;

    private FragmentPlaylistCatalogueBinding bind;
    private MainActivity activity;
    private PlaylistCatalogueViewModel playlistCatalogueViewModel;

    private PlaylistCatalogueAdapter playlistCatalogueAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();

        bind = FragmentPlaylistCatalogueBinding.inflate(inflater, container, false);
        View view = bind.getRoot();
        playlistCatalogueViewModel = new ViewModelProvider(requireActivity()).get(PlaylistCatalogueViewModel.class);

        initArtistCatalogueView();

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

    private void initArtistCatalogueView() {
        bind.playlistCatalogueRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        bind.playlistCatalogueRecyclerView.setHasFixedSize(true);

        playlistCatalogueAdapter = new PlaylistCatalogueAdapter(requireContext());
        bind.playlistCatalogueRecyclerView.setAdapter(playlistCatalogueAdapter);
        playlistCatalogueViewModel.getPlaylistList().observe(requireActivity(), playlist -> {
            playlistCatalogueAdapter.setItems(playlist);
        });
    }
}