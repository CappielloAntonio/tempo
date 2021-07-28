package com.cappielloantonio.play.subsonic;

import com.cappielloantonio.play.subsonic.api.albumsonglist.AlbumSongListClient;
import com.cappielloantonio.play.subsonic.api.browsing.BrowsingClient;
import com.cappielloantonio.play.subsonic.api.mediaretrieval.MediaRetrievalClient;
import com.cappielloantonio.play.subsonic.api.playlist.PlaylistClient;
import com.cappielloantonio.play.subsonic.api.searching.SearchingClient;
import com.cappielloantonio.play.subsonic.api.system.SystemClient;
import com.cappielloantonio.play.subsonic.base.Version;
import com.cappielloantonio.play.subsonic.models.Playlist;

import java.util.HashMap;
import java.util.Map;

public class Subsonic {
    private static final Version API_MAX_VERSION = Version.of("1.15.0");

    private Version apiVersion = API_MAX_VERSION;
    private SubsonicPreferences preferences;

    private SystemClient systemClient;
    private BrowsingClient browsingClient;
    private MediaRetrievalClient mediaRetrievalClient;
    private PlaylistClient playlistClient;
    private SearchingClient searchingClient;
    private AlbumSongListClient albumSongListClient;

    public Subsonic(SubsonicPreferences preferences) {
        this.preferences = preferences;
    }

    public Version getApiVersion() {
        return apiVersion;
    }

    public SystemClient getSystemClient() {
        if (systemClient == null) {
            systemClient = new SystemClient(this);
        }
        return systemClient;
    }

    public BrowsingClient getBrowsingClient() {
        if (browsingClient == null) {
            browsingClient = new BrowsingClient(this);
        }
        return browsingClient;
    }

    public MediaRetrievalClient getMediaRetrievalClient() {
        if (mediaRetrievalClient == null) {
            mediaRetrievalClient = new MediaRetrievalClient(this);
        }
        return mediaRetrievalClient;
    }

    public PlaylistClient getPlaylistClient() {
        if (playlistClient == null) {
            playlistClient = new PlaylistClient(this);
        }
        return playlistClient;
    }

    public SearchingClient getSearchingClient() {
        if (searchingClient == null) {
            searchingClient = new SearchingClient(this);
        }
        return searchingClient;
    }

    public AlbumSongListClient getAlbumSongListClient() {
        if (albumSongListClient == null) {
            albumSongListClient = new AlbumSongListClient(this);
        }
        return albumSongListClient;
    }

    public String getUrl() {
        return preferences.getServerUrl() + "/rest/";
    }

    public Map<String, String> getParams() {
        Map<String, String> params = new HashMap<>();
        params.put("u", preferences.getUsername());
        params.put("s", preferences.getAuthentication().getSalt());
        params.put("t", preferences.getAuthentication().getToken());
        params.put("v", getApiVersion().getVersionString());
        params.put("c", preferences.getClientName());
        params.put("f", "xml");

        return params;
    }
}
