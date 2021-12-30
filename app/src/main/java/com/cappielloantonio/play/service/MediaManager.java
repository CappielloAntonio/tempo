package com.cappielloantonio.play.service;

import android.content.Context;
import android.util.Log;

import androidx.media3.common.MediaItem;
import androidx.media3.session.MediaBrowser;

import com.cappielloantonio.play.App;
import com.cappielloantonio.play.interfaces.MediaIndexCallback;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.repository.QueueRepository;
import com.cappielloantonio.play.util.MappingUtil;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class MediaManager {
    private static final String TAG = "MediaManager";

    public static void check(ListenableFuture<MediaBrowser> mediaBrowserListenableFuture, Context context) {
        if (mediaBrowserListenableFuture != null) {
            mediaBrowserListenableFuture.addListener(() -> {
                try {
                    if (mediaBrowserListenableFuture.isDone()) {
                        if (mediaBrowserListenableFuture.get().getMediaItemCount() < 1) {
                            List<Song> songs = getQueueRepository().getSongs();
                            if (songs.size() >= 1) {
                                init(mediaBrowserListenableFuture, context, songs);
                            }
                        }
                    }
                } catch (ExecutionException | InterruptedException e) {
                    Log.e(TAG, e.getMessage());
                }
            }, MoreExecutors.directExecutor());
        }
    }

    public static void init(ListenableFuture<MediaBrowser> mediaBrowserListenableFuture, Context context, List<Song> songs) {
        if (mediaBrowserListenableFuture != null) {
            mediaBrowserListenableFuture.addListener(() -> {
                try {
                    if (mediaBrowserListenableFuture.isDone()) {
                        mediaBrowserListenableFuture.get().clearMediaItems();
                        mediaBrowserListenableFuture.get().setMediaItems(MappingUtil.mapMediaItems(context, songs));
                        mediaBrowserListenableFuture.get().seekTo(getQueueRepository().getLastPlayedSongIndex(), 0);
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

    public static void startQueue(ListenableFuture<MediaBrowser> mediaBrowserListenableFuture, Context context, List<Song> songs, int startIndex) {
        if (mediaBrowserListenableFuture != null) {
            mediaBrowserListenableFuture.addListener(() -> {
                try {
                    if (mediaBrowserListenableFuture.isDone()) {
                        mediaBrowserListenableFuture.get().clearMediaItems();
                        mediaBrowserListenableFuture.get().setMediaItems(MappingUtil.mapMediaItems(context, songs));
                        mediaBrowserListenableFuture.get().prepare();
                        mediaBrowserListenableFuture.get().seekTo(startIndex, 0);
                        mediaBrowserListenableFuture.get().play();

                        enqueueDatabase(songs, true, 0);
                    }
                } catch (ExecutionException | InterruptedException e) {
                    Log.e(TAG, e.getMessage());
                }
            }, MoreExecutors.directExecutor());
        }
    }

    public static void startQueue(ListenableFuture<MediaBrowser> mediaBrowserListenableFuture, Context context, Song song) {
        if (mediaBrowserListenableFuture != null) {
            mediaBrowserListenableFuture.addListener(() -> {
                try {
                    if (mediaBrowserListenableFuture.isDone()) {
                        mediaBrowserListenableFuture.get().clearMediaItems();
                        mediaBrowserListenableFuture.get().setMediaItem(MappingUtil.mapMediaItem(context, song));
                        mediaBrowserListenableFuture.get().prepare();
                        mediaBrowserListenableFuture.get().play();

                        enqueueDatabase(song, true, 0);
                    }
                } catch (ExecutionException | InterruptedException e) {
                    Log.e(TAG, e.getMessage());
                }
            }, MoreExecutors.directExecutor());
        }
    }

    public static void enqueue(ListenableFuture<MediaBrowser> mediaBrowserListenableFuture, Context context, List<Song> songs, boolean playImmediatelyAfter) {
        if (mediaBrowserListenableFuture != null) {
            mediaBrowserListenableFuture.addListener(() -> {
                try {
                    if (mediaBrowserListenableFuture.isDone()) {
                        if (playImmediatelyAfter) {
                            enqueueDatabase(songs, false, mediaBrowserListenableFuture.get().getNextMediaItemIndex());
                            mediaBrowserListenableFuture.get().addMediaItems(mediaBrowserListenableFuture.get().getNextMediaItemIndex(), MappingUtil.mapMediaItems(context, songs));
                        } else {
                            enqueueDatabase(songs, false, mediaBrowserListenableFuture.get().getMediaItemCount());
                            mediaBrowserListenableFuture.get().addMediaItems(MappingUtil.mapMediaItems(context, songs));
                        }


                    }
                } catch (ExecutionException | InterruptedException e) {
                    Log.e(TAG, e.getMessage());
                }
            }, MoreExecutors.directExecutor());
        }
    }

    public static void enqueue(ListenableFuture<MediaBrowser> mediaBrowserListenableFuture, Context context, Song song, boolean playImmediatelyAfter) {
        if (mediaBrowserListenableFuture != null) {
            mediaBrowserListenableFuture.addListener(() -> {
                try {
                    if (mediaBrowserListenableFuture.isDone()) {
                        if (playImmediatelyAfter) {
                            enqueueDatabase(song, false, mediaBrowserListenableFuture.get().getNextMediaItemIndex());
                            mediaBrowserListenableFuture.get().addMediaItem(mediaBrowserListenableFuture.get().getNextMediaItemIndex(), MappingUtil.mapMediaItem(context, song));
                        } else {
                            enqueueDatabase(song, false, mediaBrowserListenableFuture.get().getMediaItemCount());
                            mediaBrowserListenableFuture.get().addMediaItem(MappingUtil.mapMediaItem(context, song));
                        }
                    }
                } catch (ExecutionException | InterruptedException e) {
                    Log.e(TAG, e.getMessage());
                }
            }, MoreExecutors.directExecutor());
        }
    }

    public static void swap(ListenableFuture<MediaBrowser> mediaBrowserListenableFuture, List<Song> songs, int from, int to) {
        if (mediaBrowserListenableFuture != null) {
            mediaBrowserListenableFuture.addListener(() -> {
                try {
                    if (mediaBrowserListenableFuture.isDone()) {
                        mediaBrowserListenableFuture.get().moveMediaItem(from, to);
                        swapDatabase(songs);
                    }
                } catch (ExecutionException | InterruptedException e) {
                    Log.e(TAG, e.getMessage());
                }
            }, MoreExecutors.directExecutor());
        }
    }

    public static void remove(ListenableFuture<MediaBrowser> mediaBrowserListenableFuture, List<Song> songs, int toRemove) {
        if (mediaBrowserListenableFuture != null) {
            mediaBrowserListenableFuture.addListener(() -> {
                try {
                    if (mediaBrowserListenableFuture.isDone()) {
                        if (mediaBrowserListenableFuture.get().getMediaItemCount() > 1 && mediaBrowserListenableFuture.get().getCurrentMediaItemIndex() != toRemove) {
                            mediaBrowserListenableFuture.get().removeMediaItem(toRemove);
                            removeDatabase(songs, toRemove);
                        } else {
                            removeDatabase(songs, -1);
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
        if (mediaItem != null) getQueueRepository().setTimestamp(mediaItem.mediaId);
    }

    private static QueueRepository getQueueRepository() {
        return new QueueRepository(App.getInstance());
    }

    private static void enqueueDatabase(List<Song> songs, boolean reset, int afterIndex) {
        getQueueRepository().insertAll(songs, reset, afterIndex);
    }

    private static void enqueueDatabase(Song song, boolean reset, int afterIndex) {
        getQueueRepository().insert(song, reset, afterIndex);
    }

    private static void swapDatabase(List<Song> songs) {
        getQueueRepository().insertAll(songs, true, 0);
    }

    private static void removeDatabase(List<Song> songs, int toRemove) {
        if (toRemove != -1) {
            songs.remove(toRemove);
            getQueueRepository().insertAll(songs, true, 0);
        }
    }
}
