package com.cappielloantonio.play.subsonic.models

open class JukeboxStatus {
    /**
     * Gets the value of the currentIndex property.
     */
    /**
     * Sets the value of the currentIndex property.
     */
    var currentIndex = 0
    /**
     * Gets the value of the playing property.
     */
    /**
     * Sets the value of the playing property.
     */
    var isPlaying = false
    /**
     * Gets the value of the gain property.
     */
    /**
     * Sets the value of the gain property.
     */
    var gain = 0f
    /**
     * Gets the value of the position property.
     *
     * @return possible object is
     * [Integer]
     */
    /**
     * Sets the value of the position property.
     *
     * @param value allowed object is
     * [Integer]
     */
    var position: Int? = null
}