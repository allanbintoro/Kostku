package com.allan.kostku.Model;

import com.google.firebase.firestore.ServerTimestamp;
import com.google.protobuf.DoubleValue;

import java.io.Serializable;
import java.util.Date;

public class Transaction implements Serializable {

    private String transaction_status,transaction_id,payment_type,order_id,status_code ,gross_amount, transaction_time,
            transaction_type, spending_title, roomId, kostId;
    private Long settlement_time;

    public Transaction() {
    }

    public String getTransaction_time() {
        return transaction_time;
    }

    public void setTransaction_time(String transaction_time) {
        this.transaction_time = transaction_time;
    }

    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }

    public Long getSettlement_time() {
        return settlement_time;
    }

    public void setSettlement_time(Long settlement_time) {
        this.settlement_time = settlement_time;
    }

    public String getTransaction_status() {
        return transaction_status;
    }

    public void setTransaction_status(String transaction_status) {
        this.transaction_status = transaction_status;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getStatus_code() {
        return status_code;
    }

    public String getGross_amount() {
        return gross_amount;
    }

    public void setGross_amount(String gross_amount) {
        this.gross_amount = gross_amount;
    }

    public String getTransaction_type() {
        return transaction_type;
    }

    public void setTransaction_type(String transaction_type) {
        this.transaction_type = transaction_type;
    }

    public String getSpending_title() {
        return spending_title;
    }

    public void setSpending_title(String spending_title) {
        this.spending_title = spending_title;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getKostId() {
        return kostId;
    }

    public void setKostId(String kostId) {
        this.kostId = kostId;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transaction_status='" + transaction_status + '\'' +
                ", transaction_id='" + transaction_id + '\'' +
                ", payment_type='" + payment_type + '\'' +
                ", order_id='" + order_id + '\'' +
                ", status_code='" + status_code + '\'' +
                ", gross_amount='" + gross_amount + '\'' +
                ", transaction_time='" + transaction_time + '\'' +
                ", transaction_type='" + transaction_type + '\'' +
                ", spending_title='" + spending_title + '\'' +
                ", settlement_time=" + settlement_time +
                '}';
    }
}
