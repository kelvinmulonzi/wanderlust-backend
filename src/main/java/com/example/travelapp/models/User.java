package com.example.travelapp.models;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class User {
    private String username;
    private String password;
    private String email;
    private String role;
    @Id
    private Long id;

    public User() {
    }

    public User(String username, String password, String email, String role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
