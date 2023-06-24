package com.cappielloantonio.tempo.ui.fragment;

import android.content.ComponentName;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.cappielloantonio.tempo.viewmodel.SearchViewModel;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.Collections;

@UnstableApi
public class SearchFragment extends Fragment implements ClickCallback {
    private static final String TAG = "SearchFragment";

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
        inputFocus();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        initializeMediaBrowser();
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
        setRecentSuggestions();

        bind.searchView
                .getEditText()
                .setOnEditorActionListener((textView, actionId, keyEvent) -> {

                    String query = bind.searchView.getText().toString();

                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        if (isQueryValid(query)) {
                            search(bind.searchView.getText().toString());
                            return true;
                        } else {
                            Toast.makeText(requireContext(), getString(R.string.search_info_minimum_characters), Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    }

                    return false;
                });

        bind.searchView
                .getEditText()
                .addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                        if (count > 1) {
                            setSearchSuggestions(charSequence.toString());
                        } else {
                            setRecentSuggestions();
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
    }

    public void setRecentSuggestions() {
        bind.searchViewSuggestionContainer.removeAllViews();

        for (String suggestion : searchViewModel.getRecentSearchSuggestion()) {
            View view = LayoutInflater.from(bind.searchViewSuggestionContainer.getContext()).inflate(R.layout.item_search_suggestion, bind.searchViewSuggestionContainer, false);

            ImageView leadingImageView = view.findViewById(R.id.search_suggestion_icon);
            TextView titleView = view.findViewById(R.id.search_suggestion_title);
            ImageView tailingImageView = view.findViewById(R.id.search_suggestion_delete_icon);

            leadingImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_history, null));
            titleView.setText(suggestion);

            view.setOnClickListener(v -> search(suggestion));

            tailingImageView.setOnClickListener(v -> {
                searchViewModel.deleteRecentSearch(suggestion);
                setRecentSuggestions();
            });

            bind.searchViewSuggestionContainer.addView(view);
        }
    }

    public void setSearchSuggestions(String query) {
        searchViewModel.getSearchSuggestion(query).observe(getViewLifecycleOwner(), suggestions -> {
            bind.searchViewSuggestionContainer.removeAllViews();

            for (String suggestion : suggestions) {
                View view = LayoutInflater.from(bind.searchViewSuggestionContainer.getContext()).inflate(R.layout.item_search_suggestion, bind.searchViewSuggestionContainer, false);

                ImageView leadingImageView = view.findViewById(R.id.search_suggestion_icon);
                TextView titleView = view.findViewById(R.id.search_suggestion_title);
                ImageView tailingImageView = view.findViewById(R.id.search_suggestion_delete_icon);

                leadingImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_search, null));
                titleView.setText(suggestion);
                tailingImageView.setVisibility(View.GONE);

                view.setOnClickListener(v -> search(suggestion));

                bind.searchViewSuggestionContainer.addView(view);
            }
        });
    }

    public void search(String query) {
        searchViewModel.setQuery(query);
        bind.searchBar.setText(query);
        bind.searchView.hide();
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
        bind.searchView.show();
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
