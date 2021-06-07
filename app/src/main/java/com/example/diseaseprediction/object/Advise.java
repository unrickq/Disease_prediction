package com.example.diseaseprediction.object;

import java.util.Date;

public class Advise {
    private String adviseID;
    private String description;
    private Date dateCreate;
    private Date dateUpdate;
    private int status;

    public Advise() {
    }

    public Advise(String adviseID, String description, Date dateCreate, Date dateUpdate, int status) {
        this.adviseID = adviseID;
        this.description = description;
        this.dateCreate = dateCreate;
        this.dateUpdate = dateUpdate;
        this.status = status;
    }

    public String getAdviseID() {
        return adviseID;
    }

    public void setAdviseID(String adviseID) {
        this.adviseID = adviseID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
