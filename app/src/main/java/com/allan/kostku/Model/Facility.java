package com.allan.kostku.Model;

import java.io.Serializable;

public class Facility implements Serializable {
    private String facilityTitle, facilityDesc, facilityImage;

    public Facility() {
    }

    public Facility(String facilityTitle, String facilityDesc, String facilityImage) {
        this.facilityTitle = facilityTitle;
        this.facilityDesc = facilityDesc;
        this.facilityImage = facilityImage;
    }

    public String getFacilityTitle() {
        return facilityTitle;
    }

    public void setFacilityTitle(String facilityTitle) {
        this.facilityTitle = facilityTitle;
    }

    public String getFacilityDesc() {
        return facilityDesc;
    }

    public void setFacilityDesc(String facilityDesc) {
        this.facilityDesc = facilityDesc;
    }

    public String getFacilityImage() {
        return facilityImage;
    }

    public void setFacilityImage(String facilityImage) {
        this.facilityImage = facilityImage;
    }
}
