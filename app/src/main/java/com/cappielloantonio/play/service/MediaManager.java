package com.cappielloantonio.play.service;

import androidx.media3.common.MediaItem;
import androidx.media3.session.MediaController;

import com.cappielloantonio.play.App;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.repository.QueueRepository;

import java.util.List;

public class MediaManager {
    public static void clearQueue(MediaController mediaController) {
        mediaController.clearMediaItems();
        getQueueRepository().deleteAll();
    }

    public static void startQueue(MediaController mediaController, List<MediaItem> mediaItems, List<Song> songs) {
        clearQueue(mediaController);
        mediaController.addMediaItems(mediaItems);
        getQueueRepository().insertAllAndStartNew(songs);
    }

    public static void enqueue(MediaController mediaController, List<MediaItem> mediaItems, List<Song> songs) {
        mediaController.addMediaItems(mediaItems);
        getQueueRepository().insertAll(songs);
    }

    private static QueueRepository getQueueRepository() {
        return new QueueRepository(App.getInstance());
    }

    private static int getCurrentMediaIndex(MediaController mediaController) {
        return mediaController.getCurrentMediaItemIndex();
    }
}
