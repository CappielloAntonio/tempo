package com.cappielloantonio.play.service;

import static androidx.media3.common.util.Assertions.checkNotNull;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;

import androidx.annotation.Nullable;
import androidx.media3.common.MediaItem;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.Util;
import androidx.media3.exoplayer.offline.Download;
import androidx.media3.exoplayer.offline.DownloadCursor;
import androidx.media3.exoplayer.offline.DownloadHelper;
import androidx.media3.exoplayer.offline.DownloadIndex;
import androidx.media3.exoplayer.offline.DownloadManager;
import androidx.media3.exoplayer.offline.DownloadRequest;
import androidx.media3.exoplayer.offline.DownloadService;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

public class DownloaderTracker {
    private static final String TAG = "DownloadTracker";

    private final Context context;
    private final CopyOnWriteArraySet<Listener> listeners;
    private final HashMap<Uri, Download> downloads;
    private final DownloadIndex downloadIndex;

    public interface Listener {
        void onDownloadsChanged();
    }

    @SuppressLint("UnsafeOptInUsageError")
    public DownloaderTracker(Context context, DownloadManager downloadManager) {
        this.context = context.getApplicationContext();

        listeners = new CopyOnWriteArraySet<>();
        downloads = new HashMap<>();
        downloadIndex = downloadManager.getDownloadIndex();

        downloadManager.addListener(new DownloadManagerListener());
        loadDownloads();
    }

    @SuppressLint("UnsafeOptInUsageError")
    public void addListener(Listener listener) {
        checkNotNull(listener);
        listeners.add(listener);
    }

    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }

    @SuppressLint("UnsafeOptInUsageError")
    private DownloadRequest buildDownloadRequest(MediaItem mediaItem) {
        return DownloadHelper.forMediaItem(context, mediaItem).getDownloadRequest(Util.getUtf8Bytes(checkNotNull(mediaItem.mediaId)));
    }

    @SuppressLint("UnsafeOptInUsageError")
    public boolean isDownloaded(MediaItem mediaItem) {
        @Nullable Download download = downloads.get(checkNotNull(mediaItem.localConfiguration).uri);
        return download != null && download.state != Download.STATE_FAILED;
    }

    @SuppressLint("UnsafeOptInUsageError")
    public boolean areDownloaded(List<MediaItem> mediaItems) {
        for (MediaItem mediaItem : mediaItems) {
            @Nullable Download download = downloads.get(checkNotNull(mediaItem.localConfiguration).uri);
            if (download != null && download.state != Download.STATE_FAILED) {
                return true;
            }
        }

        return false;
    }

    @SuppressLint("UnsafeOptInUsageError")
    public void download(MediaItem mediaItem) {
        DownloadService.sendAddDownload(context, DownloaderService.class, buildDownloadRequest(mediaItem), false);
    }

    public void download(List<MediaItem> mediaItems) {
        for (MediaItem mediaItem : mediaItems) {
            download(mediaItem);
        }
    }

    @SuppressLint("UnsafeOptInUsageError")
    public void remove(MediaItem mediaItem) {
        DownloadService.sendRemoveDownload(context, DownloaderService.class, buildDownloadRequest(mediaItem).id, false);
    }

    public void remove(List<MediaItem> mediaItems) {
        for (MediaItem mediaItem : mediaItems) {
            remove(mediaItem);
        }
    }

    @SuppressLint("UnsafeOptInUsageError")
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

    private class DownloadManagerListener implements DownloadManager.Listener {
        @Override
        public void onDownloadChanged(DownloadManager downloadManager, Download download, @Nullable Exception finalException) {
            downloads.put(download.request.uri, download);
            for (Listener listener : listeners) {
                listener.onDownloadsChanged();
            }
        }

        @Override
        public void onDownloadRemoved(DownloadManager downloadManager, Download download) {
            downloads.remove(download.request.uri);
            for (Listener listener : listeners) {
                listener.onDownloadsChanged();
            }
        }
    }
}
