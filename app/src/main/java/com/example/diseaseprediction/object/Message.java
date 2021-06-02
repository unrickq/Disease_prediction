package com.example.diseaseprediction.object;

import java.util.Date;

public class Message {
    private String messageID;
    private String senderID;
    private String receiverID;
    private String message;
    private Date dateSend;
    private String sessionID;
    private int status;

    public Message() {
    }

    public Message(String messageID, String senderID, String receiverID, String message, Date dateSend, String sessionID, int status) {
        this.messageID = messageID;
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.message = message;
        this.dateSend = dateSend;
        this.sessionID = sessionID;
        this.status = status;
    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(String receiverID) {
        this.receiverID = receiverID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDateSend() {
        return dateSend;
    }

    public void setDateSend(Date dateSend) {
        this.dateSend = dateSend;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
