package com.cappielloantonio.play.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.cappielloantonio.play.R;
import com.cappielloantonio.play.adapter.AlbumCatalogueAdapter;
import com.cappielloantonio.play.databinding.FragmentAlbumCatalogueBinding;
import com.cappielloantonio.play.helper.recyclerview.ItemDecoration;
import com.cappielloantonio.play.ui.activities.MainActivity;
import com.cappielloantonio.play.viewmodel.AlbumCatalogueViewModel;

public class AlbumCatalogueFragment extends Fragment {
    private static final String TAG = "ArtistCatalogueFragment";

    private FragmentAlbumCatalogueBinding bind;
    private MainActivity activity;
    private AlbumCatalogueViewModel albumCatalogueViewModel;

    private AlbumCatalogueAdapter albumAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();

        bind = FragmentAlbumCatalogueBinding.inflate(inflater, container, false);
        View view = bind.getRoot();
        albumCatalogueViewModel = new ViewModelProvider(requireActivity()).get(AlbumCatalogueViewModel.class);

        initAlbumCatalogueView();

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

    private void initAlbumCatalogueView() {
        bind.albumCatalogueRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        bind.albumCatalogueRecyclerView.addItemDecoration(new ItemDecoration(2, 20, false));
        bind.albumCatalogueRecyclerView.setHasFixedSize(true);

        albumAdapter = new AlbumCatalogueAdapter(requireContext());
        albumAdapter.setClickListener((view, position) -> {
            Bundle bundle = new Bundle();
            bundle.putParcelable("album_object", albumAdapter.getItem(position));
            activity.navController.navigate(R.id.action_albumCatalogueFragment_to_albumPageFragment, bundle);
        });
        bind.albumCatalogueRecyclerView.setAdapter(albumAdapter);
        albumCatalogueViewModel.getAlbumList().observe(requireActivity(), albums -> {
            bind.loadingProgressBar.setVisibility(View.GONE);
            bind.albumCatalogueContainer.setVisibility(View.VISIBLE);
            albumAdapter.setItems(albums);
        });
    }
}