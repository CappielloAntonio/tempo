package com.cappielloantonio.play.ui.fragment;

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
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cappielloantonio.play.R;
import com.cappielloantonio.play.adapter.ArtistCatalogueAdapter;
import com.cappielloantonio.play.databinding.FragmentArtistCatalogueBinding;
import com.cappielloantonio.play.helper.recyclerview.GridItemDecoration;
import com.cappielloantonio.play.model.Artist;
import com.cappielloantonio.play.ui.activity.MainActivity;
import com.cappielloantonio.play.viewmodel.ArtistCatalogueViewModel;

public class ArtistCatalogueFragment extends Fragment {
    private static final String TAG = "ArtistCatalogueFragment";

    private FragmentArtistCatalogueBinding bind;
    private MainActivity activity;
    private ArtistCatalogueViewModel artistCatalogueViewModel;

    private ArtistCatalogueAdapter artistAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        initData();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();

        bind = FragmentArtistCatalogueBinding.inflate(inflater, container, false);
        View view = bind.getRoot();

        initAppBar();
        initArtistCatalogueView();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind = null;
    }

    private void initData() {
        artistCatalogueViewModel = new ViewModelProvider(requireActivity()).get(ArtistCatalogueViewModel.class);
        artistCatalogueViewModel.loadArtists(requireContext());
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
            if ((bind.artistInfoSector.getHeight() + verticalOffset) < (2 * ViewCompat.getMinimumHeight(bind.toolbar))) {
                bind.toolbar.setTitle(R.string.artist_catalogue_title);
            } else {
                bind.toolbar.setTitle(R.string.empty_string);
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initArtistCatalogueView() {
        bind.artistCatalogueRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        bind.artistCatalogueRecyclerView.addItemDecoration(new GridItemDecoration(2, 20, false));
        bind.artistCatalogueRecyclerView.setHasFixedSize(true);

        artistAdapter = new ArtistCatalogueAdapter(activity, requireContext());
        artistAdapter.setStateRestorationPolicy(RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY);
        bind.artistCatalogueRecyclerView.setAdapter(artistAdapter);
        artistCatalogueViewModel.getArtistList().observe(requireActivity(), artistList -> artistAdapter.setItems(artistList));

        bind.artistCatalogueRecyclerView.setOnTouchListener((v, event) -> {
            hideKeyboard(v);
            return false;
        });

        bind.artistListSortImageView.setOnClickListener(view -> showPopupMenu(view, R.menu.sort_artist_popup_menu));
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
                artistAdapter.getFilter().filter(newText);
                return false;
            }
        });

        searchView.setPadding(-32, 0, 0, 0);
    }

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void showPopupMenu(View view, int menuResource) {
        PopupMenu popup = new PopupMenu(requireContext(), view);
        popup.getMenuInflater().inflate(menuResource, popup.getMenu());

        popup.setOnMenuItemClickListener(menuItem -> {
            if (menuItem.getItemId() == R.id.menu_artist_sort_name) {
                artistAdapter.sort(Artist.ORDER_BY_NAME);
                return true;
            } else if (menuItem.getItemId() == R.id.menu_artist_sort_random) {
                artistAdapter.sort(Artist.ORDER_BY_RANDOM);
                return true;
            }

            return false;
        });

        popup.show();
    }
}