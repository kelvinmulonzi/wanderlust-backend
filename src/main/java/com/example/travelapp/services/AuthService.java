package com.example.travelapp.services;

import com.example.travelapp.dto.LoginRequest;
import com.example.travelapp.dto.RegisterRequest;
import com.example.travelapp.models.User;
import com.example.travelapp.repository.UserRepository;
import com.example.travelapp.Security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String login(LoginRequest loginRequest) {
        // Validate the login credentials (username and password)
        if (isValidUser(loginRequest.getUsername(), loginRequest.getPassword())) {
            // Generate and return a token (e.g., JWT)
            return jwtTokenUtil.generateJWT(loginRequest.getUsername());
        } else {
            // Throw an exception for invalid login
            throw new IllegalArgumentException("Invalid username or password");
        }
    }

    public String register(RegisterRequest registerRequest) {
        // Create a new user and save it to the database
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEmail(registerRequest.getEmail());
        user.setRole("USER"); // Default role

        userRepository.save(user);

        // Generate and return a token (e.g., JWT)
        return jwtTokenUtil.generateJWT(registerRequest.getUsername());
    }

    private boolean isValidUser(String username, String password) {
        // Validate the user by querying the database
        User user = userRepository.findByUsername(username);
        return user != null && passwordEncoder.matches(password, user.getPassword());
    }
}