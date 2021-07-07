package com.example.diseaseprediction.object;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class AdviseTest {
    Advise getAdviseTest = new Advise("adviseIDTest", "descriptionTest", new Date()
            , new Date(), 1);

    @Test
    public void getAdviseID() {
        assertEquals("adviseIDTest", getAdviseTest.getAdviseID());
    }

    @Test
    public void setAdviseID() {
        Advise setAdviseTest = new Advise("adviseIDTest", "descriptionTest", new Date()
                , new Date(), 1);
        setAdviseTest.setAdviseID("adviseIDTest2");
        assertEquals("adviseIDTest2", setAdviseTest.getAdviseID());
    }

    @Test
    public void getDescription() {
        assertEquals("descriptionTest", getAdviseTest.getDescription());
    }

    @Test
    public void setDescription() {
        Advise setAdviseTest = new Advise("adviseIDTest", "descriptionTest", new Date()
                , new Date(), 1);
        setAdviseTest.setDescription("descriptionTest2");
        assertEquals("descriptionTest", setAdviseTest.getDescription());
    }

    @Test
    public void getDateCreate() {
        assertEquals(new Date(), getAdviseTest.getDateCreate());
    }

    @Test
    public void setDateCreate() {
        Advise setAdviseTest = new Advise("adviseIDTest", "descriptionTest", new Date()
                , new Date(), 1);
        setAdviseTest.setDateCreate(new Date());
        assertEquals(new Date(), setAdviseTest.getDateCreate());
    }

    @Test
    public void getDateUpdate() {
        assertEquals(new Date(), getAdviseTest.getDateUpdate());
    }

    @Test
    public void setDateUpdate() {
        Advise setAdviseTest = new Advise("adviseIDTest", "descriptionTest", new Date()
                , new Date(), 1);
        setAdviseTest.setDateUpdate(new Date());
        assertEquals(new Date(), setAdviseTest.getDateUpdate());
    }

    @Test
    public void getStatus() {
        assertEquals(1, getAdviseTest.getStatus());
    }

    @Test
    public void setStatus() {
        Advise setAdviseTest = new Advise("adviseIDTest", "descriptionTest", new Date()
                , new Date(), 1);
        setAdviseTest.setStatus(0);
        assertEquals(0, setAdviseTest.getStatus());
    }
}