package com.example.diseaseprediction.object;

public class PredictionMedicine {
    private String predictionID;
    private String medicineID;
    private int status;

    public PredictionMedicine(String predictionID, String medicineID, int status) {
        this.predictionID = predictionID;
        this.medicineID = medicineID;
        this.status = status;
    }

    public PredictionMedicine() {
    }

    public String getPredictionID() {
        return predictionID;
    }

    public void setPredictionID(String predictionID) {
        this.predictionID = predictionID;
    }

    public String getMedicineID() {
        return medicineID;
    }

    public void setMedicineID(String medicineID) {
        this.medicineID = medicineID;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
