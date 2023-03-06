package com.cappielloantonio.play.subsonic.models;

public class JukeboxStatus {
    protected int currentIndex;
    protected boolean playing;
    protected float gain;
    protected Integer position;

    /**
     * Gets the value of the currentIndex property.
     */
    public int getCurrentIndex() {
        return currentIndex;
    }

    /**
     * Sets the value of the currentIndex property.
     */
    public void setCurrentIndex(int value) {
        this.currentIndex = value;
    }

    /**
     * Gets the value of the playing property.
     */
    public boolean isPlaying() {
        return playing;
    }

    /**
     * Sets the value of the playing property.
     */
    public void setPlaying(boolean value) {
        this.playing = value;
    }

    /**
     * Gets the value of the gain property.
     */
    public float getGain() {
        return gain;
    }

    /**
     * Sets the value of the gain property.
     */
    public void setGain(float value) {
        this.gain = value;
    }

    /**
     * Gets the value of the position property.
     *
     * @return possible object is
     * {@link Integer }
     */
    public Integer getPosition() {
        return position;
    }

    /**
     * Sets the value of the position property.
     *
     * @param value allowed object is
     *              {@link Integer }
     */
    public void setPosition(Integer value) {
        this.position = value;
    }
}
