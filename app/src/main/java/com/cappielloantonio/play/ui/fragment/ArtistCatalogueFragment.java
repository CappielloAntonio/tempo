package com.cappielloantonio.play.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cappielloantonio.play.R;
import com.cappielloantonio.play.adapter.ArtistAdapter;
import com.cappielloantonio.play.adapter.ArtistCatalogueAdapter;
import com.cappielloantonio.play.adapter.RecentMusicAdapter;
import com.cappielloantonio.play.databinding.FragmentArtistCatalogueBinding;
import com.cappielloantonio.play.helper.recyclerview.ItemlDecoration;
import com.cappielloantonio.play.model.Artist;
import com.cappielloantonio.play.ui.activities.MainActivity;
import com.cappielloantonio.play.viewmodel.ArtistCatalogueViewModel;

import java.util.ArrayList;
import java.util.List;

public class ArtistCatalogueFragment extends Fragment {
    private static final String TAG = "ArtistCatalogueFragment";

    private FragmentArtistCatalogueBinding bind;
    private ArtistCatalogueViewModel artistCatalogueViewModel;

    private ArtistCatalogueAdapter artistAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        bind = FragmentArtistCatalogueBinding.inflate(inflater, container, false);
        View view = bind.getRoot();
        artistCatalogueViewModel = new ViewModelProvider(requireActivity()).get(ArtistCatalogueViewModel.class);

        initArtistCatalogueView();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind = null;
    }

    private void initArtistCatalogueView() {
        bind.artistCatalogueRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        bind.artistCatalogueRecyclerView.addItemDecoration(new ItemlDecoration(2, 20, false));
        bind.artistCatalogueRecyclerView.setHasFixedSize(true);

        artistAdapter = new ArtistCatalogueAdapter(requireContext(), new ArrayList<>());
        artistAdapter.setClickListener((view, position) -> Toast.makeText(requireContext(), "Click: " + position, Toast.LENGTH_SHORT).show());
        bind.artistCatalogueRecyclerView.setAdapter(artistAdapter);
        artistCatalogueViewModel.getArtistList().observe(requireActivity(), artists -> {
            bind.loadingProgressBar.setVisibility(View.GONE);
            bind.artistCatalogueContainer.setVisibility(View.VISIBLE);
            artistAdapter.setItems(artists);
        });
    }
}