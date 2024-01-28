package com.cappielloantonio.tempo.service;

import static androidx.media3.common.util.Assertions.checkNotNull;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.media3.common.MediaItem;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.common.util.Util;
import androidx.media3.datasource.DataSource;
import androidx.media3.exoplayer.offline.Download;
import androidx.media3.exoplayer.offline.DownloadCursor;
import androidx.media3.exoplayer.offline.DownloadHelper;
import androidx.media3.exoplayer.offline.DownloadIndex;
import androidx.media3.exoplayer.offline.DownloadManager;
import androidx.media3.exoplayer.offline.DownloadRequest;
import androidx.media3.exoplayer.offline.DownloadService;

import com.cappielloantonio.tempo.repository.DownloadRepository;
import com.cappielloantonio.tempo.util.DownloadUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@UnstableApi
public class DownloaderManager {
    private static final String TAG = "DownloaderManager";

    private final Context context;
    private final DataSource.Factory dataSourceFactory;
    private final DownloadIndex downloadIndex;

    private static HashMap<String, Download> downloads;

    public DownloaderManager(Context context, DataSource.Factory dataSourceFactory, DownloadManager downloadManager) {
        this.context = context.getApplicationContext();
        this.dataSourceFactory = dataSourceFactory;

        downloads = new HashMap<>();
        downloadIndex = downloadManager.getDownloadIndex();

        loadDownloads();
    }

    private DownloadRequest buildDownloadRequest(MediaItem mediaItem) {
        return DownloadHelper
                .forMediaItem(
                        context,
                        mediaItem,
                        DownloadUtil.buildRenderersFactory(context, false),
                        dataSourceFactory)
                .getDownloadRequest(Util.getUtf8Bytes(checkNotNull(mediaItem.mediaId)))
                .copyWithId(mediaItem.mediaId);
    }

    public boolean isDownloaded(String mediaId) {
        @Nullable Download download = downloads.get(mediaId);
        return download != null && download.state != Download.STATE_FAILED;
    }

    public boolean isDownloaded(MediaItem mediaItem) {
        return isDownloaded(mediaItem.mediaId);
    }

    public boolean areDownloaded(List<MediaItem> mediaItems) {
        return mediaItems.stream().anyMatch(this::isDownloaded);
    }

    public void download(MediaItem mediaItem, com.cappielloantonio.tempo.model.Download download) {
        download.setDownloadUri(mediaItem.requestMetadata.mediaUri.toString());

        DownloadService.sendAddDownload(context, DownloaderService.class, buildDownloadRequest(mediaItem), false);
        insertDatabase(download);
    }

    public void download(List<MediaItem> mediaItems, List<com.cappielloantonio.tempo.model.Download> downloads) {
        for (int counter = 0; counter < mediaItems.size(); counter++) {
            download(mediaItems.get(counter), downloads.get(counter));
        }
    }

    public void remove(MediaItem mediaItem, com.cappielloantonio.tempo.model.Download download) {
        DownloadService.sendRemoveDownload(context, DownloaderService.class, buildDownloadRequest(mediaItem).id, false);
        deleteDatabase(download.getId());
        downloads.remove(download.getId());
    }

    public void remove(List<MediaItem> mediaItems, List<com.cappielloantonio.tempo.model.Download> downloads) {
        for (int counter = 0; counter < mediaItems.size(); counter++) {
            remove(mediaItems.get(counter), downloads.get(counter));
        }
    }

    public void removeAll() {
        DownloadService.sendRemoveAllDownloads(context, DownloaderService.class, false);
        deleteAllDatabase();
        DownloadUtil.eraseDownloadFolder(context);
    }

    private void loadDownloads() {
        try (DownloadCursor loadedDownloads = downloadIndex.getDownloads()) {
            while (loadedDownloads.moveToNext()) {
                Download download = loadedDownloads.getDownload();
                downloads.put(download.request.id, download);
            }
        } catch (IOException e) {
            Log.w(TAG, "Failed to query downloads", e);
        }
    }

    public static String getDownloadNotificationMessage(String id) {
        com.cappielloantonio.tempo.model.Download download = getDownloadRepository().getDownload(id);
        return download != null ? download.getTitle() : null;
    }

    public static void updateRequestDownload(Download download) {
        updateDatabase(download.request.id);
        downloads.put(download.request.id, download);
    }

    public static void removeRequestDownload(Download download) {
        deleteDatabase(download.request.id);
        downloads.remove(download.request.id);
    }

    private static DownloadRepository getDownloadRepository() {
        return new DownloadRepository();
    }

    private static void insertDatabase(com.cappielloantonio.tempo.model.Download download) {
        getDownloadRepository().insert(download);
    }

    private static void deleteDatabase(String id) {
        getDownloadRepository().delete(id);
    }

    private static void deleteAllDatabase() {
        getDownloadRepository().deleteAll();
    }

    private static void updateDatabase(String id) {
        getDownloadRepository().update(id);
    }
}