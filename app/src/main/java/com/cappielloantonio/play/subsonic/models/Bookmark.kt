package com.cappielloantonio.play.subsonic.models

import java.time.LocalDateTime

class Bookmark {
    /**
     * Gets the value of the entry property.
     *
     * @return possible object is
     * [Child]
     */
    /**
     * Sets the value of the entry property.
     *
     * @param value allowed object is
     * [Child]
     */
    var entry: Child? = null
    /**
     * Gets the value of the position property.
     */
    /**
     * Sets the value of the position property.
     */
    var position: Long = 0
    /**
     * Gets the value of the username property.
     *
     * @return possible object is
     * [String]
     */
    /**
     * Sets the value of the username property.
     *
     * @param value allowed object is
     * [String]
     */
    var username: String? = null
    /**
     * Gets the value of the comment property.
     *
     * @return possible object is
     * [String]
     */
    /**
     * Sets the value of the comment property.
     *
     * @param value allowed object is
     * [String]
     */
    var comment: String? = null
    /**
     * Gets the value of the created property.
     *
     * @return possible object is
     * [String]
     */
    /**
     * Sets the value of the created property.
     *
     * @param value allowed object is
     * [String]
     */
    var created: LocalDateTime? = null
    /**
     * Gets the value of the changed property.
     *
     * @return possible object is
     * [String]
     */
    /**
     * Sets the value of the changed property.
     *
     * @param value allowed object is
     * [String]
     */
    var changed: LocalDateTime? = null
}