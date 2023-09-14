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
    
    @Indexed(unique = true) // Ensure email uniqueness
    private String name;
    private String email;
    private boolean receiveNotifications;
    private Map<String, Boolean> notifications;

    public User() {
        this.id = UUID.randomUUID().toString();
        this.notifications = new HashMap<>();
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
}
