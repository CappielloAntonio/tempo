package com.cappielloantonio.tempo.util;

import androidx.annotation.OptIn;
import androidx.media3.common.util.UnstableApi;

import com.cappielloantonio.tempo.subsonic.models.Artist;
import com.cappielloantonio.tempo.subsonic.models.Index;
import com.cappielloantonio.tempo.subsonic.models.Indexes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@OptIn(markerClass = UnstableApi.class)
public class IndexUtil {
    public static List<Artist> getArtist(Indexes indexes) {
        if (indexes.getIndices() == null) return Collections.emptyList();

        ArrayList<Artist> toReturn = new ArrayList<>();

        for (Index index : indexes.getIndices()) {
            if (index.getArtists() != null) {
                toReturn.addAll(index.getArtists());
            }
        }

        return toReturn;
    }
}
