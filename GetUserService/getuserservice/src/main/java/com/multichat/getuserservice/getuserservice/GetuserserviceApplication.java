package com.multichat.getuserservice.getuserservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class GetuserserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GetuserserviceApplication.class, args);
	}
}
