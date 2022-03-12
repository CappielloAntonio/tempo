package com.cappielloantonio.play.viewmodel;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cappielloantonio.play.model.Album;
import com.cappielloantonio.play.model.Artist;
import com.cappielloantonio.play.model.Genre;
import com.cappielloantonio.play.model.Media;
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

    private MutableLiveData<List<Media>> songList;

    public ArrayList<String> filters = new ArrayList<>();
    public ArrayList<String> filterNames = new ArrayList<>();

    public int year = 0;

    public SongListPageViewModel(@NonNull Application application) {
        super(application);

        songRepository = new SongRepository(application);
        artistRepository = new ArtistRepository(application);
        downloadRepository = new DownloadRepository(application);
    }

    public LiveData<List<Media>> getSongList(LifecycleOwner owner) {
        songList = new MutableLiveData<>(new ArrayList<>());

        switch (title) {
            case Media.BY_GENRE:
                songList = songRepository.getSongsByGenre(genre.getId());
                break;
            case Media.BY_ARTIST:
                songList = artistRepository.getTopSongs(artist.getName(), 50);
                break;
            case Media.BY_GENRES:
                songList = songRepository.getSongsByGenres(filters);
                break;
            case Media.BY_YEAR:
                songList = songRepository.getRandomSample(500, year, year + 10);
                break;
            case Media.STARRED:
                songList = songRepository.getStarredSongs(false, -1);
                break;
            case Media.DOWNLOADED:
                downloadRepository.getLiveDownload().observe(owner, downloads -> songList.setValue(MappingUtil.mapDownloadToMedia(downloads)));
                break;
            case Media.FROM_ALBUM:
                downloadRepository.getLiveDownloadFromAlbum(album.getId()).observe(owner, downloads -> songList.setValue(MappingUtil.mapDownloadToMedia(downloads)));
                break;
        }

        return songList;
    }

    public String getFiltersTitle() {
        return TextUtils.join(", ", filterNames);
    }
}
