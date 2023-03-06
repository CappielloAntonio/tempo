package com.cappielloantonio.play.subsonic.models

import java.time.LocalDateTime

class License {
    /**
     * Gets the value of the valid property.
     */
    /**
     * Sets the value of the valid property.
     */
    var isValid = false
    /**
     * Gets the value of the email property.
     *
     * @return possible object is
     * [String]
     */
    /**
     * Sets the value of the email property.
     *
     * @param value allowed object is
     * [String]
     */
    var email: String? = null
    /**
     * Gets the value of the licenseExpires property.
     *
     * @return possible object is
     * [String]
     */
    /**
     * Sets the value of the licenseExpires property.
     *
     * @param value allowed object is
     * [String]
     */
    var licenseExpires: LocalDateTime? = null
    /**
     * Gets the value of the trialExpires property.
     *
     * @return possible object is
     * [String]
     */
    /**
     * Sets the value of the trialExpires property.
     *
     * @param value allowed object is
     * [String]
     */
    var trialExpires: LocalDateTime? = null
}