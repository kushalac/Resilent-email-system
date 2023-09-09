package com.example.demo;
import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
@Controller
public class DeleteUserController {

    @Autowired
    private UserRepository userRepoCall;

    @Autowired
    private MyKafkaProducer kafkaproducer;

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
            userRepoCall.delete(userToDelete);

            // Send a notification to the user about account deletion
            String topic = "account-deleted-topic";
            String subject = "Account Deletion Notification";
            String message = "Hello " + userToDelete.getName() + ",\n\nYour account has been deleted.";

            kafkaproducer.sendMessage(userToDelete.getName(), email, topic, message, subject);

            return ResponseEntity.status(HttpStatus.OK).body(Collections.singletonMap("message", "User with email " + email + " has been deleted"));
        } catch (Exception e) {
            // Handle the exception, log it, and return an error response
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("message", "An error occurred"));
        }
    }
}