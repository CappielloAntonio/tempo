package com.cappielloantonio.play.subsonic.models;

import java.util.ArrayList;
import java.util.List;

public class VideoInfo {
    protected List<Captions> captions;
    protected List<AudioTrack> audioTracks;
    protected List<VideoConversion> conversions;
    protected String id;

    /**
     * Gets the value of the captions property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the captions property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCaptions().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Captions }
     */
    public List<Captions> getCaptions() {
        if (captions == null) {
            captions = new ArrayList<>();
        }
        return this.captions;
    }

    /**
     * Gets the value of the audioTracks property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the audioTracks property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAudioTracks().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AudioTrack }
     */
    public List<AudioTrack> getAudioTracks() {
        if (audioTracks == null) {
            audioTracks = new ArrayList<>();
        }
        return this.audioTracks;
    }

    /**
     * Gets the value of the conversions property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the conversions property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getConversions().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link VideoConversion }
     */
    public List<VideoConversion> getConversions() {
        if (conversions == null) {
            conversions = new ArrayList<>();
        }
        return this.conversions;
    }

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
}
