package com.cappielloantonio.play.subsonic.models;

public class InternetRadioStation {
    protected String id;
    protected String name;
    protected String streamUrl;
    protected String homePageUrl;

    /**
     * Gets the value of the id property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the name property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the streamUrl property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getStreamUrl() {
        return streamUrl;
    }

    /**
     * Sets the value of the streamUrl property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setStreamUrl(String value) {
        this.streamUrl = value;
    }

    /**
     * Gets the value of the homePageUrl property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getHomePageUrl() {
        return homePageUrl;
    }

    /**
     * Sets the value of the homePageUrl property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setHomePageUrl(String value) {
        this.homePageUrl = value;
    }
}
