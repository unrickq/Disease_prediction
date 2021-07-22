package com.example.diseaseprediction.object;

/**
 * Prediction Medicine
 */
public class PredictionMedicine {

    private String predictionID;
    private String medicineID;
    private String dosage;
    private int status;

    /**
     * Create new Prediction Medicine object
     *
     * @param predictionID Prediction ID
     * @param medicineID   Medicine ID
     * @param dosage       dosage
     * @param status       0: Deleted | 1: Normal
     */
    public PredictionMedicine(String predictionID, String medicineID, String dosage, int status) {
        this.predictionID = predictionID;
        this.medicineID = medicineID;
        this.dosage = dosage;
        this.status = status;
    }

    /**
     * Constructor
     */
    public PredictionMedicine() {

    }

    /**
     * Get Prediction ID
     *
     * @return prediction ID
     */
    public String getPredictionID() {
        return predictionID;
    }

    /**
     * Set Prediction ID
     *
     * @param predictionID prediction ID
     */
    public void setPredictionID(String predictionID) {
        this.predictionID = predictionID;
    }

    /**
     * Get Medicine ID
     *
     * @return medicine ID
     */
    public String getMedicineID() {
        return medicineID;
    }

    /**
     * Set medicine ID
     *
     * @param medicineID medicine ID
     */
    public void setMedicineID(String medicineID) {
        this.medicineID = medicineID;
    }

    /**
     * Get dosage
     *
     * @return dosage
     */
    public String getDosage() {
        return dosage;
    }

    /**
     * Set dosage
     *
     * @param dosage dosage
     */
    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    /**
     * Get status. <ul>
     * <li>0: deleted</li>
     * <li>1: normal</li>
     * </ul>
     *
     * @return status
     */
    public int getStatus() {
        return status;
    }

    /**
     * Set status. <ul>
     * <li>0: deleted</li>
     * <li>1: normal</li>
     * </ul>
     *
     * @param status
     */
    public void setStatus(int status) {
        this.status = status;
    }
}
