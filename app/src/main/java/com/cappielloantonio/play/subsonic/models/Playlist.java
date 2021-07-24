package com.cappielloantonio.play.subsonic.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Playlist {
    protected List<String> allowedUsers;
    protected String id;
    protected String name;
    protected String comment;
    protected String owner;
    protected Boolean _public;
    protected int songCount;
    protected int duration;
    protected LocalDateTime created;
    protected LocalDateTime changed;
    protected String coverArtId;

    /**
     * Gets the value of the allowedUsers property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the allowedUsers property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAllowedUsers().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     */
    public List<String> getAllowedUsers() {
        if (allowedUsers == null) {
            allowedUsers = new ArrayList<String>();
        }
        return this.allowedUsers;
    }

    /**
     * Gets the value of the id property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setId(String value) {
        this.id = value;
    }

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
     * Gets the value of the owner property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Sets the value of the owner property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setOwner(String value) {
        this.owner = value;
    }

    /**
     * Gets the value of the public property.
     *
     * @return possible object is
     * {@link Boolean }
     */
    public Boolean isPublic() {
        return _public;
    }

    /**
     * Sets the value of the public property.
     *
     * @param value allowed object is
     *              {@link Boolean }
     */
    public void setPublic(Boolean value) {
        this._public = value;
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
     * Gets the value of the duration property.
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Sets the value of the duration property.
     */
    public void setDuration(int value) {
        this.duration = value;
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

    /**
     * Gets the value of the coverArtId property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getCoverArtId() {
        return coverArtId;
    }

    /**
     * Sets the value of the coverArtId property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCoverArtId(String value) {
        this.coverArtId = value;
    }
}
