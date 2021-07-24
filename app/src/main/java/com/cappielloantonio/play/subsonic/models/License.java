package com.cappielloantonio.play.subsonic.models;

import java.time.LocalDateTime;

public class License {
    protected boolean valid;
    protected String email;
    protected LocalDateTime licenseExpires;
    protected LocalDateTime trialExpires;

    /**
     * Gets the value of the valid property.
     */
    public boolean isValid() {
        return valid;
    }

    /**
     * Sets the value of the valid property.
     */
    public void setValid(boolean value) {
        this.valid = value;
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
     * Gets the value of the licenseExpires property.
     *
     * @return possible object is
     * {@link String }
     */
    public LocalDateTime getLicenseExpires() {
        return licenseExpires;
    }

    /**
     * Sets the value of the licenseExpires property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setLicenseExpires(LocalDateTime value) {
        this.licenseExpires = value;
    }

    /**
     * Gets the value of the trialExpires property.
     *
     * @return possible object is
     * {@link String }
     */
    public LocalDateTime getTrialExpires() {
        return trialExpires;
    }

    /**
     * Sets the value of the trialExpires property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTrialExpires(LocalDateTime value) {
        this.trialExpires = value;
    }
}
