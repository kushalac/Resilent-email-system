package com.example.demo.user;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Map;

@Document(collection = "user_copies")
public class UserCopy {

    @Id
    private String id;
    private String name;
    private String email;
    private boolean receiveNotifications;
    private Map<String, Boolean> notifications;
    private Map<String, String> receivedNotifications;

    // Constructors, getters, and setters

    public UserCopy() {
        // Default constructor
        this.notifications = new HashMap<>();
        this.receivedNotifications = new HashMap<>();
    }

    // Getters and setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}

