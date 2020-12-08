package com.cappielloantonio.play.util;

import com.cappielloantonio.play.model.Queue;
import com.cappielloantonio.play.model.Song;

import java.util.ArrayList;
import java.util.List;

public class QueueUtil {
    public static Queue getQueueElementFromSong(Song song) {
        return new Queue(song.getId(), 0);
    }

    public static List<Queue> getQueueElementsFromSongs(List<Song> songs) {
        List<Queue> queue = new ArrayList<>();

        for(Song song: songs) {
            queue.add(new Queue(song.getId(), 0));
        }

        return queue;
    }
}
