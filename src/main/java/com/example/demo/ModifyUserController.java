package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ModifyUserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MyKafkaProducer kafkaproducer;

    @PutMapping("/userUpdate")
    public ResponseEntity<String> updateUserByEmail(@RequestBody User updatedUser) {
        try {
            // Check if the user exists
            String userEmail = updatedUser.getEmail();
            User existingUser = userRepository.findByEmail(userEmail);

            if (existingUser != null) {
                // Update user information based on modifications
                existingUser.setName(updatedUser.getName());
                existingUser.setReceiveNotifications(updatedUser.isReceiveNotifications());
                existingUser.setNotifications(updatedUser.getNotifications());

                // Save the updated user
                userRepository.save(existingUser);

                String topic = "account-modified-topic";
                String subject = "Account Modified Notification";
                String message = "Hello " + updatedUser.getName() + ",\n\nYour account has been Updated.";

                kafkaproducer.sendMessage(updatedUser.getName(), updatedUser.getEmail(), topic, message, subject);

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
            User existingUser = userRepository.findByEmail(userEmail);

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
}