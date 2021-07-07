package com.example.diseaseprediction.object;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class MedicineTest {
    Medicine getMedicineTest = new Medicine("medicineIDTest", "nameTest",
            "descriptionTest", "manufacturerTest",
            "contentTest", new Date(), new Date(), 1);

    @Test
    public void getMedicineID() {
        assertEquals("medicineIDTest", getMedicineTest.getMedicineID());
    }

    @Test
    public void setMedicineID() {
        Medicine setMedicineTest = new Medicine("medicineIDTest", "nameTest",
                "descriptionTest", "manufacturerTest",
                "contentTest", new Date(), new Date(), 1);
        setMedicineTest.setMedicineID("medicineIDTest2");
        assertEquals("medicineIDTest2", setMedicineTest.getMedicineID());
    }

    @Test
    public void testGetName() {
        assertEquals("nameTest", getMedicineTest.getName());
    }

    @Test
    public void testSetName() {
        Medicine setMedicineTest = new Medicine("medicineIDTest", "nameTest",
                "descriptionTest", "manufacturerTest",
                "contentTest", new Date(), new Date(), 1);
        setMedicineTest.setName("nameTest2");
        assertEquals("nameTest2", setMedicineTest.getName());
    }

    @Test
    public void getDescription() {
        assertEquals("descriptionTest", getMedicineTest.getDescription());
    }

    @Test
    public void setDescription() {
        Medicine setMedicineTest = new Medicine("medicineIDTest", "nameTest",
                "descriptionTest", "manufacturerTest",
                "contentTest", new Date(), new Date(), 1);
        setMedicineTest.setDescription("descriptionTest2");
        assertEquals("descriptionTest2", setMedicineTest.getDescription());
    }

    @Test
    public void getManufacturer() {
        assertEquals("manufacturerTest", getMedicineTest.getManufacturer());
    }

    @Test
    public void setManufacturer() {
        Medicine setMedicineTest = new Medicine("medicineIDTest", "nameTest",
                "descriptionTest", "manufacturerTest",
                "contentTest", new Date(), new Date(), 1);
        setMedicineTest.setManufacturer("manufacturerTest2");
        assertEquals("manufacturerTest2", setMedicineTest.getManufacturer());
    }

    @Test
    public void getContent() {
        assertEquals("contentTest", getMedicineTest.getContent());
    }

    @Test
    public void setContent() {
        Medicine setMedicineTest = new Medicine("medicineIDTest", "nameTest",
                "descriptionTest", "manufacturerTest",
                "contentTest", new Date(), new Date(), 1);
        setMedicineTest.setContent("contentTest2");
        assertEquals("contentTest2", setMedicineTest.getContent());
    }

    @Test
    public void getDateCreate() {
        assertEquals(new Date(), getMedicineTest.getDateCreate());
    }

    @Test
    public void setDateCreate() {
        Medicine setMedicineTest = new Medicine("medicineIDTest", "nameTest",
                "descriptionTest", "manufacturerTest",
                "contentTest", new Date(), new Date(), 1);
        setMedicineTest.setDateCreate(new Date());
        assertEquals(new Date(), setMedicineTest.getDateCreate());
    }

    @Test
    public void getDateUpdate() {
        assertEquals(new Date(), getMedicineTest.getDateUpdate());
    }

    @Test
    public void setDateUpdate() {
        Medicine setMedicineTest = new Medicine("medicineIDTest", "nameTest",
                "descriptionTest", "manufacturerTest",
                "contentTest", new Date(), new Date(), 1);
        setMedicineTest.setDateUpdate(new Date());
        assertEquals(new Date(), setMedicineTest.getDateUpdate());
    }

    @Test
    public void getStatus() {
        assertEquals(1, getMedicineTest.getStatus());
    }

    @Test
    public void setStatus() {
        Medicine setMedicineTest = new Medicine("medicineIDTest", "nameTest",
                "descriptionTest", "manufacturerTest",
                "contentTest", new Date(), new Date(), 1);
        setMedicineTest.setStatus(0);
        assertEquals(0, setMedicineTest.getStatus());
    }
}