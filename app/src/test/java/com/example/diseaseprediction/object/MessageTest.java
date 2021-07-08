package com.example.diseaseprediction.object;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class MessageTest {
    Date d1 = new Date(2000, 11, 21);
    Date d2 = new Date(1999, 2, 12);
    Message getMessageTest = new Message("messageIDTest", "senderIDTest",
            "receiverIDTest", "messageTest", d1,
            "sessionIDTest", 1);

    @Test
    public void getMessageID() {
        assertEquals("messageIDTest", getMessageTest.getMessageID());
    }

    @Test
    public void setMessageID() {
        Message setMessageTest = new Message("messageIDTest", "senderIDTest",
                "receiverIDTest", "messageTest", new Date(),
                "sessionIDTest", 1);
        setMessageTest.setMessageID("messageIDTest2");
        assertEquals("messageIDTest2", setMessageTest.getMessageID());
    }

    @Test
    public void getSenderID() {
        assertEquals("senderIDTest", getMessageTest.getSenderID());
    }

    @Test
    public void setSenderID() {
        Message setMessageTest = new Message("messageIDTest", "senderIDTest",
                "receiverIDTest", "messageTest", new Date(),
                "sessionIDTest", 1);
        setMessageTest.setSenderID("senderIDTest2");
        assertEquals("senderIDTest2", setMessageTest.getSenderID());
    }

    @Test
    public void getReceiverID() {
        assertEquals("receiverIDTest", getMessageTest.getReceiverID());
    }

    @Test
    public void setReceiverID() {
        Message setMessageTest = new Message("messageIDTest", "senderIDTest",
                "receiverIDTest", "messageTest", new Date(),
                "sessionIDTest", 1);
        setMessageTest.setReceiverID("receiverIDTest");
        assertEquals("receiverIDTest", setMessageTest.getReceiverID());
    }

    @Test
    public void getMessage() {
        assertEquals("messageTest", getMessageTest.getMessage());
    }

    @Test
    public void setMessage() {
        Message setMessageTest = new Message("messageIDTest", "senderIDTest",
                "receiverIDTest", "messageTest", new Date(),
                "sessionIDTest", 1);
        setMessageTest.setMessage("messageTest2");
        assertEquals("messageTest2", setMessageTest.getMessage());
    }

    @Test
    public void getDateSend() {
        assertEquals(d1, getMessageTest.getDateSend());
    }

    @Test
    public void setDateSend() {
        Message setMessageTest = new Message("messageIDTest", "senderIDTest",
                "receiverIDTest", "messageTest", new Date(),
                "sessionIDTest", 1);
        setMessageTest.setDateSend(d2);
        assertEquals(d2, setMessageTest.getDateSend());
    }

    @Test
    public void getSessionID() {
        assertEquals("sessionIDTest", getMessageTest.getSessionID());
    }

    @Test
    public void setSessionID() {
        Message setMessageTest = new Message("messageIDTest", "senderIDTest",
                "receiverIDTest", "messageTest", new Date(),
                "sessionIDTest", 1);
        setMessageTest.setSessionID("sessionIDTest2");
        assertEquals("sessionIDTest2", setMessageTest.getSessionID());
    }

    @Test
    public void getStatus() {
        assertEquals(1, getMessageTest.getStatus());
    }

    @Test
    public void setStatus() {
        Message setMessageTest = new Message("messageIDTest", "senderIDTest",
                "receiverIDTest", "messageTest", new Date(),
                "sessionIDTest", 1);
        setMessageTest.setStatus(3);
        assertEquals(3, setMessageTest.getStatus());
    }
}