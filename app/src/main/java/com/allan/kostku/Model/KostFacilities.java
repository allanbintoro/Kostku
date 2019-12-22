package com.allan.kostku.Model;

import java.io.Serializable;

public class KostFacilities implements Serializable {
    boolean wifi, carPark, access24H;

    public KostFacilities() {
    }

    public KostFacilities(boolean wifi, boolean carPark, boolean access24H) {
        this.wifi = wifi;
        this.carPark = carPark;
        this.access24H = access24H;
    }

    public boolean isWifi() {
        return wifi;
    }

    public void setWifi(boolean wifi) {
        this.wifi = wifi;
    }

    public boolean isCarPark() {
        return carPark;
    }

    public void setCarPark(boolean carPark) {
        this.carPark = carPark;
    }

    public boolean isAccess24H() {
        return access24H;
    }

    public void setAccess24H(boolean access24H) {
        this.access24H = access24H;
    }
}
