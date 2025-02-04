package com.example.travelapp.controllers;

import com.example.travelapp.security.ApiResponse;
import com.example.travelapp.dto.AuthResponse;
import com.example.travelapp.dto.LoginRequest;
import com.example.travelapp.dto.RegisterRequest;
import com.example.travelapp.dto.VerifyOtpRequest;
import com.example.travelapp.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(value = "*")
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

    @PostMapping("/verifyotp")
    public ResponseEntity<?> verifyOtp(@RequestBody VerifyOtpRequest request) {
        try {
            boolean verified = authService.verifyOtp(request.getEmail(), request.getOtp());

            if (verified) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "OTP Verified");
                // If you want to include a token
                // response.put("token", generateToken(request.getEmail()));
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "OTP Verification Failed");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        } catch (IllegalArgumentException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}