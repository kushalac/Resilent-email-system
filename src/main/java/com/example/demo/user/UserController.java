package com.example.demo.user;

import java.util.Collections;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.MyKafkaProducer;
import com.example.demo.repo.UserRepository;
import com.mongodb.DuplicateKeyException;

@RestController
public class UserController {

    @Autowired
    UserRepository userRepoCall;
    
    @Autowired
    private MyKafkaProducer kafkaproducer;

    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> signup(@RequestBody User user) {
        try {
            String name = user.getName();
            String email = user.getEmail();
            boolean receiveNotifications = user.isReceiveNotifications();

            if (name == null || name.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Name is missing or empty"));
            } else if (email == null || email.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Email is missing or empty"));
            } else if (!email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Invalid email format"));
            }

            User existingUser = userRepoCall.findByEmail(user.getEmail());
            if (existingUser != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", "Email already exists"));
            }
            userRepoCall.save(user);
            if (receiveNotifications) {
                String topic = "signup-topic";
                kafkaproducer.sendMessage(topic,email);
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "User created successfully"));
        } catch (Exception e) {
            // Handle the exception, log it, and return an error response
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "An error occurred"));
        }
    }
    
    @DeleteMapping("/deleteUser")
    public ResponseEntity<Map<String, String>> deleteUser(@RequestBody Map<String, String> requestBody) {
        try {
            String email = requestBody.get("email");

            if (email == null || email.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", "Email is missing or empty"));
            }

            User userToDelete = userRepoCall.findByEmail(email);

            if (userToDelete == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", "User with email " + email + " not found"));
            }

            // Delete the user from the database
            //userRepoCall.delete(userToDelete);

            // Send a notification to the user about account deletion
            String topic = "account-deleted-topic";

            kafkaproducer.sendMessage(topic,email);
            
            return ResponseEntity.status(HttpStatus.OK).body(Collections.singletonMap("message", "User with email " + email + " has been deleted"));
        } catch (Exception e) {
            // Handle the exception, log it, and return an error response
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("message", "An error occurred"));
        }
    }
    
    
    @PutMapping("/userUpdate")
    public ResponseEntity<String> updateUserByEmail(@RequestBody User updatedUser) {
        try {
            // Check if the user exists
            String userEmail = updatedUser.getEmail();
            User existingUser = userRepoCall.findByEmail(userEmail);

            if (existingUser != null) {
                // Update user information based on modifications
                existingUser.setName(updatedUser.getName());
                existingUser.setReceiveNotifications(updatedUser.isReceiveNotifications());
                existingUser.setNotifications(updatedUser.getNotifications());

                // Save the updated user
                userRepoCall.save(existingUser);

                String topic = "account-modified-topic";
 
                kafkaproducer.sendMessage(topic,userEmail);
                
                return ResponseEntity.status(HttpStatus.OK).body("User updated successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred updating");
        }
    }

    
    @PostMapping("/userExists")
    public ResponseEntity<UserExistsResponse> checkUserExists(@RequestBody UserExistsRequest request) {
        try {
            String userEmail = request.getEmail();
            User existingUser = userRepoCall.findByEmail(userEmail);

            if (existingUser != null) {
                UserExistsResponse response = new UserExistsResponse(true, existingUser);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.ok(new UserExistsResponse(false, null));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new UserExistsResponse(false, null));
        }
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public List<User> getAllUsers() {
        return userRepoCall.findAll();
    }
}
