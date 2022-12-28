package com.cappielloantonio.play.service;

import static androidx.media3.common.util.Assertions.checkNotNull;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;

import androidx.annotation.Nullable;
import androidx.annotation.OptIn;
import androidx.media3.common.MediaItem;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.common.util.Util;
import androidx.media3.exoplayer.offline.Download;
import androidx.media3.exoplayer.offline.DownloadCursor;
import androidx.media3.exoplayer.offline.DownloadHelper;
import androidx.media3.exoplayer.offline.DownloadIndex;
import androidx.media3.exoplayer.offline.DownloadManager;
import androidx.media3.exoplayer.offline.DownloadRequest;
import androidx.media3.exoplayer.offline.DownloadService;

import com.cappielloantonio.play.App;
import com.cappielloantonio.play.repository.DownloadRepository;
import com.cappielloantonio.play.repository.QueueRepository;
import com.cappielloantonio.play.util.MappingUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

public class DownloaderManager {
    private static final String TAG = "DownloadTracker";

    private final Context context;
    private final HashMap<Uri, Download> downloads;
    private final DownloadIndex downloadIndex;

    public DownloaderManager(Context context, DownloadManager downloadManager) {
        this.context = context.getApplicationContext();

        downloads = new HashMap<>();
        downloadIndex = downloadManager.getDownloadIndex();

        loadDownloads();
    }

    private DownloadRequest buildDownloadRequest(MediaItem mediaItem) {
        return DownloadHelper.forMediaItem(context, mediaItem).getDownloadRequest(Util.getUtf8Bytes(checkNotNull(mediaItem.mediaId)));
    }

    @OptIn(markerClass = UnstableApi.class)
    public boolean isDownloaded(Uri uri) {
        @Nullable Download download = downloads.get(uri);
        return download != null && download.state != Download.STATE_FAILED;
    }

    public boolean isDownloaded(MediaItem mediaItem) {
        @Nullable Download download = downloads.get(checkNotNull(mediaItem.localConfiguration).uri);
        return download != null && download.state != Download.STATE_FAILED;
    }

    public boolean areDownloaded(List<MediaItem> mediaItems) {
        for (MediaItem mediaItem : mediaItems) {
            @Nullable Download download = downloads.get(checkNotNull(mediaItem.localConfiguration).uri);
            if (download != null && download.state != Download.STATE_FAILED) {
                return true;
            }
        }

        return false;
    }

    public void download(MediaItem mediaItem, com.cappielloantonio.play.model.Download download) {
        DownloadService.sendAddDownload(context, DownloaderService.class, buildDownloadRequest(mediaItem), false);
        downloadDatabase(download);
    }

    public void download(List<MediaItem> mediaItems, List<com.cappielloantonio.play.model.Download> downloads) {
        for (int counter = 0; counter < mediaItems.size(); counter++) {
            download(mediaItems.get(counter), downloads.get(counter));
        }
    }

    public void remove(MediaItem mediaItem, com.cappielloantonio.play.model.Download download) {
        DownloadService.sendRemoveDownload(context, DownloaderService.class, buildDownloadRequest(mediaItem).id, false);
        removeDatabase(download);
    }

    public void remove(List<MediaItem> mediaItems, List<com.cappielloantonio.play.model.Download> downloads) {
        for (int counter = 0; counter < mediaItems.size(); counter++) {
            remove(mediaItems.get(counter), downloads.get(counter));
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

    private static DownloadRepository getDownloadRepository() {
        return new DownloadRepository(App.getInstance());
    }

    private static void downloadDatabase(com.cappielloantonio.play.model.Download download) {
        getDownloadRepository().insert(download);
    }

    private static void removeDatabase(com.cappielloantonio.play.model.Download download) {
        getDownloadRepository().delete(download);
    }
}
