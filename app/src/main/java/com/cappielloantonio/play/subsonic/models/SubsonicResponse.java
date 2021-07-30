package com.cappielloantonio.play.subsonic.models;

import com.cappielloantonio.play.subsonic.utils.converter.ResponseStatusConverter;
import com.tickaroo.tikxml.annotation.Attribute;
import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.Xml;

@Xml(name = "subsonic-response")
public class SubsonicResponse {
    @Element
    private Error error;
    private ScanStatus scanStatus;
    private TopSongs topSongs;
    @Element(name = "similarSongs2")
    private SimilarSongs2 similarSongs2;
    private SimilarSongs similarSongs;
    @Element(name = "artistInfo2")
    private ArtistInfo2 artistInfo2;
    private ArtistInfo artistInfo;
    private AlbumInfo albumInfo;
    @Element(name = "starred2")
    private Starred2 starred2;
    private Starred starred;
    private Shares shares;
    private PlayQueue playQueue;
    private Bookmarks bookmarks;
    private InternetRadioStations internetRadioStations;
    private NewestPodcasts newestPodcasts;
    private Podcasts podcasts;
    private Lyrics lyrics;
    private Songs songsByGenre;
    @Element(name = "randomSongs")
    private Songs randomSongs;
    @Element
    private AlbumList2 albumList2;
    private AlbumList albumList;
    private ChatMessages chatMessages;
    private User user;
    private Users users;
    private License license;
    private JukeboxPlaylist jukeboxPlaylist;
    private JukeboxStatus jukeboxStatus;
    @Element(name = "playlist")
    private PlaylistWithSongs playlist;
    @Element
    private Playlists playlists;
    private SearchResult3 searchResult3;
    private SearchResult2 searchResult2;
    private SearchResult searchResult;
    private NowPlaying nowPlaying;
    private VideoInfo videoInfo;
    private Videos videos;
    private Child song;
    @Element(name = "album")
    private AlbumWithSongsID3 album;
    @Element(name = "artist")
    private ArtistWithAlbumsID3 artist;
    @Element(name = "artists")
    private ArtistsID3 artists;
    @Element
    private Genres genres;
    private Directory directory;
    private Indexes indexes;
    @Element
    private MusicFolders musicFolders;
    @Attribute(converter = ResponseStatusConverter.class)
    private ResponseStatus status;
    @Attribute
    private String version;
    @Attribute
    private String type;

    public Error getError() {
        return error;
    }

    public void setError(Error value) {
        this.error = value;
    }

    public ScanStatus getScanStatus() {
        return scanStatus;
    }

    public void setScanStatus(ScanStatus value) {
        this.scanStatus = value;
    }

    public TopSongs getTopSongs() {
        return topSongs;
    }

    public void setTopSongs(TopSongs value) {
        this.topSongs = value;
    }

    public SimilarSongs2 getSimilarSongs2() {
        return similarSongs2;
    }

    public void setSimilarSongs2(SimilarSongs2 value) {
        this.similarSongs2 = value;
    }

    public SimilarSongs getSimilarSongs() {
        return similarSongs;
    }

    public void setSimilarSongs(SimilarSongs value) {
        this.similarSongs = value;
    }

    public ArtistInfo2 getArtistInfo2() {
        return artistInfo2;
    }

    public void setArtistInfo2(ArtistInfo2 value) {
        this.artistInfo2 = value;
    }

    public ArtistInfo getArtistInfo() {
        return artistInfo;
    }

    public void setArtistInfo(ArtistInfo value) {
        this.artistInfo = value;
    }

    public AlbumInfo getAlbumInfo() {
        return albumInfo;
    }

    public void setAlbumInfo(AlbumInfo value) {
        this.albumInfo = value;
    }

    public Starred2 getStarred2() {
        return starred2;
    }

    public void setStarred2(Starred2 value) {
        this.starred2 = value;
    }

    public Starred getStarred() {
        return starred;
    }

    public void setStarred(Starred value) {
        this.starred = value;
    }

    public Shares getShares() {
        return shares;
    }

    public void setShares(Shares value) {
        this.shares = value;
    }

    public PlayQueue getPlayQueue() {
        return playQueue;
    }

    public void setPlayQueue(PlayQueue value) {
        this.playQueue = value;
    }

    public Bookmarks getBookmarks() {
        return bookmarks;
    }

    public void setBookmarks(Bookmarks value) {
        this.bookmarks = value;
    }

    public InternetRadioStations getInternetRadioStations() {
        return internetRadioStations;
    }

    public void setInternetRadioStations(InternetRadioStations value) {
        this.internetRadioStations = value;
    }

    public NewestPodcasts getNewestPodcasts() {
        return newestPodcasts;
    }

    public void setNewestPodcasts(NewestPodcasts value) {
        this.newestPodcasts = value;
    }

    public Podcasts getPodcasts() {
        return podcasts;
    }

    public void setPodcasts(Podcasts value) {
        this.podcasts = value;
    }

    public Lyrics getLyrics() {
        return lyrics;
    }

    public void setLyrics(Lyrics value) {
        this.lyrics = value;
    }

    public Songs getSongsByGenre() {
        return songsByGenre;
    }

    public void setSongsByGenre(Songs value) {
        this.songsByGenre = value;
    }

    public Songs getRandomSongs() {
        return randomSongs;
    }

    public void setRandomSongs(Songs value) {
        this.randomSongs = value;
    }

    public AlbumList2 getAlbumList2() {
        return albumList2;
    }

    public void setAlbumList2(AlbumList2 value) {
        this.albumList2 = value;
    }

    public AlbumList getAlbumList() {
        return albumList;
    }

    public void setAlbumList(AlbumList value) {
        this.albumList = value;
    }

    public ChatMessages getChatMessages() {
        return chatMessages;
    }

    public void setChatMessages(ChatMessages value) {
        this.chatMessages = value;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User value) {
        this.user = value;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users value) {
        this.users = value;
    }

    public License getLicense() {
        return license;
    }

    public void setLicense(License value) {
        this.license = value;
    }

    public JukeboxPlaylist getJukeboxPlaylist() {
        return jukeboxPlaylist;
    }

    public void setJukeboxPlaylist(JukeboxPlaylist value) {
        this.jukeboxPlaylist = value;
    }

    public JukeboxStatus getJukeboxStatus() {
        return jukeboxStatus;
    }

    public void setJukeboxStatus(JukeboxStatus value) {
        this.jukeboxStatus = value;
    }

    public PlaylistWithSongs getPlaylist() {
        return playlist;
    }

    public void setPlaylist(PlaylistWithSongs value) {
        this.playlist = value;
    }

    public Playlists getPlaylists() {
        return playlists;
    }

    public void setPlaylists(Playlists value) {
        this.playlists = value;
    }

    public SearchResult3 getSearchResult3() {
        return searchResult3;
    }

    public void setSearchResult3(SearchResult3 value) {
        this.searchResult3 = value;
    }

    public SearchResult2 getSearchResult2() {
        return searchResult2;
    }

    public void setSearchResult2(SearchResult2 value) {
        this.searchResult2 = value;
    }

    public SearchResult getSearchResult() {
        return searchResult;
    }

    public void setSearchResult(SearchResult value) {
        this.searchResult = value;
    }

    public NowPlaying getNowPlaying() {
        return nowPlaying;
    }

    public void setNowPlaying(NowPlaying value) {
        this.nowPlaying = value;
    }

    public VideoInfo getVideoInfo() {
        return videoInfo;
    }

    public void setVideoInfo(VideoInfo value) {
        this.videoInfo = value;
    }

    public Videos getVideos() {
        return videos;
    }

    public void setVideos(Videos value) {
        this.videos = value;
    }

    public Child getSong() {
        return song;
    }

    public void setSong(Child value) {
        this.song = value;
    }

    public AlbumWithSongsID3 getAlbum() {
        return album;
    }

    public void setAlbum(AlbumWithSongsID3 value) {
        this.album = value;
    }

    public ArtistWithAlbumsID3 getArtist() {
        return artist;
    }

    public void setArtist(ArtistWithAlbumsID3 value) {
        this.artist = value;
    }

    public ArtistsID3 getArtists() {
        return artists;
    }

    public void setArtists(ArtistsID3 value) {
        this.artists = value;
    }

    public Genres getGenres() {
        return genres;
    }

    public void setGenres(Genres value) {
        this.genres = value;
    }

    public Directory getDirectory() {
        return directory;
    }

    public void setDirectory(Directory value) {
        this.directory = value;
    }

    public Indexes getIndexes() {
        return indexes;
    }

    public void setIndexes(Indexes value) {
        this.indexes = value;
    }

    public MusicFolders getMusicFolders() {
        return musicFolders;
    }

    public void setMusicFolders(MusicFolders value) {
        this.musicFolders = value;
    }

    public ResponseStatus getStatus() {
        return status;
    }

    public void setStatus(ResponseStatus value) {
        this.status = value;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String value) {
        this.version = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String value) {
        this.type = value;
    }
}
