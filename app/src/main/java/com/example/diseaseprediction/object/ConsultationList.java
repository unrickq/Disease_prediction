package com.example.diseaseprediction.object;

public class ConsultationList {
    private String accountID;
    private String sessionID;

    public ConsultationList(String accountID, String sessionID) {
        this.accountID = accountID;
        this.sessionID = sessionID;
    }

    public ConsultationList() {
    }

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }
}
