package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketMessageBrokerConfig implements WebSocketMessageBrokerConfigurer {

  @Autowired
  private final WebSocketHeaderInterceptor head;

  public WebSocketMessageBrokerConfig(WebSocketHeaderInterceptor head) {
      this.head = head;
  }
  // sets up the endpoint for the STOMP over websocket connection
  // STOMP (Stream Text Object Messaging Protocol)
  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint("/ws") // connect to localhost:8081/ws USING STOMP
        .setAllowedOrigins("*"); // allows any domain to access this
  }

  // sets up the message broker
  // sets up a simple message broker to handle specific /topic publishes
  // sets up endpoint to direct messages that dont need to be handled in a messagebroker
  @Override
  public void configureMessageBroker(MessageBrokerRegistry registry) {
    registry.enableSimpleBroker("/topic");
    registry.setApplicationDestinationPrefixes("/app");
  }

  //handling inbounds
  @Override
  public void configureClientInboundChannel(ChannelRegistration registration) {
    // WebSocketHeaderInterceptor headerInterceptor = new WebSocketHeaderInterceptor();
    //SubscriberInterceptor subscriberInterceptor = new SubscriberInterceptor();
    registration.interceptors(head);
  }
}
