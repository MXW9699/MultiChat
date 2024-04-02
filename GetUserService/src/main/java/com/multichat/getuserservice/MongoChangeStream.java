package com.multichat.getuserservice;

import com.multichat.getuserservice.models.Chat;
import com.multichat.getuserservice.models.ResponseModels.ChatResponse;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.ChangeStreamEvent;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.model.changestream.OperationType;

import reactor.core.publisher.Flux;
import java.net.http.HttpClient;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest;

@Configuration
public class MongoChangeStream {

  private ReactiveMongoTemplate reactiveTemplate;
  private Flux<ChangeStreamEvent<Chat>> flux;
  private ObjectMapper objectMapper;
  private HttpClient client;

  public MongoChangeStream(ReactiveMongoTemplate reactiveTemplate) {
    this.objectMapper = new ObjectMapper();
    this.client = HttpClient.newHttpClient();
    this.reactiveTemplate = reactiveTemplate;
    startChangeStream();
  }

  public void startChangeStream() {
    flux = reactiveTemplate.changeStream(Chat.class)
        .watchCollection("Chats")
        .listen();
    // Add error handling if necessary
    flux.subscribe(changeEvent -> {
      // Handle the change event here
      if (changeEvent.getOperationType() == OperationType.INSERT) {
        // send a notification to all users
        try {
          System.out.println("inside flux: " + changeEvent.getBody());
          Chat chat = changeEvent.getBody();
          ChatResponse newChat = new ChatResponse(chat.get_id(), chat.getUsers());
          String newChatJson = objectMapper.writeValueAsString(newChat);
          System.out.println(newChatJson);
          HttpRequest request = HttpRequest.newBuilder()
              .uri(URI.create("http://localhost:8081/Chat"))
              .header("Content-Type", "application/json")
              .POST(HttpRequest.BodyPublishers.ofString(newChatJson))
              .build();

          HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
          System.out.println(response);
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        } catch (InterruptedException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    });
  }

}
