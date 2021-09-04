package com.cappielloantonio.play.subsonic.models;

import java.util.ArrayList;
import java.util.List;

public class InternetRadioStations {
    protected List<InternetRadioStation> internetRadioStations;

    /**
     * Gets the value of the internetRadioStations property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the internetRadioStations property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInternetRadioStations().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link InternetRadioStation }
     */
    public List<InternetRadioStation> getInternetRadioStations() {
        if (internetRadioStations == null) {
            internetRadioStations = new ArrayList<>();
        }
        return this.internetRadioStations;
    }

}
