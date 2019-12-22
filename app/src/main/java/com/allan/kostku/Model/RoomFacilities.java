package com.allan.kostku.Model;

import java.io.Serializable;

public class RoomFacilities implements Serializable {
    boolean Ac, Fan, Tv, Bathroom, Bed;

    public RoomFacilities() {
    }

    public RoomFacilities(boolean ac, boolean fan, boolean tv, boolean bathroom, boolean bed) {
        Ac = ac;
        Fan = fan;
        Tv = tv;
        Bathroom = bathroom;
        Bed = bed;
    }

    public boolean isAc() {
        return Ac;
    }

    public void setAc(boolean ac) {
        Ac = ac;
    }

    public boolean isFan() {
        return Fan;
    }

    public void setFan(boolean fan) {
        Fan = fan;
    }

    public boolean isTv() {
        return Tv;
    }

    public void setTv(boolean tv) {
        Tv = tv;
    }

    public boolean isBathroom() {
        return Bathroom;
    }

    public void setBathroom(boolean bathroom) {
        Bathroom = bathroom;
    }

    public boolean isBed() {
        return Bed;
    }

    public void setBed(boolean bed) {
        Bed = bed;
    }

    @Override
    public String toString() {
        return "RoomFacilities{" +
                "Ac=" + Ac +
                ", Fan=" + Fan +
                ", Tv=" + Tv +
                ", Bathroom=" + Bathroom +
                ", Bed=" + Bed +
                '}';
    }
}
