package com.example.diseaseprediction.object;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class DoctorSpecializationTest {
    Date d1 = new Date(2000, 11, 21);
    Date d2 = new Date(1999, 2, 12);
    DoctorSpecialization getDoctorSpecializationTest = new DoctorSpecialization("specializationIDTest"
            , "nameTest", d1, d1, 1);

    @Test
    public void getSpecializationID() {
        assertEquals("specializationIDTest", getDoctorSpecializationTest.getSpecializationID());
    }

    @Test
    public void setSpecializationID() {
        DoctorSpecialization setDoctorSpecializationTest = new DoctorSpecialization("specializationIDTest"
                , "nameTest", new Date(), new Date(), 1);
        setDoctorSpecializationTest.setSpecializationID("specializationIDTest2");
        assertEquals("specializationIDTest2", setDoctorSpecializationTest.getSpecializationID());
    }

    @Test
    public void testGetName() {
        assertEquals("nameTest", getDoctorSpecializationTest.getName());
    }

    @Test
    public void testSetName() {
        DoctorSpecialization setDoctorSpecializationTest = new DoctorSpecialization("specializationIDTest"
                , "nameTest", new Date(), new Date(), 1);
        setDoctorSpecializationTest.setName("nameTest2");
        assertEquals("nameTest2", setDoctorSpecializationTest.getName());
    }

    @Test
    public void getStatus() {
        assertEquals(1, getDoctorSpecializationTest.getStatus());
    }

    @Test
    public void getDateCreate() {
        assertEquals(d1, getDoctorSpecializationTest.getDateCreate());
    }

    @Test
    public void setDateCreate() {
        DoctorSpecialization setDoctorSpecializationTest = new DoctorSpecialization("specializationIDTest"
                , "nameTest", new Date(), new Date(), 1);
        setDoctorSpecializationTest.setDateCreate(d2);
        assertEquals(d2, setDoctorSpecializationTest.getDateCreate());
    }

    @Test
    public void getDateUpdate() {
        assertEquals(d1, getDoctorSpecializationTest.getDateUpdate());
    }

    @Test
    public void setDateUpdate() {
        DoctorSpecialization setDoctorSpecializationTest = new DoctorSpecialization("specializationIDTest"
                , "nameTest", new Date(), new Date(), 1);
        setDoctorSpecializationTest.setDateUpdate(d2);
        assertEquals(d2, setDoctorSpecializationTest.getDateUpdate());
    }

    @Test
    public void setStatus() {
        DoctorSpecialization setDoctorSpecializationTest = new DoctorSpecialization("specializationIDTest"
                , "nameTest", new Date(), new Date(), 1);
        setDoctorSpecializationTest.setStatus(0);
        assertEquals(0, setDoctorSpecializationTest.getStatus());
    }

    @Test
    public void testToString() {
        assertEquals("nameTest", getDoctorSpecializationTest.toString());
    }
}