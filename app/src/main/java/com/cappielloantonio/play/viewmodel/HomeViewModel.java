package com.cappielloantonio.play.viewmodel;

import androidx.lifecycle.ViewModel;

import com.cappielloantonio.play.model.Song;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {

    public List<Song> getDiscoverSongList() {
        List<Song> discover_songs = new ArrayList<>();
        discover_songs.add(new Song("Holiday", "American Idiot"));
        discover_songs.add(new Song("Brioschi", "Stanza Singola"));
        discover_songs.add(new Song("HappySad", "Ceri Singles"));
        discover_songs.add(new Song("Falling back to Earth", "Haken"));

        return discover_songs;
    }

    public List<Song> getRecentSongList() {
        List<Song> recent_songs = new ArrayList<>();
        recent_songs.add(new Song("Holiday", "American Idiot"));
        recent_songs.add(new Song("Brioschi", "Stanza Singola"));
        recent_songs.add(new Song("HappySad", "Ceri Singles"));
        recent_songs.add(new Song("Falling back to Earth", "Haken"));

        return recent_songs;
    }

    public List<Song> getMostPlayedSongList() {
        List<Song> most_played_songs = new ArrayList<>();
        most_played_songs.add(new Song("Holiday", "American Idiot"));
        most_played_songs.add(new Song("Brioschi", "Stanza Singola"));
        most_played_songs.add(new Song("HappySad", "Ceri Singles"));
        most_played_songs.add(new Song("Falling back to Earth", "Haken"));

        return most_played_songs;
    }
}
