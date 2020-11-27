package com.cappielloantonio.play.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.cappielloantonio.play.R;
import com.cappielloantonio.play.adapter.GenreCatalogueAdapter;
import com.cappielloantonio.play.databinding.FragmentGenreCatalogueBinding;
import com.cappielloantonio.play.helper.recyclerview.ItemDecoration;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.ui.activities.MainActivity;
import com.cappielloantonio.play.viewmodel.GenreCatalogueViewModel;

import java.util.ArrayList;

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

        init();
        initArtistCatalogueView();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind = null;
    }

    private void init() {
        bind.filterGenresTextViewClickable.setOnClickListener(v -> activity.navController.navigate(R.id.action_genreCatalogueFragment_to_filterFragment));
    }

    private void initArtistCatalogueView() {
        bind.genreCatalogueRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        bind.genreCatalogueRecyclerView.addItemDecoration(new ItemDecoration(2, 16, false));
        bind.genreCatalogueRecyclerView.setHasFixedSize(true);

        genreCatalogueAdapter = new GenreCatalogueAdapter(requireContext(), new ArrayList<>());
        genreCatalogueAdapter.setClickListener((view, position) -> {
            Bundle bundle = new Bundle();
            bundle.putString(Song.BY_GENRE, Song.BY_GENRE);
            bundle.putParcelable("genre_object", genreCatalogueAdapter.getItem(position));
            activity.navController.navigate(R.id.action_genreCatalogueFragment_to_songListPageFragment, bundle);
        });
        bind.genreCatalogueRecyclerView.setAdapter(genreCatalogueAdapter);

        genreCatalogueViewModel.getGenreList().observe(requireActivity(), genres -> {
            bind.loadingProgressBar.setVisibility(View.GONE);
            bind.genreCatalogueContainer.setVisibility(View.VISIBLE);
            genreCatalogueAdapter.setItems(genres);
        });
    }
}