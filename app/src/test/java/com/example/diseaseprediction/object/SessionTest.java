package com.example.diseaseprediction.object;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class SessionTest {
    Date d1 = new Date(2000, 11, 21);
    Date d2 = new Date(1999, 2, 12);
    Session getSessionTest = new Session("SessionID1", d1, d1, 1);

    @Test
    public void getDateCreate() {
        assertEquals(d1, getSessionTest.getDateCreate());
    }

    @Test
    public void setDateCreate() {
        Session setSessionTest = new Session("SessionID1", new Date(), new Date(), 1);
        setSessionTest.setDateCreate(d2);
        assertEquals(d2, setSessionTest.getDateCreate());
    }

    @Test
    public void getDateUpdate() {
        assertEquals(d1, getSessionTest.getDateUpdate());
    }

    @Test
    public void setDateUpdate() {
        Session setSessionTest = new Session("SessionID1", new Date(), new Date(), 1);
        setSessionTest.setDateCreate(d2);
        assertEquals(d2, setSessionTest.getDateCreate());
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