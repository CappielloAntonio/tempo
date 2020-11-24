package com.cappielloantonio.play.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.cappielloantonio.play.App;
import com.cappielloantonio.play.R;
import com.cappielloantonio.play.adapter.ArtistCatalogueAdapter;
import com.cappielloantonio.play.adapter.GenreAdapter;
import com.cappielloantonio.play.adapter.GenreCatalogueAdapter;
import com.cappielloantonio.play.databinding.FragmentArtistCatalogueBinding;
import com.cappielloantonio.play.databinding.FragmentGenreCatalogueBinding;
import com.cappielloantonio.play.helper.recyclerview.ItemlDecoration;
import com.cappielloantonio.play.interfaces.MediaCallback;
import com.cappielloantonio.play.model.Genre;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.model.SongGenreCross;
import com.cappielloantonio.play.repository.GenreRepository;
import com.cappielloantonio.play.ui.activities.MainActivity;
import com.cappielloantonio.play.util.PreferenceUtil;
import com.cappielloantonio.play.util.SyncUtil;
import com.cappielloantonio.play.viewmodel.ArtistCatalogueViewModel;
import com.cappielloantonio.play.viewmodel.GenreCatalogueViewModel;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class GenreCatalogueFragment extends Fragment {
    private static final String TAG = "GenreCatalogueFragment";;

    private FragmentGenreCatalogueBinding bind;
    private MainActivity activity;
    private GenreCatalogueViewModel genreCatalogueViewModel;

    private GenreCatalogueAdapter genreCatalogueAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();

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
        bind.genreCatalogueRecyclerView.addItemDecoration(new ItemlDecoration(2, 20, false));
        bind.genreCatalogueRecyclerView.setHasFixedSize(true);

        genreCatalogueAdapter = new GenreCatalogueAdapter(requireContext(), new ArrayList<>());
        genreCatalogueAdapter.setClickListener(new GenreCatalogueAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putString(Song.BY_GENRE, Song.BY_GENRE);
                bundle.putParcelable("genre_object", genreCatalogueAdapter.getItem(position));
                activity.navController.navigate(R.id.action_genreCatalogueFragment_to_songListPageFragment, bundle);
            }
        });
        bind.genreCatalogueRecyclerView.setAdapter(genreCatalogueAdapter);

        genreCatalogueViewModel.getGenreList().observe(requireActivity(), genres -> {
            bind.loadingProgressBar.setVisibility(View.GONE);
            bind.genreCatalogueContainer.setVisibility(View.VISIBLE);
            genreCatalogueAdapter.setItems(genres);
        });
    }
}