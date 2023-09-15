package com.example.demo;


import static org.mockito.Mockito.*;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.demo.repo.UserRepository;
import com.example.demo.user.User;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private MyKafkaProducer kafkaProducer;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSignupSuccess() throws Exception {
        User user = new User();
        user.setName("John");
        user.setEmail("john@example.com");
        user.setReceiveNotifications(true);

        when(userRepository.findByEmail(user.getEmail())).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"John\",\"email\":\"john@example.com\",\"receiveNotifications\":true}")
        )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User created successfully"));
    }

    @Test
    public void testSignupDuplicateEmail() throws Exception {
        User user = new User();
        user.setName("John");
        user.setEmail("john@example.com");
        user.setReceiveNotifications(true);

        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"John\",\"email\":\"john@example.com\",\"receiveNotifications\":true}")
        )
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Email already exists"));
    }

    @Test
    public void testSignupInvalidEmail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"John\",\"email\":\"invalid-email\",\"receiveNotifications\":true}")
        )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Invalid email format"));
    }

    @Test
    public void testSignupMissingName() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"john@example.com\",\"receiveNotifications\":true}")
        )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Name is missing or empty"));
    }

    @Test
    public void testSignupMissingEmail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"John\",\"receiveNotifications\":true}")
        )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Email is missing or empty"));
    }

    @Test
    public void testSignupInternalServerError() throws Exception {
        when(userRepository.findByEmail(anyString())).thenThrow(new RuntimeException("Internal server error"));

        mockMvc.perform(MockMvcRequestBuilders
                .post("/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"John\",\"email\":\"john@example.com\",\"receiveNotifications\":true}")
        )
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("An error occurred"));
    }
}
