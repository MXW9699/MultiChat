package com.multichat.getuserservice.Controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.RestController;

import com.multichat.getuserservice.Repos.ChatRepository;
import com.multichat.getuserservice.Repos.UserRepository;
import com.multichat.getuserservice.models.Chat;
import com.multichat.getuserservice.models.Message;
import com.multichat.getuserservice.models.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CrossOrigin(origins = "http://localhost:9000")
@RestController
public class UserController {

  @Autowired
  UserRepository userRepo;

  @Autowired
  ChatRepository chatRepo;

  // get a list of all users
  @GetMapping("/users")
  public ResponseEntity<?> getUsers() {
    try {
      List<User> data = userRepo.findAll();
      return ResponseEntity.ok(data);
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }

  // get a user info
  @GetMapping("/users/{name}")
  public ResponseEntity<?> getUsers(@PathVariable("name") String name) {
    try {
      User data = userRepo.findByUsername(name);
      if (data == null) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
      }
      return ResponseEntity.ok(data);
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("An error occurred while processing the request");
    }
  }

  @GetMapping("/chat/{chatID}")
  public ResponseEntity<List<Message>> getChat(@PathVariable("chatID") @NonNull String chatID) {
    System.out.println(chatID);
    Optional<Chat> chatOptional = chatRepo.findById(chatID);
    if (chatOptional.isPresent()) {
      // Extract the Chat object from Optional
      Chat selectedChat = chatOptional.get();
      return ResponseEntity.ok(selectedChat.getMessages());
    }
    return ResponseEntity.notFound().build();
  }

  @GetMapping("/chat/{chatID}/{page}/{pageSize}")
  public ResponseEntity<List<Message>> getChat(@PathVariable("chatID") @NonNull String chatID,
      @PathVariable("page") int page, @PathVariable("pageSize") int pageSize) {
    System.out.println(chatID);
    Optional<Chat> chatOptional = chatRepo.findById(chatID);
    if (chatOptional.isPresent()) {
      // Extract the Chat object from Optional
      Chat selectedChat = chatOptional.get();
      return ResponseEntity.ok(selectedChat.getMessages(page, pageSize));
    }
    return ResponseEntity.notFound().build();
  }

  // send back UserObject of the user entered. if the user does not exist, create
  // on and send back
  @PostMapping("/users")
  public ResponseEntity<?> postName(@RequestBody String name) throws Exception {
    try {
      User data = userRepo.findByUsername(name);
      if (data == null) {
        System.out.println("creating User");
        data = userRepo.save(new User(name));
      }
      return ResponseEntity.ok(data);
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("An error occurred while processing the request");
    }
  }

  // create chat from list of users
  // add a chatID to the list of users
  @PostMapping("/chat")
  public ResponseEntity<String> addChat(@RequestBody List<String> users) throws Exception {
    try {
      // List<String> users = requestBody.getUsers();
      Chat chat = chatRepo.save(new Chat(users));
      String chatID = chat.get_id();

      for (String name : users) {
        User data = userRepo.findByUsername(name);
        data.addChat(chatID);
        userRepo.save(data);
        System.out.println(data.toString());
      }
      System.out.println("chat " + chatID + " added to users " + users.toString());
      return ResponseEntity.ok(chatID);
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  // add a message to a chat
  @PostMapping("users/{username}/chat/{chatID}")
  public ResponseEntity<?> sendMessage(@PathVariable("username") @NonNull String username,
      @PathVariable("chatID") @NonNull String chatID,
      @RequestBody String message) throws Exception {

    try {
      Optional<Chat> chatOptional = chatRepo.findById(chatID);
      if (chatOptional.isPresent()) {
        // Extract the Chat object from Optional
        Chat chat = chatOptional.get();
        chat.addMessage(new Message(message, chatID, username));
        chatRepo.save(chat);
      }
      return ResponseEntity.status(200).body("Message: " + message + "from: " + username + " sent to chat " + chatID);
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

  }
}