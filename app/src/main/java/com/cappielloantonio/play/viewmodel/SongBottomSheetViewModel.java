package com.cappielloantonio.play.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.cappielloantonio.play.model.Album;
import com.cappielloantonio.play.model.Artist;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.repository.AlbumRepository;
import com.cappielloantonio.play.repository.ArtistRepository;
import com.cappielloantonio.play.repository.SongRepository;
import com.cappielloantonio.play.util.DownloadUtil;
import com.cappielloantonio.play.util.MappingUtil;
import com.cappielloantonio.play.util.PreferenceUtil;

public class SongBottomSheetViewModel extends AndroidViewModel {
    private final SongRepository songRepository;
    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;

    private Song song;

    public SongBottomSheetViewModel(@NonNull Application application) {
        super(application);

        songRepository = new SongRepository(application);
        albumRepository = new AlbumRepository(application);
        artistRepository = new ArtistRepository(application);
    }

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }

    public void setFavorite(Context context) {
        if (song.isFavorite()) {
            songRepository.unstar(song.getId());
            song.setFavorite(false);
        } else {
            songRepository.star(song.getId());
            song.setFavorite(true);

            if (PreferenceUtil.getInstance(context).isStarredSyncEnabled()) {
                DownloadUtil.getDownloadTracker(context).download(MappingUtil.mapMediaItem(context, song, false));
            }
        }
    }

    public LiveData<Album> getAlbum() {
        return albumRepository.getAlbum(song.getAlbumId());
    }

    public LiveData<Artist> getArtist() {
        return artistRepository.getArtist(song.getArtistId());
    }
}
