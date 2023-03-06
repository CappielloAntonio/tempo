package com.cappielloantonio.play.subsonic.models

import com.cappielloantonio.play.subsonic.utils.converter.ResponseStatusConverter
import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "subsonic-response")
class SubsonicResponse {
    @Element
    var error: Error? = null

    @Element(name = "scanStatus")
    var scanStatus: ScanStatus? = null

    @Element(name = "topSongs")
    var topSongs: TopSongs? = null

    @Element(name = "similarSongs2")
    var similarSongs2: SimilarSongs2? = null
    var similarSongs: SimilarSongs? = null

    @Element(name = "artistInfo2")
    var artistInfo2: ArtistInfo2? = null
    var artistInfo: ArtistInfo? = null

    @Element(name = "albumInfo")
    var albumInfo: AlbumInfo? = null

    @Element(name = "starred2")
    var starred2: Starred2? = null
    var starred: Starred? = null
    var shares: Shares? = null
    var playQueue: PlayQueue? = null
    var bookmarks: Bookmarks? = null
    var internetRadioStations: InternetRadioStations? = null

    @Element(name = "newestPodcasts")
    var newestPodcasts: NewestPodcasts? = null
    var podcasts: Podcasts? = null

    @Element(name = "lyrics")
    var lyrics: Lyrics? = null

    @Element(name = "songsByGenre")
    var songsByGenre: Songs? = null

    @Element(name = "randomSongs")
    var randomSongs: Songs? = null

    @Element
    var albumList2: AlbumList2? = null
    var albumList: AlbumList? = null
    var chatMessages: ChatMessages? = null
    var user: User? = null
    var users: Users? = null
    var license: License? = null
    var jukeboxPlaylist: JukeboxPlaylist? = null
    var jukeboxStatus: JukeboxStatus? = null

    @Element(name = "playlist")
    var playlist: PlaylistWithSongs? = null

    @Element
    var playlists: Playlists? = null

    @Element
    var searchResult3: SearchResult3? = null

    @Element
    var searchResult2: SearchResult2? = null
    var searchResult: SearchResult? = null
    var nowPlaying: NowPlaying? = null
    var videoInfo: VideoInfo? = null
    var videos: Videos? = null

    @Element(name = "song")
    var song: Child? = null

    @Element(name = "album")
    var album: AlbumWithSongsID3? = null

    @Element(name = "artist")
    var artist: ArtistWithAlbumsID3? = null

    @Element(name = "artists")
    var artists: ArtistsID3? = null

    @Element
    var genres: Genres? = null
    var directory: Directory? = null
    var indexes: Indexes? = null

    @Element
    var musicFolders: MusicFolders? = null

    @Attribute(converter = ResponseStatusConverter::class)
    var status: ResponseStatus? = null

    @Attribute
    var version: String? = null

    @Attribute
    var type: String? = null

    @Attribute
    var serverVersion: String? = null
}