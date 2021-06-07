package com.example.diseaseprediction.object;

import java.util.Date;

public class Symptom {
    private String symptomsID;
    private String name;
    private String description;
    private Date dateCreate;
    private Date dateUpdate;
    private int status;

    public Symptom() {
    }

    public Symptom(String symptomsID, String name, String description, Date dateCreate, Date dateUpdate, int status) {
        this.symptomsID = symptomsID;
        this.name = name;
        this.description = description;
        this.dateCreate = dateCreate;
        this.dateUpdate = dateUpdate;
        this.status = status;
    }

    public String getSymptomsID() {
        return symptomsID;
    }

    public void setSymptomsID(String symptomsID) {
        this.symptomsID = symptomsID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
