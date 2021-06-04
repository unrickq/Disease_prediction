package com.example.diseaseprediction.object;

public class ConsultationList {
    private String mAccountID;
    private String receiverID;
    private String sessionID;

    public ConsultationList() {
    }

    public ConsultationList(String mAccountID, String receiverID, String sessionID) {
        this.mAccountID = mAccountID;
        this.receiverID = receiverID;
        this.sessionID = sessionID;
    }

    public String getmAccountID() {
        return mAccountID;
    }

    public void setmAccountID(String mAccountID) {
        this.mAccountID = mAccountID;
    }

    public String getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(String receiverID) {
        this.receiverID = receiverID;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }
}
