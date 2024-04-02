package com.MultiChat.WebsocketServer.Models;

public class ChatMessage {
  private String username;
  private String content;
  private String chatID;

  public String getUsername() {
    return username;
  }

  public ChatMessage(String username, String content, String chatID) {
    this.username = username;
    this.content = content;
    this.chatID = chatID;
  }


  public void setUsername(String username) {
    this.username = username;
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
    return "from: " + username + "content: " + content + " " + chatID;
  }

}
