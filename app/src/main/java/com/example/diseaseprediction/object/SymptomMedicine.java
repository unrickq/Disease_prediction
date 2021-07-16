package com.example.diseaseprediction.object;

/**
 * Prediction medicine
 */
public class SymptomMedicine {
    private String symptomID;
    private String medicineID;
    private String dosage;
    private int status;

    /**
     * Create prediction medicine
     *
     * @param symptomID  prediction ID
     * @param medicineID medicine ID
     * @param status     0: Deleted | 1: Normal
     */
    public SymptomMedicine(String symptomID, String medicineID, String dosage, int status) {
        this.symptomID = symptomID;
        this.medicineID = medicineID;
        this.dosage = dosage;
        this.status = status;
    }

    /**
     * Constructor
     */
    public SymptomMedicine() {
    }

    /**
     * Get symptomID
     *
     * @return symptomID
     */
    public String getSymptomID() {
        return symptomID;
    }

    /**
     * Set symptomID
     *
     * @param symptomID symptomID
     */
    public void setSymptomID(String symptomID) {
        this.symptomID = symptomID;
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
