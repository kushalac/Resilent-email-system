package com.example.demo;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.DuplicateKeyException;

@RestController
public class SignupController {

    @Autowired
    UserRepository userRepoCall;
    
    @Autowired
    private MyKafkaProducer kafkaproducer;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody User user) {
    	try {
    		
    		String name = user.getName();
            String email = user.getEmail();
            boolean receiveNotifications = user.isReceiveNotifications();

            if (name == null || name.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Name is missing or empty");
            } else if (email == null || email.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email is missing or empty");
            } else if (!email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid email format");
            }

            User existingUser = userRepoCall.findByEmail(user.getEmail());
            if (existingUser != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists");
            }
            userRepoCall.save(user);
            if (receiveNotifications) {
            	String topic = "signup-topic";
                String subject = "Welcome to our service";
                String message = "Hello " + name + ",\n\nWelcome to our service!";

                kafkaproducer.sendMessage(name, email,topic,message,subject);
            }
            return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
        } catch (Exception e) {
            // Handle the exception, log it, and return an error response
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }


    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public List<User> getAllUsers() {
        return userRepoCall.findAll();
    }
}