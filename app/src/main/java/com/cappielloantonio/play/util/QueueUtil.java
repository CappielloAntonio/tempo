package com.cappielloantonio.play.util;

import com.cappielloantonio.play.model.Queue;
import com.cappielloantonio.play.model.Song;

import java.util.ArrayList;
import java.util.List;

public class QueueUtil {
    public static Queue getQueueElementFromSong(Song song) {
        return new Queue(0, song.getId());
    }

    public static Queue getQueueElementFromSong(Song song, int startingPosition) {
        return new Queue(startingPosition, song.getId());
    }

    public static List<Queue> getQueueElementsFromSongs(List<Song> songs) {
        int counter = 0;
        List<Queue> queue = new ArrayList<>();

        for(Song song: songs) {
            queue.add(new Queue(counter, song.getId()));
            counter++;
        }

        return queue;
    }

    public static List<Queue> getQueueElementsFromSongs(List<Song> songs, int startingPosition) {
        int counter = startingPosition;
        List<Queue> queue = new ArrayList<>();

        for(Song song: songs) {
            queue.add(new Queue(counter, song.getId()));
            counter++;
        }

        return queue;
    }
}
