package com.cappielloantonio.play.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.media3.common.util.UnstableApi;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cappielloantonio.play.R;
import com.cappielloantonio.play.databinding.FragmentIndexBinding;
import com.cappielloantonio.play.interfaces.ClickCallback;
import com.cappielloantonio.play.subsonic.models.MusicFolder;
import com.cappielloantonio.play.ui.activity.MainActivity;
import com.cappielloantonio.play.ui.adapter.MusicIndexAdapter;
import com.cappielloantonio.play.util.Constants;
import com.cappielloantonio.play.util.IndexUtil;
import com.cappielloantonio.play.viewmodel.IndexViewModel;

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
        bind.indexRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        bind.indexRecyclerView.setHasFixedSize(true);

        musicIndexAdapter = new MusicIndexAdapter(this);
        bind.indexRecyclerView.setAdapter(musicIndexAdapter);

        indexViewModel.getIndexes().observe(getViewLifecycleOwner(), indexes -> {
            if (indexes != null) {
                musicIndexAdapter.setItems(IndexUtil.getArtist(indexes));
            }
        });
    }

    @Override
    public void onMusicIndexClick(Bundle bundle) {
        Navigation.findNavController(requireView()).navigate(R.id.directoryFragment, bundle);
    }
}