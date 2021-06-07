package com.example.diseaseprediction.object;

import java.util.Date;

public class Medicine {
    private String medicineID;
    private String name;
    private String description;
    private String manufacturer;
    private String content;
    private Date dateCreate;
    private Date dateUpdate;
    private int status;

    public Medicine() {
    }

    public Medicine(String medicineID, String name, String description, String manufacturer, String content, Date dateCreate, Date dateUpdate, int status) {
        this.medicineID = medicineID;
        this.name = name;
        this.description = description;
        this.manufacturer = manufacturer;
        this.content = content;
        this.dateCreate = dateCreate;
        this.dateUpdate = dateUpdate;
        this.status = status;
    }

    public String getMedicineID() {
        return medicineID;
    }

    public void setMedicineID(String medicineID) {
        this.medicineID = medicineID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(Date dateCreate) {
        this.dateCreate = dateCreate;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
