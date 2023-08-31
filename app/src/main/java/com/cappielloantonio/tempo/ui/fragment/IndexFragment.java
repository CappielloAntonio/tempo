package com.cappielloantonio.tempo.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.media3.common.util.UnstableApi;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cappielloantonio.tempo.R;
import com.cappielloantonio.tempo.databinding.FragmentIndexBinding;
import com.cappielloantonio.tempo.interfaces.ClickCallback;
import com.cappielloantonio.tempo.subsonic.models.MusicFolder;
import com.cappielloantonio.tempo.ui.activity.MainActivity;
import com.cappielloantonio.tempo.ui.adapter.MusicIndexAdapter;
import com.cappielloantonio.tempo.util.Constants;
import com.cappielloantonio.tempo.util.IndexUtil;
import com.cappielloantonio.tempo.viewmodel.IndexViewModel;

@UnstableApi
public class IndexFragment extends Fragment implements ClickCallback {
    private static final String TAG = "IndexFragment";

    private FragmentIndexBinding bind;
    private MainActivity activity;
    private IndexViewModel indexViewModel;

    private MusicIndexAdapter musicIndexAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();

        bind = FragmentIndexBinding.inflate(inflater, container, false);
        View view = bind.getRoot();
        indexViewModel = new ViewModelProvider(requireActivity()).get(IndexViewModel.class);

        initAppBar();
        initDirectoryListView();
        init();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind = null;
    }

    private void init() {
        MusicFolder musicFolder = getArguments().getParcelable(Constants.MUSIC_FOLDER_OBJECT);

        if (musicFolder != null) {
            indexViewModel.setMusicFolder(musicFolder);
            bind.indexTitleLabel.setText(musicFolder.getName());
        }
    }

    private void initAppBar() {
        activity.setSupportActionBar(bind.toolbar);

        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        if (bind != null)
            bind.toolbar.setNavigationOnClickListener(v -> activity.navController.navigateUp());

        if (bind != null)
            bind.appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
                if ((bind.indexInfoSector.getHeight() + verticalOffset) < (2 * ViewCompat.getMinimumHeight(bind.toolbar))) {
                    bind.toolbar.setTitle(indexViewModel.getMusicFolderName());
                } else {
                    bind.toolbar.setTitle(R.string.empty_string);
                }
            });
    }

    private void initDirectoryListView() {
        MusicFolder musicFolder = getArguments().getParcelable(Constants.MUSIC_FOLDER_OBJECT);

        bind.indexRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        bind.indexRecyclerView.setHasFixedSize(true);

        musicIndexAdapter = new MusicIndexAdapter(this);
        bind.indexRecyclerView.setAdapter(musicIndexAdapter);

        indexViewModel.getIndexes(musicFolder != null ? musicFolder.getId() : null).observe(getViewLifecycleOwner(), indexes -> {
            if (indexes != null) {
                musicIndexAdapter.setItems(IndexUtil.getArtist(indexes));
            }
        });

        bind.fastScrollbar.setRecyclerView(bind.indexRecyclerView);
        bind.fastScrollbar.setViewsToUse(R.layout.layout_fast_scrollbar, R.id.fastscroller_bubble, R.id.fastscroller_handle);
    }

    @Override
    public void onMusicIndexClick(Bundle bundle) {
        Navigation.findNavController(requireView()).navigate(R.id.directoryFragment, bundle);
    }
}