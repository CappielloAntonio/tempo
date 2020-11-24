package com.cappielloantonio.play.ui.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cappielloantonio.play.R;
import com.cappielloantonio.play.adapter.AlbumArtistPageAdapter;
import com.cappielloantonio.play.adapter.RecentMusicAdapter;
import com.cappielloantonio.play.adapter.SongResultSearchAdapter;
import com.cappielloantonio.play.databinding.FragmentArtistPageBinding;
import com.cappielloantonio.play.databinding.FragmentHomeBinding;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.ui.activities.MainActivity;
import com.cappielloantonio.play.viewmodel.ArtistPageViewModel;
import com.cappielloantonio.play.viewmodel.HomeViewModel;

import java.util.ArrayList;

public class ArtistPageFragment extends Fragment {

    private FragmentArtistPageBinding bind;
    private MainActivity activity;
    private ArtistPageViewModel artistPageViewModel;

    private SongResultSearchAdapter songResultSearchAdapter;
    private AlbumArtistPageAdapter albumArtistPageAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();

        bind = FragmentArtistPageBinding.inflate(inflater, container, false);
        View view = bind.getRoot();
        artistPageViewModel = new ViewModelProvider(requireActivity()).get(ArtistPageViewModel.class);

        init();
        initTopSongsView();
        initAlbumsView();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind = null;
    }

    private void init() {
        artistPageViewModel.setArtist(getArguments().getParcelable("artist_object"));
        bind.artistNameLabel.setText(artistPageViewModel.getArtist().getName());

        bind.mostStreamedSongTextViewClickable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(Song.BY_ARTIST, Song.BY_ARTIST);
                bundle.putParcelable("artist_object", artistPageViewModel.getArtist());
                activity.navController.navigate(R.id.action_artistPageFragment_to_songListPageFragment, bundle);
            }
        });
    }

    private void initTopSongsView() {
        bind.mostStreamedSongRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        bind.mostStreamedSongRecyclerView.setHasFixedSize(true);

        songResultSearchAdapter = new SongResultSearchAdapter(requireContext(), new ArrayList<>());
        bind.mostStreamedSongRecyclerView.setAdapter(songResultSearchAdapter);
        artistPageViewModel.getArtistTopSongList().observe(requireActivity(), songs -> songResultSearchAdapter.setItems(songs));
    }

    private void initAlbumsView() {
        bind.albumsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        bind.albumsRecyclerView.setHasFixedSize(true);

        albumArtistPageAdapter = new AlbumArtistPageAdapter(requireContext(), new ArrayList<>());
        albumArtistPageAdapter.setClickListener((view, position) -> {
            Bundle bundle = new Bundle();
            bundle.putParcelable("album_object", albumArtistPageAdapter.getItem(position));
            activity.navController.navigate(R.id.action_artistPageFragment_to_albumPageFragment, bundle);
        });
        bind.albumsRecyclerView.setAdapter(albumArtistPageAdapter);
        artistPageViewModel.getAlbumList().observe(requireActivity(), songs -> albumArtistPageAdapter.setItems(songs));
    }
}