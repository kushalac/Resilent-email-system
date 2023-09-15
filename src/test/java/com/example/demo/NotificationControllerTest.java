package com.example.demo;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.demo.repo.NotificationRepository;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationRepository notificationRepository;

    @Test
    public void testCreateNewNotification() throws Exception {
        Notification notification = new Notification();
        notification.setNotificationType("Test");
        notification.setNotificationSubject("Test Subject");
        notification.setNotificationContent("Test Content");
        notification.setUserList(new ArrayList<>());

        Mockito.when(notificationRepository.existsByNotificationTypeAndNotificationSubject("Test", "Test Subject"))
                .thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.post("/notification/createNotification")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"notificationType\":\"Test\",\"notificationSubject\":\"Test Subject\",\"notificationContent\":\"Test Content\",\"userList\":[]}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Notification created and saved successfully."));
    }

    @Test
    public void testCreateDuplicateNotification() throws Exception {
        Notification notification = new Notification();
        notification.setNotificationType("Test");
        notification.setNotificationSubject("Test Subject");
        notification.setNotificationContent("Test Content");
        notification.setUserList(new ArrayList<>());

        Mockito.when(notificationRepository.existsByNotificationTypeAndNotificationSubject("Test", "Test Subject"))
                .thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/notification/createNotification")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"notificationType\":\"Test\",\"notificationSubject\":\"Test Subject\",\"notificationContent\":\"Test Content\",\"userList\":[]}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Sorry, the message of type 'Test' with the subject 'Test Subject' has already been created."));
    }
    @Test
    public void testSendNotification() throws Exception {
        Notification notification = new Notification();
        notification.setNotificationType("Test");
        notification.setNotificationSubject("Test Subject");
        notification.setNotificationContent("Test Content");
        notification.setUserList(new ArrayList<>());

        Mockito.when(notificationRepository.findByNotificationTypeAndNotificationSubject("Test", "Test Subject"))
                .thenReturn(notification);

        mockMvc.perform(MockMvcRequestBuilders.post("/notification/sendNotification")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"notificationType\":\"Test\",\"notificationSubject\":\"Test Subject\"}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Notification sent successfully."));
    }

    @Test
    public void testSendNotificationNotFound() throws Exception {
        Mockito.when(notificationRepository.findByNotificationTypeAndNotificationSubject("Test", "Test Subject"))
                .thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/notification/sendNotification")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"notificationType\":\"Test\",\"notificationSubject\":\"Test Subject\"}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Notification not found."));
    }
    
    @Test
    public void testUpdateNotification() throws Exception {
        Notification existingNotification = new Notification();
        existingNotification.setNotificationType("Test");
        existingNotification.setNotificationSubject("Test Subject");
        existingNotification.setNotificationContent("Old Content");

        Notification updatedNotification = new Notification();
        updatedNotification.setNotificationType("Test");
        updatedNotification.setNotificationSubject("Test Subject");
        updatedNotification.setNotificationContent("New Content");

        Mockito.when(notificationRepository.findByNotificationTypeAndNotificationSubject("Test", "Test Subject"))
                .thenReturn(existingNotification);

        mockMvc.perform(MockMvcRequestBuilders.put("/notification/updateNotification")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"notificationType\":\"Test\",\"notificationSubject\":\"Test Subject\",\"notificationContent\":\"New Content\"}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Notification updated successfully."));

        // Verify that the existingNotification was updated
        Mockito.verify(notificationRepository, Mockito.times(1)).save(existingNotification);
    }

 }
