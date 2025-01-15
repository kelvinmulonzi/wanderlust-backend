package com.example.travelapp.services;

import com.example.travelapp.dto.LoginRequest;
import com.example.travelapp.models.User;
import com.example.travelapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    public String login(LoginRequest loginRequest) {
        // Validate the login credentials (username and password)
        if (isValidUser(loginRequest.getUsername(), loginRequest.getPassword())) {
            // Generate and return a token (e.g., JWT)
            return generateToken(loginRequest.getUsername());
        } else {
            // Throw an exception for invalid login
            throw new IllegalArgumentException("Invalid username or password");
        }
    }

    private boolean isValidUser(String username, String password) {
        // Validate the user by querying the database
        User user = userRepository.findByUsername(username);
        return user != null && user.getPassword().equals(password);
    }

    private String generateToken(String username) {
        // Token generation logic, e.g., using JWT
        return "mock-token-for-" + username;
    }
}