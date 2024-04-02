package com.multichat.getuserservice.getuserservice.models;

import java.time.*;
import java.time.format.*;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/*
 * Message object includes:
 *  Content, username, ChatID, Timestamp 
 */
@Document(collection = "Messages")
public class Message {

  @Id 
  private ObjectId _id;
  private long timeStamp;
  private String content;
  private String chatID;
  private String username;

  // Constructor to create a message using the content string and setting current
  // timestamp
  public Message(String content, String chatID, String username) {
    this.content = content;
    this.chatID = chatID;
    this.username = username;
    this.timeStamp = System.currentTimeMillis();
  }

  public long getTimeStamp() {
    return timeStamp;
  }

  public String getUsername() {
    return username;
  }

  public void setTimeStamp(long timeStamp) {
    this.timeStamp = timeStamp;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public ObjectId get_id() {
    return _id;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getChatID() {
    return chatID;
  }

  public void setChatID(String chatID) {
    this.chatID = chatID;
  }

  public String toString() {
    return ("From: " + this.username + "to: " + this.chatID + "content: " + this.content);
  }
}
