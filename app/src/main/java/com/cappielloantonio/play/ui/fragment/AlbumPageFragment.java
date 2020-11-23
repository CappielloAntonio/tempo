package com.cappielloantonio.play.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cappielloantonio.play.adapter.AlbumArtistPageAdapter;
import com.cappielloantonio.play.adapter.SongResultSearchAdapter;
import com.cappielloantonio.play.databinding.FragmentAlbumPageBinding;
import com.cappielloantonio.play.databinding.FragmentArtistPageBinding;
import com.cappielloantonio.play.model.Album;
import com.cappielloantonio.play.ui.activities.MainActivity;
import com.cappielloantonio.play.viewmodel.AlbumPageViewModel;
import com.cappielloantonio.play.viewmodel.ArtistPageViewModel;

import java.util.ArrayList;

public class AlbumPageFragment extends Fragment {

    private FragmentAlbumPageBinding bind;
    private MainActivity activity;
    private AlbumPageViewModel albumPageViewModel;

    private SongResultSearchAdapter songResultSearchAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();

        bind = FragmentAlbumPageBinding.inflate(inflater, container, false);
        View view = bind.getRoot();
        albumPageViewModel = new ViewModelProvider(requireActivity()).get(AlbumPageViewModel.class);

        init();
        initSongsView();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind = null;
    }

    private void init() {
        albumPageViewModel.setAlbum(getArguments().getParcelable("album_object"));

        bind.albumTitleLabel.setText(albumPageViewModel.getAlbum().getTitle());
    }

    private void initSongsView() {
        bind.songRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        bind.songRecyclerView.setHasFixedSize(true);

        songResultSearchAdapter = new SongResultSearchAdapter(requireContext(), new ArrayList<>());
        bind.songRecyclerView.setAdapter(songResultSearchAdapter);
        albumPageViewModel.getAlbumSongList().observe(requireActivity(), songs -> songResultSearchAdapter.setItems(songs));
    }
}