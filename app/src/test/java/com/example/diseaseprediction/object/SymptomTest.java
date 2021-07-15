package com.example.diseaseprediction.object;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class SymptomTest {
    Date d1 = new Date(2000, 11, 21);
    Date d2 = new Date(1999, 2, 12);
    Symptom getSymptomTest = new Symptom("symptomsIDTest", "nameTest",
            "descriptionTest", d1, d1, 1);

    @Test
    public void getSymptomsID() {
        assertEquals("symptomsIDTest", getSymptomTest.getSymptomID());
    }

    @Test
    public void setSymptomsID() {
        Symptom setSymptomTest = new Symptom("symptomsIDTest", "nameTest",
                "descriptionTest", new Date(), new Date(), 1);
        setSymptomTest.setSymptomID("symptomsIDTest2");
        assertEquals("symptomsIDTest2", setSymptomTest.getSymptomID());
    }

    @Test
    public void testGetName() {
        assertEquals("nameTest", getSymptomTest.getName());
    }

    @Test
    public void testSetName() {
        Symptom setSymptomTest = new Symptom("symptomsIDTest", "nameTest",
                "descriptionTest", new Date(), new Date(), 1);
        setSymptomTest.setName("nameTest2");
        assertEquals("nameTest2", setSymptomTest.getName());
    }

    @Test
    public void getDescription() {
        assertEquals("descriptionTest", getSymptomTest.getDescription());
    }

    @Test
    public void setDescription() {
        Symptom setSymptomTest = new Symptom("symptomsIDTest", "nameTest",
                "descriptionTest", new Date(), new Date(), 1);
        setSymptomTest.setDescription("descriptionTest2");
        assertEquals("descriptionTest2", setSymptomTest.getDescription());
    }

    @Test
    public void getDateCreate() {
        assertEquals(d1, getSymptomTest.getDateCreate());
    }

    @Test
    public void setDateCreate() {
        Symptom setSymptomTest = new Symptom("symptomsIDTest", "nameTest",
                "descriptionTest", new Date(), new Date(), 1);
        setSymptomTest.setDateCreate(d2);
        assertEquals(d2, setSymptomTest.getDateCreate());
    }

    @Test
    public void getDateUpdate() {
        assertEquals(d1, getSymptomTest.getDateUpdate());
    }

    @Test
    public void setDateUpdate() {
        Symptom setSymptomTest = new Symptom("symptomsIDTest", "nameTest",
                "descriptionTest", new Date(), new Date(), 1);
        setSymptomTest.setDateUpdate(d2);
        assertEquals(d2, setSymptomTest.getDateUpdate());
    }

    @Test
    public void getStatus() {
        assertEquals(1, getSymptomTest.getStatus());
    }

    @Test
    public void setStatus() {
        Symptom setSymptomTest = new Symptom("symptomsIDTest", "nameTest",
                "descriptionTest", new Date(), new Date(), 1);
        setSymptomTest.setStatus(0);
        assertEquals(0, setSymptomTest.getStatus());
    }

    @Test
    public void testToString() {
        assertEquals("nameTest", getSymptomTest.getName());
    }
}