package com.example.demo.user;

import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class TokenService {

    private final Map<String, String> confirmationTokens = new HashMap<>();

    public String generateToken(String email) {
        String token = UUID.randomUUID().toString();
        confirmationTokens.put(token, email);
        return token;
    }

    public boolean isValidToken(String token) {
        return confirmationTokens.containsKey(token);
    }

    public String getEmailByToken(String token) {
        return confirmationTokens.get(token);
    }

    public void removeToken(String token) {
        confirmationTokens.remove(token);
    }
}
