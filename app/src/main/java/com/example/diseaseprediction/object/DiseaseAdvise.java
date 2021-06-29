package com.example.diseaseprediction.object;

public class DiseaseAdvise {
    private String diseaseID;
    private String adviseID;
    private int status;

    public DiseaseAdvise() {
    }

    public DiseaseAdvise(String diseaseID, String adviseID, int status) {
        this.diseaseID = diseaseID;
        this.adviseID = adviseID;
        this.status = status;
    }

    public String getDiseaseID() {
        return diseaseID;
    }

    public void setDiseaseID(String diseaseID) {
        this.diseaseID = diseaseID;
    }

    public String getAdviseID() {
        return adviseID;
    }

    public void setAdviseID(String adviseID) {
        this.adviseID = adviseID;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
