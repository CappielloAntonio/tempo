package com.cappielloantonio.play.util;

import com.cappielloantonio.play.model.Queue;
import com.cappielloantonio.play.model.Song;

import java.util.ArrayList;
import java.util.List;

public class QueueUtil {
    private static final String TAG = "QueueUtil";

    public static List<Queue> getQueueElementsFromSongs(List<Song> songs) {
        int counter = 0;
        List<Queue> queue = new ArrayList<>();

        for (Song song : songs) {
            queue.add(new Queue(counter, song.getId(), song.getTitle(), song.getAlbumId(), song.getAlbumName(), song.getArtistId(), song.getArtistName(), song.getPrimary(), song.getDuration(), 0));
            counter++;
        }

        return queue;
    }
}
