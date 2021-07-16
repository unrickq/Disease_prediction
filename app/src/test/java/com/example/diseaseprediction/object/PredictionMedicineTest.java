package com.example.diseaseprediction.object;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PredictionMedicineTest {

    PredictionMedicine getPredictionMedicineTest = new PredictionMedicine("predictionID",
        "medicineID", "once a day", 1);

    @Test
    public void getPredictionIDTest() {
        assertEquals("predictionID", getPredictionMedicineTest.getPredictionID());
    }

    @Test
    public void setPredictionIDTest() {
        PredictionMedicine setPredictionMedicineTest = new PredictionMedicine("predictionID",
            "medicineID", "once a day", 1);
        setPredictionMedicineTest.setPredictionID("predictionID");
        assertEquals("predictionID", setPredictionMedicineTest.getPredictionID());
    }

    @Test
    public void getMedicineID() {
        assertEquals("medicineIDTest", getPredictionMedicineTest.getMedicineID());
    }

    @Test
    public void setMedicineID() {
        PredictionMedicine setPredictionMedicineTest = new PredictionMedicine("predictionID",
            "medicineID", "once a day", 1);
        setPredictionMedicineTest.setMedicineID("medicineIDTest2");
        assertEquals("medicineIDTest2", setPredictionMedicineTest.getMedicineID());
    }

    @Test
    public void getDosage() {
        assertEquals("once a day", getPredictionMedicineTest.getDosage());
    }

    @Test
    public void setDosage() {
        PredictionMedicine setPredictionMedicineTest = new PredictionMedicine("predictionID",
            "medicineID", "once a day", 1);
        setPredictionMedicineTest.setDosage("twice a day");
        assertEquals("twice a day", setPredictionMedicineTest.getDosage());
    }

    @Test
    public void getStatus() {
        assertEquals(1, getPredictionMedicineTest.getStatus());
    }

    @Test
    public void setStatus() {
        PredictionMedicine setPredictionMedicineTest = new PredictionMedicine("predictionID",
            "medicineID", "once a day", 1);
        setPredictionMedicineTest.setStatus(0);
        assertEquals(0, setPredictionMedicineTest.getStatus());
    }
}
