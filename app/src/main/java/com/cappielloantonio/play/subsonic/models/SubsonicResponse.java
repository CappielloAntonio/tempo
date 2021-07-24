package com.cappielloantonio.play.subsonic.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubsonicResponse {
    @SerializedName("error")
    @Expose
    public Error error;

    public ScanStatus scanStatus;
    public TopSongs topSongs;
    public SimilarSongs2 similarSongs2;
    public SimilarSongs similarSongs;
    public ArtistInfo2 artistInfo2;
    public ArtistInfo artistInfo;
    public AlbumInfo albumInfo;
    public Starred2 starred2;
    public Starred starred;
    public Shares shares;
    public PlayQueue playQueue;
    public Bookmarks bookmarks;
    public InternetRadioStations internetRadioStations;
    public NewestPodcasts newestPodcasts;
    public Podcasts podcasts;
    public Lyrics lyrics;
    public Songs songsByGenre;
    public Songs randomSongs;
    public AlbumList2 albumList2;
    public AlbumList albumList;
    public ChatMessages chatMessages;
    public User user;
    public Users users;
    public License license;
    public JukeboxPlaylist jukeboxPlaylist;
    public JukeboxStatus jukeboxStatus;
    public PlaylistWithSongs playlist;
    public Playlists playlists;
    public SearchResult3 searchResult3;
    public SearchResult2 searchResult2;
    public SearchResult searchResult;
    public NowPlaying nowPlaying;
    public VideoInfo videoInfo;
    public Videos videos;
    public Child song;
    public AlbumWithSongsID3 album;
    public ArtistWithAlbumsID3 artist;
    public ArtistsID3 artists;
    public Genres genres;
    public Directory directory;
    public Indexes indexes;
    public MusicFolders musicFolders;

    @SerializedName("status")
    @Expose
    public ResponseStatus status;

    @SerializedName("version")
    @Expose
    public String version;

    @SerializedName("type")
    @Expose
    public String type;

    /**
     * Gets the value of the error property.
     *
     * @return possible object is
     * {@link Error }
     */
    public Error getError() {
        return error;
    }

    /**
     * Sets the value of the error property.
     *
     * @param value allowed object is
     *              {@link Error }
     */
    public void setError(Error value) {
        this.error = value;
    }

    /**
     * Gets the value of the scanStatus property.
     *
     * @return possible object is
     * {@link ScanStatus }
     */
    public ScanStatus getScanStatus() {
        return scanStatus;
    }

    /**
     * Sets the value of the scanStatus property.
     *
     * @param value allowed object is
     *              {@link ScanStatus }
     */
    public void setScanStatus(ScanStatus value) {
        this.scanStatus = value;
    }

    /**
     * Gets the value of the topSongs property.
     *
     * @return possible object is
     * {@link TopSongs }
     */
    public TopSongs getTopSongs() {
        return topSongs;
    }

    /**
     * Sets the value of the topSongs property.
     *
     * @param value allowed object is
     *              {@link TopSongs }
     */
    public void setTopSongs(TopSongs value) {
        this.topSongs = value;
    }

    /**
     * Gets the value of the similarSongs2 property.
     *
     * @return possible object is
     * {@link SimilarSongs2 }
     */
    public SimilarSongs2 getSimilarSongs2() {
        return similarSongs2;
    }

    /**
     * Sets the value of the similarSongs2 property.
     *
     * @param value allowed object is
     *              {@link SimilarSongs2 }
     */
    public void setSimilarSongs2(SimilarSongs2 value) {
        this.similarSongs2 = value;
    }

    /**
     * Gets the value of the similarSongs property.
     *
     * @return possible object is
     * {@link SimilarSongs }
     */
    public SimilarSongs getSimilarSongs() {
        return similarSongs;
    }

    /**
     * Sets the value of the similarSongs property.
     *
     * @param value allowed object is
     *              {@link SimilarSongs }
     */
    public void setSimilarSongs(SimilarSongs value) {
        this.similarSongs = value;
    }

    /**
     * Gets the value of the artistInfo2 property.
     *
     * @return possible object is
     * {@link ArtistInfo2 }
     */
    public ArtistInfo2 getArtistInfo2() {
        return artistInfo2;
    }

    /**
     * Sets the value of the artistInfo2 property.
     *
     * @param value allowed object is
     *              {@link ArtistInfo2 }
     */
    public void setArtistInfo2(ArtistInfo2 value) {
        this.artistInfo2 = value;
    }

    /**
     * Gets the value of the artistInfo property.
     *
     * @return possible object is
     * {@link ArtistInfo }
     */
    public ArtistInfo getArtistInfo() {
        return artistInfo;
    }

    /**
     * Sets the value of the artistInfo property.
     *
     * @param value allowed object is
     *              {@link ArtistInfo }
     */
    public void setArtistInfo(ArtistInfo value) {
        this.artistInfo = value;
    }

    /**
     * Gets the value of the albumInfo property.
     *
     * @return possible object is
     * {@link AlbumInfo }
     */
    public AlbumInfo getAlbumInfo() {
        return albumInfo;
    }

    /**
     * Sets the value of the albumInfo property.
     *
     * @param value allowed object is
     *              {@link AlbumInfo }
     */
    public void setAlbumInfo(AlbumInfo value) {
        this.albumInfo = value;
    }

    /**
     * Gets the value of the starred2 property.
     *
     * @return possible object is
     * {@link Starred2 }
     */
    public Starred2 getStarred2() {
        return starred2;
    }

    /**
     * Sets the value of the starred2 property.
     *
     * @param value allowed object is
     *              {@link Starred2 }
     */
    public void setStarred2(Starred2 value) {
        this.starred2 = value;
    }

    /**
     * Gets the value of the starred property.
     *
     * @return possible object is
     * {@link Starred }
     */
    public Starred getStarred() {
        return starred;
    }

    /**
     * Sets the value of the starred property.
     *
     * @param value allowed object is
     *              {@link Starred }
     */
    public void setStarred(Starred value) {
        this.starred = value;
    }

    /**
     * Gets the value of the shares property.
     *
     * @return possible object is
     * {@link Shares }
     */
    public Shares getShares() {
        return shares;
    }

    /**
     * Sets the value of the shares property.
     *
     * @param value allowed object is
     *              {@link Shares }
     */
    public void setShares(Shares value) {
        this.shares = value;
    }

    /**
     * Gets the value of the playQueue property.
     *
     * @return possible object is
     * {@link PlayQueue }
     */
    public PlayQueue getPlayQueue() {
        return playQueue;
    }

    /**
     * Sets the value of the playQueue property.
     *
     * @param value allowed object is
     *              {@link PlayQueue }
     */
    public void setPlayQueue(PlayQueue value) {
        this.playQueue = value;
    }

    /**
     * Gets the value of the bookmarks property.
     *
     * @return possible object is
     * {@link Bookmarks }
     */
    public Bookmarks getBookmarks() {
        return bookmarks;
    }

    /**
     * Sets the value of the bookmarks property.
     *
     * @param value allowed object is
     *              {@link Bookmarks }
     */
    public void setBookmarks(Bookmarks value) {
        this.bookmarks = value;
    }

    /**
     * Gets the value of the internetRadioStations property.
     *
     * @return possible object is
     * {@link InternetRadioStations }
     */
    public InternetRadioStations getInternetRadioStations() {
        return internetRadioStations;
    }

    /**
     * Sets the value of the internetRadioStations property.
     *
     * @param value allowed object is
     *              {@link InternetRadioStations }
     */
    public void setInternetRadioStations(InternetRadioStations value) {
        this.internetRadioStations = value;
    }

    /**
     * Gets the value of the newestPodcasts property.
     *
     * @return possible object is
     * {@link NewestPodcasts }
     */
    public NewestPodcasts getNewestPodcasts() {
        return newestPodcasts;
    }

    /**
     * Sets the value of the newestPodcasts property.
     *
     * @param value allowed object is
     *              {@link NewestPodcasts }
     */
    public void setNewestPodcasts(NewestPodcasts value) {
        this.newestPodcasts = value;
    }

    /**
     * Gets the value of the podcasts property.
     *
     * @return possible object is
     * {@link Podcasts }
     */
    public Podcasts getPodcasts() {
        return podcasts;
    }

    /**
     * Sets the value of the podcasts property.
     *
     * @param value allowed object is
     *              {@link Podcasts }
     */
    public void setPodcasts(Podcasts value) {
        this.podcasts = value;
    }

    /**
     * Gets the value of the lyrics property.
     *
     * @return possible object is
     * {@link Lyrics }
     */
    public Lyrics getLyrics() {
        return lyrics;
    }

    /**
     * Sets the value of the lyrics property.
     *
     * @param value allowed object is
     *              {@link Lyrics }
     */
    public void setLyrics(Lyrics value) {
        this.lyrics = value;
    }

    /**
     * Gets the value of the songsByGenre property.
     *
     * @return possible object is
     * {@link Songs }
     */
    public Songs getSongsByGenre() {
        return songsByGenre;
    }

    /**
     * Sets the value of the songsByGenre property.
     *
     * @param value allowed object is
     *              {@link Songs }
     */
    public void setSongsByGenre(Songs value) {
        this.songsByGenre = value;
    }

    /**
     * Gets the value of the randomSongs property.
     *
     * @return possible object is
     * {@link Songs }
     */
    public Songs getRandomSongs() {
        return randomSongs;
    }

    /**
     * Sets the value of the randomSongs property.
     *
     * @param value allowed object is
     *              {@link Songs }
     */
    public void setRandomSongs(Songs value) {
        this.randomSongs = value;
    }

    /**
     * Gets the value of the albumList2 property.
     *
     * @return possible object is
     * {@link AlbumList2 }
     */
    public AlbumList2 getAlbumList2() {
        return albumList2;
    }

    /**
     * Sets the value of the albumList2 property.
     *
     * @param value allowed object is
     *              {@link AlbumList2 }
     */
    public void setAlbumList2(AlbumList2 value) {
        this.albumList2 = value;
    }

    /**
     * Gets the value of the albumList property.
     *
     * @return possible object is
     * {@link AlbumList }
     */
    public AlbumList getAlbumList() {
        return albumList;
    }

    /**
     * Sets the value of the albumList property.
     *
     * @param value allowed object is
     *              {@link AlbumList }
     */
    public void setAlbumList(AlbumList value) {
        this.albumList = value;
    }

    /**
     * Gets the value of the chatMessages property.
     *
     * @return possible object is
     * {@link ChatMessages }
     */
    public ChatMessages getChatMessages() {
        return chatMessages;
    }

    /**
     * Sets the value of the chatMessages property.
     *
     * @param value allowed object is
     *              {@link ChatMessages }
     */
    public void setChatMessages(ChatMessages value) {
        this.chatMessages = value;
    }

    /**
     * Gets the value of the user property.
     *
     * @return possible object is
     * {@link User }
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the value of the user property.
     *
     * @param value allowed object is
     *              {@link User }
     */
    public void setUser(User value) {
        this.user = value;
    }

    /**
     * Gets the value of the users property.
     *
     * @return possible object is
     * {@link Users }
     */
    public Users getUsers() {
        return users;
    }

    /**
     * Sets the value of the users property.
     *
     * @param value allowed object is
     *              {@link Users }
     */
    public void setUsers(Users value) {
        this.users = value;
    }

    /**
     * Gets the value of the license property.
     *
     * @return possible object is
     * {@link License }
     */
    public License getLicense() {
        return license;
    }

    /**
     * Sets the value of the license property.
     *
     * @param value allowed object is
     *              {@link License }
     */
    public void setLicense(License value) {
        this.license = value;
    }

    /**
     * Gets the value of the jukeboxPlaylist property.
     *
     * @return possible object is
     * {@link JukeboxPlaylist }
     */
    public JukeboxPlaylist getJukeboxPlaylist() {
        return jukeboxPlaylist;
    }

    /**
     * Sets the value of the jukeboxPlaylist property.
     *
     * @param value allowed object is
     *              {@link JukeboxPlaylist }
     */
    public void setJukeboxPlaylist(JukeboxPlaylist value) {
        this.jukeboxPlaylist = value;
    }

    /**
     * Gets the value of the jukeboxStatus property.
     *
     * @return possible object is
     * {@link JukeboxStatus }
     */
    public JukeboxStatus getJukeboxStatus() {
        return jukeboxStatus;
    }

    /**
     * Sets the value of the jukeboxStatus property.
     *
     * @param value allowed object is
     *              {@link JukeboxStatus }
     */
    public void setJukeboxStatus(JukeboxStatus value) {
        this.jukeboxStatus = value;
    }

    /**
     * Gets the value of the playlist property.
     *
     * @return possible object is
     * {@link PlaylistWithSongs }
     */
    public PlaylistWithSongs getPlaylist() {
        return playlist;
    }

    /**
     * Sets the value of the playlist property.
     *
     * @param value allowed object is
     *              {@link PlaylistWithSongs }
     */
    public void setPlaylist(PlaylistWithSongs value) {
        this.playlist = value;
    }

    /**
     * Gets the value of the playlists property.
     *
     * @return possible object is
     * {@link Playlists }
     */
    public Playlists getPlaylists() {
        return playlists;
    }

    /**
     * Sets the value of the playlists property.
     *
     * @param value allowed object is
     *              {@link Playlists }
     */
    public void setPlaylists(Playlists value) {
        this.playlists = value;
    }

    /**
     * Gets the value of the searchResult3 property.
     *
     * @return possible object is
     * {@link SearchResult3 }
     */
    public SearchResult3 getSearchResult3() {
        return searchResult3;
    }

    /**
     * Sets the value of the searchResult3 property.
     *
     * @param value allowed object is
     *              {@link SearchResult3 }
     */
    public void setSearchResult3(SearchResult3 value) {
        this.searchResult3 = value;
    }

    /**
     * Gets the value of the searchResult2 property.
     *
     * @return possible object is
     * {@link SearchResult2 }
     */
    public SearchResult2 getSearchResult2() {
        return searchResult2;
    }

    /**
     * Sets the value of the searchResult2 property.
     *
     * @param value allowed object is
     *              {@link SearchResult2 }
     */
    public void setSearchResult2(SearchResult2 value) {
        this.searchResult2 = value;
    }

    /**
     * Gets the value of the searchResult property.
     *
     * @return possible object is
     * {@link SearchResult }
     */
    public SearchResult getSearchResult() {
        return searchResult;
    }

    /**
     * Sets the value of the searchResult property.
     *
     * @param value allowed object is
     *              {@link SearchResult }
     */
    public void setSearchResult(SearchResult value) {
        this.searchResult = value;
    }

    /**
     * Gets the value of the nowPlaying property.
     *
     * @return possible object is
     * {@link NowPlaying }
     */
    public NowPlaying getNowPlaying() {
        return nowPlaying;
    }

    /**
     * Sets the value of the nowPlaying property.
     *
     * @param value allowed object is
     *              {@link NowPlaying }
     */
    public void setNowPlaying(NowPlaying value) {
        this.nowPlaying = value;
    }

    /**
     * Gets the value of the videoInfo property.
     *
     * @return possible object is
     * {@link VideoInfo }
     */
    public VideoInfo getVideoInfo() {
        return videoInfo;
    }

    /**
     * Sets the value of the videoInfo property.
     *
     * @param value allowed object is
     *              {@link VideoInfo }
     */
    public void setVideoInfo(VideoInfo value) {
        this.videoInfo = value;
    }

    /**
     * Gets the value of the videos property.
     *
     * @return possible object is
     * {@link Videos }
     */
    public Videos getVideos() {
        return videos;
    }

    /**
     * Sets the value of the videos property.
     *
     * @param value allowed object is
     *              {@link Videos }
     */
    public void setVideos(Videos value) {
        this.videos = value;
    }

    /**
     * Gets the value of the song property.
     *
     * @return possible object is
     * {@link Child }
     */
    public Child getSong() {
        return song;
    }

    /**
     * Sets the value of the song property.
     *
     * @param value allowed object is
     *              {@link Child }
     */
    public void setSong(Child value) {
        this.song = value;
    }

    /**
     * Gets the value of the album property.
     *
     * @return possible object is
     * {@link AlbumWithSongsID3 }
     */
    public AlbumWithSongsID3 getAlbum() {
        return album;
    }

    /**
     * Sets the value of the album property.
     *
     * @param value allowed object is
     *              {@link AlbumWithSongsID3 }
     */
    public void setAlbum(AlbumWithSongsID3 value) {
        this.album = value;
    }

    /**
     * Gets the value of the artist property.
     *
     * @return possible object is
     * {@link ArtistWithAlbumsID3 }
     */
    public ArtistWithAlbumsID3 getArtist() {
        return artist;
    }

    /**
     * Sets the value of the artist property.
     *
     * @param value allowed object is
     *              {@link ArtistWithAlbumsID3 }
     */
    public void setArtist(ArtistWithAlbumsID3 value) {
        this.artist = value;
    }

    /**
     * Gets the value of the artists property.
     *
     * @return possible object is
     * {@link ArtistsID3 }
     */
    public ArtistsID3 getArtists() {
        return artists;
    }

    /**
     * Sets the value of the artists property.
     *
     * @param value allowed object is
     *              {@link ArtistsID3 }
     */
    public void setArtists(ArtistsID3 value) {
        this.artists = value;
    }

    /**
     * Gets the value of the genres property.
     *
     * @return possible object is
     * {@link Genres }
     */
    public Genres getGenres() {
        return genres;
    }

    /**
     * Sets the value of the genres property.
     *
     * @param value allowed object is
     *              {@link Genres }
     */
    public void setGenres(Genres value) {
        this.genres = value;
    }

    /**
     * Gets the value of the directory property.
     *
     * @return possible object is
     * {@link Directory }
     */
    public Directory getDirectory() {
        return directory;
    }

    /**
     * Sets the value of the directory property.
     *
     * @param value allowed object is
     *              {@link Directory }
     */
    public void setDirectory(Directory value) {
        this.directory = value;
    }

    /**
     * Gets the value of the indexes property.
     *
     * @return possible object is
     * {@link Indexes }
     */
    public Indexes getIndexes() {
        return indexes;
    }

    /**
     * Sets the value of the indexes property.
     *
     * @param value allowed object is
     *              {@link Indexes }
     */
    public void setIndexes(Indexes value) {
        this.indexes = value;
    }

    /**
     * Gets the value of the musicFolders property.
     *
     * @return possible object is
     * {@link MusicFolders }
     */
    public MusicFolders getMusicFolders() {
        return musicFolders;
    }

    /**
     * Sets the value of the musicFolders property.
     *
     * @param value allowed object is
     *              {@link MusicFolders }
     */
    public void setMusicFolders(MusicFolders value) {
        this.musicFolders = value;
    }

    /**
     * Gets the value of the status property.
     *
     * @return possible object is
     * {@link ResponseStatus }
     */
    public ResponseStatus getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     *
     * @param value allowed object is
     *              {@link ResponseStatus }
     */
    public void setStatus(ResponseStatus value) {
        this.status = value;
    }

    /**
     * Gets the value of the version property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setVersion(String value) {
        this.version = value;
    }

    /**
     * Gets the value of the type property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setType(String value) {
        this.type = value;
    }

}
