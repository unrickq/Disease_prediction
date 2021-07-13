package com.example.diseaseprediction.object;

public class PredictionSymptom {
    private String  predictionID;
    private String symptomID;

    public PredictionSymptom(String predictionID, String symptomID, int status) {
        this.predictionID = predictionID;
        this.symptomID = symptomID;
        this.status = status;
    }

    public String getPredictionID() {
        return predictionID;
    }

    public void setPredictionID(String predictionID) {
        this.predictionID = predictionID;
    }

    public String getSymptomID() {
        return symptomID;
    }

    public void setSymptomID(String symptomID) {
        this.symptomID = symptomID;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    private int status;

}
