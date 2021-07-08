package com.example.diseaseprediction.object;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class PredictionTest {
    Date d1 = new Date(2000, 11, 21);
    Date d2 = new Date(1999, 2, 12);
    Prediction getPredictionTest = new Prediction("predictionIDTest", "patientIDTest",
            "doctorIDTest", "sessionIDTest", "doctorSessionIDTest",
            "diseaseIDTest", "notesTest", d1, d1,
            "hiddenSpecializationIDTest", 1);

    @Test
    public void getPredictionID() {
        assertEquals("predictionIDTest", getPredictionTest.getPredictionID());
    }

    @Test
    public void setPredictionID() {
        Prediction setPredictionTest = new Prediction("predictionIDTest", "patientIDTest",
                "doctorIDTest", "sessionIDTest", "doctorSessionIDTest",
                "diseaseIDTest", "notesTest", new Date(), new Date(),
                "hiddenSpecializationIDTest", 1);
        setPredictionTest.setPredictionID("predictionIDTest2");
        assertEquals("predictionIDTest2", setPredictionTest.getPredictionID());
    }

    @Test
    public void getPatientID() {
        assertEquals("patientIDTest", getPredictionTest.getPatientID());
    }

    @Test
    public void setPatientID() {
        Prediction setPredictionTest = new Prediction("predictionIDTest", "patientIDTest",
                "doctorIDTest", "sessionIDTest", "doctorSessionIDTest",
                "diseaseIDTest", "notesTest", new Date(), new Date(),
                "hiddenSpecializationIDTest", 1);
        setPredictionTest.setPatientID("patientIDTest2");
        assertEquals("patientIDTest2", setPredictionTest.getPatientID());
    }

    @Test
    public void getDoctorID() {
        assertEquals("doctorIDTest", getPredictionTest.getDoctorID());
    }

    @Test
    public void setDoctorID() {
        Prediction setPredictionTest = new Prediction("predictionIDTest", "patientIDTest",
                "doctorIDTest", "sessionIDTest", "doctorSessionIDTest",
                "diseaseIDTest", "notesTest", new Date(), new Date(),
                "hiddenSpecializationIDTest", 1);
        setPredictionTest.setDoctorID("doctorIDTest2");
        assertEquals("doctorIDTest2", setPredictionTest.getDoctorID());
    }

    @Test
    public void getSessionID() {
        assertEquals("sessionIDTest", getPredictionTest.getSessionID());
    }

    @Test
    public void setSessionID() {
        Prediction setPredictionTest = new Prediction("predictionIDTest", "patientIDTest",
                "doctorIDTest", "sessionIDTest", "doctorSessionIDTest",
                "diseaseIDTest", "notesTest", new Date(), new Date(),
                "hiddenSpecializationIDTest", 1);
        setPredictionTest.setSessionID("sessionIDTest2");
        assertEquals("sessionIDTest2", setPredictionTest.getSessionID());
    }

    @Test
    public void getDiseaseID() {
        assertEquals("diseaseIDTest", getPredictionTest.getDiseaseID());
    }

    @Test
    public void setDiseaseID() {
        Prediction setPredictionTest = new Prediction("predictionIDTest", "patientIDTest",
                "doctorIDTest", "sessionIDTest", "doctorSessionIDTest",
                "diseaseIDTest", "notesTest", new Date(), new Date(),
                "hiddenSpecializationIDTest", 1);
        setPredictionTest.setDiseaseID("diseaseIDTest2");
        assertEquals("diseaseIDTest2", setPredictionTest.getDiseaseID());
    }

    @Test
    public void getDoctorSessionID() {
        assertEquals("doctorSessionIDTest", getPredictionTest.getDoctorSessionID());
    }

    @Test
    public void setDoctorSessionID() {
        Prediction setPredictionTest = new Prediction("predictionIDTest", "patientIDTest",
                "doctorIDTest", "sessionIDTest", "doctorSessionIDTest",
                "diseaseIDTest", "notesTest", new Date(), new Date(),
                "hiddenSpecializationIDTest", 1);
        setPredictionTest.setDoctorSessionID("doctorSessionIDTest2");
        assertEquals("doctorSessionIDTest2", setPredictionTest.getDoctorSessionID());
    }

    @Test
    public void getNotes() {
        assertEquals("notesTest", getPredictionTest.getNotes());
    }

    @Test
    public void setNotes() {
        Prediction setPredictionTest = new Prediction("predictionIDTest", "patientIDTest",
                "doctorIDTest", "sessionIDTest", "doctorSessionIDTest",
                "diseaseIDTest", "notesTest", new Date(), new Date(),
                "hiddenSpecializationIDTest", 1);
        setPredictionTest.setNotes("notesTest2");
        assertEquals("notesTest2", setPredictionTest.getNotes());
    }

    @Test
    public void getDateCreate() {
        assertEquals(d1, getPredictionTest.getDateCreate());
    }

    @Test
    public void setDateCreate() {
        Prediction setPredictionTest = new Prediction("predictionIDTest", "patientIDTest",
                "doctorIDTest", "sessionIDTest", "doctorSessionIDTest",
                "diseaseIDTest", "notesTest", new Date(), new Date(),
                "hiddenSpecializationIDTest", 1);
        setPredictionTest.setDateCreate(d2);
        assertEquals(d2, setPredictionTest.getDateCreate());
    }

    @Test
    public void getDateUpdate() {
        assertEquals(d1, getPredictionTest.getDateUpdate());
    }

    @Test
    public void setDateUpdate() {
        Prediction setPredictionTest = new Prediction("predictionIDTest", "patientIDTest",
                "doctorIDTest", "sessionIDTest", "doctorSessionIDTest",
                "diseaseIDTest", "notesTest", new Date(), new Date(),
                "hiddenSpecializationIDTest", 1);
        setPredictionTest.setDateUpdate(d2);
        assertEquals(d2, setPredictionTest.getDateUpdate());
    }

    @Test
    public void getHiddenSpecializationID() {
        assertEquals("hiddenSpecializationIDTest", getPredictionTest.getHiddenSpecializationID());
    }

    @Test
    public void setHiddenSpecializationID() {
        Prediction setPredictionTest = new Prediction("predictionIDTest", "patientIDTest",
                "doctorIDTest", "sessionIDTest", "doctorSessionIDTest",
                "diseaseIDTest", "notesTest", new Date(), new Date(),
                "hiddenSpecializationIDTest", 1);
        setPredictionTest.setHiddenSpecializationID("hiddenSpecializationIDTest2");
        assertEquals("hiddenSpecializationIDTest2", setPredictionTest.getHiddenSpecializationID());
    }

    @Test
    public void getStatus() {
        assertEquals(1, getPredictionTest.getStatus());
    }

    @Test
    public void setStatus() {
        Prediction setPredictionTest = new Prediction("predictionIDTest", "patientIDTest",
                "doctorIDTest", "sessionIDTest", "doctorSessionIDTest",
                "diseaseIDTest", "notesTest", new Date(), new Date(),
                "hiddenSpecializationIDTest", 1);
        setPredictionTest.setStatus(0);
        assertEquals(0, setPredictionTest.getStatus());
    }

}