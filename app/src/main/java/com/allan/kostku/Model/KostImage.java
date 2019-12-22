package com.allan.kostku.Model;

import java.io.Serializable;

public class KostImage implements Serializable {
    String kostImage, parkImage;

    public KostImage() {
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
}
