package com.cappielloantonio.play.util;

import com.cappielloantonio.play.model.Queue;
import com.cappielloantonio.play.model.Song;

import java.util.ArrayList;
import java.util.List;

public class QueueUtil {
    private static final String TAG = "QueueUtil";

    public static Queue getQueueElementFromSong(Song song, int startingPosition) {
        return new Queue(startingPosition, song.getId());
    }

    public static List<Queue> getQueueElementsFromSongs(List<Song> songs) {
        int counter = 0;
        List<Queue> queue = new ArrayList<>();

        for (Song song : songs) {
            queue.add(new Queue(counter, song.getId()));
            counter++;
        }

        return queue;
    }

    public static List<String> getIDsFromSongs(List<Song> songs) {
        List<String> IDs = new ArrayList<>();

        for (Song song : songs) {
            IDs.add(song.getId());
        }

        return IDs;
    }

    public static List<Song> orderSongByIdList(List<String> IDs, List<Song> songs) {
        List<Song> orderedSong = new ArrayList<>();

        for (String ID : IDs) {
            for (Song song : songs) {
                if (ID.equals(song.getId())) {
                    orderedSong.add(song);
                    break;
                }
            }
        }

        return orderedSong;
    }
}
