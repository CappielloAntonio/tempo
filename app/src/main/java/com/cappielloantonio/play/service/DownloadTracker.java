/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cappielloantonio.play.service;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentManager;

import com.cappielloantonio.play.App;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.repository.SongRepository;
import com.cappielloantonio.play.util.MusicUtil;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.drm.DrmSession;
import com.google.android.exoplayer2.drm.DrmSessionEventListener;
import com.google.android.exoplayer2.drm.OfflineLicenseHelper;
import com.google.android.exoplayer2.offline.Download;
import com.google.android.exoplayer2.offline.DownloadCursor;
import com.google.android.exoplayer2.offline.DownloadHelper;
import com.google.android.exoplayer2.offline.DownloadHelper.LiveContentUnsupportedException;
import com.google.android.exoplayer2.offline.DownloadIndex;
import com.google.android.exoplayer2.offline.DownloadManager;
import com.google.android.exoplayer2.offline.DownloadRequest;
import com.google.android.exoplayer2.offline.DownloadService;
import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector.MappedTrackInfo;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.Util;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

import static com.google.android.exoplayer2.util.Assertions.checkNotNull;
import static com.google.android.exoplayer2.util.Assertions.checkStateNotNull;

/**
 * Tracks media that has been downloaded.
 */
public class DownloadTracker {

    private static final String TAG = "DownloadTracker";
    private final Context context;
    private final HttpDataSource.Factory httpDataSourceFactory;
    private final CopyOnWriteArraySet<Listener> listeners;
    private final HashMap<Uri, Download> downloads;
    private final DownloadIndex downloadIndex;
    private final DefaultTrackSelector.Parameters trackSelectorParameters;

    public DownloadTracker(Context context,HttpDataSource.Factory httpDataSourceFactory,DownloadManager downloadManager) {
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

    @Nullable
    public DownloadRequest getDownloadRequest(Uri uri) {
        return new DownloadRequest.Builder(uri.toString(), uri).build();
    }

    public void toggleDownload(List<Song> songs) {
        SongRepository songRepository = new SongRepository(App.getInstance());

        for(Song song: songs) {
            MediaItem mediaItem = MusicUtil.getMediaItemFromSong(song);

            @Nullable Download download = downloads.get(checkNotNull(mediaItem.playbackProperties).uri);

            if (download != null && download.state != Download.STATE_FAILED) {
                song.setOffline(false);
                DownloadService.sendRemoveDownload(context, PlayDownloadService.class, download.request.id, false);
            } else {
                song.setOffline(true);
                DownloadService.sendAddDownload(context, PlayDownloadService.class, getDownloadRequest(mediaItem.playbackProperties.uri),false);
            }

            songRepository.setOfflineStatus(song);
        }
    }

    public void removeAllDownloads() {
        SongRepository songRepository = new SongRepository(App.getInstance());
        songRepository.setAllOffline();
        DownloadService.sendRemoveAllDownloads(context, PlayDownloadService.class, false);
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
        public void onDownloadChanged(
                @NonNull DownloadManager downloadManager,
                @NonNull Download download,
                @Nullable Exception finalException) {
            downloads.put(download.request.uri, download);
            for (Listener listener : listeners) {
                listener.onDownloadsChanged();
            }
        }

        @Override
        public void onDownloadRemoved(
                @NonNull DownloadManager downloadManager, @NonNull Download download) {
            downloads.remove(download.request.uri);
            for (Listener listener : listeners) {
                listener.onDownloadsChanged();
            }
        }
    }
}
