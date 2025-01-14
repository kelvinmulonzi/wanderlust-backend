package com.example.travelapp.models;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "UserInfo")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String email;
    private String role;

    public User() {

    }
}

