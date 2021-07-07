package com.example.diseaseprediction.object;

/**
 * Consultation list store chat of 2 accounts corresponding to the session
 */
public class ConsultationList {
    private String accountOne;
    private String accountTwo;
    private String sessionID;

    /**
     * Consultation List constructor
     */
    public ConsultationList() {
    }

    /**
     * Create consultation list
     *
     * @param accountOne Account ID one in chat session
     * @param accountTwo Account ID two in chat session
     * @param sessionID  Chat session ID
     */
    public ConsultationList(String accountOne, String accountTwo, String sessionID) {
        this.accountOne = accountOne;
        this.accountTwo = accountTwo;
        this.sessionID = sessionID;
    }

    /**
     * Get Account ID one
     *
     * @return Account ID one
     */
    public String getAccountOne() {
        return accountOne;
    }

    /**
     * Set Account ID one
     *
     * @param accountOne Account ID one
     */
    public void setAccountOne(String accountOne) {
        this.accountOne = accountOne;
    }

    /**
     * Get Account ID two
     *
     * @return Account ID two
     */
    public String getAccountTwo() {
        return accountTwo;
    }

    /**
     * Set Account ID two
     *
     * @param accountTwo Account ID two
     */
    public void setAccountTwo(String accountTwo) {
        this.accountTwo = accountTwo;
    }

    /**
     * Get Session ID
     *
     * @return Session ID
     */
    public String getSessionID() {
        return sessionID;
    }

    /**
     * Set Session ID
     *
     * @param sessionID Session ID
     */
    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }
}
