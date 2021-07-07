package com.example.diseaseprediction.object;

/**
 * Prediction medicine
 */
public class PredictionMedicine {
    private String predictionID;
    private String medicineID;
    private int status;

    /**
     * Create prediction medicine
     *
     * @param predictionID prediction ID
     * @param medicineID   medicine ID
     * @param status       0: Deleted | 1: Normal
     */
    public PredictionMedicine(String predictionID, String medicineID, int status) {
        this.predictionID = predictionID;
        this.medicineID = medicineID;
        this.status = status;
    }

    /**
     * Constructor
     */
    public PredictionMedicine() {
    }

    /**
     * Get predictionID
     *
     * @return predictionID
     */
    public String getPredictionID() {
        return predictionID;
    }

    /**
     * Set predictionID
     *
     * @param predictionID predictionID
     */
    public void setPredictionID(String predictionID) {
        this.predictionID = predictionID;
    }

    /**
     * Get medicineID
     *
     * @return medicineID
     */
    public String getMedicineID() {
        return medicineID;
    }

    /**
     * Set medicineID
     *
     * @param medicineID medicineID
     */
    public void setMedicineID(String medicineID) {
        this.medicineID = medicineID;
    }

    /**
     * Get status 0: Deleted | 1: Normal
     *
     * @return status
     */
    public int getStatus() {
        return status;
    }

    /**
     * Set status 0: Deleted | 1: Normal
     *
     * @param status status
     */
    public void setStatus(int status) {
        this.status = status;
    }
}
