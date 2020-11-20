package com.cappielloantonio.play.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cappielloantonio.play.adapter.AlbumAdapter;
import com.cappielloantonio.play.adapter.ArtistAdapter;
import com.cappielloantonio.play.adapter.GenreAdapter;
import com.cappielloantonio.play.adapter.PlaylistAdapter;
import com.cappielloantonio.play.databinding.FragmentLibraryBinding;
import com.cappielloantonio.play.model.Genre;
import com.cappielloantonio.play.model.Playlist;
import com.cappielloantonio.play.ui.activities.MainActivity;
import com.cappielloantonio.play.viewmodel.LibraryViewModel;

import java.util.ArrayList;
import java.util.List;

public class LibraryFragment extends Fragment {
    private static final String TAG = "LibraryFragment";

    private FragmentLibraryBinding bind;
    private MainActivity activity;
    private LibraryViewModel libraryViewModel;

    private AlbumAdapter albumAdapter;
    private ArtistAdapter artistAdapter;
    private GenreAdapter genreAdapter;
    private PlaylistAdapter playlistAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();

        bind = FragmentLibraryBinding.inflate(inflater, container, false);
        View view = bind.getRoot();
        libraryViewModel = new ViewModelProvider(requireActivity()).get(LibraryViewModel.class);

        initAlbumView();
        initArtistView();
        initGenreView();
        initPlaylistView();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind = null;
    }

    private void initAlbumView() {
        bind.albumRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        bind.albumRecyclerView.setHasFixedSize(true);

        albumAdapter = new AlbumAdapter(requireContext(), libraryViewModel.getAlbumSample());
        albumAdapter.setClickListener((view, position) -> Toast.makeText(requireContext(), "Album: " + position, Toast.LENGTH_SHORT).show());
        bind.albumRecyclerView.setAdapter(albumAdapter);
    }

    private void initArtistView() {
        bind.artistRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        bind.artistRecyclerView.setHasFixedSize(true);

        artistAdapter = new ArtistAdapter(requireContext(), libraryViewModel.getArtistSample());
        artistAdapter.setClickListener((view, position) -> Toast.makeText(requireContext(), "Artist: " + position, Toast.LENGTH_SHORT).show());
        bind.artistRecyclerView.setAdapter(artistAdapter);
    }

    private void initGenreView() {
        bind.genreRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 3, GridLayoutManager.HORIZONTAL, false));
        bind.genreRecyclerView.setHasFixedSize(true);

        genreAdapter = new GenreAdapter(requireContext(), new ArrayList<>());
        genreAdapter.setClickListener((view, position) -> Toast.makeText(requireContext(), "Genre: " + position, Toast.LENGTH_SHORT).show());
        bind.genreRecyclerView.setAdapter(genreAdapter);
        libraryViewModel.getGenreList().observe(requireActivity(), genres -> genreAdapter.setItems(genres));
    }

    private void initPlaylistView() {
        bind.playlistRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        bind.playlistRecyclerView.setHasFixedSize(true);

        playlistAdapter = new PlaylistAdapter(requireContext(), libraryViewModel.getPlaylist());
        playlistAdapter.setClickListener((view, position) -> Toast.makeText(requireContext(), "Playlist: " + position, Toast.LENGTH_SHORT).show());
        bind.playlistRecyclerView.setAdapter(playlistAdapter);
    }
}
