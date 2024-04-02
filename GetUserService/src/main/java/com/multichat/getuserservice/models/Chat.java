package com.multichat.getuserservice.getuserservice.models;

import java.util.List;
import java.util.ArrayList;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Chats")
public class Chat {
  @Id
  private ObjectId _id;
  private List<String> users;
  private List<Message> messages;

  // constructor to create a new chat
  public Chat(List<String> users) {
    this.users = users;
    this.messages = new ArrayList<Message>();
  }

  public String get_id() {
    return _id.toString();
  }

  public List<Message> getMessages() {
    return messages;
  }

  public List<Message> getMessages(int page, int pageSize) {
    int end = messages.size() - page * pageSize;
    int start = messages.size() - (page + 1) * pageSize;
    return messages.subList(start, end);
  }

  public void setMessages(List<Message> messages) {
    this.messages = messages;
  }

  public List<String> getUsers() {
    return users;
  }

  public void setUsers(List<String> users) {
    this.users = users;
  }

  public void addMessage(Message message) {
    messages.add(message);
  }

}
