package com.allan.kostku.Model;

import java.io.Serializable;

public class Room implements Serializable {
    private String roomId, roomName, roomDesc, roomPrice, roomWide, kostId, userId;
    private boolean roomStatus;

    public Room() {
    }

    public Room(String roomId, String roomName, String roomDesc, String roomPrice, String roomWide,
                String kostId, String userId, boolean roomStatus) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.roomDesc = roomDesc;
        this.roomPrice = roomPrice;
        this.roomWide = roomWide;
        this.kostId = kostId;
        this.userId = userId;
        this.roomStatus = roomStatus;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomDesc() {
        return roomDesc;
    }

    public void setRoomDesc(String roomDesc) {
        this.roomDesc = roomDesc;
    }

    public String getRoomPrice() {
        return roomPrice;
    }

    public void setRoomPrice(String roomPrice) {
        this.roomPrice = roomPrice;
    }

    public String getRoomWide() {
        return roomWide;
    }

    public void setRoomWide(String roomWide) {
        this.roomWide = roomWide;
    }

    public String getKostId() {
        return kostId;
    }

    public void setKostId(String kostId) {
        this.kostId = kostId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isRoomStatus() {
        return roomStatus;
    }

    public void setRoomStatus(boolean roomStatus) {
        this.roomStatus = roomStatus;
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomId='" + roomId + '\'' +
                ", roomName='" + roomName + '\'' +
                ", roomDesc='" + roomDesc + '\'' +
                ", roomPrice='" + roomPrice + '\'' +
                ", roomWide='" + roomWide + '\'' +
                ", kostId='" + kostId + '\'' +
                ", userId='" + userId + '\'' +
                ", roomStatus=" + roomStatus +
                '}';
    }
}
