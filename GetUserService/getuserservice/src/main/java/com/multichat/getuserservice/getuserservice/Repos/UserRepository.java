package com.multichat.getuserservice.getuserservice.Repos;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.multichat.getuserservice.getuserservice.models.User;

//Spring understands the format of the function and will search in the User class for the usernameproperty
public interface UserRepository extends MongoRepository<User, ObjectId> {
  User findByUsername(String username);
}
