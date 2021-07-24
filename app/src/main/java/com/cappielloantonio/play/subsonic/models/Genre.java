package com.cappielloantonio.play.subsonic.models;

public class Genre {
    protected String name;
    protected int songCount;
    protected int albumCount;

    /**
     * Gets the value of the name property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the songCount property.
     */
    public int getSongCount() {
        return songCount;
    }

    /**
     * Sets the value of the songCount property.
     */
    public void setSongCount(int value) {
        this.songCount = value;
    }

    /**
     * Gets the value of the albumCount property.
     */
    public int getAlbumCount() {
        return albumCount;
    }

    /**
     * Sets the value of the albumCount property.
     */
    public void setAlbumCount(int value) {
        this.albumCount = value;
    }
}
