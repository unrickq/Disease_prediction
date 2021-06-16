package com.example.diseaseprediction.object;

import java.util.Date;

/**
 * Notification
 */
public class Notification {
    private String notificationID;
    private String accountID;
    private String content;
    private Date dateCreate;
    private Date dateUpdate;
    private int status;

    /**
     * Notification constructor
     */
    public Notification() {
    }

    /**
     * Create notification
     * @param notificationID    Notification ID
     * @param accountID         Account ID
     * @param content           Content
     * @param dateCreate        Date create
     * @param dateUpdate        Date update
     * @param status            0: Deleted | 1: Normal
     */
    public Notification(String notificationID, String accountID, String content, Date dateCreate, Date dateUpdate, int status) {
        this.notificationID = notificationID;
        this.accountID = accountID;
        this.content = content;
        this.dateCreate = dateCreate;
        this.dateUpdate = dateUpdate;
        this.status = status;
    }

    /**
     * Get Notification ID
     * @return Notification ID
     */
    public String getNotificationID() {
        return notificationID;
    }

    /**
     * Set Notification ID
     * @param notificationID Notification ID
     */
    public void setNotificationID(String notificationID) {
        this.notificationID = notificationID;
    }

    /**
     * Get Account ID
     * @return Account ID
     */
    public String getAccountID() {
        return accountID;
    }

    /**
     * Set Account ID
     * @param accountID Account ID
     */
    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    /**
     * Get Content
     * @return Content
     */
    public String getContent() {
        return content;
    }

    /**
     * Set Content
     * @param content Content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Get Date create
     * @return Date create
     */
    public Date getDateCreate() {
        return dateCreate;
    }

    /**
     * Set Date create
     * @param dateCreate Date create
     */
    public void setDateCreate(Date dateCreate) {
        this.dateCreate = dateCreate;
    }

    /**
     * Get Date update
     * @return Date update
     */
    public Date getDateUpdate() {
        return dateUpdate;
    }

    /**
     * Date update
     * @param dateUpdate Date update
     */
    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    /**
     * Get Status
     * @return 0: Deleted | 1: Normal
     */
    public int getStatus() {
        return status;
    }

    /**
     * Set Status. 0: Deleted | 1: Normal
     * @param status 0: Deleted | 1: Normal
     */
    public void setStatus(int status) {
        this.status = status;
    }
}
