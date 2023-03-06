package com.cappielloantonio.play.subsonic.models

import java.time.LocalDateTime

class Artist {
    /**
     * Gets the value of the id property.
     *
     * @return possible object is
     * [String]
     */
    /**
     * Sets the value of the id property.
     *
     * @param value allowed object is
     * [String]
     */
    var id: String? = null
    /**
     * Gets the value of the name property.
     *
     * @return possible object is
     * [String]
     */
    /**
     * Sets the value of the name property.
     *
     * @param value allowed object is
     * [String]
     */
    var name: String? = null
    /**
     * Gets the value of the starred property.
     *
     * @return possible object is
     * [String]
     */
    /**
     * Sets the value of the starred property.
     *
     * @param value allowed object is
     * [String]
     */
    var starred: LocalDateTime? = null
    /**
     * Gets the value of the userRating property.
     *
     * @return possible object is
     * [Integer]
     */
    /**
     * Sets the value of the userRating property.
     *
     * @param value allowed object is
     * [Integer]
     */
    var userRating: Int? = null
    /**
     * Gets the value of the averageRating property.
     *
     * @return possible object is
     * [Double]
     */
    /**
     * Sets the value of the averageRating property.
     *
     * @param value allowed object is
     * [Double]
     */
    var averageRating: Double? = null
}