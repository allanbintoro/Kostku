package com.allan.kostku.Model;

import com.google.firebase.firestore.ServerTimestamp;

import java.io.Serializable;
import java.util.Date;

public class Room implements Serializable {
    private String roomId, roomName, roomDesc, roomPrice, roomWide, kostId, userId, bathroomImage2, roomImage;
    private boolean roomStatus;
    private RoomFacilities roomFacilities;
//    private @ServerTimestamp Date dateIn, dueDate;
    private Long dateIn, dueDate;

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

    public String getBathroomImage2() {
        return bathroomImage2;
    }

    public void setBathroomImage2(String bathroomImage2) {
        this.bathroomImage2 = bathroomImage2;
    }

    public String getRoomImage() {
        return roomImage;
    }

    public void setRoomImage(String roomImage) {
        this.roomImage = roomImage;
    }

    public RoomFacilities getRoomFacilities() {
        return roomFacilities;
    }

    public void setRoomFacilities(RoomFacilities roomFacilities) {
        this.roomFacilities = roomFacilities;
    }

    public Long getDateIn() {
        return dateIn;
    }

    public void setDateIn(Long dateIn) {
        this.dateIn = dateIn;
    }

    public Long getDueDate() {
        return dueDate;
    }

    public void setDueDate(Long dueDate) {
        this.dueDate = dueDate;
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
                ", bathroomImage='" + bathroomImage2 + '\'' +
                ", roomImage='" + roomImage + '\'' +
                ", dateIn='" + dateIn + '\'' +
                ", dueDate='" + dueDate + '\'' +
                ", roomStatus=" + roomStatus +
                ", roomFacilities=" + roomFacilities +
                '}';
    }
}
