package com.MultiChat.WebsocketServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/*
@EnableAutoConfiguration: enable Spring Boot’s auto-configuration mechanism
@componentscan :enable @Component scan on the package where the application is located (con.example.demo)
@configuration : allow to register extra beans in the context or import additional configuration classes
 */
@SpringBootApplication //same as the above 3
public class MultiChatBox {

  //start the spring boot application
  public static void main(String[] args) {
    SpringApplication.run(MultiChatBox.class,args);
  }
}
