package com.cappielloantonio.play.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.cappielloantonio.play.R;
import com.cappielloantonio.play.adapter.ArtistCatalogueAdapter;
import com.cappielloantonio.play.databinding.FragmentArtistCatalogueBinding;
import com.cappielloantonio.play.helper.recyclerview.ItemDecoration;
import com.cappielloantonio.play.ui.activities.MainActivity;
import com.cappielloantonio.play.viewmodel.ArtistCatalogueViewModel;

public class ArtistCatalogueFragment extends Fragment {
    private static final String TAG = "ArtistCatalogueFragment";

    private FragmentArtistCatalogueBinding bind;
    private MainActivity activity;
    private ArtistCatalogueViewModel artistCatalogueViewModel;

    private ArtistCatalogueAdapter artistAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();

        bind = FragmentArtistCatalogueBinding.inflate(inflater, container, false);
        View view = bind.getRoot();
        artistCatalogueViewModel = new ViewModelProvider(requireActivity()).get(ArtistCatalogueViewModel.class);

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
        bind.artistCatalogueRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        bind.artistCatalogueRecyclerView.addItemDecoration(new ItemDecoration(2, 20, false));
        bind.artistCatalogueRecyclerView.setHasFixedSize(true);

        artistAdapter = new ArtistCatalogueAdapter(requireContext());
        artistAdapter.setClickListener((view, position) -> {
            Bundle bundle = new Bundle();
            bundle.putParcelable("artist_object", artistAdapter.getItem(position));
            activity.navController.navigate(R.id.action_artistCatalogueFragment_to_artistPageFragment, bundle);
        });
        bind.artistCatalogueRecyclerView.setAdapter(artistAdapter);
        artistCatalogueViewModel.getArtistList().observe(requireActivity(), artists -> {
            bind.loadingProgressBar.setVisibility(View.GONE);
            bind.artistCatalogueContainer.setVisibility(View.VISIBLE);
            artistAdapter.setItems(artists);
        });
    }
}