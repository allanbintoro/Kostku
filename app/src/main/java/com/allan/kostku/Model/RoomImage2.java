package com.allan.kostku.Model;

import java.io.Serializable;

public class RoomImage2 implements Serializable {
    String roomImage2, bathroomImage2;

    public RoomImage2() {
    }

    public RoomImage2(String roomImage2, String bathroomImage2) {
        this.roomImage2 = roomImage2;
        this.bathroomImage2 = bathroomImage2;
    }

    public String getRoomImage2() {
        return roomImage2;
    }

    public void setRoomImage2(String roomImage2) {
        this.roomImage2 = roomImage2;
    }

    public String getBathroomImage2() {
        return bathroomImage2;
    }

    public void setBathroomImage2(String bathroomImage2) {
        this.bathroomImage2 = bathroomImage2;
    }

    @Override
    public String toString() {
        return "RoomImage{" +
                "roomImage='" + roomImage2 + '\'' +
                ", bathroomImage='" + bathroomImage2 + '\'' +
                '}';
    }
}
