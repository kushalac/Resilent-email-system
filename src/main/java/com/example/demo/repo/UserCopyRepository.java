package com.example.demo.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.user.UserCopy;

public interface UserCopyRepository extends MongoRepository<UserCopy, String> {
   
}

