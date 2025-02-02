package com.example.travelapp.security;


import org.springframework.stereotype.Component;

@Component
public class JWTAuthenticationResponse {
    private String token;
    private String tokenType="Bearer";

    public void setToken(String token) {

        this.token = token;
    }
    public String getToken(){

        return token;
    }
    public String getTokenType(){

        return tokenType;
    }
}
