package com.example.diseaseprediction.object;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class SpecializationTest {
    Date d1 = new Date(2000, 11, 21);
    Date d2 = new Date(1999, 2, 12);
    Specialization getSpecializationTest = new Specialization("specializationIDTest"
            , "nameTest", d1, d1, 1);

    @Test
    public void getSpecializationID() {
        assertEquals("specializationIDTest", getSpecializationTest.getSpecializationID());
    }

    @Test
    public void setSpecializationID() {
        Specialization setSpecializationTest = new Specialization("specializationIDTest"
                , "nameTest", new Date(), new Date(), 1);
        setSpecializationTest.setSpecializationID("specializationIDTest2");
        assertEquals("specializationIDTest2", setSpecializationTest.getSpecializationID());
    }

    @Test
    public void testGetName() {
        assertEquals("nameTest", getSpecializationTest.getName());
    }

    @Test
    public void testSetName() {
        Specialization setSpecializationTest = new Specialization("specializationIDTest"
                , "nameTest", new Date(), new Date(), 1);
        setSpecializationTest.setName("nameTest2");
        assertEquals("nameTest2", setSpecializationTest.getName());
    }

    @Test
    public void getStatus() {
        assertEquals(1, getSpecializationTest.getStatus());
    }

    @Test
    public void getDateCreate() {
        assertEquals(d1, getSpecializationTest.getDateCreate());
    }

    @Test
    public void setDateCreate() {
        Specialization setSpecializationTest = new Specialization("specializationIDTest"
                , "nameTest", new Date(), new Date(), 1);
        setSpecializationTest.setDateCreate(d2);
        assertEquals(d2, setSpecializationTest.getDateCreate());
    }

    @Test
    public void getDateUpdate() {
        assertEquals(d1, getSpecializationTest.getDateUpdate());
    }

    @Test
    public void setDateUpdate() {
        Specialization setSpecializationTest = new Specialization("specializationIDTest"
                , "nameTest", new Date(), new Date(), 1);
        setSpecializationTest.setDateUpdate(d2);
        assertEquals(d2, setSpecializationTest.getDateUpdate());
    }

    @Test
    public void setStatus() {
        Specialization setSpecializationTest = new Specialization("specializationIDTest"
                , "nameTest", new Date(), new Date(), 1);
        setSpecializationTest.setStatus(0);
        assertEquals(0, setSpecializationTest.getStatus());
    }

    @Test
    public void testToString() {
        assertEquals("nameTest", getSpecializationTest.toString());
    }
}