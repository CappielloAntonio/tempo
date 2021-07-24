package com.cappielloantonio.play.subsonic.models;

import java.time.LocalDateTime;

public class Bookmark {
    protected Child entry;
    protected long position;
    protected String username;
    protected String comment;
    protected LocalDateTime created;
    protected LocalDateTime changed;

    /**
     * Gets the value of the entry property.
     *
     * @return possible object is
     * {@link Child }
     */
    public Child getEntry() {
        return entry;
    }

    /**
     * Sets the value of the entry property.
     *
     * @param value allowed object is
     *              {@link Child }
     */
    public void setEntry(Child value) {
        this.entry = value;
    }

    /**
     * Gets the value of the position property.
     */
    public long getPosition() {
        return position;
    }

    /**
     * Sets the value of the position property.
     */
    public void setPosition(long value) {
        this.position = value;
    }

    /**
     * Gets the value of the username property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the value of the username property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setUsername(String value) {
        this.username = value;
    }

    /**
     * Gets the value of the comment property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getComment() {
        return comment;
    }

    /**
     * Sets the value of the comment property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setComment(String value) {
        this.comment = value;
    }

    /**
     * Gets the value of the created property.
     *
     * @return possible object is
     * {@link String }
     */
    public LocalDateTime getCreated() {
        return created;
    }

    /**
     * Sets the value of the created property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCreated(LocalDateTime value) {
        this.created = value;
    }

    /**
     * Gets the value of the changed property.
     *
     * @return possible object is
     * {@link String }
     */
    public LocalDateTime getChanged() {
        return changed;
    }

    /**
     * Sets the value of the changed property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setChanged(LocalDateTime value) {
        this.changed = value;
    }
}
