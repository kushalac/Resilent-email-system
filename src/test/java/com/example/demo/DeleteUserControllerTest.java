package com.example.demo;

import static org.mockito.Mockito.*;
import java.util.Collections;
import java.util.Map;
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
public class DeleteUserControllerTest {

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
    public void testDeleteUserNotFound() throws Exception {
        String email = "nonexistent@example.com";

        when(userRepository.findByEmail(email)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/deleteUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"" + email + "\"}")
        )
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User with email " + email + " not found"));
    }

    @Test
    public void testDeleteUserBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/deleteUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}") // Missing "email" field
        )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Email is missing or empty"));
    }

    @Test
    public void testDeleteUserInternalServerError() throws Exception {
        String email = "user@example.com";

        when(userRepository.findByEmail(email)).thenThrow(new RuntimeException("Internal server error"));

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/deleteUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"" + email + "\"}")
        )
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("An error occurred"));
    }
}
