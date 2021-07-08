package com.example.diseaseprediction.object;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class DiseaseTest {
    Date d1 = new Date(2000, 11, 21);
    Date d2 = new Date(1999, 2, 12);
    Disease getDiseaseTest = new Disease("diseaseIDTest", "specializationIDTest",
            "nameTest", "descriptionTest", d1, d1, 1);

    @Test
    public void getDiseaseID() {
        assertEquals("diseaseIDTest", getDiseaseTest.getDiseaseID());
    }

    @Test
    public void setDiseaseID() {
        Disease setDiseaseTest = new Disease("diseaseIDTest", "specializationIDTest",
                "nameTest", "descriptionTest", new Date(), new Date(), 1);
        setDiseaseTest.setDiseaseID("diseaseIDTest2");
        assertEquals("diseaseIDTest2", setDiseaseTest.getDiseaseID());
    }

    @Test
    public void getSpecializationID() {
        assertEquals("specializationIDTest", getDiseaseTest.getSpecializationID());
    }

    @Test
    public void setSpecializationID() {
        Disease setDiseaseTest = new Disease("diseaseIDTest", "specializationIDTest",
                "nameTest", "descriptionTest", new Date(), new Date(), 1);
        setDiseaseTest.setSpecializationID("specializationIDTest2");
        assertEquals("specializationIDTest2", setDiseaseTest.getSpecializationID());
    }

    @Test
    public void testGetName() {
        assertEquals("nameTest", getDiseaseTest.getName());
    }

    @Test
    public void testSetName() {
        Disease setDiseaseTest = new Disease("diseaseIDTest", "specializationIDTest",
                "nameTest", "descriptionTest", new Date(), new Date(), 1);
        setDiseaseTest.setName("nameTest2");
        assertEquals("nameTest2", setDiseaseTest.getName());
    }

    @Test
    public void getDescription() {
        assertEquals("descriptionTest", getDiseaseTest.getDescription());
    }

    @Test
    public void setDescription() {
        Disease setDiseaseTest = new Disease("diseaseIDTest", "specializationIDTest",
                "nameTest", "descriptionTest", new Date(), new Date(), 1);
        setDiseaseTest.setDescription("descriptionTest2");
        assertEquals("descriptionTest2", setDiseaseTest.getDescription());
    }

    @Test
    public void getDateCreate() {
        assertEquals(d1, getDiseaseTest.getDateCreate());
    }

    @Test
    public void setDateCreate() {
        Disease setDiseaseTest = new Disease("diseaseIDTest", "specializationIDTest",
                "nameTest", "descriptionTest", new Date(), new Date(), 1);
        setDiseaseTest.setDateCreate(d2);
        assertEquals(d2, setDiseaseTest.getDateCreate());
    }

    @Test
    public void getDateUpdate() {
        assertEquals(d1, getDiseaseTest.getDateUpdate());
    }

    @Test
    public void setDateUpdate() {
        Disease setDiseaseTest = new Disease("diseaseIDTest", "specializationIDTest",
                "nameTest", "descriptionTest", new Date(), new Date(), 1);
        setDiseaseTest.setDateUpdate(d2);
        assertEquals(d2, setDiseaseTest.getDateUpdate());
    }

    @Test
    public void getStatus() {
        assertEquals(1, getDiseaseTest.getStatus());
    }

    @Test
    public void setStatus() {
        Disease setDiseaseTest = new Disease("diseaseIDTest", "specializationIDTest",
                "nameTest", "descriptionTest", new Date(), new Date(), 1);
        setDiseaseTest.setStatus(0);
        assertEquals(0, setDiseaseTest.getStatus());
    }

    @Test
    public void testToString() {
        assertEquals("nameTest", getDiseaseTest.toString());
    }
}