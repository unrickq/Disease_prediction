package com.example.diseaseprediction.object;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class DoctorInfoTest {
    Date d1 = new Date(2000, 11, 21);
    Date d2 = new Date(1999, 2, 12);
    DoctorInfo getDoctorInfoTest = new DoctorInfo("accountIDTest", "specializationIDTest",
            "shortDescriptionTest", 1.5, d1,
            d1, 1);

    @Test
    public void getAccountID() {
        assertEquals("accountIDTest", getDoctorInfoTest.getAccountID());
    }

    @Test
    public void setAccountID() {
        DoctorInfo setDoctorInfoTest = new DoctorInfo("accountIDTest", "specializationIDTest",
                "shortDescriptionTest", 1, new Date(),
                new Date(), 1);
        setDoctorInfoTest.setAccountID("accountIDTest2");
        assertEquals("accountIDTest2", setDoctorInfoTest.getAccountID());
    }

    @Test
    public void getSpecializationID() {
        assertEquals("specializationIDTest", getDoctorInfoTest.getSpecializationID());
    }

    @Test
    public void setSpecializationID() {
        DoctorInfo setDoctorInfoTest = new DoctorInfo("accountIDTest", "specializationIDTest",
                "shortDescriptionTest", 1, new Date(),
                new Date(), 1);
        setDoctorInfoTest.setSpecializationID("specializationIDTest2");
        assertEquals("specializationIDTest2", setDoctorInfoTest.getSpecializationID());
    }

    @Test
    public void getShortDescription() {
        assertEquals("shortDescriptionTest", getDoctorInfoTest.getShortDescription());
    }

    @Test
    public void setShortDescription() {
        DoctorInfo setDoctorInfoTest = new DoctorInfo("accountIDTest", "specializationIDTest",
                "shortDescriptionTest", 1, new Date(),
                new Date(), 1);
        setDoctorInfoTest.setShortDescription("shortDescriptionTest2");
        assertEquals("shortDescriptionTest2", setDoctorInfoTest.getShortDescription());
    }

    @Test
    public void getExperience() {
        assertEquals(1, getDoctorInfoTest.getExperience(), 0.5);
    }

    @Test
    public void setExperience() {
        DoctorInfo setDoctorInfoTest = new DoctorInfo("accountIDTest", "specializationIDTest",
                "shortDescriptionTest", 1, new Date(),
                new Date(), 1);
        setDoctorInfoTest.setExperience(2.2);
        assertEquals(2.2, setDoctorInfoTest.getExperience(), 0.2);
    }

    @Test
    public void getDateCreate() {
        assertEquals(d1, getDoctorInfoTest.getDateCreate());
    }

    @Test
    public void setDateCreate() {
        DoctorInfo setDoctorInfoTest = new DoctorInfo("accountIDTest", "specializationIDTest",
                "shortDescriptionTest", 1, new Date(),
                new Date(), 1);
        setDoctorInfoTest.setDateCreate(d2);
        assertEquals(d2, setDoctorInfoTest.getDateCreate());
    }

    @Test
    public void getDateUpdate() {
        assertEquals(d1, getDoctorInfoTest.getDateUpdate());
    }

    @Test
    public void setDateUpdate() {
        DoctorInfo setDoctorInfoTest = new DoctorInfo("accountIDTest", "specializationIDTest",
                "shortDescriptionTest", 1, new Date(),
                new Date(), 1);
        setDoctorInfoTest.setDateUpdate(d2);
        assertEquals(d2, setDoctorInfoTest.getDateUpdate());
    }

    @Test
    public void getStatus() {
        assertEquals(1, getDoctorInfoTest.getStatus());
    }

    @Test
    public void setStatus() {
        DoctorInfo setDoctorInfoTest = new DoctorInfo("accountIDTest", "specializationIDTest",
                "shortDescriptionTest", 1, new Date(),
                new Date(), 1);
        setDoctorInfoTest.setStatus(0);
        assertEquals(0, setDoctorInfoTest.getStatus());
    }
}