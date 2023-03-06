package com.cappielloantonio.play.subsonic.models;

import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.Xml;

import java.util.ArrayList;
import java.util.List;

@Xml
public class Genres {
    @Element
    protected List<Genre> genres;

    public List<Genre> getGenres() {
        if (genres == null) {
            genres = new ArrayList<>();
        }
        return this.genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }
}
