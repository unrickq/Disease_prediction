package com.example.diseaseprediction.object;

import java.util.Date;

/**
 * Symptom
 */
public class Symptom {
    private String symptomsID;
    private String name;
    private String description;
    private Date dateCreate;
    private Date dateUpdate;
    private int status;

    /**
     * Symptom constructor
     */
    public Symptom() {
    }

    /**
     * Create new symptom
     * @param symptomsID    Symptom ID
     * @param name          Name of symptom
     * @param description   Description of symptom
     * @param dateCreate    Date create
     * @param dateUpdate    Date update
     * @param status        0: Deleted | 1: Normal
     */
    public Symptom(String symptomsID, String name, String description, Date dateCreate, Date dateUpdate, int status) {
        this.symptomsID = symptomsID;
        this.name = name;
        this.description = description;
        this.dateCreate = dateCreate;
        this.dateUpdate = dateUpdate;
        this.status = status;
    }

    /**
     * Get Symptom ID
     * @return Symptom ID
     */
    public String getSymptomsID() {
        return symptomsID;
    }

    /**
     * Set Symptom ID
     * @param symptomsID Symptom ID
     */
    public void setSymptomsID(String symptomsID) {
        this.symptomsID = symptomsID;
    }

    /**
     * Get Name
     * @return Name
     */
    public String getName() {
        return name;
    }

    /**
     * Set Name
     * @param name Name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get Description
     * @return Description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set Description
     * @param description Description
     */
    public void setDescription(String description) {
        this.description = description;
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
     * Set Date update
     * @param dateUpdate
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
     *
     * @param status 0: Deleted | 1: Normal
     */
    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return this.getName();
    }
}
