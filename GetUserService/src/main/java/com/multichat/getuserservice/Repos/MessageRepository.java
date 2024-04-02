package com.multichat.getuserservice.getuserservice.Repos;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.multichat.getuserservice.getuserservice.models.Message;

public interface MessageRepository extends MongoRepository<Message,Object>{
}
