package com.example.travelapp.controllers;

import com.example.travelapp.config.ApiResponse;
import com.example.travelapp.dto.AuthResponse;
import com.example.travelapp.dto.LoginRequest;
import com.example.travelapp.dto.RegisterRequest;
import com.example.travelapp.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody LoginRequest request) {
        try {
            String token = authService.login(request);
            AuthResponse authResponse = new AuthResponse(token, request.getUsername());

            ApiResponse<AuthResponse> apiResponse = new ApiResponse<>(
                    HttpStatus.OK.value(),
                    "Login Successful",
                    authResponse
            );

            return ResponseEntity.ok(apiResponse);
        } catch (IllegalArgumentException e) {
            ApiResponse<AuthResponse> apiResponse = new ApiResponse<>(
                    HttpStatus.UNAUTHORIZED.value(),
                    "Login Failed",
                    null,
                    e.getMessage()
            );

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiResponse);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@RequestBody RegisterRequest request) {
        try {
            String token = authService.register(request);
            AuthResponse authResponse = new AuthResponse(token, request.getUsername(), "Registration Successful");

            ApiResponse<AuthResponse> apiResponse = new ApiResponse<>(
                    HttpStatus.CREATED.value(),
                    "Registration Successful",
                    authResponse
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
        } catch (IllegalArgumentException e) {
            ApiResponse<AuthResponse> apiResponse = new ApiResponse<>(
                    HttpStatus.BAD_REQUEST.value(),
                    "Registration Failed",
                    null,
                    e.getMessage()
            );

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
        }
    }
}