package com.example.demo.user;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Document(collection = "users")
public class User {

    @Id
    private String id;

    @Indexed(unique = true)
    private String name;
    private String email;
    private boolean receiveNotifications;
    private Map<String, Boolean> notifications;
    private Map<String, String> receivedNotifications; // Store received notifications as formatted strings
    private boolean active; // New "active" field

    public User() {
        this.id = UUID.randomUUID().toString();
        this.notifications = new HashMap<>();
        this.receivedNotifications = new HashMap<>();
        this.active = true; // Set the "active" field to true by default during signup
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isReceiveNotifications() {
        return receiveNotifications;
    }

    public void setReceiveNotifications(boolean receiveNotifications) {
        this.receiveNotifications = receiveNotifications;
    }

    public Map<String, Boolean> getNotifications() {
        return notifications;
    }

    public void setNotifications(Map<String, Boolean> notifications) {
        this.notifications = notifications;
    }

    public Map<String, String> getReceivedNotifications() {
        return receivedNotifications;
    }

    public void setReceivedNotifications(Map<String, String> receivedNotifications) {
        this.receivedNotifications = receivedNotifications;
    }

    public void logReceivedNotification(String notificationId, String formattedTimestamp) {
        receivedNotifications.put(notificationId, formattedTimestamp);
    }

    public boolean hasReceivedNotification(String notificationId) {
        return receivedNotifications.containsKey(notificationId);
    }

    public String getReceivedNotificationTimestamp(String notificationId) {
        return receivedNotifications.get(notificationId);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
