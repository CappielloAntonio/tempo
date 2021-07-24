package com.cappielloantonio.play.subsonic.models;

public class NowPlayingEntry extends Child {
    protected String username;
    protected int minutesAgo;
    protected int playerId;
    protected String playerName;

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
     * Gets the value of the minutesAgo property.
     */
    public int getMinutesAgo() {
        return minutesAgo;
    }

    /**
     * Sets the value of the minutesAgo property.
     */
    public void setMinutesAgo(int value) {
        this.minutesAgo = value;
    }

    /**
     * Gets the value of the playerId property.
     */
    public int getPlayerId() {
        return playerId;
    }

    /**
     * Sets the value of the playerId property.
     */
    public void setPlayerId(int value) {
        this.playerId = value;
    }

    /**
     * Gets the value of the playerName property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Sets the value of the playerName property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPlayerName(String value) {
        this.playerName = value;
    }
}
