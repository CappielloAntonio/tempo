package com.cappielloantonio.play.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.cappielloantonio.play.subsonic.models.ArtistID3
import com.cappielloantonio.play.subsonic.models.ArtistInfo2
import com.cappielloantonio.play.subsonic.models.ArtistWithAlbumsID3
import com.cappielloantonio.play.subsonic.models.SimilarArtistID3
import com.cappielloantonio.play.util.MappingUtil
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
class Artist(
    val id: String?,
    val name: String?,
    val primary: String?,
    val albumCount: Int?,
    var starred: Boolean?,
    val bio: String?,
    val imageUrl: String?,
    val lastfm: String?,
    val albums: List<Album>?,
    val similarArtists: List<Artist>?,
) : Parcelable {

    constructor(artistID3: ArtistID3) : this(
        artistID3.id,
        artistID3.name,
        artistID3.coverArtId,
        artistID3.albumCount,
        artistID3.starred != null,
        null,
        null,
        null,
        null,
        null
    )

    constructor(artistWithAlbumsID3: ArtistWithAlbumsID3) : this(
        artistWithAlbumsID3.id,
        artistWithAlbumsID3.name,
        artistWithAlbumsID3.coverArtId,
        artistWithAlbumsID3.albumCount,
        artistWithAlbumsID3.starred != null,
        null,
        null,
        null,
        MappingUtil.mapAlbum(artistWithAlbumsID3.albums),
        null
    )

    constructor(similarArtistID3: SimilarArtistID3) : this(
        similarArtistID3.id,
        similarArtistID3.name,
        similarArtistID3.coverArtId,
        similarArtistID3.albumCount,
        null,
        null,
        null,
        null,
        null,
        null
    )

    constructor(artistInfo2: ArtistInfo2) : this(
        null,
        null,
        null,
        null,
        null,
        artistInfo2.biography,
        artistInfo2.largeImageUrl,
        artistInfo2.lastFmUrl,
        null,
        MappingUtil.mapSimilarArtist(artistInfo2.similarArtists)
    )

    constructor(id: String, name: String) : this(
        id,
        name,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null
    )

    constructor(download: Download) : this(
        download.artistId,
        download.artistName
    )

    companion object {
        const val DOWNLOADED = "DOWNLOADED"
        const val STARRED = "STARRED"
        const val ORDER_BY_NAME = "ORDER_BY_NAME"
        const val ORDER_BY_RANDOM = "ORDER_BY_RANDOM"
    }
}