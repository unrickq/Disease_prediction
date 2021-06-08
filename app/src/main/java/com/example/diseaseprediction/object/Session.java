package com.example.diseaseprediction.object;

import java.util.Date;

public class Session {
    private String sessionID;
    private Date dateCreate;
    private Date dateUpdate;
    private int status;

    public Session() {
    }

    public Session(String sessionID, Date dateCreate, Date dateUpdate, int status) {
        this.sessionID = sessionID;
        this.dateCreate = dateCreate;
        this.dateUpdate = dateUpdate;
        this.status = status;
    }

    public Date getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(Date dateCreate) {
        this.dateCreate = dateCreate;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
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
