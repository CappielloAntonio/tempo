package com.cappielloantonio.play.subsonic.models;

import com.tickaroo.tikxml.annotation.Attribute;
import com.tickaroo.tikxml.annotation.Xml;

@Xml
public class ScanStatus {
    @Attribute
    protected boolean scanning;
    @Attribute
    protected Long count;
    
    public boolean isScanning() {
        return scanning;
    }

    public void setScanning(boolean value) {
        this.scanning = value;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long value) {
        this.count = value;
    }
}
