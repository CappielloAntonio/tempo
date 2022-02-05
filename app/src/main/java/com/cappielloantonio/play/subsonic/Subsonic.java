package com.cappielloantonio.play.subsonic;

import android.content.Context;

import com.cappielloantonio.play.subsonic.api.albumsonglist.AlbumSongListClient;
import com.cappielloantonio.play.subsonic.api.browsing.BrowsingClient;
import com.cappielloantonio.play.subsonic.api.mediaannotation.MediaAnnotationClient;
import com.cappielloantonio.play.subsonic.api.medialibraryscanning.MediaLibraryScanningClient;
import com.cappielloantonio.play.subsonic.api.mediaretrieval.MediaRetrievalClient;
import com.cappielloantonio.play.subsonic.api.playlist.PlaylistClient;
import com.cappielloantonio.play.subsonic.api.podcast.PodcastClient;
import com.cappielloantonio.play.subsonic.api.searching.SearchingClient;
import com.cappielloantonio.play.subsonic.api.system.SystemClient;
import com.cappielloantonio.play.subsonic.base.Version;

import java.util.HashMap;
import java.util.Map;

public class Subsonic {
    private static final Version API_MAX_VERSION = Version.of("1.15.0");

    private final Context context;

    private final Version apiVersion = API_MAX_VERSION;
    private final SubsonicPreferences preferences;

    private SystemClient systemClient;
    private BrowsingClient browsingClient;
    private MediaRetrievalClient mediaRetrievalClient;
    private PlaylistClient playlistClient;
    private SearchingClient searchingClient;
    private AlbumSongListClient albumSongListClient;
    private MediaAnnotationClient mediaAnnotationClient;
    private PodcastClient podcastClient;
    private MediaLibraryScanningClient mediaLibraryScanningClient;

    public Subsonic(Context context, SubsonicPreferences preferences) {
        this.context = context;
        this.preferences = preferences;
    }

    public Version getApiVersion() {
        return apiVersion;
    }

    public SystemClient getSystemClient() {
        if (systemClient == null) {
            systemClient = new SystemClient(context, this);
        }
        return systemClient;
    }

    public BrowsingClient getBrowsingClient() {
        if (browsingClient == null) {
            browsingClient = new BrowsingClient(context, this);
        }
        return browsingClient;
    }

    public MediaRetrievalClient getMediaRetrievalClient() {
        if (mediaRetrievalClient == null) {
            mediaRetrievalClient = new MediaRetrievalClient(context, this);
        }
        return mediaRetrievalClient;
    }

    public PlaylistClient getPlaylistClient() {
        if (playlistClient == null) {
            playlistClient = new PlaylistClient(context, this);
        }
        return playlistClient;
    }

    public SearchingClient getSearchingClient() {
        if (searchingClient == null) {
            searchingClient = new SearchingClient(context, this);
        }
        return searchingClient;
    }

    public AlbumSongListClient getAlbumSongListClient() {
        if (albumSongListClient == null) {
            albumSongListClient = new AlbumSongListClient(context, this);
        }
        return albumSongListClient;
    }

    public MediaAnnotationClient getMediaAnnotationClient() {
        if (mediaAnnotationClient == null) {
            mediaAnnotationClient = new MediaAnnotationClient(context, this);
        }
        return mediaAnnotationClient;
    }

    public PodcastClient getPodcastClient() {
        if (podcastClient == null) {
            podcastClient = new PodcastClient(context, this);
        }
        return podcastClient;
    }

    public MediaLibraryScanningClient getMediaLibraryScanningClient() {
        if (mediaLibraryScanningClient == null) {
            mediaLibraryScanningClient = new MediaLibraryScanningClient(context, this);
        }
        return mediaLibraryScanningClient;
    }

    public String getUrl() {
        String url = preferences.getServerUrl() + "/rest/";

        return url.replace("//rest", "/rest");
    }

    public Map<String, String> getParams() {
        Map<String, String> params = new HashMap<>();
        params.put("u", preferences.getUsername());
        params.put("s", preferences.getAuthentication().getSalt());
        params.put("t", preferences.getAuthentication().getToken());
        params.put("v", getApiVersion().getVersionString());
        params.put("c", preferences.getClientName());
        params.put("f", "xml");

        if (preferences.getPassword() != null && !preferences.getPassword().trim().equals("")) params.put("p", preferences.getPassword());

        return params;
    }
}
