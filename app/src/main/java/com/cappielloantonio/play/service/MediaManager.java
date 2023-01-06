package com.cappielloantonio.play.service;

import android.content.Context;
import android.util.Log;

import androidx.media3.common.MediaItem;
import androidx.media3.session.MediaBrowser;

import com.cappielloantonio.play.App;
import com.cappielloantonio.play.interfaces.MediaIndexCallback;
import com.cappielloantonio.play.model.Media;
import com.cappielloantonio.play.repository.ChronologyRepository;
import com.cappielloantonio.play.repository.QueueRepository;
import com.cappielloantonio.play.repository.SongRepository;
import com.cappielloantonio.play.util.MappingUtil;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class MediaManager {
    private static final String TAG = "MediaManager";

    public static void reset(ListenableFuture<MediaBrowser> mediaBrowserListenableFuture) {
        if (mediaBrowserListenableFuture != null) {
            mediaBrowserListenableFuture.addListener(() -> {
                try {
                    if (mediaBrowserListenableFuture.isDone()) {
                        if (mediaBrowserListenableFuture.get().isPlaying()) {
                            mediaBrowserListenableFuture.get().pause();
                        }

                        mediaBrowserListenableFuture.get().stop();
                        mediaBrowserListenableFuture.get().clearMediaItems();
                        clearDatabase();
                    }
                } catch (ExecutionException | InterruptedException e) {
                    Log.e(TAG, e.getMessage());
                }
            }, MoreExecutors.directExecutor());
        }
    }

    public static void hide(ListenableFuture<MediaBrowser> mediaBrowserListenableFuture) {
        if (mediaBrowserListenableFuture != null) {
            mediaBrowserListenableFuture.addListener(() -> {
                try {
                    if (mediaBrowserListenableFuture.isDone()) {
                        if (mediaBrowserListenableFuture.get().isPlaying()) {
                            mediaBrowserListenableFuture.get().pause();
                        }
                    }
                } catch (ExecutionException | InterruptedException e) {
                    Log.e(TAG, e.getMessage());
                }
            }, MoreExecutors.directExecutor());
        }
    }

    public static void check(ListenableFuture<MediaBrowser> mediaBrowserListenableFuture, Context context) {
        if (mediaBrowserListenableFuture != null) {
            mediaBrowserListenableFuture.addListener(() -> {
                try {
                    if (mediaBrowserListenableFuture.isDone()) {
                        if (mediaBrowserListenableFuture.get().getMediaItemCount() < 1) {
                            List<Media> media = getQueueRepository().getMedia();
                            if (media != null && media.size() >= 1) {
                                init(mediaBrowserListenableFuture, context, media);
                            }
                        }
                    }
                } catch (ExecutionException | InterruptedException e) {
                    Log.e(TAG, e.getMessage());
                }
            }, MoreExecutors.directExecutor());
        }
    }

    public static void init(ListenableFuture<MediaBrowser> mediaBrowserListenableFuture, Context context, List<Media> media) {
        if (mediaBrowserListenableFuture != null) {
            mediaBrowserListenableFuture.addListener(() -> {
                try {
                    if (mediaBrowserListenableFuture.isDone()) {
                        mediaBrowserListenableFuture.get().clearMediaItems();
                        mediaBrowserListenableFuture.get().setMediaItems(MappingUtil.mapMediaItems(context, media, true));
                        mediaBrowserListenableFuture.get().seekTo(getQueueRepository().getLastPlayedMediaIndex(), getQueueRepository().getLastPlayedMediaTimestamp());
                        mediaBrowserListenableFuture.get().prepare();
                    }
                } catch (ExecutionException | InterruptedException e) {
                    Log.e(TAG, e.getMessage());
                }
            }, MoreExecutors.directExecutor());
        }
    }

    public static void prepare(ListenableFuture<MediaBrowser> mediaBrowserListenableFuture) {
        if (mediaBrowserListenableFuture != null) {
            mediaBrowserListenableFuture.addListener(() -> {
                try {
                    if (mediaBrowserListenableFuture.isDone()) {
                        mediaBrowserListenableFuture.get().prepare();
                    }
                } catch (ExecutionException | InterruptedException e) {
                    Log.e(TAG, e.getMessage());
                }
            }, MoreExecutors.directExecutor());
        }
    }

    public static void play(ListenableFuture<MediaBrowser> mediaBrowserListenableFuture) {
        if (mediaBrowserListenableFuture != null) {
            mediaBrowserListenableFuture.addListener(() -> {
                try {
                    if (mediaBrowserListenableFuture.isDone()) {
                        mediaBrowserListenableFuture.get().play();
                    }
                } catch (ExecutionException | InterruptedException e) {
                    Log.e(TAG, e.getMessage());
                }
            }, MoreExecutors.directExecutor());
        }
    }

    public static void pause(ListenableFuture<MediaBrowser> mediaBrowserListenableFuture) {
        if (mediaBrowserListenableFuture != null) {
            mediaBrowserListenableFuture.addListener(() -> {
                try {
                    if (mediaBrowserListenableFuture.isDone()) {
                        mediaBrowserListenableFuture.get().pause();
                    }
                } catch (ExecutionException | InterruptedException e) {
                    Log.e(TAG, e.getMessage());
                }
            }, MoreExecutors.directExecutor());
        }
    }

    public static void stop(ListenableFuture<MediaBrowser> mediaBrowserListenableFuture) {
        if (mediaBrowserListenableFuture != null) {
            mediaBrowserListenableFuture.addListener(() -> {
                try {
                    if (mediaBrowserListenableFuture.isDone()) {
                        mediaBrowserListenableFuture.get().stop();
                    }
                } catch (ExecutionException | InterruptedException e) {
                    Log.e(TAG, e.getMessage());
                }
            }, MoreExecutors.directExecutor());
        }
    }

    public static void clearQueue(ListenableFuture<MediaBrowser> mediaBrowserListenableFuture) {
        if (mediaBrowserListenableFuture != null) {
            mediaBrowserListenableFuture.addListener(() -> {
                try {
                    if (mediaBrowserListenableFuture.isDone()) {
                        mediaBrowserListenableFuture.get().clearMediaItems();
                    }
                } catch (ExecutionException | InterruptedException e) {
                    Log.e(TAG, e.getMessage());
                }
            }, MoreExecutors.directExecutor());
        }
    }

    public static void startQueue(ListenableFuture<MediaBrowser> mediaBrowserListenableFuture, Context context, List<Media> media, int startIndex) {
        if (mediaBrowserListenableFuture != null) {
            mediaBrowserListenableFuture.addListener(() -> {
                try {
                    if (mediaBrowserListenableFuture.isDone()) {
                        mediaBrowserListenableFuture.get().clearMediaItems();
                        mediaBrowserListenableFuture.get().setMediaItems(MappingUtil.mapMediaItems(context, media, true));
                        mediaBrowserListenableFuture.get().prepare();
                        mediaBrowserListenableFuture.get().seekTo(startIndex, 0);
                        mediaBrowserListenableFuture.get().play();
                        enqueueDatabase(media, true, 0);
                    }
                } catch (ExecutionException | InterruptedException e) {
                    Log.e(TAG, e.getMessage());
                }
            }, MoreExecutors.directExecutor());
        }
    }

    public static void startQueue(ListenableFuture<MediaBrowser> mediaBrowserListenableFuture, Context context, Media media) {
        if (mediaBrowserListenableFuture != null) {
            mediaBrowserListenableFuture.addListener(() -> {
                try {
                    if (mediaBrowserListenableFuture.isDone()) {
                        mediaBrowserListenableFuture.get().clearMediaItems();
                        mediaBrowserListenableFuture.get().setMediaItem(MappingUtil.mapMediaItem(context, media, true));
                        mediaBrowserListenableFuture.get().prepare();
                        mediaBrowserListenableFuture.get().play();
                        enqueueDatabase(media, true, 0);
                    }
                } catch (ExecutionException | InterruptedException e) {
                    Log.e(TAG, e.getMessage());
                }
            }, MoreExecutors.directExecutor());
        }
    }

    public static void enqueue(ListenableFuture<MediaBrowser> mediaBrowserListenableFuture, Context context, List<Media> media, boolean playImmediatelyAfter) {
        if (mediaBrowserListenableFuture != null) {
            mediaBrowserListenableFuture.addListener(() -> {
                try {
                    if (mediaBrowserListenableFuture.isDone()) {
                        if (playImmediatelyAfter && mediaBrowserListenableFuture.get().getNextMediaItemIndex() != -1) {
                            enqueueDatabase(media, false, mediaBrowserListenableFuture.get().getNextMediaItemIndex());
                            mediaBrowserListenableFuture.get().addMediaItems(mediaBrowserListenableFuture.get().getNextMediaItemIndex(), MappingUtil.mapMediaItems(context, media, true));
                        } else {
                            enqueueDatabase(media, false, mediaBrowserListenableFuture.get().getMediaItemCount());
                            mediaBrowserListenableFuture.get().addMediaItems(MappingUtil.mapMediaItems(context, media, true));
                        }
                    }
                } catch (ExecutionException | InterruptedException e) {
                    Log.e(TAG, e.getMessage());
                }
            }, MoreExecutors.directExecutor());
        }
    }

    public static void enqueue(ListenableFuture<MediaBrowser> mediaBrowserListenableFuture, Context context, Media media, boolean playImmediatelyAfter) {
        if (mediaBrowserListenableFuture != null) {
            mediaBrowserListenableFuture.addListener(() -> {
                try {
                    if (mediaBrowserListenableFuture.isDone()) {
                        if (playImmediatelyAfter && mediaBrowserListenableFuture.get().getNextMediaItemIndex() != -1) {
                            enqueueDatabase(media, false, mediaBrowserListenableFuture.get().getNextMediaItemIndex());
                            mediaBrowserListenableFuture.get().addMediaItem(mediaBrowserListenableFuture.get().getNextMediaItemIndex(), MappingUtil.mapMediaItem(context, media, true));
                        } else {
                            enqueueDatabase(media, false, mediaBrowserListenableFuture.get().getMediaItemCount());
                            mediaBrowserListenableFuture.get().addMediaItem(MappingUtil.mapMediaItem(context, media, true));
                        }
                    }
                } catch (ExecutionException | InterruptedException e) {
                    Log.e(TAG, e.getMessage());
                }
            }, MoreExecutors.directExecutor());
        }
    }

    public static void swap(ListenableFuture<MediaBrowser> mediaBrowserListenableFuture, List<Media> media, int from, int to) {
        if (mediaBrowserListenableFuture != null) {
            mediaBrowserListenableFuture.addListener(() -> {
                try {
                    if (mediaBrowserListenableFuture.isDone()) {
                        mediaBrowserListenableFuture.get().moveMediaItem(from, to);
                        swapDatabase(media);
                    }
                } catch (ExecutionException | InterruptedException e) {
                    Log.e(TAG, e.getMessage());
                }
            }, MoreExecutors.directExecutor());
        }
    }

    public static void remove(ListenableFuture<MediaBrowser> mediaBrowserListenableFuture, List<Media> media, int toRemove) {
        if (mediaBrowserListenableFuture != null) {
            mediaBrowserListenableFuture.addListener(() -> {
                try {
                    if (mediaBrowserListenableFuture.isDone()) {
                        if (mediaBrowserListenableFuture.get().getMediaItemCount() > 1 && mediaBrowserListenableFuture.get().getCurrentMediaItemIndex() != toRemove) {
                            mediaBrowserListenableFuture.get().removeMediaItem(toRemove);
                            removeDatabase(media, toRemove);
                        } else {
                            removeDatabase(media, -1);
                        }
                    }
                } catch (ExecutionException | InterruptedException e) {
                    Log.e(TAG, e.getMessage());
                }
            }, MoreExecutors.directExecutor());
        }
    }

    public static void getCurrentIndex(ListenableFuture<MediaBrowser> mediaBrowserListenableFuture, MediaIndexCallback callback) {
        if (mediaBrowserListenableFuture != null) {
            mediaBrowserListenableFuture.addListener(() -> {
                try {
                    if (mediaBrowserListenableFuture.isDone()) {
                        callback.onRecovery(mediaBrowserListenableFuture.get().getCurrentMediaItemIndex());
                    }
                } catch (ExecutionException | InterruptedException e) {
                    Log.e(TAG, e.getMessage());
                }
            }, MoreExecutors.directExecutor());
        }
    }

    public static void setLastPlayedTimestamp(MediaItem mediaItem) {
        if (mediaItem != null) getQueueRepository().setLastPlayedTimestamp(mediaItem.mediaId);
    }

    public static void setPlayingPausedTimestamp(MediaItem mediaItem, long ms) {
        if (mediaItem != null)
            getQueueRepository().setPlayingPausedTimestamp(mediaItem.mediaId, ms);
    }

    public static void scrobble(MediaItem mediaItem) {
        if (mediaItem != null)
            if (getQueueRepository().isMediaPlayingPlausible(mediaItem))
                getSongRepository().scrobble(mediaItem.mediaMetadata.extras.getString("id"));
    }

    public static void saveChronology(MediaItem mediaItem) {
        if (mediaItem != null)
            if (getQueueRepository().isMediaPlayingPlausible(mediaItem))
                getChronologyRepository().insert(MappingUtil.mapChronology(mediaItem));
    }

    private static QueueRepository getQueueRepository() {
        return new QueueRepository(App.getInstance());
    }

    private static SongRepository getSongRepository() {
        return new SongRepository(App.getInstance());
    }

    private static ChronologyRepository getChronologyRepository() {
        return new ChronologyRepository(App.getInstance());
    }

    private static void enqueueDatabase(List<Media> media, boolean reset, int afterIndex) {
        getQueueRepository().insertAll(media, reset, afterIndex);
    }

    private static void enqueueDatabase(Media media, boolean reset, int afterIndex) {
        getQueueRepository().insert(media, reset, afterIndex);
    }

    private static void swapDatabase(List<Media> media) {
        getQueueRepository().insertAll(media, true, 0);
    }

    private static void removeDatabase(List<Media> media, int toRemove) {
        if (toRemove != -1) {
            media.remove(toRemove);
            getQueueRepository().insertAll(media, true, 0);
        }
    }

    public static void clearDatabase() {
        getQueueRepository().deleteAll();
    }
}
