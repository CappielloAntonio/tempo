package com.cappielloantonio.tempo.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.PopupMenu;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.cappielloantonio.tempo.subsonic.models.AlbumID3;
import com.cappielloantonio.tempo.ui.activity.MainActivity;
import com.cappielloantonio.tempo.ui.adapter.AlbumHorizontalAdapter;
import com.cappielloantonio.tempo.util.Constants;
import com.cappielloantonio.tempo.viewmodel.AlbumListPageViewModel;

import java.util.List;

@OptIn(markerClass = UnstableApi.class)
public class AlbumListPageFragment extends Fragment implements ClickCallback {
    private FragmentAlbumListPageBinding bind;

    private MainActivity activity;
    private AlbumListPageViewModel albumListPageViewModel;
    private AlbumHorizontalAdapter albumHorizontalAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

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

        bind.toolbar.setNavigationOnClickListener(v -> {
            hideKeyboard(v);
            activity.navController.navigateUp();
        });

        bind.appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if ((bind.albumInfoSector.getHeight() + verticalOffset) < (2 * ViewCompat.getMinimumHeight(bind.toolbar))) {
                bind.toolbar.setTitle(R.string.album_list_page_title);
            } else {
                bind.toolbar.setTitle(R.string.empty_string);
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initAlbumListView() {
        bind.albumListRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        bind.albumListRecyclerView.setHasFixedSize(true);

        albumHorizontalAdapter = new AlbumHorizontalAdapter(
                this,
                (albumListPageViewModel.title.equals(Constants.ALBUM_DOWNLOADED) || albumListPageViewModel.title.equals(Constants.ALBUM_FROM_ARTIST))
        );

        bind.albumListRecyclerView.setAdapter(albumHorizontalAdapter);
        albumListPageViewModel.getAlbumList(getViewLifecycleOwner()).observe(getViewLifecycleOwner(), albums -> {
            albumHorizontalAdapter.setItems(albums);
            setAlbumListPageSubtitle(albums);
            setAlbumListPageSorter();
        });

        bind.albumListRecyclerView.setOnTouchListener((v, event) -> {
            hideKeyboard(v);
            return false;
        });

        bind.albumListSortImageView.setOnClickListener(view -> showPopupMenu(view, R.menu.sort_horizontal_album_popup_menu));
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                albumHorizontalAdapter.getFilter().filter(newText);
                return false;
            }
        });

        searchView.setPadding(-32, 0, 0, 0);
    }

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void showPopupMenu(View view, int menuResource) {
        PopupMenu popup = new PopupMenu(requireContext(), view);
        popup.getMenuInflater().inflate(menuResource, popup.getMenu());

        popup.setOnMenuItemClickListener(menuItem -> {
            if (menuItem.getItemId() == R.id.menu_horizontal_album_sort_name) {
                albumHorizontalAdapter.sort(Constants.ALBUM_ORDER_BY_NAME);
                return true;
            } else if (menuItem.getItemId() == R.id.menu_horizontal_album_sort_most_recently_starred) {
                albumHorizontalAdapter.sort(Constants.ALBUM_ORDER_BY_MOST_RECENTLY_STARRED);
                return true;
            } else if (menuItem.getItemId() == R.id.menu_horizontal_album_sort_least_recently_starred) {
                albumHorizontalAdapter.sort(Constants.ALBUM_ORDER_BY_LEAST_RECENTLY_STARRED);
                return true;
            }

            return false;
        });

        popup.show();
    }

    private void setAlbumListPageSubtitle(List<AlbumID3> albums) {
        switch (albumListPageViewModel.title) {
            case Constants.ALBUM_RECENTLY_PLAYED:
            case Constants.ALBUM_MOST_PLAYED:
            case Constants.ALBUM_RECENTLY_ADDED:
                bind.pageSubtitleLabel.setText(albums.size() < albumListPageViewModel.maxNumber ?
                        getString(R.string.generic_list_page_count, albums.size()) :
                        getString(R.string.generic_list_page_count_unknown, albumListPageViewModel.maxNumber)
                );
                break;
            case Constants.ALBUM_STARRED:
                bind.pageSubtitleLabel.setText(getString(R.string.generic_list_page_count, albums.size()));
                break;
        }
    }

    private void setAlbumListPageSorter() {
        switch (albumListPageViewModel.title) {
            case Constants.ALBUM_RECENTLY_PLAYED:
            case Constants.ALBUM_MOST_PLAYED:
            case Constants.ALBUM_RECENTLY_ADDED:
                bind.albumListSortImageView.setVisibility(View.GONE);
                break;
            case Constants.ALBUM_STARRED:
                bind.albumListSortImageView.setVisibility(View.VISIBLE);
                break;
        }
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