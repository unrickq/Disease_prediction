package com.example.diseaseprediction.object;

import java.util.Date;

public class Message {

  private String messageID;
  private String senderID;
  private String receiverID;
  private String message;
  private Date dateSend;
  private String sessionID;
  private int status;

  /**
   * Init new Message
   */
  public Message() {
  }

  /**
   * Init new Message
   *
   * @param messageID  Message ID
   * @param senderID   Sender ID
   * @param receiverID Receiver ID
   * @param message    Message content
   * @param dateSend   Send date
   * @param sessionID  Session ID
   * @param status     Message status
   */
  public Message(String messageID, String senderID, String receiverID, String message, Date dateSend,
                 String sessionID, int status) {
    this.messageID = messageID;
    this.senderID = senderID;
    this.receiverID = receiverID;
    this.message = message;
    this.dateSend = dateSend;
    this.sessionID = sessionID;
    this.status = status;
  }

  /**
   * Get Message ID
   *
   * @return message ID
   */
  public String getMessageID() {
    return messageID;
  }

  /**
   * set Message ID
   *
   * @param messageID message ID
   */
  public void setMessageID(String messageID) {
    this.messageID = messageID;
  }

  /**
   * Get sender ID
   *
   * @return sender ID
   */
  public String getSenderID() {
    return senderID;
  }

  /**
   * Set sender ID
   *
   * @param senderID sender ID
   */
  public void setSenderID(String senderID) {
    this.senderID = senderID;
  }

  /**
   * Get receiver ID
   *
   * @return receiver ID
   */
  public String getReceiverID() {
    return receiverID;
  }

  /**
   * Set receiver ID
   *
   * @param receiverID receiver ID
   */
  public void setReceiverID(String receiverID) {
    this.receiverID = receiverID;
  }

  /**
   * Get message content
   *
   * @return message content
   */
  public String getMessage() {
    return message;
  }

  /**
   * Set message content
   *
   * @param message message content
   */
  public void setMessage(String message) {
    this.message = message;
  }

  /**
   * Get send date
   *
   * @return send date
   */
  public Date getDateSend() {
    return dateSend;
  }

  /**
   * Set send date
   *
   * @param dateSend send date
   */
  public void setDateSend(Date dateSend) {
    this.dateSend = dateSend;
  }

  /**
   * Get session ID
   *
   * @return session ID
   */
  public String getSessionID() {
    return sessionID;
  }

  /**
   * Set session ID
   *
   * @param sessionID session ID
   */
  public void setSessionID(String sessionID) {
    this.sessionID = sessionID;
  }

  /**
   * Get message status
   *
   * @return message status
   */
  public int getStatus() {
    return status;
  }

  /**
   * Set message status
   *
   * @param status messages status
   */
  public void setStatus(int status) {
    this.status = status;
  }
}
