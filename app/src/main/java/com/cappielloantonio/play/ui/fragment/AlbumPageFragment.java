package com.cappielloantonio.play.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cappielloantonio.play.App;
import com.cappielloantonio.play.R;
import com.cappielloantonio.play.adapter.AlbumArtistPageOrSimilarAdapter;
import com.cappielloantonio.play.adapter.SongHorizontalAdapter;
import com.cappielloantonio.play.databinding.FragmentAlbumPageBinding;
import com.cappielloantonio.play.glide.CustomGlideRequest;
import com.cappielloantonio.play.repository.QueueRepository;
import com.cappielloantonio.play.service.MusicPlayerRemote;
import com.cappielloantonio.play.ui.activity.MainActivity;
import com.cappielloantonio.play.util.DownloadUtil;
import com.cappielloantonio.play.viewmodel.AlbumPageViewModel;

import java.util.Collections;

public class AlbumPageFragment extends Fragment {
    private static final String TAG = "AlbumPageFragment";

    private FragmentAlbumPageBinding bind;
    private MainActivity activity;
    private AlbumPageViewModel albumPageViewModel;

    private SongHorizontalAdapter songHorizontalAdapter;
    private AlbumArtistPageOrSimilarAdapter albumArtistPageOrSimilarAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.album_page_menu, menu);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initAppBar();
        initAlbumInfoTextButton();
        initMusicButton();
        initSimilarAlbumsView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();

        bind = FragmentAlbumPageBinding.inflate(inflater, container, false);
        View view = bind.getRoot();
        albumPageViewModel = new ViewModelProvider(requireActivity()).get(AlbumPageViewModel.class);

        init();
        initBackCover();
        initSongsView();

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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_download_album:
                DownloadUtil.getDownloadTracker(requireContext()).toggleDownload(albumPageViewModel.getAlbumSongLiveList().getValue());
                return true;
            default:
                break;
        }

        return false;
    }

    private void init() {
        albumPageViewModel.setAlbum(getArguments().getParcelable("album_object"));
    }

    private void initAppBar() {
        activity.setSupportActionBar(bind.animToolbar);

        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        bind.animToolbar.setTitle(albumPageViewModel.getAlbum().getTitle());

        bind.albumNameLabel.setText(albumPageViewModel.getAlbum().getTitle());
        bind.albumArtistLabel.setText(albumPageViewModel.getAlbum().getArtistName());
        bind.albumReleaseYearLabel.setText(albumPageViewModel.getAlbum().getYear() != 0 ? String.valueOf(albumPageViewModel.getAlbum().getYear()) : "");

        bind.animToolbar.setNavigationOnClickListener(v -> activity.navController.navigateUp());
    }

    private void initAlbumInfoTextButton() {
        bind.albumArtistLabel.setOnClickListener(v -> {
            albumPageViewModel.getArtist().observe(requireActivity(), artist -> {
                if (artist != null) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("artist_object", artist);
                    activity.navController.navigate(R.id.action_albumPageFragment_to_artistPageFragment, bundle);
                } else
                    Toast.makeText(requireContext(), "Error retrieving artist", Toast.LENGTH_SHORT).show();
            });
        });

        bind.albumReleaseYearLabel.setOnClickListener(v -> {
            /*Bundle bundle = new Bundle();
            bundle.putString(Song.BY_YEAR, Song.BY_YEAR);
            bundle.putInt("year_object", albumPageViewModel.getAlbum().getYear());
            activity.navController.navigate(R.id.action_albumPageFragment_to_songListPageFragment, bundle);*/
        });
    }

    private void initMusicButton() {
        albumPageViewModel.getAlbumSongLiveList().observe(requireActivity(), songs -> {
            if (bind != null) {
                bind.albumPagePlayButton.setOnClickListener(v -> {
                    QueueRepository queueRepository = new QueueRepository(App.getInstance());
                    queueRepository.insertAllAndStartNew(songs);

                    activity.isBottomSheetInPeek(true);
                    activity.setBottomSheetMusicInfo(songs.get(0));

                    MusicPlayerRemote.openQueue(songs, 0, true);
                });

                bind.albumPageShuffleButton.setOnClickListener(v -> {
                    Collections.shuffle(songs);

                    QueueRepository queueRepository = new QueueRepository(App.getInstance());
                    queueRepository.insertAllAndStartNew(songs);

                    activity.isBottomSheetInPeek(true);
                    activity.setBottomSheetMusicInfo(songs.get(0));

                    MusicPlayerRemote.openQueue(songs, 0, true);
                });
            }
        });
    }

    private void initBackCover() {
        CustomGlideRequest.Builder
                .from(requireContext(), albumPageViewModel.getAlbum().getPrimary(), albumPageViewModel.getAlbum().getBlurHash(), CustomGlideRequest.ALBUM_PIC)
                .build()
                .into(bind.albumCoverImageView);
    }

    private void initSongsView() {
        bind.songRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        bind.songRecyclerView.setHasFixedSize(true);

        songHorizontalAdapter = new SongHorizontalAdapter(activity, requireContext(), getChildFragmentManager());
        bind.songRecyclerView.setAdapter(songHorizontalAdapter);

        albumPageViewModel.getAlbumSongLiveList().observe(requireActivity(), songs -> {
            songHorizontalAdapter.setItems(songs);
        });
    }

    private void initSimilarAlbumsView() {

    }
}