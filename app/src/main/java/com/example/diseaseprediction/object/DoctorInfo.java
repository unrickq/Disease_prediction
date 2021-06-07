package com.example.diseaseprediction.object;

public class DoctorInfo {
    private String accountID;
    private String specialist;
    private String academicLevel;
    private String certificate;
    private double experience;
    private int status;

    public DoctorInfo() {
    }

    public DoctorInfo(String accountID, String specialist, String academicLevel, String certificate, double experience, int status) {
        this.accountID = accountID;
        this.specialist = specialist;
        this.academicLevel = academicLevel;
        this.certificate = certificate;
        this.experience = experience;
        this.status = status;
    }

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public String getSpecialist() {
        return specialist;
    }

    public void setSpecialist(String specialist) {
        this.specialist = specialist;
    }

    public String getAcademicLevel() {
        return academicLevel;
    }

    public void setAcademicLevel(String academicLevel) {
        this.academicLevel = academicLevel;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public double getExperience() {
        return experience;
    }

    public void setExperience(double experience) {
        this.experience = experience;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
