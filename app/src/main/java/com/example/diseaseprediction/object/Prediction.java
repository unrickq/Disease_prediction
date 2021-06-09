package com.example.diseaseprediction.object;

import java.util.Date;

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

    public Prediction() {
    }

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

    public String getPredictionID() {
        return predictionID;
    }

    public void setPredictionID(String predictionID) {
        this.predictionID = predictionID;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getDiseaseID() {
        return diseaseID;
    }

    public void setDiseaseID(String diseaseID) {
        this.diseaseID = diseaseID;
    }

    public String getSymptomsID() {
        return symptomsID;
    }

    public void setSymptomsID(String symptomsID) {
        this.symptomsID = symptomsID;
    }

    public String getMedicineID() {
        return medicineID;
    }

    public void setMedicineID(String medicineID) {
        this.medicineID = medicineID;
    }

    public String getAdviseID() {
        return adviseID;
    }

    public void setAdviseID(String adviseID) {
        this.adviseID = adviseID;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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
