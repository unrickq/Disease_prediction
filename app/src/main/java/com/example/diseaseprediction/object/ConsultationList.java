package com.example.diseaseprediction.object;

public class ConsultationList {
    private String accountOne;
    private String accountTwo;
    private String sessionID;

    public ConsultationList() {
    }

    public ConsultationList(String accountOne, String accountTwo, String sessionID) {
        this.accountOne = accountOne;
        this.accountTwo = accountTwo;
        this.sessionID = sessionID;
    }

    public String getAccountOne() {
        return accountOne;
    }

    public void setAccountOne(String accountOne) {
        this.accountOne = accountOne;
    }

    public String getAccountTwo() {
        return accountTwo;
    }

    public void setAccountTwo(String accountTwo) {
        this.accountTwo = accountTwo;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }
}
