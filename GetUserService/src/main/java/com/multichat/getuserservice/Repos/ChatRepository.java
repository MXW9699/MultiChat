package com.multichat.getuserservice.Repos;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.multichat.getuserservice.models.Chat;

public interface ChatRepository extends MongoRepository<Chat, String> {
}
