package com.example.demo;

import com.example.demo.user.User;
import com.example.demo.user.UserController;
import com.example.demo.repo.UserRepository;
import com.example.demo.MyKafkaProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.*;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

public class UpdateControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserRepository userRepoCall;

    @Mock
    private MyKafkaProducer kafkaproducer;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testUpdateUserByEmailSuccess() {
        // Create an existing user for testing
        User existingUser = new User();
        existingUser.setName("Existing User");
        existingUser.setEmail("existing@example.com");
        existingUser.setReceiveNotifications(true);

        // Create an updated user
        User updatedUser = new User();
        updatedUser.setName("Updated User");
        updatedUser.setEmail("existing@example.com");
        updatedUser.setReceiveNotifications(false);

        // Mock UserRepository behavior
        when(userRepoCall.findByEmail(updatedUser.getEmail())).thenReturn(existingUser);
        when(userRepoCall.save(existingUser)).thenReturn(existingUser);

        // Mock KafkaProducer behavior to return a completed CompletableFuture
        CompletableFuture<Void> completedFuture = CompletableFuture.completedFuture(null);
        when(kafkaproducer.sendMessage(anyString(), anyString())).thenReturn(completedFuture);

        // Call the updateUserByEmail method
        ResponseEntity<String> response = userController.updateUserByEmail(updatedUser);

        // Assert the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User updated successfully", response.getBody());
    }


    @Test
    public void testUpdateUserByEmailNotFound() {
        // Create an updated user
        User updatedUser = new User();
        updatedUser.setName("Updated User");
        updatedUser.setEmail("nonexistent@example.com");
        updatedUser.setReceiveNotifications(false);

        // Mock UserRepository behavior to return null (user not found)
        when(userRepoCall.findByEmail(updatedUser.getEmail())).thenReturn(null);

        // Call the updateUserByEmail method
        ResponseEntity<String> response = userController.updateUserByEmail(updatedUser);

        // Assert the response
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found", response.getBody());

        // Verify that the user information was not updated
        verify(userRepoCall, never()).save(any(User.class));
        verify(kafkaproducer, never()).sendMessage(anyString(), anyString());
    }
}


