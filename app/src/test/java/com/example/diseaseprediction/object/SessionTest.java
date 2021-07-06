package com.example.diseaseprediction.object;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class SessionTest {

//    @Test
//    public void getDateCreate() {
//    }
//
//    @Test
//    public void setDateCreate() {
//    }
//
//    @Test
//    public void getDateUpdate() {
//    }
//
//    @Test
//    public void setDateUpdate() {
//    }

    @Test
    public void getSessionID() {
        Session ss = new Session("SessionID1", new Date(), new Date(), 1);
        assertEquals("SessionID1", ss.getSessionID());
    }

//    @Test
//    public void setSessionID() {
//    }
//
//    @Test
//    public void getStatus() {
//    }
//
//    @Test
//    public void setStatus() {
//    }
}