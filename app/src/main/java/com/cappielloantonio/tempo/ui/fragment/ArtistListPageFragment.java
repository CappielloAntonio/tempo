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
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.media3.common.util.UnstableApi;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cappielloantonio.tempo.R;
import com.cappielloantonio.tempo.databinding.FragmentArtistListPageBinding;
import com.cappielloantonio.tempo.interfaces.ClickCallback;
import com.cappielloantonio.tempo.subsonic.models.AlbumID3;
import com.cappielloantonio.tempo.subsonic.models.ArtistID3;
import com.cappielloantonio.tempo.ui.activity.MainActivity;
import com.cappielloantonio.tempo.ui.adapter.ArtistHorizontalAdapter;
import com.cappielloantonio.tempo.util.Constants;
import com.cappielloantonio.tempo.viewmodel.ArtistListPageViewModel;

import java.util.List;

@UnstableApi
public class ArtistListPageFragment extends Fragment implements ClickCallback {
    private FragmentArtistListPageBinding bind;

    private MainActivity activity;
    private ArtistListPageViewModel artistListPageViewModel;

    private ArtistHorizontalAdapter artistHorizontalAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();

        bind = FragmentArtistListPageBinding.inflate(inflater, container, false);
        View view = bind.getRoot();
        artistListPageViewModel = new ViewModelProvider(requireActivity()).get(ArtistListPageViewModel.class);

        init();
        initAppBar();
        initArtistListView();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind = null;
    }

    private void init() {
        if (requireArguments().getString(Constants.ARTIST_STARRED) != null) {
            artistListPageViewModel.title = Constants.ARTIST_STARRED;
            bind.pageTitleLabel.setText(R.string.artist_list_page_starred);
        } else if (requireArguments().getString(Constants.ARTIST_DOWNLOADED) != null) {
            artistListPageViewModel.title = Constants.ARTIST_DOWNLOADED;
            bind.pageTitleLabel.setText(R.string.artist_list_page_downloaded);
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
            if ((bind.artistInfoSector.getHeight() + verticalOffset) < (2 * ViewCompat.getMinimumHeight(bind.toolbar))) {
                bind.toolbar.setTitle(R.string.artist_list_page_title);
            } else {
                bind.toolbar.setTitle(R.string.empty_string);
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initArtistListView() {
        bind.artistListRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        bind.artistListRecyclerView.setHasFixedSize(true);

        artistHorizontalAdapter = new ArtistHorizontalAdapter(this);
        bind.artistListRecyclerView.setAdapter(artistHorizontalAdapter);
        artistListPageViewModel.getArtistList(getViewLifecycleOwner()).observe(getViewLifecycleOwner(), artists -> {
            artistHorizontalAdapter.setItems(artists);
            setArtistListPageSubtitle(artists);
            setArtistListPageSorter();
        });

        bind.artistListRecyclerView.setOnTouchListener((v, event) -> {
            hideKeyboard(v);
            return false;
        });

        bind.artistListSortImageView.setOnClickListener(view -> showPopupMenu(view, R.menu.sort_horizontal_artist_popup_menu));
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
                artistHorizontalAdapter.getFilter().filter(newText);
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
            if (menuItem.getItemId() == R.id.menu_horizontal_artist_sort_name) {
                artistHorizontalAdapter.sort(Constants.ARTIST_ORDER_BY_NAME);
                return true;
            } else if (menuItem.getItemId() == R.id.menu_horizontal_artist_sort_most_recently_starred) {
                artistHorizontalAdapter.sort(Constants.ARTIST_ORDER_BY_MOST_RECENTLY_STARRED);
                return true;
            } else if (menuItem.getItemId() == R.id.menu_horizontal_artist_sort_least_recently_starred) {
                artistHorizontalAdapter.sort(Constants.ARTIST_ORDER_BY_LEAST_RECENTLY_STARRED);
                return true;
            }

            return false;
        });

        popup.show();
    }

    private void setArtistListPageSubtitle(List<ArtistID3> artists) {
        switch (artistListPageViewModel.title) {
            case Constants.ARTIST_STARRED:
            case Constants.ARTIST_DOWNLOADED:
                bind.pageSubtitleLabel.setText(getString(R.string.generic_list_page_count, artists.size()));
                break;
        }
    }

    private void setArtistListPageSorter() {
        switch (artistListPageViewModel.title) {
            case Constants.ARTIST_STARRED:
            case Constants.ARTIST_DOWNLOADED:
                bind.artistListSortImageView.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onArtistClick(Bundle bundle) {
        Navigation.findNavController(requireView()).navigate(R.id.artistPageFragment, bundle);
    }

    @Override
    public void onArtistLongClick(Bundle bundle) {
        Navigation.findNavController(requireView()).navigate(R.id.artistBottomSheetDialog, bundle);
    }
}