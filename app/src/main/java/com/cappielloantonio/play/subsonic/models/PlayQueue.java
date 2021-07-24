package com.cappielloantonio.play.subsonic.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PlayQueue {
    protected List<Child> entries;
    protected Integer current;
    protected Long position;
    protected String username;
    protected LocalDateTime changed;
    protected String changedBy;

    /**
     * Gets the value of the entries property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the entries property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEntries().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Child }
     */
    public List<Child> getEntries() {
        if (entries == null) {
            entries = new ArrayList<Child>();
        }
        return this.entries;
    }

    /**
     * Gets the value of the current property.
     *
     * @return possible object is
     * {@link Integer }
     */
    public Integer getCurrent() {
        return current;
    }

    /**
     * Sets the value of the current property.
     *
     * @param value allowed object is
     *              {@link Integer }
     */
    public void setCurrent(Integer value) {
        this.current = value;
    }

    /**
     * Gets the value of the position property.
     *
     * @return possible object is
     * {@link Long }
     */
    public Long getPosition() {
        return position;
    }

    /**
     * Sets the value of the position property.
     *
     * @param value allowed object is
     *              {@link Long }
     */
    public void setPosition(Long value) {
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
     * Gets the value of the changedBy property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getChangedBy() {
        return changedBy;
    }

    /**
     * Sets the value of the changedBy property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setChangedBy(String value) {
        this.changedBy = value;
    }
}
