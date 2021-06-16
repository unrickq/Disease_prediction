package com.example.diseaseprediction.object;

import java.util.Date;

/**
 * Prediction
 */
public class Prediction {
    private String predictionID;
    private String patientID;
    private String doctorID;
    private String sessionID;
    private String diseaseID;
    private String symptomsID;
    private String medicineID;
    private String adviseID;
    private String notes;
    private Date dateCreate;
    private Date dateUpdate;
    private int status;

    /**
     * Prediction constructor
     */
    public Prediction() {
    }

    /**
     * Create Prediction
     * @param predictionID  Prediction ID
     * @param patientID     Patient ID
     * @param doctorID      Doctor ID
     * @param sessionID     Session ID
     * @param diseaseID     Disease ID
     * @param symptomsID    Symptoms ID
     * @param medicineID    Medicine ID
     * @param adviseID      Advise ID
     * @param notes         Notes
     * @param dateCreate    Date create
     * @param dateUpdate    Date update
     * @param status        0: Deleted | 1: Normal
     */
    public Prediction(String predictionID, String patientID, String doctorID, String sessionID, String diseaseID, String symptomsID, String medicineID, String adviseID, String notes, Date dateCreate, Date dateUpdate, int status) {
        this.predictionID = predictionID;
        this.patientID = patientID;
        this.doctorID = doctorID;
        this.sessionID = sessionID;
        this.diseaseID = diseaseID;
        this.symptomsID = symptomsID;
        this.medicineID = medicineID;
        this.adviseID = adviseID;
        this.notes = notes;
        this.dateCreate = dateCreate;
        this.dateUpdate = dateUpdate;
        this.status = status;
    }

    /**
     * Get Prediction ID
     * @return Prediction ID
     */
    public String getPredictionID() {
        return predictionID;
    }

    /**
     * Set Prediction ID
     * @param predictionID Prediction ID
     */
    public void setPredictionID(String predictionID) {
        this.predictionID = predictionID;
    }

    /**
     * Get Patient ID
     * @return Patient ID
     */
    public String getPatientID() {
        return patientID;
    }

    /**
     * Set Patient ID
     * @param patientID Patient ID
     */
    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    /**
     * Get Doctor ID
     * @return Doctor ID
     */
    public String getDoctorID() {
        return doctorID;
    }

    /**
     * Set Doctor ID
     * @param doctorID Doctor ID
     */
    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }

    /**
     * Get Session ID
     * @return Session ID
     */
    public String getSessionID() {
        return sessionID;
    }

    /**
     * Set Session ID
     * @param sessionID Session ID
     */
    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    /**
     * Get Disease ID
     * @return Disease ID
     */
    public String getDiseaseID() {
        return diseaseID;
    }

    /**
     * Set Disease ID
     * @param diseaseID Disease ID
     */
    public void setDiseaseID(String diseaseID) {
        this.diseaseID = diseaseID;
    }

    /**
     * Get Symptoms ID
     * @return Symptoms ID
     */
    public String getSymptomsID() {
        return symptomsID;
    }

    /**
     * Set Symptoms ID
     * @param symptomsID Symptoms ID
     */
    public void setSymptomsID(String symptomsID) {
        this.symptomsID = symptomsID;
    }

    /**
     * Get Medicine ID
     * @return Medicine ID
     */
    public String getMedicineID() {
        return medicineID;
    }

    /**
     * Set Medicine ID
     * @param medicineID Medicine ID
     */
    public void setMedicineID(String medicineID) {
        this.medicineID = medicineID;
    }

    /**
     * Get Advise ID
     * @return Advise ID
     */
    public String getAdviseID() {
        return adviseID;
    }

    /**
     * Set Advise ID
     * @param adviseID Advise ID
     */
    public void setAdviseID(String adviseID) {
        this.adviseID = adviseID;
    }

    /**
     * Get Notes
     * @return Notes
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Set Notes
     * @param notes Notes
     */
    public void setNotes(String notes) {
        this.notes = notes;
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
