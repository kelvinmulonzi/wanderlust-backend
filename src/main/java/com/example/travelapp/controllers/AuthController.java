package com.example.travelapp.controllers;

import com.example.travelapp.dto.AuthResponse;
import com.example.travelapp.dto.LoginRequest;
import com.example.travelapp.dto.RegisterRequest;
import com.example.travelapp.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @RequestMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        String token = authService.login(request);
        AuthResponse response = new AuthResponse(token);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        String token = authService.register(request);
        AuthResponse response = new AuthResponse(token);
        return ResponseEntity.ok(response);
    }

}
