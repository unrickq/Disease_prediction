package com.example.diseaseprediction.object;

import java.util.Date;

/**
 * Session
 */
public class Session {
    private String sessionID;
    private Date dateCreate;
    private Date dateUpdate;
    private int status;

    /**
     * Session constructor
     */
    public Session() {
    }

    /**
     * Create new session
     *
     * @param sessionID  Session ID
     * @param dateCreate Date create
     * @param dateUpdate Date update
     * @param status     0: End session | 1: In session
     */
    public Session(String sessionID, Date dateCreate, Date dateUpdate, int status) {
        this.sessionID = sessionID;
        this.dateCreate = dateCreate;
        this.dateUpdate = dateUpdate;
        this.status = status;
    }

    /**
     * Get Date create
     *
     * @return date create
     */
    public Date getDateCreate() {
        return dateCreate;
    }

    /**
     * Set Date create
     *
     * @param dateCreate date create
     */
    public void setDateCreate(Date dateCreate) {
        this.dateCreate = dateCreate;
    }

    /**
     * Get Date update
     *
     * @return Date update
     */
    public Date getDateUpdate() {
        return dateUpdate;
    }

    /**
     * Set Date update
     *
     * @param dateUpdate Date update
     */
    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    /**
     * Get Session ID
     *
     * @return Session ID
     */
    public String getSessionID() {
        return sessionID;
    }

    /**
     * Set Session ID
     *
     * @param sessionID Session ID
     */
    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    /**
     * Get Status
     *
     * @return 0: End session | 1: In session
     */
    public int getStatus() {
        return status;
    }

    /**
     * Set Status. 0: End session | 1: In session
     *
     * @param status 0: End session | 1: In session
     */
    public void setStatus(int status) {
        this.status = status;
    }
}
