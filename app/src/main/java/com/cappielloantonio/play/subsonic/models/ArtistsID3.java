package com.cappielloantonio.play.subsonic.models;

import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.Xml;

import java.util.ArrayList;
import java.util.List;

@Xml
public class ArtistsID3 {
    @Element(name = "index")
    protected List<IndexID3> indices;
    protected String ignoredArticles;

    public List<IndexID3> getIndices() {
        if (indices == null) {
            indices = new ArrayList<>();
        }
        return this.indices;
    }

    public void setIndices(List<IndexID3> indices) {
        this.indices = indices;
    }

    public String getIgnoredArticles() {
        return ignoredArticles;
    }

    public void setIgnoredArticles(String value) {
        this.ignoredArticles = value;
    }
}
