package com.multichat.getuserservice.getuserservice.models;

import java.util.List;
import java.util.ArrayList;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.bson.types.ObjectId;

/*
 * Users object includes:
 *  UserID, ChatID, Timestamp 
 */

 //the getter functions control the response outout.

@Document(collection = "Users")
public class User {

  @Id
  private ObjectId _id;

  @Indexed(unique = true)
  private String username;

  private List<String> chatList;

// constructor intializing empty chat list
public User(String username) {
  this.username = username;
  this.chatList = new ArrayList<>();
}

//objectiD will print a object but to print the string you have to to string.
public String get_id() {
  return _id.toString();
}

public String getUsername() {
  return username;
}

public void setUsername(String username) {
  this.username = username;
}

public List<String> getChatList() {
  return chatList;
}

public void setChatList(List<String> chatList) {
  this.chatList = chatList;
}

public void addChat(String chatID) {
  this.chatList.add(chatID);
}

public void deleteChat(String chatID) {
  this.chatList.remove(chatID);
  }

  public String toString(){
    return ("username: " + this.username + ", chat:" +  chatList.toString());
  }
}
