package com.example.travelapp.dto;

public class AuthResponse {
    private String authenticationToken;
    private String username;

    public AuthResponse(String authenticationToken) {
        this.authenticationToken = authenticationToken;
        this.username = username;
    }

    public String getAuthenticationToken() {
        return authenticationToken;
    }

    public void setAuthenticationToken(String authenticationToken) {
        this.authenticationToken = authenticationToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}