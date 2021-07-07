package com.example.diseaseprediction.object;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class SymptomTest {
    Symptom getSymptomTest = new Symptom("symptomsIDTest", "nameTest",
            "descriptionTest", new Date(), new Date(), 1);

    @Test
    public void getSymptomsID() {
        assertEquals("symptomsIDTest", getSymptomTest.getSymptomsID());
    }

    @Test
    public void setSymptomsID() {
        Symptom setSymptomTest = new Symptom("symptomsIDTest", "nameTest",
                "descriptionTest", new Date(), new Date(), 1);
        setSymptomTest.setSymptomsID("symptomsIDTest2");
        assertEquals("symptomsIDTest2", setSymptomTest.getSymptomsID());
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
        assertEquals(new Date(), getSymptomTest.getDateCreate());
    }

    @Test
    public void setDateCreate() {
        Symptom setSymptomTest = new Symptom("symptomsIDTest", "nameTest",
                "descriptionTest", new Date(), new Date(), 1);
        setSymptomTest.setDateCreate(new Date());
        assertEquals(new Date(), setSymptomTest.getDateCreate());
    }

    @Test
    public void getDateUpdate() {
        assertEquals(new Date(), getSymptomTest.getDateUpdate());
    }

    @Test
    public void setDateUpdate() {
        Symptom setSymptomTest = new Symptom("symptomsIDTest", "nameTest",
                "descriptionTest", new Date(), new Date(), 1);
        setSymptomTest.setDateUpdate(new Date());
        assertEquals(new Date(), setSymptomTest.getDateUpdate());
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