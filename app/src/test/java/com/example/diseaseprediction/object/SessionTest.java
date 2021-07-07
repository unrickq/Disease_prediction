package com.example.diseaseprediction.object;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class SessionTest {

    Session getSessionTest = new Session("SessionID1", new Date(), new Date(), 1);

    @Test
    public void getDateCreate() {
        assertEquals(new Date(), getSessionTest.getDateCreate());
    }

    @Test
    public void setDateCreate() {
        Session setSessionTest = new Session("SessionID1", new Date(), new Date(), 1);
        setSessionTest.setDateCreate(new Date());
        assertEquals(new Date(), setSessionTest.getDateCreate());
    }

    @Test
    public void getDateUpdate() {
        assertEquals(new Date(), getSessionTest.getDateUpdate());
    }

    @Test
    public void setDateUpdate() {
        Session setSessionTest = new Session("SessionID1", new Date(), new Date(), 1);
        setSessionTest.setDateCreate(new Date());
        assertEquals(new Date(), setSessionTest.getDateCreate());
    }

    @Test
    public void getSessionID() {
        assertEquals("SessionID1", getSessionTest.getSessionID());
    }

    @Test
    public void setSessionID() {
        Session setSessionTest = new Session("SessionID1", new Date(), new Date(), 1);
        setSessionTest.setSessionID("sessionTest");
        assertEquals("sessionTest", setSessionTest.getSessionID());
    }

    @Test
    public void getStatus() {
        assertEquals(1, getSessionTest.getStatus());
    }

    @Test
    public void setStatus() {
        Session setSessionTest = new Session("SessionID1", new Date(), new Date(), 1);
        setSessionTest.setStatus(0);
        assertEquals(0, setSessionTest.getStatus());
    }
}