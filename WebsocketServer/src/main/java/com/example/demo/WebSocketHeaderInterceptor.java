package com.example.demo;

import java.util.List;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

//intercepts incomming messages 
@Component
public class WebSocketHeaderInterceptor implements ChannelInterceptor {

  public boolean isNewChat(StompHeaderAccessor accessor) {
    Object headers = accessor.getHeader("nativeHeaders");
    if (headers instanceof MultiValueMap<?, ?>) {
      MultiValueMap<?, ?> nativeHeaders = (MultiValueMap<?, ?>) headers;
      Object newChatValue = nativeHeaders.get("newChat");
      if (newChatValue instanceof List<?>) {
        return (((List<?>) newChatValue).get(0)).equals("true");
      }
    }
    return false;
  }

  @Override
  public Message<?> preSend(Message<?> message, MessageChannel channel) {
    StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
    if (StompCommand.CONNECT.equals(accessor.getCommand())) {
      System.out.println("we are connecting via interceptor");
      System.out.println(accessor.getMessageHeaders());
    }
    return message;
  }

  @Override
  public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
    StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
    if (StompCommand.CONNECT.equals(accessor.getCommand())) {
      System.out.println("after connecting");
    }
  }
}
