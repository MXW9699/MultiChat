package com.multichat.getuserservice.Repos;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.multichat.getuserservice.models.Message;

public interface MessageRepository extends MongoRepository<Message,Object>{
}
