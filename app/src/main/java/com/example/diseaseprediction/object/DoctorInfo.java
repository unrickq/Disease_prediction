package com.example.diseaseprediction.object;

/**
 * Doctor information
 */
public class DoctorInfo {
    private String accountID;
    private String specialist;
    private String academicLevel;
    private String certificate;
    private double experience;
    private int status;

    /**
     * Doctor information constructor
     */
    public DoctorInfo() {
    }

    /**
     * Create doctor information
     * @param accountID     Account ID
     * @param specialist    Special list
     * @param academicLevel Academic level
     * @param certificate   Certificate
     * @param experience    Experience
     * @param status        0: Deleted | 1: Normal
     */
    public DoctorInfo(String accountID, String specialist, String academicLevel, String certificate, double experience, int status) {
        this.accountID = accountID;
        this.specialist = specialist;
        this.academicLevel = academicLevel;
        this.certificate = certificate;
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
     * Get Special list
     * @return Special list
     */
    public String getSpecialist() {
        return specialist;
    }

    /**
     * Set Special list
     * @param specialist Special list
     */
    public void setSpecialist(String specialist) {
        this.specialist = specialist;
    }

    /**
     * Get Academic level
     * @return Academic level
     */
    public String getAcademicLevel() {
        return academicLevel;
    }

    /**
     * Set Academic level
     * @param academicLevel
     */
    public void setAcademicLevel(String academicLevel) {
        this.academicLevel = academicLevel;
    }

    /**
     * Get Certificate
     * @return Certificate
     */
    public String getCertificate() {
        return certificate;
    }

    /**
     * Set Certificate
     * @param certificate Certificate
     */
    public void setCertificate(String certificate) {
        this.certificate = certificate;
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
     * @param experience
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
