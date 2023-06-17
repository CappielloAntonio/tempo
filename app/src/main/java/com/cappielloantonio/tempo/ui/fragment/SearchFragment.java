package com.cappielloantonio.tempo.ui.fragment;

import android.content.ComponentName;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.session.MediaBrowser;
import androidx.media3.session.SessionToken;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cappielloantonio.tempo.R;
import com.cappielloantonio.tempo.databinding.FragmentSearchBinding;
import com.cappielloantonio.tempo.helper.recyclerview.CustomLinearSnapHelper;
import com.cappielloantonio.tempo.interfaces.ClickCallback;
import com.cappielloantonio.tempo.service.MediaManager;
import com.cappielloantonio.tempo.service.MediaService;
import com.cappielloantonio.tempo.ui.activity.MainActivity;
import com.cappielloantonio.tempo.ui.adapter.AlbumAdapter;
import com.cappielloantonio.tempo.ui.adapter.ArtistAdapter;
import com.cappielloantonio.tempo.ui.adapter.SongHorizontalAdapter;
import com.cappielloantonio.tempo.util.Constants;
import com.cappielloantonio.tempo.util.MusicUtil;
import com.cappielloantonio.tempo.viewmodel.SearchViewModel;
import com.google.common.util.concurrent.ListenableFuture;
import com.paulrybitskyi.persistentsearchview.adapters.model.SuggestionItem;
import com.paulrybitskyi.persistentsearchview.listeners.OnSuggestionChangeListener;
import com.paulrybitskyi.persistentsearchview.utils.SuggestionCreationUtil;

import java.util.Collections;

@UnstableApi
public class SearchFragment extends Fragment implements ClickCallback {
    private FragmentSearchBinding bind;
    private MainActivity activity;
    private SearchViewModel searchViewModel;

    private ArtistAdapter artistAdapter;
    private AlbumAdapter albumAdapter;
    private SongHorizontalAdapter songHorizontalAdapter;

    private ListenableFuture<MediaBrowser> mediaBrowserListenableFuture;

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
        initializeMediaBrowser();
    }

    @Override
    public void onResume() {
        super.onResume();
        inputFocus();
    }

    @Override
    public void onStop() {
        releaseMediaBrowser();
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind = null;
    }

    private void initSearchResultView() {
        // Artists
        bind.searchResultArtistRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        bind.searchResultArtistRecyclerView.setHasFixedSize(true);

        artistAdapter = new ArtistAdapter(this, false, false);
        bind.searchResultArtistRecyclerView.setAdapter(artistAdapter);

        CustomLinearSnapHelper artistSnapHelper = new CustomLinearSnapHelper();
        artistSnapHelper.attachToRecyclerView(bind.searchResultArtistRecyclerView);

        // Albums
        bind.searchResultAlbumRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        bind.searchResultAlbumRecyclerView.setHasFixedSize(true);

        albumAdapter = new AlbumAdapter(this);
        bind.searchResultAlbumRecyclerView.setAdapter(albumAdapter);

        CustomLinearSnapHelper albumSnapHelper = new CustomLinearSnapHelper();
        albumSnapHelper.attachToRecyclerView(bind.searchResultAlbumRecyclerView);

        // Songs
        bind.searchResultTracksRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        bind.searchResultTracksRecyclerView.setHasFixedSize(true);

        songHorizontalAdapter = new SongHorizontalAdapter(this, true);
        bind.searchResultTracksRecyclerView.setAdapter(songHorizontalAdapter);
    }

    private void initSearchView() {
        if (isQueryValid(searchViewModel.getQuery())) {
            search(searchViewModel.getQuery());
        }

        bind.persistentSearchView.setInputQuery(searchViewModel.getQuery());
        setSuggestions();

        bind.persistentSearchView.setOnSearchQueryChangeListener((searchView, oldQuery, newQuery) -> {
            if (!newQuery.trim().equals("") && newQuery.trim().length() > 1) {
                searchViewModel.getSearchSuggestion(newQuery).observe(getViewLifecycleOwner(), suggestions -> searchView.setSuggestions(SuggestionCreationUtil.asRegularSearchSuggestions(MusicUtil.getReadableStrings(suggestions)), false));
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
            } else {
                Toast.makeText(requireContext(), getString(R.string.search_info_minimum_characters), Toast.LENGTH_SHORT).show();
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
        searchViewModel.search(query).observe(getViewLifecycleOwner(), result -> {
            if (bind != null) {
                if (result.getArtists() != null) {
                    bind.searchArtistSector.setVisibility(!result.getArtists().isEmpty() ? View.VISIBLE : View.GONE);
                    artistAdapter.setItems(result.getArtists());
                } else {
                    artistAdapter.setItems(Collections.emptyList());
                    bind.searchArtistSector.setVisibility(View.GONE);
                }

                if (result.getAlbums() != null) {
                    bind.searchAlbumSector.setVisibility(!result.getAlbums().isEmpty() ? View.VISIBLE : View.GONE);
                    albumAdapter.setItems(result.getAlbums());
                } else {
                    albumAdapter.setItems(Collections.emptyList());
                    bind.searchAlbumSector.setVisibility(View.GONE);
                }

                if (result.getSongs() != null) {
                    bind.searchSongSector.setVisibility(!result.getSongs().isEmpty() ? View.VISIBLE : View.GONE);
                    songHorizontalAdapter.setItems(result.getSongs());
                } else {
                    songHorizontalAdapter.setItems(Collections.emptyList());
                    bind.searchSongSector.setVisibility(View.GONE);
                }
            }
        });

        bind.searchResultLayout.setVisibility(View.VISIBLE);
    }

    private boolean isQueryValid(String query) {
        return !query.equals("") && query.trim().length() > 2;
    }

    private void inputFocus() {
        bind.persistentSearchView.expand();
    }

    private void initializeMediaBrowser() {
        mediaBrowserListenableFuture = new MediaBrowser.Builder(requireContext(), new SessionToken(requireContext(), new ComponentName(requireContext(), MediaService.class))).buildAsync();
    }

    private void releaseMediaBrowser() {
        MediaBrowser.releaseFuture(mediaBrowserListenableFuture);
    }

    @Override
    public void onMediaClick(Bundle bundle) {
        MediaManager.startQueue(mediaBrowserListenableFuture, bundle.getParcelableArrayList(Constants.TRACKS_OBJECT), bundle.getInt(Constants.ITEM_POSITION));
        activity.setBottomSheetInPeek(true);
    }

    @Override
    public void onMediaLongClick(Bundle bundle) {
        Navigation.findNavController(requireView()).navigate(R.id.songBottomSheetDialog, bundle);
    }

    @Override
    public void onAlbumClick(Bundle bundle) {
        Navigation.findNavController(requireView()).navigate(R.id.albumPageFragment, bundle);
    }

    @Override
    public void onAlbumLongClick(Bundle bundle) {
        Navigation.findNavController(requireView()).navigate(R.id.albumBottomSheetDialog, bundle);
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
