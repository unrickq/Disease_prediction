package com.example.diseaseprediction.object;

import java.util.Date;

public class Account {
    private String accountId;
    private int typeID;
    private String phone ;
    private String name ;
    private int gender;
    private String address ;
    private String email ;
    private String image ;
    private Date dateCreate;
    private Date dateUpdate;
    private int status;

    public Account() {
    }

    public Account(String accountId, int typeID, String phone, String name, int gender, String address, String email, String image, Date dateCreate, Date dateUpdate, int status) {
        this.accountId = accountId;
        this.typeID = typeID;
        this.phone = phone;
        this.name = name;
        this.gender = gender;
        this.address = address;
        this.email = email;
        this.image = image;
        this.dateCreate = dateCreate;
        this.dateUpdate = dateUpdate;
        this.status = status;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public int getTypeID() {
        return typeID;
    }

    public void setTypeID(int typeID) {
        this.typeID = typeID;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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
