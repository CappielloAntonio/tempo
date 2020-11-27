package com.cappielloantonio.play.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.cappielloantonio.play.database.AppDatabase;
import com.cappielloantonio.play.database.dao.SongDao;
import com.cappielloantonio.play.database.dao.SongGenreCrossDao;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.model.SongGenreCross;

import java.util.ArrayList;
import java.util.List;

public class SongRepository {
    private SongDao songDao;
    private SongGenreCrossDao songGenreCrossDao;
    private LiveData<List<Song>> searchListLiveSongs;
    private LiveData<List<Song>> listLiveSampleRecentlyAddedSongs;
    private LiveData<List<Song>> listLiveSampleRecentlyPlayedSongs;
    private LiveData<List<Song>> listLiveSampleMostPlayedSongs;
    private LiveData<List<Song>> listLiveSampleArtistTopSongs;
    private LiveData<List<Song>> listLiveAlbumSongs;
    private LiveData<List<Song>> listLiveSongByGenre;
    private LiveData<List<Song>> listLiveFilteredSongs;


    public SongRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        songDao = database.songDao();
        songGenreCrossDao = database.songGenreCrossDao();
    }

    public LiveData<List<Song>> searchListLiveSong(String title) {
        searchListLiveSongs = songDao.searchSong(title);
        return searchListLiveSongs;
    }

    public LiveData<List<Song>> getListLiveRecentlyAddedSampleSong(int number) {
        listLiveSampleRecentlyAddedSongs = songDao.getRecentlyAddedSample(number);
        return listLiveSampleRecentlyAddedSongs;
    }

    public LiveData<List<Song>> getListLiveRecentlyPlayedSampleSong(int number) {
        listLiveSampleRecentlyPlayedSongs = songDao.getRecentlyPlayedSample(number);
        return listLiveSampleRecentlyPlayedSongs;
    }

    public LiveData<List<Song>> getListLiveMostPlayedSampleSong(int number) {
        listLiveSampleMostPlayedSongs = songDao.getMostPlayedSample(number);
        return listLiveSampleMostPlayedSongs;
    }

    public LiveData<List<Song>> getListLiveSongByGenre(String genreID) {
        listLiveSongByGenre = songDao.getSongByGenre(genreID);
        return listLiveSongByGenre;
    }

    public LiveData<List<Song>> getArtistListLiveTopSongSample(String artistID) {
        listLiveSampleArtistTopSongs = songDao.getArtistTopSongsSample(artistID, 5);
        return listLiveSampleArtistTopSongs;
    }

    public LiveData<List<Song>> getArtistListLiveTopSong(String artistID) {
        listLiveSampleArtistTopSongs = songDao.getArtistTopSongs(artistID);
        return listLiveSampleArtistTopSongs;
    }

    public LiveData<List<Song>> getAlbumListLiveSong(String albumID) {
        listLiveAlbumSongs = songDao.getAlbumSong(albumID);
        return listLiveAlbumSongs;
    }

    public LiveData<List<Song>> getFilteredListLiveSong(ArrayList<String> filters) {
        listLiveFilteredSongs = songDao.getFilteredSong(filters);
        return listLiveFilteredSongs;
    }

    public List<String> getSearchSuggestion(String query) {
        List<String> suggestions = new ArrayList<>();

        SearchSuggestionsThreadSafe suggestionsThread = new SearchSuggestionsThreadSafe(songDao, query, 5);
        Thread thread = new Thread(suggestionsThread);
        thread.start();

        try {
            thread.join();
            suggestions = suggestionsThread.getSuggestions();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return suggestions;
    }

    /*
     * Funzione che ritorna l'intero set di canzoni.
     * Utilizzato per l'aggiornamento del catalogo.
     */
    public List<Song> getCatalogue() {
        List<Song> catalogue = new ArrayList<>();

        GetCatalogueThreadSafe getCatalogueThread = new GetCatalogueThreadSafe(songDao);
        Thread thread = new Thread(getCatalogueThread);
        thread.start();

        try {
            thread.join();
            catalogue = getCatalogueThread.getCatalogue();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return catalogue;
    }

    public boolean exist(Song song) {
        boolean exist = false;

        ExistThreadSafe existThread = new ExistThreadSafe(songDao, song);
        Thread thread = new Thread(existThread);
        thread.start();

        try {
            thread.join();
            exist = existThread.exist();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return exist;
    }

    public void insert(Song song) {
        InsertThreadSafe insert = new InsertThreadSafe(songDao, song);
        Thread thread = new Thread(insert);
        thread.start();
    }

    public void insertAll(ArrayList<Song> songs) {
        InsertAllThreadSafe insertAll = new InsertAllThreadSafe(songDao, songGenreCrossDao, songs);
        Thread thread = new Thread(insertAll);
        thread.start();
    }

    public void delete(Song song) {
        DeleteThreadSafe delete = new DeleteThreadSafe(songDao, song);
        Thread thread = new Thread(delete);
        thread.start();
    }

    public void update(Song song) {
        song.nowPlaying();

        UpdateThreadSafe update = new UpdateThreadSafe(songDao, song);
        Thread thread = new Thread(update);
        thread.start();
    }

    public void getAll() {
        GetCatalogueThreadSafe catalogue = new GetCatalogueThreadSafe(songDao);
        Thread thread = new Thread(catalogue);
        thread.start();
    }

    public void insertSongPerGenre(ArrayList<SongGenreCross> songGenreCrosses) {
        InsertPerGenreThreadSafe insertPerGenre = new InsertPerGenreThreadSafe(songGenreCrossDao, songGenreCrosses);
        Thread thread = new Thread(insertPerGenre);
        thread.start();
    }

    public void deleteAllSong() {
        DeleteAllSongThreadSafe delete = new DeleteAllSongThreadSafe(songDao);
        Thread thread = new Thread(delete);
        thread.start();
    }

    public void deleteAllSongGenreCross() {
        DeleteAllSongGenreCrossThreadSafe delete = new DeleteAllSongGenreCrossThreadSafe(songGenreCrossDao);
        Thread thread = new Thread(delete);
        thread.start();
    }

    public List<Song> getRandomSample(int number) {
        List<Song> sample = new ArrayList<>();

        PickRandomThreadSafe randomThread = new PickRandomThreadSafe(songDao, number);
        Thread thread = new Thread(randomThread);
        thread.start();

        try {
            thread.join();
            sample = randomThread.getSample();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return sample;
    }

    private static class ExistThreadSafe implements Runnable {
        private SongDao songDao;
        private Song song;
        private boolean exist = false;

        public ExistThreadSafe(SongDao songDao, Song song) {
            this.songDao = songDao;
            this.song = song;
        }

        @Override
        public void run() {
            exist = songDao.exist(song.getId());
        }

        public boolean exist() {
            return exist;
        }
    }

    private static class InsertThreadSafe implements Runnable {
        private SongDao songDao;
        private Song song;

        public InsertThreadSafe(SongDao songDao, Song song) {
            this.songDao = songDao;
            this.song = song;
        }

        @Override
        public void run() {
            songDao.insert(song);
        }
    }

    private static class InsertAllThreadSafe implements Runnable {
        private SongDao songDao;
        private SongGenreCrossDao songGenreCrossDao;
        private ArrayList<Song> songs;

        public InsertAllThreadSafe(SongDao songDao, SongGenreCrossDao songGenreCrossDao, ArrayList<Song> songs) {
            this.songDao = songDao;
            this.songGenreCrossDao = songGenreCrossDao;
            this.songs = songs;
        }

        @Override
        public void run() {
            songDao.deleteAll();
            songGenreCrossDao.deleteAll();
            songDao.insertAll(songs);
        }
    }

    private static class DeleteThreadSafe implements Runnable {
        private SongDao songDao;
        private Song song;

        public DeleteThreadSafe(SongDao songDao, Song song) {
            this.songDao = songDao;
            this.song = song;
        }

        @Override
        public void run() {
            songDao.delete(song);
        }
    }

    private static class UpdateThreadSafe implements Runnable {
        private SongDao songDao;
        private Song song;

        public UpdateThreadSafe(SongDao songDao, Song song) {
            this.songDao = songDao;
            this.song = song;
        }

        @Override
        public void run() {
            songDao.update(song);
        }
    }

    private static class PickRandomThreadSafe implements Runnable {
        private SongDao songDao;
        private int elementNumber;
        private List<Song> sample;

        public PickRandomThreadSafe(SongDao songDao, int number) {
            this.songDao = songDao;
            this.elementNumber = number;
        }

        @Override
        public void run() {
            sample = songDao.random(elementNumber);
        }

        public List<Song> getSample() {
            return sample;
        }
    }

    private static class SearchSuggestionsThreadSafe implements Runnable {
        private SongDao songDao;
        private String query;
        private int number;
        private List<String> suggestions = new ArrayList<>();

        public SearchSuggestionsThreadSafe(SongDao songDao, String query, int number) {
            this.songDao = songDao;
            this.query = query;
            this.number = number;
        }

        @Override
        public void run() {
            suggestions = songDao.searchSuggestions(query, number);
        }

        public List<String> getSuggestions() {
            return suggestions;
        }
    }

    private static class GetCatalogueThreadSafe implements Runnable {
        private SongDao songDao;
        private List<Song> catalogue = new ArrayList<>();

        public GetCatalogueThreadSafe(SongDao songDao) {
            this.songDao = songDao;
        }

        @Override
        public void run() {
            catalogue = songDao.getAllList();
        }

        public List<Song> getCatalogue() {
            return catalogue;
        }
    }

    private static class InsertPerGenreThreadSafe implements Runnable {
        private SongGenreCrossDao songGenreCrossDao;
        private ArrayList<SongGenreCross> cross;

        public InsertPerGenreThreadSafe(SongGenreCrossDao songGenreCrossDao, ArrayList<SongGenreCross> cross) {
            this.songGenreCrossDao = songGenreCrossDao;
            this.cross = cross;
        }

        @Override
        public void run() {
            songGenreCrossDao.insertAll(cross);
        }
    }

    private static class DeleteAllSongThreadSafe implements Runnable {
        private SongDao songDao;

        public DeleteAllSongThreadSafe(SongDao songDao) {
            this.songDao = songDao;
        }

        @Override
        public void run() {
            songDao.deleteAll();
        }
    }

    private static class DeleteAllSongGenreCrossThreadSafe implements Runnable {
        private SongGenreCrossDao songGenreCrossDao;

        public DeleteAllSongGenreCrossThreadSafe(SongGenreCrossDao songGenreCrossDao) {
            this.songGenreCrossDao = songGenreCrossDao;
        }

        @Override
        public void run() {
            songGenreCrossDao.deleteAll();
        }
    }
}
