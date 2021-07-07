package com.example.diseaseprediction.object;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ConsultationListTest {
    ConsultationList getConsultationListTest = new ConsultationList("accountTest1",
            "accountTest2", "sessionTest1");

    @Test
    public void getAccountOne() {
        assertEquals("accountTest1", getConsultationListTest.getAccountOne());
    }

    @Test
    public void setAccountOne() {
        ConsultationList setConsultationListTest = new ConsultationList("accountTest1",
                "accountTest2", "sessionTest1");
        setConsultationListTest.setAccountOne("accountTest3");
        assertEquals("accountTest3", getConsultationListTest.getAccountOne());
    }

    @Test
    public void getAccountTwo() {
        assertEquals("accountTest2", getConsultationListTest.getAccountTwo());
    }

    @Test
    public void setAccountTwo() {
        ConsultationList setConsultationListTest = new ConsultationList("accountTest1",
                "accountTest2", "sessionTest1");
        setConsultationListTest.setAccountTwo("accountTest4");
        assertEquals("accountTest4", getConsultationListTest.getAccountTwo());
    }

    @Test
    public void getSessionID() {
        assertEquals("sessionTest1", getConsultationListTest.getSessionID());
    }

    @Test
    public void setSessionID() {
        ConsultationList setConsultationListTest = new ConsultationList("accountTest1",
                "accountTest2", "sessionTest1");
        setConsultationListTest.setSessionID("sessionTest2");
        assertEquals("sessionTest2", getConsultationListTest.getSessionID());
    }
}