package com.cappielloantonio.tempo.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.media3.common.util.UnstableApi;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cappielloantonio.tempo.R;
import com.cappielloantonio.tempo.databinding.FragmentAlbumListPageBinding;
import com.cappielloantonio.tempo.interfaces.ClickCallback;
import com.cappielloantonio.tempo.ui.activity.MainActivity;
import com.cappielloantonio.tempo.ui.adapter.AlbumHorizontalAdapter;
import com.cappielloantonio.tempo.util.Constants;
import com.cappielloantonio.tempo.util.MusicUtil;
import com.cappielloantonio.tempo.viewmodel.AlbumListPageViewModel;

@OptIn(markerClass = UnstableApi.class)
public class AlbumListPageFragment extends Fragment implements ClickCallback {
    private FragmentAlbumListPageBinding bind;

    private MainActivity activity;
    private AlbumListPageViewModel albumListPageViewModel;
    private AlbumHorizontalAdapter albumHorizontalAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();

        bind = FragmentAlbumListPageBinding.inflate(inflater, container, false);
        View view = bind.getRoot();
        albumListPageViewModel = new ViewModelProvider(requireActivity()).get(AlbumListPageViewModel.class);

        init();
        initAppBar();
        initAlbumListView();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind = null;
    }

    private void init() {
        if (requireArguments().getString(Constants.ALBUM_RECENTLY_PLAYED) != null) {
            albumListPageViewModel.title = Constants.ALBUM_RECENTLY_PLAYED;
            bind.pageTitleLabel.setText(R.string.album_list_page_recently_played);
        } else if (requireArguments().getString(Constants.ALBUM_MOST_PLAYED) != null) {
            albumListPageViewModel.title = Constants.ALBUM_MOST_PLAYED;
            bind.pageTitleLabel.setText(R.string.album_list_page_most_played);
        } else if (requireArguments().getString(Constants.ALBUM_RECENTLY_ADDED) != null) {
            albumListPageViewModel.title = Constants.ALBUM_RECENTLY_ADDED;
            bind.pageTitleLabel.setText(R.string.album_list_page_recently_added);
        } else if (requireArguments().getString(Constants.ALBUM_STARRED) != null) {
            albumListPageViewModel.title = Constants.ALBUM_STARRED;
            bind.pageTitleLabel.setText(R.string.album_list_page_starred);
        } else if (requireArguments().getString(Constants.ALBUM_NEW_RELEASES) != null) {
            albumListPageViewModel.title = Constants.ALBUM_NEW_RELEASES;
            bind.pageTitleLabel.setText(R.string.album_list_page_new_releases);
        } else if (requireArguments().getString(Constants.ALBUM_DOWNLOADED) != null) {
            albumListPageViewModel.title = Constants.ALBUM_DOWNLOADED;
            bind.pageTitleLabel.setText(R.string.album_list_page_downloaded);
        } else if (requireArguments().getParcelable(Constants.ARTIST_OBJECT) != null) {
            albumListPageViewModel.artist = requireArguments().getParcelable(Constants.ARTIST_OBJECT);
            albumListPageViewModel.title = Constants.ALBUM_FROM_ARTIST;
            bind.pageTitleLabel.setText(albumListPageViewModel.artist.getName());
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
            if ((bind.albumInfoSector.getHeight() + verticalOffset) < (2 * ViewCompat.getMinimumHeight(bind.toolbar))) {
                bind.toolbar.setTitle(R.string.album_list_page_title);
            } else {
                bind.toolbar.setTitle(R.string.empty_string);
            }
        });
    }

    private void initAlbumListView() {
        bind.albumListRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        bind.albumListRecyclerView.setHasFixedSize(true);

        albumHorizontalAdapter = new AlbumHorizontalAdapter(
                this,
                (albumListPageViewModel.title.equals(Constants.ALBUM_DOWNLOADED) || albumListPageViewModel.title.equals(Constants.ALBUM_FROM_ARTIST))
        );

        bind.albumListRecyclerView.setAdapter(albumHorizontalAdapter);
        albumListPageViewModel.getAlbumList(getViewLifecycleOwner()).observe(getViewLifecycleOwner(), albums -> albumHorizontalAdapter.setItems(albums));
    }

    @Override
    public void onAlbumClick(Bundle bundle) {
        Navigation.findNavController(requireView()).navigate(R.id.albumPageFragment, bundle);
    }

    @Override
    public void onAlbumLongClick(Bundle bundle) {
        Navigation.findNavController(requireView()).navigate(R.id.albumBottomSheetDialog, bundle);
    }
}