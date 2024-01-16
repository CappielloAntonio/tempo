package com.cappielloantonio.tempo.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cappielloantonio.tempo.interfaces.StarCallback;
import com.cappielloantonio.tempo.model.Chronology;
import com.cappielloantonio.tempo.model.Favorite;
import com.cappielloantonio.tempo.repository.AlbumRepository;
import com.cappielloantonio.tempo.repository.ArtistRepository;
import com.cappielloantonio.tempo.repository.ChronologyRepository;
import com.cappielloantonio.tempo.repository.FavoriteRepository;
import com.cappielloantonio.tempo.repository.SharingRepository;
import com.cappielloantonio.tempo.repository.SongRepository;
import com.cappielloantonio.tempo.subsonic.models.AlbumID3;
import com.cappielloantonio.tempo.subsonic.models.ArtistID3;
import com.cappielloantonio.tempo.subsonic.models.Child;
import com.cappielloantonio.tempo.subsonic.models.Share;
import com.cappielloantonio.tempo.util.Preferences;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class HomeViewModel extends AndroidViewModel {
    private static final String TAG = "HomeViewModel";

    private final SongRepository songRepository;
    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;
    private final ChronologyRepository chronologyRepository;
    private final FavoriteRepository favoriteRepository;
    private final SharingRepository sharingRepository;

    private final MutableLiveData<List<Child>> dicoverSongSample = new MutableLiveData<>(null);
    private final MutableLiveData<List<AlbumID3>> newReleasedAlbum = new MutableLiveData<>(null);
    private final MutableLiveData<List<Child>> starredTracksSample = new MutableLiveData<>(null);
    private final MutableLiveData<List<ArtistID3>> starredArtistsSample = new MutableLiveData<>(null);
    private final MutableLiveData<List<ArtistID3>> bestOfArtists = new MutableLiveData<>(null);
    private final MutableLiveData<List<Child>> starredTracks = new MutableLiveData<>(null);
    private final MutableLiveData<List<AlbumID3>> starredAlbums = new MutableLiveData<>(null);
    private final MutableLiveData<List<ArtistID3>> starredArtists = new MutableLiveData<>(null);
    private final MutableLiveData<List<AlbumID3>> mostPlayedAlbumSample = new MutableLiveData<>(null);
    private final MutableLiveData<List<AlbumID3>> recentlyPlayedAlbumSample = new MutableLiveData<>(null);
    private final MutableLiveData<List<Integer>> years = new MutableLiveData<>(null);
    private final MutableLiveData<List<AlbumID3>> recentlyAddedAlbumSample = new MutableLiveData<>(null);

    private final MutableLiveData<List<Chronology>> thisGridTopSong = new MutableLiveData<>(null);
    private final MutableLiveData<List<Child>> mediaInstantMix = new MutableLiveData<>(null);
    private final MutableLiveData<List<Child>> artistInstantMix = new MutableLiveData<>(null);
    private final MutableLiveData<List<Child>> artistBestOf = new MutableLiveData<>(null);
    private final MutableLiveData<List<Share>> shares = new MutableLiveData<>(null);


    public HomeViewModel(@NonNull Application application) {
        super(application);

        songRepository = new SongRepository();
        albumRepository = new AlbumRepository();
        artistRepository = new ArtistRepository();
        chronologyRepository = new ChronologyRepository();
        favoriteRepository = new FavoriteRepository();
        sharingRepository = new SharingRepository();

        setOfflineFavorite();
    }

    public LiveData<List<Child>> getDiscoverSongSample(LifecycleOwner owner) {
        if (dicoverSongSample.getValue() == null) {
            songRepository.getRandomSample(10, null, null).observe(owner, dicoverSongSample::postValue);
        }

        return dicoverSongSample;
    }

    public LiveData<List<Child>> getRandomShuffleSample() {
        return songRepository.getRandomSample(100, null, null);
    }

    public LiveData<List<Chronology>> getGridSongSample(LifecycleOwner owner) {
        String server = Preferences.getServerId();
        chronologyRepository.getLastWeek(server).observe(owner, thisGridTopSong::postValue);
        return thisGridTopSong;
    }

    public LiveData<List<AlbumID3>> getRecentlyReleasedAlbums(LifecycleOwner owner) {
        if (newReleasedAlbum.getValue() == null) {
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);

            albumRepository.getAlbums("byYear", 500, currentYear, currentYear).observe(owner, albums -> {
                if (albums != null) {
                    albums.sort(Comparator.comparing(AlbumID3::getCreated).reversed());
                    newReleasedAlbum.postValue(albums.subList(0, Math.min(20, albums.size())));
                }
            });
        }

        return newReleasedAlbum;
    }

    public LiveData<List<Child>> getStarredTracksSample(LifecycleOwner owner) {
        if (starredTracksSample.getValue() == null) {
            songRepository.getStarredSongs(true, 10).observe(owner, starredTracksSample::postValue);
        }

        return starredTracksSample;
    }

    public LiveData<List<ArtistID3>> getStarredArtistsSample(LifecycleOwner owner) {
        if (starredArtistsSample.getValue() == null) {
            artistRepository.getStarredArtists(true, 10).observe(owner, starredArtistsSample::postValue);
        }

        return starredArtistsSample;
    }

    public LiveData<List<ArtistID3>> getBestOfArtists(LifecycleOwner owner) {
        if (bestOfArtists.getValue() == null) {
            artistRepository.getStarredArtists(true, 20).observe(owner, bestOfArtists::postValue);
        }

        return bestOfArtists;
    }

    public LiveData<List<Child>> getStarredTracks(LifecycleOwner owner) {
        if (starredTracks.getValue() == null) {
            songRepository.getStarredSongs(true, 20).observe(owner, starredTracks::postValue);
        }

        return starredTracks;
    }

    public LiveData<List<AlbumID3>> getStarredAlbums(LifecycleOwner owner) {
        if (starredAlbums.getValue() == null) {
            albumRepository.getStarredAlbums(true, 20).observe(owner, starredAlbums::postValue);
        }

        return starredAlbums;
    }

    public LiveData<List<ArtistID3>> getStarredArtists(LifecycleOwner owner) {
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

    public LiveData<List<AlbumID3>> getMostPlayedAlbums(LifecycleOwner owner) {
        if (mostPlayedAlbumSample.getValue() == null) {
            albumRepository.getAlbums("frequent", 20, null, null).observe(owner, mostPlayedAlbumSample::postValue);
        }

        return mostPlayedAlbumSample;
    }

    public LiveData<List<AlbumID3>> getMostRecentlyAddedAlbums(LifecycleOwner owner) {
        if (recentlyAddedAlbumSample.getValue() == null) {
            albumRepository.getAlbums("newest", 20, null, null).observe(owner, recentlyAddedAlbumSample::postValue);
        }

        return recentlyAddedAlbumSample;
    }

    public LiveData<List<AlbumID3>> getRecentlyPlayedAlbumList(LifecycleOwner owner) {
        if (recentlyPlayedAlbumSample.getValue() == null) {
            albumRepository.getAlbums("recent", 20, null, null).observe(owner, recentlyPlayedAlbumSample::postValue);
        }

        return recentlyPlayedAlbumSample;
    }

    public LiveData<List<Child>> getMediaInstantMix(LifecycleOwner owner, Child media) {
        mediaInstantMix.setValue(Collections.emptyList());

        songRepository.getInstantMix(media, 20).observe(owner, mediaInstantMix::postValue);

        return mediaInstantMix;
    }

    public LiveData<List<Child>> getArtistInstantMix(LifecycleOwner owner, ArtistID3 artist) {
        artistInstantMix.setValue(Collections.emptyList());

        artistRepository.getTopSongs(artist.getName(), 10).observe(owner, artistInstantMix::postValue);

        return artistInstantMix;
    }

    public LiveData<List<Child>> getArtistBestOf(LifecycleOwner owner, ArtistID3 artist) {
        artistBestOf.setValue(Collections.emptyList());

        artistRepository.getTopSongs(artist.getName(), 10).observe(owner, artistBestOf::postValue);

        return artistBestOf;
    }

    public LiveData<List<Share>> getShares(LifecycleOwner owner) {
        if (shares.getValue() == null) {
            sharingRepository.getShares().observe(owner, shares::postValue);
        }

        return shares;
    }

    public LiveData<List<Child>> getAllStarredTracks() {
        return songRepository.getStarredSongs(false, -1);
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

    public void refreshShares(LifecycleOwner owner) {
        sharingRepository.getShares().observe(owner, this.shares::postValue);
    }

    public void setOfflineFavorite() {
        ArrayList<Favorite> favorites = getFavorites();
        ArrayList<Favorite> favoritesToSave = getFavoritesToSave(favorites);
        ArrayList<Favorite> favoritesToDelete = getFavoritesToDelete(favorites, favoritesToSave);

        manageFavoriteToSave(favoritesToSave);
        manageFavoriteToDelete(favoritesToDelete);
    }

    private ArrayList<Favorite> getFavorites() {
        return new ArrayList<>(favoriteRepository.getFavorites());
    }

    private ArrayList<Favorite> getFavoritesToSave(ArrayList<Favorite> favorites) {
        HashMap<String, Favorite> filteredMap = new HashMap<>();

        for (Favorite favorite : favorites) {
            String key = favorite.toString();

            if (!filteredMap.containsKey(key) || favorite.getTimestamp() > filteredMap.get(key).getTimestamp()) {
                filteredMap.put(key, favorite);
            }
        }

        return new ArrayList<>(filteredMap.values());
    }

    private ArrayList<Favorite> getFavoritesToDelete(ArrayList<Favorite> favorites, ArrayList<Favorite> favoritesToSave) {
        ArrayList<Favorite> favoritesToDelete = new ArrayList<>();

        for (Favorite favorite : favorites) {
            if (!favoritesToSave.contains(favorite)) {
                favoritesToDelete.add(favorite);
            }
        }

        return favoritesToDelete;
    }

    private void manageFavoriteToSave(ArrayList<Favorite> favoritesToSave) {
        for (Favorite favorite : favoritesToSave) {
            if (favorite.getToStar()) {
                favoriteToStar(favorite);
            } else {
                favoriteToUnstar(favorite);
            }
        }
    }

    private void manageFavoriteToDelete(ArrayList<Favorite> favoritesToDelete) {
        for (Favorite favorite : favoritesToDelete) {
            favoriteRepository.delete(favorite);
        }
    }

    private void favoriteToStar(Favorite favorite) {
        if (favorite.getSongId() != null) {
            favoriteRepository.star(favorite.getSongId(), null, null, new StarCallback() {
                @Override
                public void onSuccess() {
                    favoriteRepository.delete(favorite);
                }
            });
        } else if (favorite.getAlbumId() != null) {
            favoriteRepository.star(null, favorite.getAlbumId(), null, new StarCallback() {
                @Override
                public void onSuccess() {
                    favoriteRepository.delete(favorite);
                }
            });
        } else if (favorite.getArtistId() != null) {
            favoriteRepository.star(null, null, favorite.getArtistId(), new StarCallback() {
                @Override
                public void onSuccess() {
                    favoriteRepository.delete(favorite);
                }
            });
        }
    }

    private void favoriteToUnstar(Favorite favorite) {
        if (favorite.getSongId() != null) {
            favoriteRepository.unstar(favorite.getSongId(), null, null, new StarCallback() {
                @Override
                public void onSuccess() {
                    favoriteRepository.delete(favorite);
                }
            });
        } else if (favorite.getAlbumId() != null) {
            favoriteRepository.unstar(null, favorite.getAlbumId(), null, new StarCallback() {
                @Override
                public void onSuccess() {
                    favoriteRepository.delete(favorite);
                }
            });
        } else if (favorite.getArtistId() != null) {
            favoriteRepository.unstar(null, null, favorite.getArtistId(), new StarCallback() {
                @Override
                public void onSuccess() {
                    favoriteRepository.delete(favorite);
                }
            });
        }
    }
}
