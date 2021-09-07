package com.cappielloantonio.play.service;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.cappielloantonio.play.R;
import com.cappielloantonio.play.interfaces.Playback;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.util.DownloadUtil;
import com.cappielloantonio.play.util.MusicUtil;
import com.cappielloantonio.play.util.PreferenceUtil;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.database.ExoDatabaseProvider;
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;

import java.io.File;

public class MultiPlayer implements Playback {
    public static final String TAG = MultiPlayer.class.getSimpleName();

    private final Context context;
    private final SimpleExoPlayer exoPlayer;

    private SimpleCache simpleCache;
    private PlaybackCallbacks callbacks;

    private final ExoPlayer.EventListener eventListener = new ExoPlayer.EventListener() {
        @Override
        public void onPlayWhenReadyChanged(boolean playWhenReady, int reason) {
            Log.i(TAG, String.format("onPlayWhenReadyChanged: %b %d", playWhenReady, reason));

            if (callbacks != null) {
                callbacks.onReadyChanged(playWhenReady, reason);
            }
        }

        @Override
        public void onPlaybackStateChanged(int state) {
            Log.i(TAG, String.format("onPlaybackStateChanged: %d", state));

            if (callbacks != null) {
                callbacks.onStateChanged(state);
            }
        }

        @Override
        public void onPlaybackSuppressionReasonChanged(@Player.PlaybackSuppressionReason int playbackSuppressionReason) {
            Log.i(TAG, String.format("onPlaybackSuppressionReasonChanged: %d", playbackSuppressionReason));

            if (callbacks != null) {
                callbacks.onStateChanged(Player.STATE_READY);
            }
        }

        @Override
        public void onMediaItemTransition(MediaItem mediaItem, int reason) {
            Log.i(TAG, String.format("onMediaItemTransition: %s %d", mediaItem, reason));

            if (exoPlayer.getMediaItemCount() > 1) {
                exoPlayer.removeMediaItem(0);
            }

            if (callbacks != null) {
                callbacks.onTrackChanged(reason);
            }
        }

        @Override
        public void onPositionDiscontinuity(int reason) {
            Log.i(TAG, String.format("onPositionDiscontinuity: %d", reason));
        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {
            Log.i(TAG, String.format("onPlayerError: %s", error.getMessage()));
            Toast.makeText(context, context.getResources().getString(R.string.exo_info_unplayable_file), Toast.LENGTH_SHORT).show();

            exoPlayer.clearMediaItems();
            exoPlayer.prepare();
        }
    };

    public MultiPlayer(Context context) {
        this.context = context;
        setSimpleCache(context);

        // Create a read-only cache data source factory using the download cache.
        DataSource.Factory cacheDataSourceFactory = new CacheDataSource.Factory()
                .setCache(simpleCache)
                .setCache(DownloadUtil.getDownloadCache(context))
                .setUpstreamDataSourceFactory(DownloadUtil.getHttpDataSourceFactory(context))
                .setCacheWriteDataSinkFactory(null); // Disable writing.

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(C.USAGE_MEDIA)
                .setContentType(C.CONTENT_TYPE_MUSIC)
                .build();

        exoPlayer = new SimpleExoPlayer.Builder(context)
                .setMediaSourceFactory(new DefaultMediaSourceFactory(cacheDataSourceFactory))
                .setAudioAttributes(audioAttributes, true)
                .setHandleAudioBecomingNoisy(true)
                .setWakeMode(C.WAKE_MODE_NETWORK)
                .build();

        exoPlayer.addListener(eventListener);
        exoPlayer.prepare();
    }

    private void setSimpleCache(Context context) {
        long cacheSize = PreferenceUtil.getInstance(context).getMediaCacheSize();
        LeastRecentlyUsedCacheEvictor recentlyUsedCache = new LeastRecentlyUsedCacheEvictor(cacheSize);
        ExoDatabaseProvider databaseProvider = new ExoDatabaseProvider(context);

        File cacheDirectory = new File(context.getCacheDir(), "exoplayer");
        simpleCache = new SimpleCache(cacheDirectory, recentlyUsedCache, databaseProvider);
    }

    @Override
    public void setDataSource(Song song) {
        String uri = MusicUtil.getSongStreamUri(context, song);
        MediaItem mediaItem = exoPlayer.getCurrentMediaItem();

        if (mediaItem != null && mediaItem.playbackProperties != null && mediaItem.playbackProperties.uri.toString().equals(uri)) {
            return;
        }

        exoPlayer.clearMediaItems();
        appendDataSource(MusicUtil.getSongStreamUri(context, song));
        exoPlayer.seekTo(0, 0);
    }


    @Override
    public void queueDataSource(Song song) {
        while (exoPlayer.getMediaItemCount() > 1) {
            exoPlayer.removeMediaItem(1);
        }

        appendDataSource(MusicUtil.getSongStreamUri(context, song));
    }

    private void appendDataSource(String path) {
        Uri uri = Uri.parse(path);
        MediaItem mediaItem = MediaItem.fromUri(uri);

        exoPlayer.addMediaItem(mediaItem);
    }

    @Override
    public void setCallbacks(Playback.PlaybackCallbacks callbacks) {
        this.callbacks = callbacks;
    }

    @Override
    public boolean isReady() {
        return exoPlayer.getPlayWhenReady();
    }

    @Override
    public boolean isPlaying() {
        return exoPlayer.getPlayWhenReady() && exoPlayer.getPlaybackSuppressionReason() == Player.PLAYBACK_SUPPRESSION_REASON_NONE;
    }

    @Override
    public boolean isLoading() {
        return exoPlayer.getPlaybackState() == Player.STATE_BUFFERING;
    }

    @Override
    public void start() {
        exoPlayer.setPlayWhenReady(true);
    }

    @Override
    public void pause() {
        exoPlayer.setPlayWhenReady(false);
    }

    @Override
    public void stop() {
        simpleCache.release();
        exoPlayer.release();
    }

    @Override
    public int getProgress() {
        return (int) exoPlayer.getCurrentPosition();
    }

    @Override
    public void setProgress(int progress) {
        exoPlayer.seekTo(progress);
    }

    @Override
    public int getDuration() {
        return (int) exoPlayer.getDuration();
    }

    @Override
    public int getVolume() {
        return (int) (exoPlayer.getVolume() * 100);
    }

    @Override
    public void setVolume(int volume) {
        exoPlayer.setVolume(volume / 100f);
    }
}
