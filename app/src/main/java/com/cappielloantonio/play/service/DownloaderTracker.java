package com.cappielloantonio.play.service;

import static androidx.media3.common.util.Assertions.checkNotNull;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.media3.common.MediaItem;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.Util;
import androidx.media3.datasource.HttpDataSource;
import androidx.media3.exoplayer.RenderersFactory;
import androidx.media3.exoplayer.offline.Download;
import androidx.media3.exoplayer.offline.DownloadCursor;
import androidx.media3.exoplayer.offline.DownloadHelper;
import androidx.media3.exoplayer.offline.DownloadIndex;
import androidx.media3.exoplayer.offline.DownloadManager;
import androidx.media3.exoplayer.offline.DownloadRequest;
import androidx.media3.exoplayer.offline.DownloadService;
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector;
import androidx.media3.exoplayer.trackselection.MappingTrackSelector;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class DownloaderTracker {
    private static final String TAG = "DownloadTracker";

    private final Context context;
    private final HttpDataSource.Factory httpDataSourceFactory;
    private final CopyOnWriteArraySet<Listener> listeners;
    private final HashMap<Uri, Download> downloads;
    private final DownloadIndex downloadIndex;
    private final DefaultTrackSelector.Parameters trackSelectorParameters;

    @Nullable
    private StartDownloadDialogHelper startDownloadDialogHelper;

    public interface Listener {
        void onDownloadsChanged();
    }

    @SuppressLint("UnsafeOptInUsageError")
    public DownloaderTracker(Context context, HttpDataSource.Factory httpDataSourceFactory, DownloadManager downloadManager) {
        this.context = context.getApplicationContext();
        this.httpDataSourceFactory = httpDataSourceFactory;

        listeners = new CopyOnWriteArraySet<>();
        downloads = new HashMap<>();
        downloadIndex = downloadManager.getDownloadIndex();
        trackSelectorParameters = DownloadHelper.getDefaultTrackSelectorParameters(context);

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
    public boolean isDownloaded(MediaItem mediaItem) {
        @Nullable Download download = downloads.get(checkNotNull(mediaItem.localConfiguration).uri);
        return download != null && download.state != Download.STATE_FAILED;
    }

    @Nullable
    public DownloadRequest getDownloadRequest(Uri uri) {
        @Nullable Download download = downloads.get(uri);
        return download != null && download.state != Download.STATE_FAILED ? download.request : null;
    }

    @SuppressLint("UnsafeOptInUsageError")
    public void toggleDownload(FragmentManager fragmentManager, MediaItem mediaItem, RenderersFactory renderersFactory) {
        @Nullable Download download = downloads.get(checkNotNull(mediaItem.localConfiguration).uri);
        if (download != null && download.state != Download.STATE_FAILED) {
            androidx.media3.exoplayer.offline.DownloadService.sendRemoveDownload(context, DownloaderService.class, download.request.id, false);
        } else {
            if (startDownloadDialogHelper != null) {
                startDownloadDialogHelper.release();
            }
            startDownloadDialogHelper = new StartDownloadDialogHelper(fragmentManager, DownloadHelper.forMediaItem(context, mediaItem, renderersFactory, httpDataSourceFactory), mediaItem);
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

    private final class StartDownloadDialogHelper implements DownloadHelper.Callback, DialogInterface.OnClickListener, DialogInterface.OnDismissListener {
        private final FragmentManager fragmentManager;
        private final DownloadHelper downloadHelper;
        private final MediaItem mediaItem;

        private TrackSelectionDialog trackSelectionDialog;
        private MappingTrackSelector.MappedTrackInfo mappedTrackInfo;

        @SuppressLint("UnsafeOptInUsageError")
        public StartDownloadDialogHelper(FragmentManager fragmentManager, DownloadHelper downloadHelper, MediaItem mediaItem) {
            this.fragmentManager = fragmentManager;
            this.downloadHelper = downloadHelper;
            this.mediaItem = mediaItem;
            downloadHelper.prepare(this);
        }

        @SuppressLint("UnsafeOptInUsageError")
        public void release() {
            downloadHelper.release();
            if (trackSelectionDialog != null) {
                trackSelectionDialog.dismiss();
            }
        }

        @SuppressLint("UnsafeOptInUsageError")
        @Override
        public void onPrepared(DownloadHelper helper) {
            onDownloadPrepared(helper);
        }

        @SuppressLint("UnsafeOptInUsageError")
        @Override
        public void onPrepareError(DownloadHelper helper, IOException e) {
            Toast.makeText(context, R.string.download_start_error, Toast.LENGTH_LONG).show();
            Log.e(TAG, e.getMessage());
        }

        // DialogInterface.OnClickListener implementation.

        @SuppressLint("UnsafeOptInUsageError")
        @Override
        public void onClick(DialogInterface dialog, int which) {
            for (int periodIndex = 0; periodIndex < downloadHelper.getPeriodCount(); periodIndex++) {
                downloadHelper.clearTrackSelections(periodIndex);
                for (int i = 0; i < mappedTrackInfo.getRendererCount(); i++) {
                    if (!trackSelectionDialog.getIsDisabled(i)) {
                        downloadHelper.addTrackSelectionForSingleRenderer(periodIndex, i, trackSelectorParameters, trackSelectionDialog.getOverrides(i));
                    }
                }
            }

            DownloadRequest downloadRequest = buildDownloadRequest();

            if (downloadRequest.streamKeys.isEmpty()) {
                return;
            }

            startDownload(downloadRequest);
        }

        @SuppressLint("UnsafeOptInUsageError")
        @Override
        public void onDismiss(DialogInterface dialogInterface) {
            trackSelectionDialog = null;
            downloadHelper.release();
        }

        @SuppressLint("UnsafeOptInUsageError")
        private void onDownloadPrepared(DownloadHelper helper) {
            if (helper.getPeriodCount() == 0) {
                Log.d(TAG, "No periods found. Downloading entire stream.");
                startDownload();
                downloadHelper.release();
                return;
            }

            mappedTrackInfo = downloadHelper.getMappedTrackInfo(0);

            if (!TrackSelectionDialog.willHaveContent(mappedTrackInfo)) {
                Log.d(TAG, "No dialog content. Downloading entire stream.");
                startDownload();
                downloadHelper.release();
                return;
            }

            trackSelectionDialog = TrackSelectionDialog.createForMappedTrackInfoAndParameters(R.string.exo_download_description, mappedTrackInfo, trackSelectorParameters, false, true, this, this);

            trackSelectionDialog.show(fragmentManager, null);
        }

        private void startDownload() {
            startDownload(buildDownloadRequest());
        }

        @SuppressLint("UnsafeOptInUsageError")
        private void startDownload(DownloadRequest downloadRequest) {
            DownloadService.sendAddDownload(context, DownloaderService.class, downloadRequest, false);
        }

        @SuppressLint("UnsafeOptInUsageError")
        private DownloadRequest buildDownloadRequest() {
            return downloadHelper.getDownloadRequest(Util.getUtf8Bytes(checkNotNull(mediaItem.mediaMetadata.title.toString()))).copyWithKeySetId(keySetId);
        }
    }
}
