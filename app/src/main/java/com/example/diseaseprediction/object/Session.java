package com.example.diseaseprediction.object;

public class Session {
    private String sessionID;
    private int status;

    public Session() {
    }

    public Session(String sessionID, int status) {
        this.sessionID = sessionID;
        this.status = status;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    //session: 1 | end session: 0
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
