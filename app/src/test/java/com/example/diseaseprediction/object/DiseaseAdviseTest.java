package com.example.diseaseprediction.object;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DiseaseAdviseTest {
    DiseaseAdvise getDiseaseAdviseTest = new DiseaseAdvise("diseaseIDTest",
            "adviseIDTest", 1);

    @Test
    public void getDiseaseID() {
        assertEquals("diseaseIDTest", getDiseaseAdviseTest.getDiseaseID());
    }

    @Test
    public void setDiseaseID() {
        DiseaseAdvise setDiseaseAdviseTest = new DiseaseAdvise("diseaseIDTest",
                "adviseIDTest", 1);
        setDiseaseAdviseTest.setDiseaseID("diseaseIDTest2");
        assertEquals("diseaseIDTest2", setDiseaseAdviseTest.getDiseaseID());
    }

    @Test
    public void getAdviseID() {
        assertEquals("adviseIDTest", getDiseaseAdviseTest.getAdviseID());
    }

    @Test
    public void setAdviseID() {
        DiseaseAdvise setDiseaseAdviseTest = new DiseaseAdvise("diseaseIDTest",
                "adviseIDTest", 1);
        setDiseaseAdviseTest.setAdviseID("adviseIDTest2");
        assertEquals("adviseIDTest2", setDiseaseAdviseTest.getAdviseID());
    }

    @Test
    public void getStatus() {
        DiseaseAdvise setDiseaseAdviseTest = new DiseaseAdvise("diseaseIDTest",
                "adviseIDTest", 1);
        setDiseaseAdviseTest.setStatus(0);
        assertEquals(0, setDiseaseAdviseTest.getStatus());
    }

    @Test
    public void setStatus() {
        assertEquals(1, getDiseaseAdviseTest.getStatus());
    }
}