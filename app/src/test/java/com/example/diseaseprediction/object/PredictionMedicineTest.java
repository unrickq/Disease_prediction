package com.example.diseaseprediction.object;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PredictionMedicineTest {
    PredictionMedicine getPredictionMedicineTest = new PredictionMedicine("predictionIDTest",
            "medicineIDTest", 1);

    @Test
    public void getPredictionID() {
        assertEquals("predictionIDTest", getPredictionMedicineTest.getPredictionID());
    }

    @Test
    public void setPredictionID() {
        PredictionMedicine setPredictionMedicineTest = new PredictionMedicine("predictionIDTest",
                "medicineIDTest", 1);
        setPredictionMedicineTest.setPredictionID("predictionIDTest2");
        assertEquals("predictionIDTest2", setPredictionMedicineTest.getPredictionID());
    }

    @Test
    public void getMedicineID() {
        assertEquals("medicineIDTest", getPredictionMedicineTest.getMedicineID());
    }

    @Test
    public void setMedicineID() {
        PredictionMedicine setPredictionMedicineTest = new PredictionMedicine("predictionIDTest",
                "medicineIDTest", 1);
        setPredictionMedicineTest.setMedicineID("medicineIDTest2");
        assertEquals("medicineIDTest2", setPredictionMedicineTest.getMedicineID());
    }

    @Test
    public void getStatus() {
        assertEquals(1, getPredictionMedicineTest.getStatus());
    }

    @Test
    public void setStatus() {
        PredictionMedicine setPredictionMedicineTest = new PredictionMedicine("predictionIDTest",
                "medicineIDTest", 1);
        setPredictionMedicineTest.setStatus(0);
        assertEquals(0, setPredictionMedicineTest.getStatus());
    }
}