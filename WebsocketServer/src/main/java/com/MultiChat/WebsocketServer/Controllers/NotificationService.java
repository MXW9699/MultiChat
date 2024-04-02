package com.MultiChat.WebsocketServer.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.MultiChat.WebsocketServer.Models.Chat;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CrossOrigin(origins = "*")
@RestController
public class NotificationService {

  @Autowired
  SimpMessagingTemplate simpMessagingTemplate;

  @PostMapping("/Chat")
  public void sendUpdates(@RequestBody Chat param) {
    System.out.println(param);
    List<String> users = param.getUsers();
    for (String user : users) {
      simpMessagingTemplate.convertAndSend("/topic/user/" + user, param.getChatID());
    }
  }
}
