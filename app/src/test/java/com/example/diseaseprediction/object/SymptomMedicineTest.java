package com.example.diseaseprediction.object;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SymptomMedicineTest {
    SymptomMedicine getSymptomMedicineTest = new SymptomMedicine("symptomIDTest",
            "medicineIDTest", "dosageTest", 1);

    @Test
    public void getSymptomID() {
        assertEquals("symptomIDTest", getSymptomMedicineTest.getSymptomID());
    }

    @Test
    public void setSymptomID() {
        SymptomMedicine setSymptomMedicineTest = new SymptomMedicine("symptomIDTest",
                "medicineIDTest", "dosageTest", 1);
        setSymptomMedicineTest.setSymptomID("symptomIDTest2");
        assertEquals("symptomIDTest2", setSymptomMedicineTest.getSymptomID());
    }

    @Test
    public void getMedicineID() {
        assertEquals("medicineIDTest", getSymptomMedicineTest.getMedicineID());
    }

    @Test
    public void setMedicineID() {
        SymptomMedicine setSymptomMedicineTest = new SymptomMedicine("symptomIDTest",
                "medicineIDTest", "dosageTest", 1);
        setSymptomMedicineTest.setMedicineID("medicineIDTest2");
        assertEquals("medicineIDTest2", setSymptomMedicineTest.getMedicineID());
    }

    @Test
    public void getDosage() {
        assertEquals("dosageTest", getSymptomMedicineTest.getDosage());
    }

    @Test
    public void setDosage() {
        SymptomMedicine setSymptomMedicineTest = new SymptomMedicine("symptomIDTest",
                "medicineIDTest", "dosageTest", 1);
        setSymptomMedicineTest.setDosage("dosageTest2");
        assertEquals("dosageTest2", setSymptomMedicineTest.getDosage());
    }

    @Test
    public void getStatus() {
        assertEquals(1, getSymptomMedicineTest.getStatus());
    }

    @Test
    public void setStatus() {
        SymptomMedicine setSymptomMedicineTest = new SymptomMedicine("symptomIDTest",
                "medicineIDTest", "dosageTest", 1);
        setSymptomMedicineTest.setStatus(0);
        assertEquals(0, setSymptomMedicineTest.getStatus());
    }
}