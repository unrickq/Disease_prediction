package com.example.diseaseprediction.object;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class AccountTest {
    Account getAccountTest = new Account("accountID1", 0, 0, "0694003303", "nameTest"
            , 1, "addressTest", "emailTest",
            "imageTest", new Date(), new Date(), 1);

    @Test
    public void getAccountId() {
        assertEquals("accountID1", getAccountTest.getAccountId());
    }

    @Test
    public void setAccountId() {
        Account setAccountTest = new Account("accountID1", 0, 0, "0694003303", "nameTest"
                , 1, "addressTest", "emailTest",
                "imageTest", new Date(), new Date(), 1);
        setAccountTest.setAccountId("accountTest2");
        assertEquals("accountTest2", setAccountTest.getAccountId());
    }

    @Test
    public void getTypeID() {
        assertEquals(0, getAccountTest.getTypeID());
    }

    @Test
    public void setTypeID() {
        Account setAccountTest = new Account("accountID1", 0, 0, "0694003303", "nameTest"
                , 1, "addressTest", "emailTest",
                "imageTest", new Date(), new Date(), 1);
        setAccountTest.setTypeID(1);
        assertEquals(1, setAccountTest.getTypeID());
    }

    @Test
    public void getTypeLogin() {
        assertEquals(0, getAccountTest.getTypeID());
    }

    @Test
    public void setTypeLogin() {
        Account setAccountTest = new Account("accountID1", 0, 0, "0694003303", "nameTest"
                , 1, "addressTest", "emailTest",
                "imageTest", new Date(), new Date(), 1);
        setAccountTest.setTypeLogin(1);
        assertEquals(1, setAccountTest.getTypeLogin());
    }

    @Test
    public void getPhone() {
        assertEquals("0694003303", getAccountTest.getPhone());
    }

    @Test
    public void setPhone() {
        Account setAccountTest = new Account("accountID1", 0, 0, "0694003303", "nameTest"
                , 1, "addressTest", "emailTest",
                "imageTest", new Date(), new Date(), 1);
        setAccountTest.setPhone("098339332");
        assertEquals("098339332", setAccountTest.getPhone());
    }

    @Test
    public void testGetName() {
        assertEquals("nameTest", getAccountTest.getName());
    }

    @Test
    public void testSetName() {
        Account setAccountTest = new Account("accountID1", 0, 0, "0694003303", "nameTest"
                , 1, "addressTest", "emailTest",
                "imageTest", new Date(), new Date(), 1);
        setAccountTest.setName("nameTest2");
        assertEquals("nameTest2", setAccountTest.getName());
    }

    @Test
    public void getGender() {
        assertEquals(1, getAccountTest.getGender());
    }

    @Test
    public void setGender() {
        Account setAccountTest = new Account("accountID1", 0, 0, "0694003303", "nameTest"
                , 1, "addressTest", "emailTest",
                "imageTest", new Date(), new Date(), 1);
        setAccountTest.setGender(2);
        assertEquals(2, setAccountTest.getGender());
    }

    @Test
    public void getAddress() {
        assertEquals("addressTest", getAccountTest.getAddress());
    }

    @Test
    public void setAddress() {
        Account setAccountTest = new Account("accountID1", 0, 0, "0694003303", "nameTest"
                , 1, "addressTest", "emailTest",
                "imageTest", new Date(), new Date(), 1);
        setAccountTest.setAddress("addressTest2");
        assertEquals("addressTest2", setAccountTest.getAddress());
    }

    @Test
    public void getEmail() {
        assertEquals("emailTest", getAccountTest.getEmail());
    }

    @Test
    public void setEmail() {
        Account setAccountTest = new Account("accountID1", 0, 0, "0694003303", "nameTest"
                , 1, "addressTest", "emailTest",
                "imageTest", new Date(), new Date(), 1);
        setAccountTest.setEmail("email@test.com");
        assertEquals("email@test.com", setAccountTest.getEmail());
    }

    @Test
    public void getImage() {
        assertEquals("imageTest", getAccountTest.getImage());
    }

    @Test
    public void setImage() {
        Account setAccountTest = new Account("accountID1", 0, 0, "0694003303", "nameTest"
                , 1, "addressTest", "emailTest",
                "imageTest", new Date(), new Date(), 1);
        setAccountTest.setImage("imageTest2");
        assertEquals("imageTest2", setAccountTest.getImage());
    }

    @Test
    public void getDateCreate() {
        assertEquals(new Date(), getAccountTest.getDateCreate());
    }

    @Test
    public void setDateCreate() {
        Account setAccountTest = new Account("accountID1", 0, 0, "0694003303", "nameTest"
                , 1, "addressTest", "emailTest",
                "imageTest", new Date(), new Date(), 1);
        setAccountTest.setDateCreate(new Date());
        assertEquals(new Date(), setAccountTest.getDateCreate());
    }

    @Test
    public void getDateUpdate() {
        assertEquals(new Date(), getAccountTest.getDateUpdate());
    }

    @Test
    public void setDateUpdate() {
        Account setAccountTest = new Account("accountID1", 0, 0, "0694003303", "nameTest"
                , 1, "addressTest", "emailTest",
                "imageTest", new Date(), new Date(), 1);
        setAccountTest.setDateUpdate(new Date());
        assertEquals(new Date(), setAccountTest.getDateUpdate());
    }

    @Test
    public void getStatus() {
        assertEquals(1, getAccountTest.getStatus());
    }

    @Test
    public void setStatus() {
        Account setAccountTest = new Account("accountID1", 0, 0, "0694003303", "nameTest"
                , 1, "addressTest", "emailTest",
                "imageTest", new Date(), new Date(), 1);
        setAccountTest.setStatus(0);
        assertEquals(0, setAccountTest.getStatus());
    }
}