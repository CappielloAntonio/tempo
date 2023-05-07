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

import com.cappielloantonio.play.R;
import com.cappielloantonio.play.databinding.FragmentPodcastChannelCatalogueBinding;
import com.cappielloantonio.play.helper.recyclerview.GridItemDecoration;
import com.cappielloantonio.play.interfaces.ClickCallback;
import com.cappielloantonio.play.ui.activity.MainActivity;
import com.cappielloantonio.play.ui.adapter.PodcastChannelCatalogueAdapter;
import com.cappielloantonio.play.viewmodel.PodcastChannelCatalogueViewModel;

@OptIn(markerClass = UnstableApi.class)
public class PodcastChannelCatalogueFragment extends Fragment implements ClickCallback {
    private static final String TAG = "PodcastChannelCatalogue";

    private FragmentPodcastChannelCatalogueBinding bind;
    private MainActivity activity;
    private PodcastChannelCatalogueViewModel podcastChannelCatalogueViewModel;

    private PodcastChannelCatalogueAdapter podcastChannelCatalogueAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();

        bind = FragmentPodcastChannelCatalogueBinding.inflate(inflater, container, false);
        View view = bind.getRoot();
        podcastChannelCatalogueViewModel = new ViewModelProvider(requireActivity()).get(PodcastChannelCatalogueViewModel.class);

        initAppBar();
        initPodcastChannelCatalogueView();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind = null;
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
            if ((bind.podcastChannelInfoSector.getHeight() + verticalOffset) < (2 * ViewCompat.getMinimumHeight(bind.toolbar))) {
                bind.toolbar.setTitle(R.string.podcast_channel_catalogue_title);
            } else {
                bind.toolbar.setTitle(R.string.empty_string);
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initPodcastChannelCatalogueView() {
        bind.podcastChannelCatalogueRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        bind.podcastChannelCatalogueRecyclerView.addItemDecoration(new GridItemDecoration(2, 20, false));
        bind.podcastChannelCatalogueRecyclerView.setHasFixedSize(true);

        podcastChannelCatalogueAdapter = new PodcastChannelCatalogueAdapter(this);
        podcastChannelCatalogueAdapter.setStateRestorationPolicy(RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY);
        bind.podcastChannelCatalogueRecyclerView.setAdapter(podcastChannelCatalogueAdapter);
        podcastChannelCatalogueViewModel.getPodcastChannels(getViewLifecycleOwner()).observe(getViewLifecycleOwner(), albums -> {
            if (albums != null) {
                podcastChannelCatalogueAdapter.setItems(albums);
            }
        });

        bind.podcastChannelCatalogueRecyclerView.setOnTouchListener((v, event) -> {
            hideKeyboard(v);
            return false;
        });
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
                podcastChannelCatalogueAdapter.getFilter().filter(newText);
                return false;
            }
        });

        searchView.setPadding(-32, 0, 0, 0);
    }

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onPodcastChannelClick(Bundle bundle) {
        Navigation.findNavController(requireView()).navigate(R.id.podcastChannelPageFragment, bundle);
        hideKeyboard(requireView());
    }

    @Override
    public void onPodcastChannelLongClick(Bundle bundle) {
        // Navigation.findNavController(requireView()).navigate(R.id.albumBottomSheetDialog, bundle);
    }
}