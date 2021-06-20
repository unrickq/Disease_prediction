package com.example.diseaseprediction.object;

/**
 * Doctor information
 */
public class DoctorInfo {
    private String accountID;
    private String specializationID;
    private String shortDescription;
    private double experience;
    private int status;

    /**
     * Doctor information constructor
     */
    public DoctorInfo() {
    }

    /**
     * Create doctor information
     * @param accountID         Account ID
     * @param specializationID  Specialization ID
     * @param shortDescription  shortDescription
     * @param experience        Experience
     * @param status            0: Deleted | 1: Normal
     */
    public DoctorInfo(String accountID, String specializationID, String shortDescription, double experience, int status) {
        this.accountID = accountID;
        this.specializationID = specializationID;
        this.shortDescription = shortDescription;
        this.experience = experience;
        this.status = status;
    }

    /**
     * Get Account ID
     * @return Account ID
     */
    public String getAccountID() {
        return accountID;
    }

    /**
     * Set Account ID
     * @param accountID Account ID
     */
    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    /**
     * Get Specialization ID
     * @return Specialization ID
     */
    public String getSpecializationID() {
        return specializationID;
    }

    /**
     * Set Specialization ID
     * @param specializationID Specialization ID
     */
    public void setSpecializationID(String specializationID) {
        this.specializationID = specializationID;
    }

    /**
     * Get short description
     * @return Short description
     */
    public String getShortDescription() {
        return shortDescription;
    }

    /**
     * Set short description
     * @param shortDescription short description
     */
    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    /**
     * Get Experience
     * @return Experience
     */
    public double getExperience() {
        return experience;
    }

    /**
     * Set Experience
     * @param experience Experience
     */
    public void setExperience(double experience) {
        this.experience = experience;
    }

    /**
     * Get status
     * @return 0: Deleted | 1: Normal
     */
    public int getStatus() {
        return status;
    }

    /**
     * Set status. 0: Deleted | 1: Normal
     * @param status 0: Deleted | 1: Normal
     */
    public void setStatus(int status) {
        this.status = status;
    }
}
