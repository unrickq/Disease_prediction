package com.example.diseaseprediction.object;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class AdviseTest {
    Date d1 = new Date(2000, 11, 21);
    Date d2 = new Date(1999, 2, 12);
    Advise getAdviseTest = new Advise("adviseIDTest", "descriptionTest", d1
            , d1, 1);

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
        assertEquals("descriptionTest2", setAdviseTest.getDescription());
    }

    @Test
    public void getDateCreate() {
        assertEquals(d1, getAdviseTest.getDateCreate());
    }

    @Test
    public void setDateCreate() {
        Advise setAdviseTest = new Advise("adviseIDTest", "descriptionTest", new Date()
                , new Date(), 1);
        setAdviseTest.setDateCreate(d2);
        assertEquals(d2, setAdviseTest.getDateCreate());
    }

    @Test
    public void getDateUpdate() {
        assertEquals(d1, getAdviseTest.getDateUpdate());
    }

    @Test
    public void setDateUpdate() {
        Advise setAdviseTest = new Advise("adviseIDTest", "descriptionTest", new Date()
                , new Date(), 1);
        setAdviseTest.setDateUpdate(d2);
        assertEquals(d2, setAdviseTest.getDateUpdate());
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