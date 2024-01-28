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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cappielloantonio.tempo.R;
import com.cappielloantonio.tempo.databinding.FragmentAlbumCatalogueBinding;
import com.cappielloantonio.tempo.helper.recyclerview.GridItemDecoration;
import com.cappielloantonio.tempo.interfaces.ClickCallback;
import com.cappielloantonio.tempo.ui.activity.MainActivity;
import com.cappielloantonio.tempo.ui.adapter.AlbumCatalogueAdapter;
import com.cappielloantonio.tempo.util.Constants;
import com.cappielloantonio.tempo.viewmodel.AlbumCatalogueViewModel;

@OptIn(markerClass = UnstableApi.class)
public class AlbumCatalogueFragment extends Fragment implements ClickCallback {
    private static final String TAG = "ArtistCatalogueFragment";

    private FragmentAlbumCatalogueBinding bind;
    private MainActivity activity;
    private AlbumCatalogueViewModel albumCatalogueViewModel;

    private AlbumCatalogueAdapter albumAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        initData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        albumCatalogueViewModel.stopLoading();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();

        bind = FragmentAlbumCatalogueBinding.inflate(inflater, container, false);
        View view = bind.getRoot();

        initAppBar();
        initAlbumCatalogueView();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind = null;
    }

    private void initData() {
        albumCatalogueViewModel = new ViewModelProvider(requireActivity()).get(AlbumCatalogueViewModel.class);
        albumCatalogueViewModel.loadAlbums();
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
                bind.toolbar.setTitle(R.string.album_catalogue_title);
            } else {
                bind.toolbar.setTitle(R.string.empty_string);
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initAlbumCatalogueView() {
        bind.albumCatalogueRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        bind.albumCatalogueRecyclerView.addItemDecoration(new GridItemDecoration(2, 20, false));
        bind.albumCatalogueRecyclerView.setHasFixedSize(true);

        albumAdapter = new AlbumCatalogueAdapter(this);
        albumAdapter.setStateRestorationPolicy(RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY);
        bind.albumCatalogueRecyclerView.setAdapter(albumAdapter);
        albumCatalogueViewModel.getAlbumList().observe(getViewLifecycleOwner(), albums -> albumAdapter.setItems(albums));

        bind.albumCatalogueRecyclerView.setOnTouchListener((v, event) -> {
            hideKeyboard(v);
            return false;
        });

        bind.albumListSortImageView.setOnClickListener(view -> showPopupMenu(view, R.menu.sort_album_popup_menu));
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
                albumAdapter.getFilter().filter(newText);
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
            if (menuItem.getItemId() == R.id.menu_album_sort_name) {
                albumAdapter.sort(Constants.ALBUM_ORDER_BY_NAME);
                return true;
            } else if (menuItem.getItemId() == R.id.menu_album_sort_artist) {
                albumAdapter.sort(Constants.ALBUM_ORDER_BY_ARTIST);
                return true;
            } else if (menuItem.getItemId() == R.id.menu_album_sort_year) {
                albumAdapter.sort(Constants.ALBUM_ORDER_BY_YEAR);
                return true;
            } else if (menuItem.getItemId() == R.id.menu_album_sort_random) {
                albumAdapter.sort(Constants.ALBUM_ORDER_BY_RANDOM);
                return true;
            }

            return false;
        });

        popup.show();
    }

    @Override
    public void onAlbumClick(Bundle bundle) {
        Navigation.findNavController(requireView()).navigate(R.id.albumPageFragment, bundle);
        hideKeyboard(requireView());
    }

    @Override
    public void onAlbumLongClick(Bundle bundle) {
        Navigation.findNavController(requireView()).navigate(R.id.albumBottomSheetDialog, bundle);
    }
}