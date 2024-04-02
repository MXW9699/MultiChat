package main.java.com.multichat.getuserservice.models.ResponseModels;

import java.util.List;

public class ChatResponse {

  public class ChatResponse {
    private String ChatID;
    private List<String> Users;

    public ChatResponse(String chatID, List<String> users) {
      ChatID = chatID;
      Users = users;
    }

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

}
