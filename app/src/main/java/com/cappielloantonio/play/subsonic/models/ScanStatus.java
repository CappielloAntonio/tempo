package com.cappielloantonio.play.subsonic.models;

public class ScanStatus {
    protected boolean scanning;
    protected Long count;

    /**
     * Gets the value of the scanning property.
     */
    public boolean isScanning() {
        return scanning;
    }

    /**
     * Sets the value of the scanning property.
     */
    public void setScanning(boolean value) {
        this.scanning = value;
    }

    /**
     * Gets the value of the count property.
     *
     * @return possible object is
     * {@link Long }
     */
    public Long getCount() {
        return count;
    }

    /**
     * Sets the value of the count property.
     *
     * @param value allowed object is
     *              {@link Long }
     */
    public void setCount(Long value) {
        this.count = value;
    }
}
