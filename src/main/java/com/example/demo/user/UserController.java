package com.example.demo.user;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.MyKafkaProducer;
import com.example.demo.repo.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

@CrossOrigin(origins = "http://localhost:3000")
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
            String password = user.getPassword(); // Get the password from the User object

            if (name == null || name.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Name is missing or empty"));
            } else if (email == null || email.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Email is missing or empty"));
            } else if (!email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Invalid email format"));
            }

            User existingUser = userRepoCall.findByEmail(user.getEmail());
            if (existingUser != null && !existingUser.isActive()) {
                // If the user exists but is not active, it's considered a new signup
                userRepoCall.delete(existingUser); // Remove the existing inactive user
            } else if (existingUser != null && existingUser.isActive()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", "Email already exists"));
            }

            if (password == null || password.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Password is missing or empty"));
            }

            // Hash the password
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String hashedPassword = encoder.encode(password);
            user.setPassword(hashedPassword);

            user.setActive(true); // Set the user as active
            userRepoCall.save(user);

            // Kafka message sending code (assuming kafkaproducer is correctly configured)
            boolean receiveNotifications = user.isReceiveNotifications();
            if (receiveNotifications) {
                String topic = "signup-topic";
                kafkaproducer.sendMessage(topic, email);
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "User created successfully"));
        } catch (Exception e) {
            // Handle the exception, log it, and return an error response
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "An error occurred"));
        }
    }
    
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); // Configure PasswordEncoder here

    @PostMapping("/signin")
    public ResponseEntity<String> signin(@RequestBody Map<String, String> signinRequest) {
        try {
            String email = signinRequest.get("email");
            String password = signinRequest.get("password");

            if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
                return ResponseEntity.badRequest().body("Please provide both email and password.");
            }

            User user = userRepoCall.findByEmail(email);
            if (user == null) {
                return ResponseEntity.badRequest().body("User not found. Please check your credentials.");
            }

            // Use BCryptPasswordEncoder to verify the password
            if (!passwordEncoder.matches(password, user.getPassword())) {
                return ResponseEntity.badRequest().body("Incorrect password. Please try again.");
            }
            if(user.isActive())
            {
            	return ResponseEntity.ok(user.getName());
            }
            else {
            	return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

        } catch (Exception e) {
            // Handle the exception, log it, and return an error response
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }
    @PutMapping("/userUpdate")
    public ResponseEntity<String> updateUserByEmail(@RequestBody User updatedUser) {
        try {
            // Check if the user exists and is active
            String userEmail = updatedUser.getEmail();
            User existingUser = userRepoCall.findByEmail(userEmail);

            if (existingUser != null && existingUser.isActive()) {
                // Update user information based on modifications
                existingUser.setName(updatedUser.getName());
                existingUser.setReceiveNotifications(updatedUser.isReceiveNotifications());
                existingUser.setNotifications(updatedUser.getNotifications());

                // Save the updated user
                userRepoCall.save(existingUser);

                String topic = "account-modified-topic";
                kafkaproducer.sendMessage(topic, userEmail);

                return ResponseEntity.status(HttpStatus.OK).body("User updated successfully");
            } else if (existingUser != null && !existingUser.isActive()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
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

            if (existingUser != null && existingUser.isActive()) {
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
