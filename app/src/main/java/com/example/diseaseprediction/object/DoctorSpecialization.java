package com.example.diseaseprediction.object;

public class DoctorSpecialization {
    private String specializationID;
    private String name;
    private int status;

    /**
     * Doctor specialization constructor
     */
    public DoctorSpecialization() {
    }

    /**
     * Create doctor specialization
     * @param specializationID  Specialization ID
     * @param name              Name of specialization
     * @param status            0: Deleted | 1: Normal
     */
    public DoctorSpecialization(String specializationID, String name, int status) {
        this.specializationID = specializationID;
        this.name = name;
        this.status = status;
    }

    /**
     * Get specialization ID
     * @return specialization ID
     */
    public String getSpecializationID() {
        return specializationID;
    }

    /**
     * Set specialization ID
     * @param specializationID specialization ID
     */
    public void setSpecializationID(String specializationID) {
        this.specializationID = specializationID;
    }

    /**
     * Get name
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Set name
     * @param name name
     */
    public void setName(String name) {
        this.name = name;
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

    /**
     * Set string return is name
     * Return name of object in spinner
     * @return name
     */
    @Override
    public String toString() {
        return name;
    }
}
