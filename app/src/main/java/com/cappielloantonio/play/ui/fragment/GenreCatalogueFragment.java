package com.cappielloantonio.play.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.cappielloantonio.play.adapter.ArtistCatalogueAdapter;
import com.cappielloantonio.play.adapter.GenreAdapter;
import com.cappielloantonio.play.adapter.GenreCatalogueAdapter;
import com.cappielloantonio.play.databinding.FragmentArtistCatalogueBinding;
import com.cappielloantonio.play.databinding.FragmentGenreCatalogueBinding;
import com.cappielloantonio.play.viewmodel.ArtistCatalogueViewModel;
import com.cappielloantonio.play.viewmodel.GenreCatalogueViewModel;

import java.util.ArrayList;

public class GenreCatalogueFragment extends Fragment {
    private static final String TAG = "GenreCatalogueFragment";;

    private FragmentGenreCatalogueBinding bind;
    private GenreCatalogueViewModel genreCatalogueViewModel;

    private GenreCatalogueAdapter genreCatalogueAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        bind = FragmentGenreCatalogueBinding.inflate(inflater, container, false);
        View view = bind.getRoot();
        genreCatalogueViewModel = new ViewModelProvider(requireActivity()).get(GenreCatalogueViewModel.class);

        initArtistCatalogueView();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind = null;
    }

    private void initArtistCatalogueView() {
        bind.genreCatalogueRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        bind.genreCatalogueRecyclerView.setHasFixedSize(true);

        genreCatalogueAdapter = new GenreCatalogueAdapter(requireContext(), new ArrayList<>());
        genreCatalogueAdapter.setClickListener((view, position) -> Toast.makeText(requireContext(), "Click: " + position, Toast.LENGTH_SHORT).show());
        bind.genreCatalogueRecyclerView.setAdapter(genreCatalogueAdapter);
        genreCatalogueViewModel.getGenreList().observe(requireActivity(), genres -> {
            bind.loadingProgressBar.setVisibility(View.GONE);
            bind.genreCatalogueContainer.setVisibility(View.VISIBLE);
            genreCatalogueAdapter.setItems(genres);
        });
    }
}