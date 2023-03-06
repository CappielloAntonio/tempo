package com.cappielloantonio.play.subsonic.models;

import java.util.ArrayList;
import java.util.List;

public class Shares {
    protected List<Share> shares;

    /**
     * Gets the value of the shares property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the shares property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getShares().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Share }
     */
    public List<Share> getShares() {
        if (shares == null) {
            shares = new ArrayList<>();
        }
        return this.shares;
    }
}
