package com.cappielloantonio.play.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cappielloantonio.play.R;
import com.cappielloantonio.play.adapter.AlbumAdapter;
import com.cappielloantonio.play.adapter.AlbumCatalogueAdapter;
import com.cappielloantonio.play.adapter.ArtistAdapter;
import com.cappielloantonio.play.adapter.ArtistCatalogueAdapter;
import com.cappielloantonio.play.adapter.GenreCatalogueAdapter;
import com.cappielloantonio.play.adapter.RecentSearchAdapter;
import com.cappielloantonio.play.adapter.SongResultSearchAdapter;
import com.cappielloantonio.play.databinding.FragmentSearchBinding;
import com.cappielloantonio.play.helper.recyclerview.GridItemDecoration;
import com.cappielloantonio.play.model.RecentSearch;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.ui.activities.MainActivity;
import com.cappielloantonio.play.viewmodel.SearchViewModel;
import com.paulrybitskyi.persistentsearchview.adapters.model.SuggestionItem;
import com.paulrybitskyi.persistentsearchview.listeners.OnSuggestionChangeListener;
import com.paulrybitskyi.persistentsearchview.utils.SuggestionCreationUtil;

import java.util.ArrayList;

public class SearchFragment extends Fragment {
    private static final String TAG = "SearchFragment";

    private FragmentSearchBinding bind;
    private MainActivity activity;
    private SearchViewModel searchViewModel;

    private SongResultSearchAdapter songResultSearchAdapter;
    private AlbumAdapter albumAdapter;
    private ArtistAdapter artistAdapter;
    private GenreCatalogueAdapter genreCatalogueAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();

        bind = FragmentSearchBinding.inflate(inflater, container, false);
        View view = bind.getRoot();
        searchViewModel = new ViewModelProvider(requireActivity()).get(SearchViewModel.class);

        initSearchResultView();
        initSearchView();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        activity.setBottomNavigationBarVisibility(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        inputFocus();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind = null;
    }

    private void initSearchResultView() {
        // Songs
        bind.searchResultTracksRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        bind.searchResultTracksRecyclerView.setHasFixedSize(true);

        songResultSearchAdapter = new SongResultSearchAdapter(activity, requireContext(), getChildFragmentManager());
        bind.searchResultTracksRecyclerView.setAdapter(songResultSearchAdapter);

        // Albums
        bind.searchResultAlbumRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        bind.searchResultAlbumRecyclerView.setHasFixedSize(true);

        albumAdapter = new AlbumAdapter(requireContext());
        bind.searchResultAlbumRecyclerView.setAdapter(albumAdapter);

        // Artists
        bind.searchResultArtistRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        bind.searchResultArtistRecyclerView.setHasFixedSize(true);

        artistAdapter = new ArtistAdapter(requireContext());
        bind.searchResultArtistRecyclerView.setAdapter(artistAdapter);

        // Genres
        bind.searchResultGenreRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        bind.searchResultGenreRecyclerView.addItemDecoration(new GridItemDecoration(2, 16, false));
        bind.searchResultGenreRecyclerView.setHasFixedSize(true);

        genreCatalogueAdapter = new GenreCatalogueAdapter(requireContext());
        genreCatalogueAdapter.setClickListener((view, position) -> {
            Bundle bundle = new Bundle();
            bundle.putString(Song.BY_GENRE, Song.BY_GENRE);
            bundle.putParcelable("genre_object", genreCatalogueAdapter.getItem(position));
            activity.navController.navigate(R.id.action_searchFragment_to_songListPageFragment, bundle);
        });
        bind.searchResultGenreRecyclerView.setAdapter(genreCatalogueAdapter);
    }

    private void initSearchView() {
        if (isQueryValid(searchViewModel.getQuery())) {
            search(searchViewModel.getQuery());
        }

        bind.persistentSearchView.setInputQuery(searchViewModel.getQuery());
        setSuggestions();

        bind.persistentSearchView.setOnSearchQueryChangeListener((searchView, oldQuery, newQuery) -> {
            if (!newQuery.trim().equals("") && newQuery.trim().length() > 1) {
                searchView.setSuggestions(SuggestionCreationUtil.asRegularSearchSuggestions(searchViewModel.getSearchSuggestion(newQuery)), false);
            } else {
                setSuggestions();
            }
        });

        bind.persistentSearchView.setOnSuggestionChangeListener(new OnSuggestionChangeListener() {
            @Override
            public void onSuggestionPicked(SuggestionItem suggestion) {
                search(suggestion.getItemModel().getText());
            }

            @Override
            public void onSuggestionRemoved(SuggestionItem suggestion) {
            }
        });

        bind.persistentSearchView.setOnSearchConfirmedListener((searchView, query) -> {
            if (isQueryValid(query)) {
                searchView.collapse();
                search(query);
            }
            else {
                Toast.makeText(requireContext(), "Enter at least three characters", Toast.LENGTH_SHORT).show();
            }
        });

        bind.persistentSearchView.setOnSuggestionChangeListener(new OnSuggestionChangeListener() {
            @Override
            public void onSuggestionPicked(SuggestionItem suggestion) {
                search(suggestion.getItemModel().getText());
            }

            @Override
            public void onSuggestionRemoved(SuggestionItem suggestion) {
                searchViewModel.deleteRecentSearch(suggestion.getItemModel().getText());
            }
        });

        bind.persistentSearchView.setOnClearInputBtnClickListener(v -> searchViewModel.setQuery(""));
    }

    private void setSuggestions() {
        bind.persistentSearchView.setSuggestions(SuggestionCreationUtil.asRecentSearchSuggestions(searchViewModel.getRecentSearchSuggestion()), false);
    }

    public void search(String query) {
        searchViewModel.setQuery(query);

        bind.persistentSearchView.setInputQuery(query);
        performSearch(query);
    }

    private void performSearch(String query) {
        searchViewModel.searchSong(query).observe(requireActivity(), songs -> {
            if(bind != null) bind.searchSongSector.setVisibility(!songs.isEmpty() ? View.VISIBLE : View.GONE);
            songResultSearchAdapter.setItems(songs);
        });
        searchViewModel.searchAlbum(query).observe(requireActivity(), albums -> {
            if(bind != null) bind.searchAlbumSector.setVisibility(!albums.isEmpty() ? View.VISIBLE : View.GONE);
            albumAdapter.setItems(albums);
        });
        searchViewModel.searchArtist(query).observe(requireActivity(), artists -> {
            if(bind != null) bind.searchArtistSector.setVisibility(!artists.isEmpty() ? View.VISIBLE : View.GONE);
            artistAdapter.setItems(artists);
        });
        searchViewModel.searchGenre(query).observe(requireActivity(), genres -> {
            if(bind != null) bind.searchGenreSector.setVisibility(!genres.isEmpty() ? View.VISIBLE : View.GONE);
            genreCatalogueAdapter.setItems(genres);
        });

        bind.searchResultLayout.setVisibility(View.VISIBLE);
    }

    private boolean isQueryValid(String query) {
        return !query.equals("") && query.trim().length() > 2;
    }

    private void inputFocus() {
        if(!isQueryValid(searchViewModel.getQuery())) {
            bind.persistentSearchView.expand();
        }
    }
}
