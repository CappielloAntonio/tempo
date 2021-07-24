package com.cappielloantonio.play.subsonic.models;

public class Lyrics {
    protected String content;
    protected String artist;
    protected String title;

    /**
     * Gets the value of the content property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets the value of the content property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setContent(String value) {
        this.content = value;
    }

    /**
     * Gets the value of the artist property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getArtist() {
        return artist;
    }

    /**
     * Sets the value of the artist property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setArtist(String value) {
        this.artist = value;
    }

    /**
     * Gets the value of the title property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the value of the title property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTitle(String value) {
        this.title = value;
    }
}
