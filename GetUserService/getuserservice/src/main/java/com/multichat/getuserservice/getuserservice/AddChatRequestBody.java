package com.multichat.getuserservice.getuserservice;

import java.util.List;

public class AddChatRequestBody {
  private String ChatID;
  private List<String> Users;
  
  public String getChatID() {
    return ChatID;
  }
  public void setChatID(String chatID) {
    ChatID = chatID;
  }
  public List<String> getUsers() {
    return Users;
  }
  public void setUsers(List<String> users) {
    Users = users;
  }

  
}
