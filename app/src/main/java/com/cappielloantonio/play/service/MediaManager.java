package com.cappielloantonio.play.service;

import android.content.Context;
import android.util.Log;

import androidx.media3.session.MediaBrowser;
import androidx.media3.session.MediaController;

import com.cappielloantonio.play.App;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.repository.QueueRepository;
import com.cappielloantonio.play.util.MappingUtil;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class MediaManager {
    private static final String TAG = "MediaManager";

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
                            mediaBrowserListenableFuture.get().addMediaItems(mediaBrowserListenableFuture.get().getCurrentMediaItemIndex(), MappingUtil.mapMediaItems(context, songs));
                        } else {
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
                            mediaBrowserListenableFuture.get().addMediaItem(mediaBrowserListenableFuture.get().getCurrentMediaItemIndex(), MappingUtil.mapMediaItem(context, song));
                        } else {
                            mediaBrowserListenableFuture.get().addMediaItem(MappingUtil.mapMediaItem(context, song));
                        }
                    }
                } catch (ExecutionException | InterruptedException e) {
                    Log.e(TAG, e.getMessage());
                }
            }, MoreExecutors.directExecutor());
        }
    }

    private static QueueRepository getQueueRepository() {
        return new QueueRepository(App.getInstance());
    }

    private static int getCurrentMediaIndex(MediaController mediaController) {
        return mediaController.getCurrentMediaItemIndex();
    }
}
