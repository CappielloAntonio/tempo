package com.cappielloantonio.play.subsonic.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class User {
    protected List<Integer> folders;
    protected String username;
    protected String email;
    protected boolean scrobblingEnabled;
    protected Integer maxBitRate;
    protected boolean adminRole;
    protected boolean settingsRole;
    protected boolean downloadRole;
    protected boolean uploadRole;
    protected boolean playlistRole;
    protected boolean coverArtRole;
    protected boolean commentRole;
    protected boolean podcastRole;
    protected boolean streamRole;
    protected boolean jukeboxRole;
    protected boolean shareRole;
    protected boolean videoConversionRole;
    protected LocalDateTime avatarLastChanged;

    /**
     * Gets the value of the folders property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the folders property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFolders().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Integer }
     */
    public List<Integer> getFolders() {
        if (folders == null) {
            folders = new ArrayList<Integer>();
        }
        return this.folders;
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
     * Gets the value of the email property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the value of the email property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setEmail(String value) {
        this.email = value;
    }

    /**
     * Gets the value of the scrobblingEnabled property.
     */
    public boolean isScrobblingEnabled() {
        return scrobblingEnabled;
    }

    /**
     * Sets the value of the scrobblingEnabled property.
     */
    public void setScrobblingEnabled(boolean value) {
        this.scrobblingEnabled = value;
    }

    /**
     * Gets the value of the maxBitRate property.
     *
     * @return possible object is
     * {@link Integer }
     */
    public Integer getMaxBitRate() {
        return maxBitRate;
    }

    /**
     * Sets the value of the maxBitRate property.
     *
     * @param value allowed object is
     *              {@link Integer }
     */
    public void setMaxBitRate(Integer value) {
        this.maxBitRate = value;
    }

    /**
     * Gets the value of the adminRole property.
     */
    public boolean isAdminRole() {
        return adminRole;
    }

    /**
     * Sets the value of the adminRole property.
     */
    public void setAdminRole(boolean value) {
        this.adminRole = value;
    }

    /**
     * Gets the value of the settingsRole property.
     */
    public boolean isSettingsRole() {
        return settingsRole;
    }

    /**
     * Sets the value of the settingsRole property.
     */
    public void setSettingsRole(boolean value) {
        this.settingsRole = value;
    }

    /**
     * Gets the value of the downloadRole property.
     */
    public boolean isDownloadRole() {
        return downloadRole;
    }

    /**
     * Sets the value of the downloadRole property.
     */
    public void setDownloadRole(boolean value) {
        this.downloadRole = value;
    }

    /**
     * Gets the value of the uploadRole property.
     */
    public boolean isUploadRole() {
        return uploadRole;
    }

    /**
     * Sets the value of the uploadRole property.
     */
    public void setUploadRole(boolean value) {
        this.uploadRole = value;
    }

    /**
     * Gets the value of the playlistRole property.
     */
    public boolean isPlaylistRole() {
        return playlistRole;
    }

    /**
     * Sets the value of the playlistRole property.
     */
    public void setPlaylistRole(boolean value) {
        this.playlistRole = value;
    }

    /**
     * Gets the value of the coverArtRole property.
     */
    public boolean isCoverArtRole() {
        return coverArtRole;
    }

    /**
     * Sets the value of the coverArtRole property.
     */
    public void setCoverArtRole(boolean value) {
        this.coverArtRole = value;
    }

    /**
     * Gets the value of the commentRole property.
     */
    public boolean isCommentRole() {
        return commentRole;
    }

    /**
     * Sets the value of the commentRole property.
     */
    public void setCommentRole(boolean value) {
        this.commentRole = value;
    }

    /**
     * Gets the value of the podcastRole property.
     */
    public boolean isPodcastRole() {
        return podcastRole;
    }

    /**
     * Sets the value of the podcastRole property.
     */
    public void setPodcastRole(boolean value) {
        this.podcastRole = value;
    }

    /**
     * Gets the value of the streamRole property.
     */
    public boolean isStreamRole() {
        return streamRole;
    }

    /**
     * Sets the value of the streamRole property.
     */
    public void setStreamRole(boolean value) {
        this.streamRole = value;
    }

    /**
     * Gets the value of the jukeboxRole property.
     */
    public boolean isJukeboxRole() {
        return jukeboxRole;
    }

    /**
     * Sets the value of the jukeboxRole property.
     */
    public void setJukeboxRole(boolean value) {
        this.jukeboxRole = value;
    }

    /**
     * Gets the value of the shareRole property.
     */
    public boolean isShareRole() {
        return shareRole;
    }

    /**
     * Sets the value of the shareRole property.
     */
    public void setShareRole(boolean value) {
        this.shareRole = value;
    }

    /**
     * Gets the value of the videoConversionRole property.
     */
    public boolean isVideoConversionRole() {
        return videoConversionRole;
    }

    /**
     * Sets the value of the videoConversionRole property.
     */
    public void setVideoConversionRole(boolean value) {
        this.videoConversionRole = value;
    }

    /**
     * Gets the value of the avatarLastChanged property.
     *
     * @return possible object is
     * {@link String }
     */
    public LocalDateTime getAvatarLastChanged() {
        return avatarLastChanged;
    }

    /**
     * Sets the value of the avatarLastChanged property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setAvatarLastChanged(LocalDateTime value) {
        this.avatarLastChanged = value;
    }
}
