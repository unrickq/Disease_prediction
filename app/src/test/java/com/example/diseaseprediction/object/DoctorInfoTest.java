package com.example.diseaseprediction.object;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class DoctorInfoTest {
    DoctorInfo getDoctorInfoTest = new DoctorInfo("accountIDTest", "specializationIDTest",
            "shortDescriptionTest", 1, new Date(),
            new Date(), 1);

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
        assertEquals(1, getDoctorInfoTest.getExperience());
    }

    @Test
    public void setExperience() {
        DoctorInfo setDoctorInfoTest = new DoctorInfo("accountIDTest", "specializationIDTest",
                "shortDescriptionTest", 1, new Date(),
                new Date(), 1);
        setDoctorInfoTest.setExperience(2);
        assertEquals(2, setDoctorInfoTest.getExperience());
    }

    @Test
    public void getDateCreate() {
        assertEquals(new Date(), getDoctorInfoTest.getDateCreate());
    }

    @Test
    public void setDateCreate() {
        DoctorInfo setDoctorInfoTest = new DoctorInfo("accountIDTest", "specializationIDTest",
                "shortDescriptionTest", 1, new Date(),
                new Date(), 1);
        setDoctorInfoTest.setDateCreate(new Date());
        assertEquals(new Date(), setDoctorInfoTest.getDateCreate());
    }

    @Test
    public void getDateUpdate() {
        assertEquals(new Date(), getDoctorInfoTest.getDateUpdate());
    }

    @Test
    public void setDateUpdate() {
        DoctorInfo setDoctorInfoTest = new DoctorInfo("accountIDTest", "specializationIDTest",
                "shortDescriptionTest", 1, new Date(),
                new Date(), 1);
        setDoctorInfoTest.setDateUpdate(new Date());
        assertEquals(new Date(), setDoctorInfoTest.getDateUpdate());
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