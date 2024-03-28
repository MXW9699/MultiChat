package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.DestinationVariable;

// import java.util.ArrayList;
// import java.util.List;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
// import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.WebSocketSession;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Controller
public class WebSocketController {

  @Autowired
  SimpMessagingTemplate simpMessagingTemplate;
  // private static final List<WebSocketSession> sessionIDs = new ArrayList<>();
  // private final SimpMessagingTemplate messagingTemplate;

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
