package com.cappielloantonio.play.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.cappielloantonio.play.adapter.AlbumCatalogueAdapter;
import com.cappielloantonio.play.adapter.ArtistCatalogueAdapter;
import com.cappielloantonio.play.databinding.FragmentAlbumCatalogueBinding;
import com.cappielloantonio.play.databinding.FragmentArtistCatalogueBinding;
import com.cappielloantonio.play.viewmodel.AlbumCatalogueViewModel;
import com.cappielloantonio.play.viewmodel.ArtistCatalogueViewModel;

import java.util.ArrayList;

public class AlbumCatalogueFragment extends Fragment {
    private static final String TAG = "ArtistCatalogueFragment";

    private FragmentAlbumCatalogueBinding bind;
    private AlbumCatalogueViewModel albumCatalogueViewModel;

    private AlbumCatalogueAdapter albumAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        bind = FragmentAlbumCatalogueBinding.inflate(inflater, container, false);
        View view = bind.getRoot();
        albumCatalogueViewModel = new ViewModelProvider(requireActivity()).get(AlbumCatalogueViewModel.class);

        initAlbumCatalogueView();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind = null;
    }

    private void initAlbumCatalogueView() {
        bind.albumCatalogueRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        bind.albumCatalogueRecyclerView.setHasFixedSize(true);

        albumAdapter = new AlbumCatalogueAdapter(requireContext(), new ArrayList<>());
        albumAdapter.setClickListener((view, position) -> Toast.makeText(requireContext(), "Click: " + position, Toast.LENGTH_SHORT).show());
        bind.albumCatalogueRecyclerView.setAdapter(albumAdapter);
        albumCatalogueViewModel.getAlbumList().observe(requireActivity(), albums -> {
            bind.loadingProgressBar.setVisibility(View.GONE);
            bind.albumCatalogueContainer.setVisibility(View.VISIBLE);
            albumAdapter.setItems(albums);
        });
    }
}