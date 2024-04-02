package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.example.demo.Models.ChatMessage;

@Controller
public class WebSocketController {

  @Autowired
  SimpMessagingTemplate simpMessagingTemplate;

  @MessageMapping("/greetings")
  @SendTo("/topic/greetings")
  public String initialReply(String user) throws Exception {
    System.out.println(user + " subscribed");
    return "Welcome to the chat room " + user + "!";
  }

  @MessageMapping("/chat/{id}")
  public void messageHandler(@DestinationVariable("id") String id, ChatMessage message) {
    System.out.println(message);
    simpMessagingTemplate.convertAndSend("/topic/chat/" + id, message);
  }

  @EventListener
  @SendTo("/topic/greetings")
  public static String onDisconnectionHandler(SessionDisconnectEvent event) {
    System.out.println(" has disconnected");
    return " has disconnected";
  }
}
