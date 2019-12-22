package com.allan.kostku.Model;

import java.io.Serializable;

public class RoomImage implements Serializable {
    String roomImage, bathroomImage;

    public RoomImage() {
    }

    public RoomImage(String roomImage, String bathroomImage) {
        this.roomImage = roomImage;
        this.bathroomImage = bathroomImage;
    }

    public String getRoomImage() {
        return roomImage;
    }

    public void setRoomImage(String roomImage) {
        this.roomImage = roomImage;
    }

    public String getBathroomImage() {
        return bathroomImage;
    }

    public void setBathroomImage(String bathroomImage) {
        this.bathroomImage = bathroomImage;
    }

    @Override
    public String toString() {
        return "RoomImage{" +
                "roomImage='" + roomImage + '\'' +
                ", bathroomImage='" + bathroomImage + '\'' +
                '}';
    }
}
