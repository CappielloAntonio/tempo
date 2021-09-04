package com.cappielloantonio.play.service;

import static com.google.android.exoplayer2.util.Assertions.checkNotNull;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cappielloantonio.play.App;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.repository.DownloadRepository;
import com.cappielloantonio.play.util.MappingUtil;
import com.cappielloantonio.play.util.MusicUtil;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.offline.Download;
import com.google.android.exoplayer2.offline.DownloadCursor;
import com.google.android.exoplayer2.offline.DownloadHelper;
import com.google.android.exoplayer2.offline.DownloadIndex;
import com.google.android.exoplayer2.offline.DownloadManager;
import com.google.android.exoplayer2.offline.DownloadRequest;
import com.google.android.exoplayer2.offline.DownloadService;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.Log;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

public class DownloadTracker {

    private static final String TAG = "DownloadTracker";
    private final Context context;
    private final HttpDataSource.Factory httpDataSourceFactory;
    private final CopyOnWriteArraySet<Listener> listeners;
    private final HashMap<Uri, Download> downloads;
    private final DownloadIndex downloadIndex;
    private final DefaultTrackSelector.Parameters trackSelectorParameters;

    public DownloadTracker(Context context, HttpDataSource.Factory httpDataSourceFactory, DownloadManager downloadManager) {
        this.context = context.getApplicationContext();
        this.httpDataSourceFactory = httpDataSourceFactory;

        listeners = new CopyOnWriteArraySet<>();
        downloads = new HashMap<>();
        downloadIndex = downloadManager.getDownloadIndex();
        trackSelectorParameters = DownloadHelper.getDefaultTrackSelectorParameters(context);

        downloadManager.addListener(new DownloadManagerListener());
        loadDownloads();
    }

    public void addListener(Listener listener) {
        checkNotNull(listener);
        listeners.add(listener);
    }

    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }

    public boolean isDownloaded(Song song) {
        MediaItem mediaItem = MusicUtil.getMediaItemFromSong(song);
        @Nullable Download download = downloads.get(checkNotNull(mediaItem.playbackProperties).uri);
        return download != null && download.state != Download.STATE_FAILED;
    }

    public boolean isDownloaded(List<Song> songs) {
        for (Song song : songs) {
            MediaItem mediaItem = MusicUtil.getMediaItemFromSong(song);
            @Nullable Download download = downloads.get(checkNotNull(mediaItem.playbackProperties).uri);

            if (download != null && download.state != Download.STATE_FAILED) {
                return true;
            }
        }

        return false;
    }

    @Nullable
    public DownloadRequest getDownloadRequest(String id, Uri uri) {
        return new DownloadRequest.Builder(id, uri).build();
    }

    public void download(List<Song> songs, String playlistId, String playlistName) {
        DownloadRepository downloadRepository = new DownloadRepository(App.getInstance());

        for (Song song : songs) {
            if (isDownloaded(song)) {
                downloadRepository.insert(MappingUtil.mapToDownload(song, playlistId, playlistName));
                continue;
            }

            MediaItem mediaItem = MusicUtil.getMediaItemFromSong(song);
            DownloadService.sendAddDownload(context, DownloaderService.class, getDownloadRequest(song.getId(), checkNotNull(mediaItem.playbackProperties).uri), false);
            downloadRepository.insert(MappingUtil.mapToDownload(song, playlistId, playlistName));
        }
    }

    public void remove(List<Song> songs) {
        DownloadRepository downloadRepository = new DownloadRepository(App.getInstance());

        for (Song song : songs) {
            MediaItem mediaItem = MusicUtil.getMediaItemFromSong(song);

            @Nullable Download download = downloads.get(checkNotNull(mediaItem.playbackProperties).uri);

            if (download != null && download.state != Download.STATE_FAILED) {
                DownloadService.sendRemoveDownload(context, DownloaderService.class, download.request.id, false);
                downloadRepository.delete(MappingUtil.mapToDownload(song, null, null));
            }
        }
    }

    private void loadDownloads() {
        try (DownloadCursor loadedDownloads = downloadIndex.getDownloads()) {
            while (loadedDownloads.moveToNext()) {
                Download download = loadedDownloads.getDownload();
                downloads.put(download.request.uri, download);
            }
        } catch (IOException e) {
            Log.w(TAG, "Failed to query downloads", e);
        }
    }


    public interface Listener {
        void onDownloadsChanged();
    }

    private class DownloadManagerListener implements DownloadManager.Listener {
        @Override
        public void onDownloadChanged(@NonNull DownloadManager downloadManager, @NonNull Download download, @Nullable Exception finalException) {
            downloads.put(download.request.uri, download);

            for (Listener listener : listeners) {
                listener.onDownloadsChanged();
            }
        }

        @Override
        public void onDownloadRemoved(@NonNull DownloadManager downloadManager, @NonNull Download download) {
            downloads.remove(download.request.uri);

            for (Listener listener : listeners) {
                listener.onDownloadsChanged();
            }
        }
    }
}
