package com.cappielloantonio.play.viewmodel;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cappielloantonio.play.model.Album;
import com.cappielloantonio.play.model.Artist;
import com.cappielloantonio.play.model.Genre;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.repository.ArtistRepository;
import com.cappielloantonio.play.repository.DownloadRepository;
import com.cappielloantonio.play.repository.SongRepository;
import com.cappielloantonio.play.util.MappingUtil;

import java.util.ArrayList;
import java.util.List;

public class SongListPageViewModel extends AndroidViewModel {
    private final SongRepository songRepository;
    private final ArtistRepository artistRepository;
    private final DownloadRepository downloadRepository;

    public String title;
    public String toolbarTitle;
    public Genre genre;
    public Artist artist;
    public Album album;

    private MutableLiveData<List<Song>> songList;

    public ArrayList<String> filters = new ArrayList<>();
    public ArrayList<String> filterNames = new ArrayList<>();

    public int year = 0;

    public SongListPageViewModel(@NonNull Application application) {
        super(application);

        songRepository = new SongRepository(application);
        artistRepository = new ArtistRepository(application);
        downloadRepository = new DownloadRepository(application);
    }

    public LiveData<List<Song>> getSongList(LifecycleOwner owner) {
        songList = new MutableLiveData<>(new ArrayList<>());

        switch (title) {
            case Song.BY_GENRE:
                songList = songRepository.getSongsByGenre(genre.getId());
                break;
            case Song.BY_ARTIST:
                songList = artistRepository.getTopSongs(artist.getName(), 50);
                break;
            case Song.BY_GENRES:
                songList = songRepository.getSongsByGenres(filters);
                break;
            case Song.BY_YEAR:
                songList = songRepository.getRandomSample(500, year, year + 10);
                break;
            case Song.STARRED:
                songList = songRepository.getStarredSongs(false, -1);
                break;
            case Song.DOWNLOADED:
                downloadRepository.getLiveDownload().observe(owner, downloads -> songList.setValue(MappingUtil.mapDownloadToSong(downloads)));
                break;
            case Song.FROM_ALBUM:
                downloadRepository.getLiveDownloadFromAlbum(album.getId()).observe(owner, downloads -> songList.setValue(MappingUtil.mapDownloadToSong(downloads)));
                break;
        }

        return songList;
    }

    public String getFiltersTitle() {
        return TextUtils.join(", ", filterNames);
    }
}
