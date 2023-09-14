package com.example.demo.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.user.User;


public interface UserRepository extends MongoRepository<User, String> {
	User findByEmail(String email);
}