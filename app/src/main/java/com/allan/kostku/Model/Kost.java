package com.allan.kostku.Model;

import java.io.Serializable;

public class Kost implements Serializable {
    private String kostId, kostLocation, kostName, kostDesc, kostType, kostOwnerId, kostImage, parkImage;

    private KostFacilities kostFacilities;

    public Kost() {
    }

    public Kost(String kostId, String kostLocation, String kostName, String kostDesc, String kostType, String kostOwnerId, KostFacilities kostFacilities) {
        this.kostId = kostId;
        this.kostLocation = kostLocation;
        this.kostName = kostName;
        this.kostDesc = kostDesc;
        this.kostType = kostType;
        this.kostOwnerId = kostOwnerId;
        this.kostFacilities = kostFacilities;
    }

    public String getKostId() {
        return kostId;
    }

    public void setKostId(String kostId) {
        this.kostId = kostId;
    }

    public String getKostLocation() {
        return kostLocation;
    }

    public void setKostLocation(String kostLocation) {
        this.kostLocation = kostLocation;
    }

    public String getKostName() {
        return kostName;
    }

    public void setKostName(String kostName) {
        this.kostName = kostName;
    }

    public String getKostDesc() {
        return kostDesc;
    }

    public void setKostDesc(String kostDesc) {
        this.kostDesc = kostDesc;
    }

    public String getKostType() {
        return kostType;
    }

    public void setKostType(String kostType) {
        this.kostType = kostType;
    }

    public String getKostOwnerId() {
        return kostOwnerId;
    }

    public void setKostOwnerId(String kostOwnerId) {
        this.kostOwnerId = kostOwnerId;
    }

    public KostFacilities getKostFacilities() {
        return kostFacilities;
    }

    public void setKostFacilities(KostFacilities kostFacilities) {
        this.kostFacilities = kostFacilities;
    }

    public String getKostImage() {
        return kostImage;
    }

    public void setKostImage(String kostImage) {
        this.kostImage = kostImage;
    }

    public String getParkImage() {
        return parkImage;
    }

    public void setParkImage(String parkImage) {
        this.parkImage = parkImage;
    }

    @Override
    public String toString() {
        return "Kost{" +
                "kostId='" + kostId + '\'' +
                ", kostLocation='" + kostLocation + '\'' +
                ", kostName='" + kostName + '\'' +
                ", kostDesc='" + kostDesc + '\'' +
                ", kostType='" + kostType + '\'' +
                ", kostOwnerId='" + kostOwnerId + '\'' +
                ", kostImage='" + kostImage + '\'' +
                ", parkImage='" + parkImage + '\'' +
                ", kostFacilities=" + kostFacilities +
                '}';
    }
}





