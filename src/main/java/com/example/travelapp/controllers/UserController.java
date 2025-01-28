package com.example.travelapp.controllers;

import com.example.travelapp.dto.UserDTO;
import com.example.travelapp.models.User;
import com.example.travelapp.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")  // Adjust this according to your CORS requirements
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }



    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        try {
            User user = userService.getUser(id);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        try {
           User user=userService.getUser(id);

            user.setEmail(userDTO.getEmail());
            user.setRole(userDTO.getRole());
          user.setUsername(userDTO.getUsername());
            User updatedUser = userService.updateUser(id, user);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to update user: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    // Exception handler for common exceptions
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleResponseStatusException(ResponseStatusException ex) {
        return new ResponseEntity<>(ex.getReason(), ex.getStatusCode());
    }
}