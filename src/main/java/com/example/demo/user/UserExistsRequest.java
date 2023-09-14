package com.example.demo.user;

public class UserExistsRequest {
    private String email;

    public UserExistsRequest() {
        // Default constructor
    }

    public UserExistsRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}