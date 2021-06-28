package com.example.diseaseprediction.object;

public class RecommendSymptom {
    private String messageID;
    private String symptomsID;
    private int status;

    /**
     * Create recommend symptom
     *
     * @param messageID  Message ID
     * @param symptomsID Symptom ID
     * @param status     Status (0: Unchecked | 1: Checked)
     */
    public RecommendSymptom(String messageID, String symptomsID, int status) {
        this.messageID = messageID;
        this.symptomsID = symptomsID;
        this.status = status;
    }

    /**
     * Constructor
     */
    public RecommendSymptom() {
    }

    /**
     * Get Message ID
     *
     * @return Message ID
     */
    public String getMessageID() {
        return messageID;
    }

    /**
     * Set Message ID
     *
     * @param messageID Message ID
     */
    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    /**
     * Get Symptom ID
     *
     * @return Symptom ID
     */
    public String getSymptomsID() {
        return symptomsID;
    }

    /**
     * Set Symptom ID
     *
     * @param symptomsID Symptom ID
     */
    public void setSymptomsID(String symptomsID) {
        this.symptomsID = symptomsID;
    }

    /**
     * Get Status (0: Unchecked | 1: Checked)
     *
     * @return Status
     */
    public int getStatus() {
        return status;
    }

    /**
     * Set Status (0: Unchecked | 1: Checked)
     *
     * @param status Status (0: Unchecked | 1: Checked)
     */
    public void setStatus(int status) {
        this.status = status;
    }
}
