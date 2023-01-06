package com.cappielloantonio.play.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cappielloantonio.play.App;
import com.cappielloantonio.play.model.Album;
import com.cappielloantonio.play.model.Artist;
import com.cappielloantonio.play.model.Chronology;
import com.cappielloantonio.play.model.Media;
import com.cappielloantonio.play.model.Playlist;
import com.cappielloantonio.play.repository.AlbumRepository;
import com.cappielloantonio.play.repository.ArtistRepository;
import com.cappielloantonio.play.repository.ChronologyRepository;
import com.cappielloantonio.play.repository.PlaylistRepository;
import com.cappielloantonio.play.repository.PodcastRepository;
import com.cappielloantonio.play.repository.SongRepository;
import com.cappielloantonio.play.util.PreferenceUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HomeViewModel extends AndroidViewModel {
    private static final String TAG = "HomeViewModel";

    private final SongRepository songRepository;
    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;
    private final PlaylistRepository playlistRepository;
    private final PodcastRepository podcastRepository;
    private final ChronologyRepository chronologyRepository;

    private final MutableLiveData<List<Media>> dicoverSongSample = new MutableLiveData<>(null);
    private final MutableLiveData<List<Album>> newReleasedAlbum = new MutableLiveData<>(null);
    private final MutableLiveData<List<Media>> starredTracksSample = new MutableLiveData<>(null);
    private final MutableLiveData<List<Artist>> starredArtistsSample = new MutableLiveData<>(null);
    private final MutableLiveData<List<Artist>> bestOfArtists = new MutableLiveData<>(null);
    private final MutableLiveData<List<Media>> starredTracks = new MutableLiveData<>(null);
    private final MutableLiveData<List<Album>> starredAlbums = new MutableLiveData<>(null);
    private final MutableLiveData<List<Artist>> starredArtists = new MutableLiveData<>(null);
    private final MutableLiveData<List<Album>> mostPlayedAlbumSample = new MutableLiveData<>(null);
    private final MutableLiveData<List<Album>> recentlyPlayedAlbumSample = new MutableLiveData<>(null);
    private final MutableLiveData<List<Integer>> years = new MutableLiveData<>(null);
    private final MutableLiveData<List<Album>> recentlyAddedAlbumSample = new MutableLiveData<>(null);
    private final MutableLiveData<List<Playlist>> pinnedPlaylists = new MutableLiveData<>(null);
    private final MutableLiveData<List<Media>> newestPodcastEpisodes = new MutableLiveData<>(null);

    private final MutableLiveData<List<Chronology>> thisGridTopSong = new MutableLiveData<>(null);
    private final MutableLiveData<List<Media>> mediaInstantMix = new MutableLiveData<>(null);
    private final MutableLiveData<List<Media>> artistInstantMix = new MutableLiveData<>(null);
    private final MutableLiveData<List<Media>> artistBestOf = new MutableLiveData<>(null);

    public HomeViewModel(@NonNull Application application) {
        super(application);

        songRepository = new SongRepository(application);
        albumRepository = new AlbumRepository(application);
        artistRepository = new ArtistRepository(application);
        playlistRepository = new PlaylistRepository(application);
        podcastRepository = new PodcastRepository(application);
        chronologyRepository = new ChronologyRepository(application);
    }

    public LiveData<List<Media>> getDiscoverSongSample(LifecycleOwner owner) {
        if (dicoverSongSample.getValue() == null) {
            songRepository.getRandomSample(10, null, null).observe(owner, dicoverSongSample::postValue);
        }

        return dicoverSongSample;
    }

    public LiveData<List<Chronology>> getGridSongSample(LifecycleOwner owner) {
        Calendar cal = Calendar.getInstance();
        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);

        if (thisGridTopSong.getValue() == null) {
            if (dayOfMonth >= 7) {
                chronologyRepository.getThisWeek().observe(owner, thisGridTopSong::postValue);
            } else {
                chronologyRepository.getLastWeek().observe(owner, thisGridTopSong::postValue);
            }
        }

        return thisGridTopSong;
    }

    public LiveData<List<Album>> getRecentlyReleasedAlbums(LifecycleOwner owner) {
        if (newReleasedAlbum.getValue() == null) {
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);

            albumRepository.getAlbums("byYear", 500, currentYear, currentYear).observe(owner, albums -> {
                albums.sort(Comparator.comparing(Album::getCreated).reversed());
                newReleasedAlbum.postValue(albums.subList(0, Math.min(20, albums.size())));
            });
        }

        return newReleasedAlbum;
    }

    public LiveData<List<Media>> getStarredTracksSample(LifecycleOwner owner) {
        if (starredTracksSample.getValue() == null) {
            songRepository.getStarredSongs(true, 10).observe(owner, starredTracksSample::postValue);
        }

        return starredTracksSample;
    }

    public LiveData<List<Artist>> getStarredArtistsSample(LifecycleOwner owner) {
        if (starredArtistsSample.getValue() == null) {
            artistRepository.getStarredArtists(true, 10).observe(owner, starredArtistsSample::postValue);
        }

        return starredArtistsSample;
    }

    public LiveData<List<Artist>> getBestOfArtists(LifecycleOwner owner) {
        if (bestOfArtists.getValue() == null) {
            artistRepository.getStarredArtists(true, 20).observe(owner, bestOfArtists::postValue);
        }

        return bestOfArtists;
    }

    public LiveData<List<Media>> getStarredTracks(LifecycleOwner owner) {
        if (starredTracks.getValue() == null) {
            songRepository.getStarredSongs(true, 20).observe(owner, starredTracks::postValue);
        }

        return starredTracks;
    }

    public LiveData<List<Album>> getStarredAlbums(LifecycleOwner owner) {
        if (starredAlbums.getValue() == null) {
            albumRepository.getStarredAlbums(true, 20).observe(owner, starredAlbums::postValue);
        }

        return starredAlbums;
    }

    public LiveData<List<Artist>> getStarredArtists(LifecycleOwner owner) {
        if (starredArtists.getValue() == null) {
            artistRepository.getStarredArtists(true, 20).observe(owner, starredArtists::postValue);
        }

        return starredArtists;
    }

    public LiveData<List<Integer>> getYearList(LifecycleOwner owner) {
        if (years.getValue() == null) {
            albumRepository.getDecades().observe(owner, years::postValue);
        }

        return years;
    }

    public LiveData<List<Album>> getMostPlayedAlbums(LifecycleOwner owner) {
        if (mostPlayedAlbumSample.getValue() == null) {
            albumRepository.getAlbums("frequent", 20, null, null).observe(owner, mostPlayedAlbumSample::postValue);
        }

        return mostPlayedAlbumSample;
    }

    public LiveData<List<Album>> getMostRecentlyAddedAlbums(LifecycleOwner owner) {
        if (recentlyAddedAlbumSample.getValue() == null) {
            albumRepository.getAlbums("newest", 20, null, null).observe(owner, recentlyAddedAlbumSample::postValue);
        }

        return recentlyAddedAlbumSample;
    }

    public LiveData<List<Album>> getRecentlyPlayedAlbumList(LifecycleOwner owner) {
        if (recentlyPlayedAlbumSample.getValue() == null) {
            albumRepository.getAlbums("recent", 20, null, null).observe(owner, recentlyPlayedAlbumSample::postValue);
        }

        return recentlyPlayedAlbumSample;
    }

    public LiveData<List<Playlist>> getPinnedPlaylistList(LifecycleOwner owner, int maxNumber, boolean random) {
        playlistRepository.getPinnedPlaylists(PreferenceUtil.getInstance(App.getInstance()).getServerId()).observe(owner, playlists -> {
            if (random) Collections.shuffle(playlists);
            List<Playlist> subPlaylist = playlists.subList(0, Math.min(maxNumber, playlists.size()));
            pinnedPlaylists.postValue(subPlaylist);
        });

        return pinnedPlaylists;
    }

    public LiveData<List<Media>> getPlaylistSongLiveList(String playlistId) {
        return playlistRepository.getPlaylistSongs(playlistId);
    }

    public LiveData<List<Media>> getNewestPodcastEpisodes(LifecycleOwner owner) {
        if (newestPodcastEpisodes.getValue() == null) {
            podcastRepository.getNewestPodcastEpisodes(20).observe(owner, newestPodcastEpisodes::postValue);
        }

        return newestPodcastEpisodes;
    }

    public LiveData<List<Media>> getMediaInstantMix(LifecycleOwner owner, Media media) {
        mediaInstantMix.setValue(Collections.emptyList());

        songRepository.getInstantMix(media, 20).observe(owner, mediaInstantMix::postValue);

        return mediaInstantMix;
    }

    public LiveData<ArrayList<Media>> getArtistInstantMix(Artist artist) {
        return artistRepository.getInstantMix(artist, 20);
    }

    public LiveData<List<Media>> getArtistBestOf(Artist artist) {
        return artistRepository.getTopSongs(artist.getName(), 10);
    }

    public void refreshDiscoverySongSample(LifecycleOwner owner) {
        songRepository.getRandomSample(10, null, null).observe(owner, dicoverSongSample::postValue);
    }

    public void refreshSimilarSongSample(LifecycleOwner owner) {
        songRepository.getStarredSongs(true, 10).observe(owner, starredTracksSample::postValue);
    }

    public void refreshRadioArtistSample(LifecycleOwner owner) {
        artistRepository.getStarredArtists(true, 10).observe(owner, starredArtistsSample::postValue);
    }

    public void refreshBestOfArtist(LifecycleOwner owner) {
        artistRepository.getStarredArtists(true, 20).observe(owner, bestOfArtists::postValue);
    }

    public void refreshStarredTracks(LifecycleOwner owner) {
        songRepository.getStarredSongs(true, 20).observe(owner, starredTracks::postValue);
    }

    public void refreshStarredAlbums(LifecycleOwner owner) {
        albumRepository.getStarredAlbums(true, 20).observe(owner, starredAlbums::postValue);
    }

    public void refreshStarredArtists(LifecycleOwner owner) {
        artistRepository.getStarredArtists(true, 20).observe(owner, starredArtists::postValue);
    }

    public void refreshMostPlayedAlbums(LifecycleOwner owner) {
        albumRepository.getAlbums("frequent", 20, null, null).observe(owner, mostPlayedAlbumSample::postValue);
    }

    public void refreshMostRecentlyAddedAlbums(LifecycleOwner owner) {
        albumRepository.getAlbums("newest", 20, null, null).observe(owner, recentlyAddedAlbumSample::postValue);
    }

    public void refreshRecentlyPlayedAlbumList(LifecycleOwner owner) {
        albumRepository.getAlbums("recent", 20, null, null).observe(owner, recentlyPlayedAlbumSample::postValue);
    }
}
