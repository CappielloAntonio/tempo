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
import com.cappielloantonio.play.adapter.GenreCatalogueAdapter;
import com.cappielloantonio.play.databinding.FragmentGenreCatalogueBinding;
import com.cappielloantonio.play.helper.recyclerview.GridItemDecoration;
import com.cappielloantonio.play.model.Artist;
import com.cappielloantonio.play.model.Genre;
import com.cappielloantonio.play.model.Media;
import com.cappielloantonio.play.ui.activity.MainActivity;
import com.cappielloantonio.play.viewmodel.GenreCatalogueViewModel;

public class GenreCatalogueFragment extends Fragment {
    private static final String TAG = "GenreCatalogueFragment";

    private FragmentGenreCatalogueBinding bind;
    private MainActivity activity;
    private GenreCatalogueViewModel genreCatalogueViewModel;

    private GenreCatalogueAdapter genreCatalogueAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();

        bind = FragmentGenreCatalogueBinding.inflate(inflater, container, false);
        View view = bind.getRoot();
        genreCatalogueViewModel = new ViewModelProvider(requireActivity()).get(GenreCatalogueViewModel.class);

        init();
        initAppBar();
        initGenreCatalogueView();

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

    private void init() {
        bind.filterGenresTextViewClickable.setOnClickListener(v -> activity.navController.navigate(R.id.action_genreCatalogueFragment_to_filterFragment));
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
            if ((bind.genreInfoSector.getHeight() + verticalOffset) < (2 * ViewCompat.getMinimumHeight(bind.toolbar))) {
                bind.toolbar.setTitle(R.string.genre_catalogue_title);
            } else {
                bind.toolbar.setTitle(R.string.empty_string);
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initGenreCatalogueView() {
        bind.genreCatalogueRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        bind.genreCatalogueRecyclerView.addItemDecoration(new GridItemDecoration(2, 16, false));
        bind.genreCatalogueRecyclerView.setHasFixedSize(true);

        genreCatalogueAdapter = new GenreCatalogueAdapter(activity, requireContext());
        genreCatalogueAdapter.setStateRestorationPolicy(RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY);
        bind.genreCatalogueRecyclerView.setAdapter(genreCatalogueAdapter);
        genreCatalogueAdapter.setClickListener((view, position) -> {
            Bundle bundle = new Bundle();
            bundle.putString(Media.BY_GENRE, Media.BY_GENRE);
            bundle.putParcelable("genre_object", genreCatalogueAdapter.getItem(position));
            activity.navController.navigate(R.id.action_genreCatalogueFragment_to_songListPageFragment, bundle);
        });

        genreCatalogueViewModel.getGenreList().observe(requireActivity(), genres -> genreCatalogueAdapter.setItems(genres));

        bind.genreCatalogueRecyclerView.setOnTouchListener((v, event) -> {
            hideKeyboard(v);
            return false;
        });

        bind.genreListSortImageView.setOnClickListener(view -> showPopupMenu(view, R.menu.sort_genre_popup_menu));
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
                genreCatalogueAdapter.getFilter().filter(newText);
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
            if (menuItem.getItemId() == R.id.menu_genre_sort_name) {
                genreCatalogueAdapter.sort(Genre.ORDER_BY_NAME);
                return true;
            } else if (menuItem.getItemId() == R.id.menu_genre_sort_random) {
                genreCatalogueAdapter.sort(Genre.ORDER_BY_RANDOM);
                return true;
            }

            return false;
        });

        popup.show();
    }
}